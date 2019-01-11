package com.utility.xml;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

import com.dao.utility.CRAType;
import com.database.utility.DBConnection;
import com.excel.utility.ExcelFileReader;

public class MainClass {
	
	private static ExcelFileReader dataFromExcel = null;
	private static DBConnection dataFromDb = null;
	private static Map<String,String> mapXmlColumnKeyColumn;
	private static Map<String,CRAType> mapOrderColumnCRATypeColumn;
	private static Map<String,String> mapReqColumnFromDB;
	private static String orderLineItems;
	private static String[] payTermAndCodeDetails;
	
	public MainClass(){
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
	     /*
        *1. Fetch the Data for the columns we are looking for from the DB. 
        *2. Store above data as key/value pairs in Map.  
        *3. Create another Map of XMLElement(as keys) to be Modfied and corresponding 
        *   DB Columns Names(values) 
        *4. Above two loops need be itterated to replace the Values(Column_Name) 
        *   in Map2 with Values(from db) in Map1.  
        *5. Final itterate Map2 and using XPath/XQuery locate them in XML 
        *   and replace the values of those found Elements with corresponding 
        *   values in Map2. 
        * 
        * how to remove child node in xml using java
        *   
        *6. Same Program need to following 3 different Xml files 
        *	For CRA Order type: DPS_CGR.xml
        *  For MCR Credit Order type: MCR_Credit_Debit.xml
        *  For MCT Credit Order type: MCT_Credit.xml
        *  
        *7. https://docs.oracle.com/javase/tutorial/jaxp/dom/readingXML.html   
        *
        *	 http://stackoverflow.com/questions/10512183/wrong-document-error-while-appending-a-node-in-xml
        *
        * */
		    XMLModification xmlMod = new XMLModification();
		    try{
		    	
		    	System.out.println("Please enter the location of the file.");	
			    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));	
				String excelConfigFile = br.readLine();
				System.out.println(excelConfigFile);
				dataFromExcel = xmlMod.getDataFromExcel();
				dataFromDb = xmlMod.getDataFromDb();
				
			    mapXmlColumnKeyColumn = dataFromExcel.loadXmlAndKeyColumns(excelConfigFile);
			    mapOrderColumnCRATypeColumn = dataFromExcel.getMapOrderCRAType();
			    xmlMod.setMapOrderColumnCRATypeColumn(mapOrderColumnCRATypeColumn);
			    
			    for(String orderNumber : mapOrderColumnCRATypeColumn.keySet()){
			    	CRAType craType = mapOrderColumnCRATypeColumn.get(orderNumber);
			    
			    	if(craType.getPartial().equals("Y")){
			    		mapReqColumnFromDB = dataFromDb.loadOrderCustomerDetails(orderNumber,craType.getSKU(),craType.getEnvironment());
			    	}else{
			    		mapReqColumnFromDB = dataFromDb.loadOrderCustomerDetails(orderNumber,null,craType.getEnvironment());
			    	}
					
			    	orderLineItems = dataFromDb.loadOrderLineItems(orderNumber,craType.getEnvironment(),craType.getSKU());
			    	xmlMod.setOrderLineItems(orderLineItems);
					payTermAndCodeDetails = dataFromDb.loadPaymentTerm(orderNumber,craType.getEnvironment());
					xmlMod.setPayTermAndCodeDetails(payTermAndCodeDetails);
					
					xmlMod.createMapOfXmlElementsWithDbColumnValues(mapXmlColumnKeyColumn,mapReqColumnFromDB);
					
					xmlMod.finalXmlModification(mapXmlColumnKeyColumn.get("OutputFileLocation"),orderNumber,mapOrderColumnCRATypeColumn.get(orderNumber));
			    }
			    
		    }catch(Exception e){
				e.printStackTrace();
			}


	}

}
