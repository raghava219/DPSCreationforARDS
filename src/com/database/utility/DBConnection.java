package com.database.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;


public class DBConnection{
	
	   // JDBC driver name and database URL
	   private static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";  
	   //  Database credentials
	   private final String USER = "XXSQL_USER";
	   private final String PASS = "sql_user";
	   private final Map<String,String> mapReqColumnFromDB = new LinkedHashMap<String,String>();
	  
	   private Connection con = null;
	   private Statement st = null;
	   private ResultSet rs = null;
	
	
	public Connection getConnection(String environment) {
			try{
				Class.forName(JDBC_DRIVER);
		        System.out.println("Connecting to database...");
		        Properties props = new Properties();
		        InputStream inStream = new FileInputStream("./config/environment.properties");
		        props.load(inStream);
		        String DB_URL = props.getProperty(environment).trim();
		        con = DriverManager.getConnection(DB_URL,USER,PASS);
			}catch(Exception e){
				e.printStackTrace();
			}
	        return con;
	}
	
	public String getSQLFromFile(String relFilePath,String order_number){
		StringBuffer sbf = new StringBuffer();
		String temp = null;
		try{
			File CustomerQuery = new File(relFilePath);
			FileReader fr = new FileReader(CustomerQuery);
			BufferedReader br = new BufferedReader(fr);
			while((temp = br.readLine())!=null){
				sbf.append(temp.trim() + "\n");
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return sbf.toString().replace("param_value", order_number);	
	}
	
	public String getSQLForVaringSkus(String filePath,String order_number,String sku_details) throws Exception{
		File CustomerQuery = new File(filePath);
		FileReader fr = new FileReader(CustomerQuery);
		BufferedReader br = new BufferedReader(fr);
		String temp = null;
		StringBuffer sbf = new StringBuffer();

		while((temp = br.readLine())!=null){
			if(temp.trim().contains("sku_order_item") && sku_details == null){
				continue;
			}
			sbf.append(temp.trim() + "\n");
		}
		if(sku_details != null){
		    sku_details = sku_details.replace(",", "','");
		    temp = sbf.toString().replace("sku_order_item", sku_details).replace("param_value", order_number);
		} else {
			temp = sbf.toString().replace("param_value", order_number);
		}
		return temp;		
	}
	
	public Map<String,String> loadOrderCustomerDetails(String order_number,String sku_details,String environment){
		
		try{
			String totalSellingPrice = null;
			
			String order_sqlStmt = "./Scripts/CustomerQuery.sql";
				   order_sqlStmt = this.getSQLFromFile(order_sqlStmt,order_number);
				   
			String camLocation = "./Scripts/Cam_Location.sql";
				   camLocation = this.getSQLFromFile(camLocation,order_number);
			String totalSellPrice = "./Scripts/total_selling_price.sql";	   
				  
			if(sku_details != null){
				totalSellingPrice = getSQLForVaringSkus(totalSellPrice,order_number,sku_details);
			} else {
				totalSellingPrice = getSQLForVaringSkus(totalSellPrice,order_number,null);
			}
			
			con = this.getConnection(environment);
			st = con.createStatement();
			ResultSet rs = null;
			
			if(order_sqlStmt != null){
				rs = st.executeQuery(order_sqlStmt); 
				while(rs.next()){
					mapReqColumnFromDB.put("DB_Order_Number",rs.getString("Order_Number"));
					mapReqColumnFromDB.put("DB_BUID",rs.getString("Global_BUID"));
					mapReqColumnFromDB.put("DB_Order_Type",rs.getString("Order_Type"));
					mapReqColumnFromDB.put("DB_Customer_number",rs.getString("Base_Customer_Account_Number"));
					mapReqColumnFromDB.put("DB_Company_number",rs.getString("Base_Cust_Orig_System_order"));
					mapReqColumnFromDB.put("DB_sales_channel_code",rs.getString("Base_Customer_L8_Channel_Code"));
				}
			}
			
			if(camLocation!=null){
				rs = st.executeQuery(camLocation);
				while(rs.next()){
					mapReqColumnFromDB.put("AddressSeqNumber",rs.getString("CAM_LOCATION_ID"));
				}
			}
			
			if(totalSellingPrice!=null){
				rs = st.executeQuery(totalSellingPrice);
				while(rs.next()){
					mapReqColumnFromDB.put("OrderDisputeAmountMinusTaxes",rs.getString("Total_Price"));
				}
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{ if(con != null){ con.close(); }
			}catch(Exception e){ e.printStackTrace(); }
			try{ if(st != null){ st.close(); }
			}catch(Exception e){ e.printStackTrace(); }
			try{ if(rs != null){ st.close(); }
			}catch(Exception e){ e.printStackTrace(); }
		}
	    return mapReqColumnFromDB;
	}

	public String loadOrderLineItems(String order_number,String environmnet,String sku_details){
		
		StringBuilder builder = new StringBuilder();
		try{
			String filePath = "./Scripts/Order_Lines.sql";
			String sql_stmt = this.getSQLForVaringSkus(filePath, order_number, sku_details);
			con = getConnection(environmnet);
			st = con.createStatement();
			rs = st.executeQuery(sql_stmt);
			builder.append("<inp1:Items>");
			
		while(rs.next()){
			String desc = rs.getString("Sku_Description").toString().trim();
			int splCharIndex = 0;
			if((splCharIndex = desc.indexOf("&")) > 0){
				desc = desc.substring(0,splCharIndex)+"&#38;"+desc.substring(splCharIndex+1);
			} else if((splCharIndex = desc.indexOf("<")) > 0){
				desc = desc.substring(0,splCharIndex)+"&lt;"+desc.substring(splCharIndex+1);
			} else if((splCharIndex = desc.indexOf(">")) > 0){
				desc = desc.substring(0,splCharIndex)+"&gt;"+desc.substring(splCharIndex+1);
			} else if((splCharIndex = desc.indexOf("'")) > 0){
				desc = desc.substring(0,splCharIndex)+"&#39;"+desc.substring(splCharIndex+1);
			} else if((splCharIndex = desc.indexOf("\"")) > 0){
				desc = desc.substring(0,splCharIndex)+"&#34;"+desc.substring(splCharIndex+1);
			}
			
			String inDate = rs.getString("SKU_EOLDATE").toString().trim();
			System.out.println("INPUT DATE: "+inDate);
			SimpleDateFormat generatedDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.US);
			generatedDateTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			
			String	outputString = "<ns0:OrderLineItem xmlns:ns0="+
				"\"http://www.dell.com/it/services/isp/dispatch/dataModel/3.0/\""+
				" LineNumber=\""+rs.getInt("Line_Number")+"\">"+
			    "<ns0:Tie>"+rs.getString("Line_Number")+"</ns0:Tie>"+
				"<ns0:Part>"+
				"<ns0:PartID/>"+
				"<ns0:SKUNumber>"+rs.getString("SKU_Number").toString().trim()+"</ns0:SKUNumber>"+
				"<ns0:Description>"+desc+"</ns0:Description>"+
				"<ns0:Price>"+rs.getDouble("Selling_Price")+"</ns0:Price>"+
				"</ns0:Part>"+
				"<ns0:Quantity>"+rs.getInt("Ord_Qty")+"</ns0:Quantity>"+
				"<ns0:DisputeAmount>"+rs.getDouble("Selling_Price")+"</ns0:DisputeAmount>"+
				"<ns0:ReceivedDateTime>"+rs.getString("SKU_EOLDATE")+"</ns0:ReceivedDateTime>"+
				"</ns0:OrderLineItem>";
			
				builder.append(outputString);
		}
		 builder.append("</inp1:Items>");
		
	    } catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{ if(con != null){ con.close(); }
			}catch(Exception e){ e.printStackTrace(); }
			try{ if(st != null){ st.close(); }
			}catch(Exception e){ e.printStackTrace(); }
			try{ if(rs != null){ st.close(); }
			}catch(Exception e){ e.printStackTrace(); }
		}
		return builder.toString();
	}

	public String[] loadPaymentTerm(String order_number,String environment){
		
		String[] paymentTermsAndCodesArray = new String[2];
		StringBuilder payTermBuilder = new StringBuilder();
		StringBuilder payCodeBuilder = new StringBuilder();
		
		try{
			String filePath = "./Scripts/Payment_term_Paycode_value_Amount.sql";
			String sql_stmt = this.getSQLFromFile(filePath,order_number);
			con = getConnection(environment);
			st = con.createStatement();
			rs = st.executeQuery(sql_stmt);
			payTermBuilder.append("<inp1:PaymentTerms>");
			payCodeBuilder.append("<inp1:Paycodes>");
	
			while(rs.next()){
				String payCode = rs.getString("PAYCODE").toString().trim();
				String paymentTermDesc = null;
				if(payCode.equalsIgnoreCase("V")){
					paymentTermDesc = "Credit/Deb.Card";
				} else if(payCode.equalsIgnoreCase("P") || payCode.equalsIgnoreCase("W")){
					paymentTermDesc = "Prepaid";
				} else {
					paymentTermDesc = "Credit/Deb.Card";
				}
				double payAmount = rs.getDouble("AMOUNT");
				
				String	paymentTermString = "<ns0:PaymentTerm xmlns:ns0="+
				"\"http://www.dell.com/it/services/isp/dispatch/dataModel/3.0/\">"+
				    "<ns0:Code>"+paymentTermDesc+"</ns0:Code>"+
					"<ns0:Amount>"+payAmount+"</ns0:Amount>"+
					"<ns0:Description>"+paymentTermDesc+"</ns0:Description>"+
					"</ns0:PaymentTerm>";
				payTermBuilder.append(paymentTermString);
				
			 	String	payCodeString = "<ns0:Paycode xmlns:ns0="+
					"\"http://www.dell.com/it/services/isp/dispatch/dataModel/3.0/\">"+
				    "<ns0:Code>"+payCode+"</ns0:Code>"+
					"<ns0:Amount>"+payAmount+"</ns0:Amount>"+
					"<ns0:Description>"+paymentTermDesc+"</ns0:Description>"+
					"</ns0:Paycode>";
			 	payCodeBuilder.append(payCodeString);
					
		    } 
			payTermBuilder.append("</inp1:PaymentTerms>");
			payCodeBuilder.append("</inp1:Paycodes>"); 
		
			paymentTermsAndCodesArray[0] = payTermBuilder.toString();
			paymentTermsAndCodesArray[1] = payCodeBuilder.toString();
		   
	    } catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{ if(con != null){ con.close(); }
			}catch(Exception e){ e.printStackTrace(); }
			try{ if(st != null){ st.close(); }
			}catch(Exception e){ e.printStackTrace(); }
			try{ if(rs != null){ st.close(); }
			}catch(Exception e){ e.printStackTrace(); }
		}
		return paymentTermsAndCodesArray;
	}

}

