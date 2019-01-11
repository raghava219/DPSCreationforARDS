package com.utility.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.dao.utility.CRAType;
import com.database.utility.DBConnection;
import com.excel.utility.ExcelFileReader;

public class XMLModification {

	private ExcelFileReader dataFromExcel = null;
	private DBConnection dataFromDb = null;
	private Map<String,CRAType> mapOrderColumnCRATypeColumn;
	private Map<String,String> finalXmlMapColAndKeyValues = new LinkedHashMap<String,String>();
	private String orderLineItems;
	private String[] payTermAndCodeDetails;
	
	public XMLModification(){
		dataFromExcel = new ExcelFileReader();
		dataFromDb = new DBConnection();
	}
	
	public DBConnection getDataFromDb() {
		return dataFromDb;
	}
	public ExcelFileReader getDataFromExcel() {
		return dataFromExcel;
	}
	public String getOrderLineItems() {
		return orderLineItems;
	}

	public void setOrderLineItems(String orderLineItems) {
		this.orderLineItems = orderLineItems;
	}


	public String[] getPayTermAndCodeDetails() {
		return payTermAndCodeDetails;
	}

	public void setPayTermAndCodeDetails(String[] payTermAndCodeDetails) {
		this.payTermAndCodeDetails = payTermAndCodeDetails;
	}

	public Map<String, CRAType> getMapOrderColumnCRATypeColumn() {
		return mapOrderColumnCRATypeColumn;
	}

	public void setMapOrderColumnCRATypeColumn(
			Map<String, CRAType> mapOrderColumnCRATypeColumn) {
		this.mapOrderColumnCRATypeColumn = mapOrderColumnCRATypeColumn;
	}

	public void createMapOfXmlElementsWithDbColumnValues(Map<String,String> mapXmlColumnKeyColumn, Map<String,String> mapReqColumnFromDB){
		for(String XmlElement : mapXmlColumnKeyColumn.keySet()){
			finalXmlMapColAndKeyValues.put(XmlElement, mapXmlColumnKeyColumn.get(XmlElement));
		}
		for(String dbXmlElement : mapReqColumnFromDB.keySet()){
			finalXmlMapColAndKeyValues.put(dbXmlElement, mapReqColumnFromDB.get(dbXmlElement));
		}
	}
	
	public static Node createNodeFromXMLString(String xml) throws Exception {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		return dBuilder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")))
        .getDocumentElement();
}
	
	public void finalXmlModification(String outLocation, String order_number,CRAType craType) throws Exception{
		
		try {
		
		String XmlFileToBeModified = finalXmlMapColAndKeyValues.get("XmlFile");
		System.out.println("File To Be Modified"+XmlFileToBeModified);
		
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(XmlFileToBeModified);
		XPath xpath = XPathFactory.newInstance().newXPath();
		doc.getDocumentElement().normalize();
		
	
		
		NodeList GeneratedDateTime_Xpath = (NodeList)xpath.evaluate("//*[local-name()='GeneratedDateTime']",doc,XPathConstants.NODESET);
		NodeList DispatchCreatedDateTime_Xpath = (NodeList)xpath.evaluate("//*[local-name()='DispatchCreatedDateTime']",doc,XPathConstants.NODESET);
		NodeList OrderLineItem_Xpath = (NodeList)xpath.evaluate("//*[local-name()='OrderLineItem']",doc,XPathConstants.NODESET);
		NodeList Technician_Xpath = (NodeList)xpath.evaluate("//*[local-name()='Technician']",doc,XPathConstants.NODESET);
		NodeList Order_Xpath = (NodeList)xpath.evaluate("//*[local-name()='Order']",doc,XPathConstants.NODESET);
		NodeList DispatchID_Xpath = (NodeList)xpath.evaluate("//*[local-name()='DispatchID']",doc,XPathConstants.NODESET);
		NodeList DispatchStatusDate_Xpath = (NodeList)xpath.evaluate("//*[local-name()='DispatchStatusDate']",doc,XPathConstants.NODESET);
		NodeList EventDateTime_Xpath = (NodeList)xpath.evaluate("//*[local-name()='EventDateTime']",doc,XPathConstants.NODESET);
		NodeList DispatchOfferAttributes_Xpath = (NodeList)xpath.evaluate("//*[local-name()='DispatchOfferAttributes']",doc,XPathConstants.NODESET);
		NodeList DisputeReasonCode_Xpath = (NodeList)xpath.evaluate("//*[local-name()='DisputeReasonCode']",doc,XPathConstants.NODESET);
		
		NodeList BUID_Xpath = (NodeList)xpath.evaluate("//*[local-name()='BUID']",doc,XPathConstants.NODESET);
		NodeList CustomerNumber_Xpath = (NodeList)xpath.evaluate("//*[local-name()='CustomerNumber']",doc,XPathConstants.NODESET);
		NodeList CompanyNumber_Xpath = (NodeList)xpath.evaluate("//*[local-name()='CompanyNumber']",doc,XPathConstants.NODESET);
		NodeList SalesChannel_Xpath = (NodeList)xpath.evaluate("//*[local-name()='SalesChannel']",doc,XPathConstants.NODESET);
		NodeList PaymentTerm_Xpath = (NodeList)xpath.evaluate("//*[local-name()='PaymentTerm']",doc,XPathConstants.NODESET);
		NodeList Paycode_Xpath = (NodeList)xpath.evaluate("//*[local-name()='Paycode']",doc,XPathConstants.NODESET);		
		NodeList AddressSeqNumber_Xpath = (NodeList)xpath.evaluate("//*[local-name()='AddressSeqNumber']",doc,XPathConstants.NODESET);
		NodeList Company_Xpath = (NodeList)xpath.evaluate("//*[local-name()='Company']",doc,XPathConstants.NODESET);
		
		Node OrderDisputeAmountMinusTaxes_Xpath = (Node)xpath.evaluate("//*[local-name()='OrderDisputeAmountMinusTaxes']",doc,XPathConstants.NODE); 
		Node CreditFreightFlag_Xpath = (Node)xpath.evaluate("//*[local-name()='CreditFreightFlag']",doc,XPathConstants.NODE);
		Node ForceFreightFlag_Xpath = (Node)xpath.evaluate("//*[local-name()='ForceFreightFlag']",doc,XPathConstants.NODE);
		Node ForceToManualReview_Xpath = (Node)xpath.evaluate("//*[local-name()='ForceToManualReview']",doc,XPathConstants.NODE);
		Node RefundOnly_Xpath = (Node)xpath.evaluate("//*[local-name()='RefundOnly']",doc,XPathConstants.NODE);
		Node NoCreditMemo_Xpath = (Node)xpath.evaluate("//*[local-name()='NoCreditMemo']",doc,XPathConstants.NODE);
		Node CreditDellOnly_Xpath = (Node)xpath.evaluate("//*[local-name()='CreditDellOnly']",doc,XPathConstants.NODE);
		Node TaxOnly_Xpath = (Node)xpath.evaluate("//*[local-name()='TaxOnly']",doc,XPathConstants.NODE);
		Node NoTaxAllowed_Xpath = (Node)xpath.evaluate("//*[local-name()='NoTaxAllowed']",doc,XPathConstants.NODE);
		Node CheckThreshold_Xpath = (Node)xpath.evaluate("//*[local-name()='CheckThreshold']",doc,XPathConstants.NODE);
		Node PercentageFlag_Xpath = (Node)xpath.evaluate("//*[local-name()='PercentageFlag']",doc,XPathConstants.NODE);
		
		
		//http://stackoverflow.com/questions/8405087/what-is-this-date-format-2011-08-12t201746-384z
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat generatedDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", java.util.Locale.US);
		//generatedDateTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		String currentTimeStamp = generatedDateTimeFormat.format(timestamp);
		
		
		GeneratedDateTime_Xpath.item(0).setTextContent(generatedDateTimeFormat.format(timestamp));
		DispatchCreatedDateTime_Xpath.item(0).setTextContent(generatedDateTimeFormat.format(timestamp));
		// a.	Change the LoginID as NT Login name and Badge Number as BUID value
		
		NodeList childNodesOfTechnician_Xpath = Technician_Xpath.item(0).getChildNodes();
		for (int tech = 0; tech < childNodesOfTechnician_Xpath.getLength(); tech++) {
			Node childNode = childNodesOfTechnician_Xpath.item(tech);
				if(childNode.getNodeType() == Node.ELEMENT_NODE){
					for(String finalKey: finalXmlMapColAndKeyValues.keySet()){
						if(childNode.getNodeName().contains(finalKey)){
							childNode.setTextContent(finalXmlMapColAndKeyValues.get(finalKey));
						}
					}
				}
		}
		
		//b.	Change the Order number, BU_ID, Order Type, and Source_System based on Original Order 
		NodeList childNodesOfOrder_Xpath = Order_Xpath.item(0).getChildNodes();
		for (int order = 0; order < childNodesOfOrder_Xpath.getLength(); order++) {
			Node childNode = childNodesOfOrder_Xpath.item(order);
				if(childNode.getNodeType() == Node.ELEMENT_NODE){
					System.out.println("Order chiled Node Name "+childNode.getNodeName());
					String nodeName = childNode.getNodeName(); 
						if(nodeName.equals("ns0:Number")){
							childNode.setTextContent(finalXmlMapColAndKeyValues.get("DB_Order_Number"));
						} else if(nodeName.equals("ns0:BUID")){
							childNode.setTextContent(finalXmlMapColAndKeyValues.get("DB_BUID"));
						} else if(nodeName.equals("ns0:Type")){
							if(craType.getDPSType().equals("CRA")){
								childNode.setTextContent("US Credit Good Returned");
							} else if(craType.getDPSType().equals("MCT")){
								childNode.setTextContent("MCT");
							}
						} else if(nodeName.equals("ns0:SourceSystem")){
							childNode.setTextContent("GCM_DP");
						}
				}
		}
		
		//c.	Enter the Unique DPS number 
		DispatchID_Xpath.item(0).setTextContent(finalXmlMapColAndKeyValues.get("DispatchID"));
		DispatchStatusDate_Xpath.item(0).setTextContent(currentTimeStamp);
		EventDateTime_Xpath.item(0).setTextContent(currentTimeStamp);
		
		/* Doubt on this requirement	
		 	d. Enter  “DPSTYPE” : CRA
				Note: For whichever DPS needs to create enter particular DPS TYPE
			e. Enter ISPActivityProcessType: for CRA Order as:” CRA - Credit Return Authorization”
				Note:use the excel sheet for different process type
			f.	Enter ISPActivitySubType for CRA Order as: Credit with Return

		*/
		
		NodeList childNodesOfDispatchOfferAttributes_Xpath = DispatchOfferAttributes_Xpath.item(0).getChildNodes();
		
		for (int dispatch = 0; dispatch < childNodesOfDispatchOfferAttributes_Xpath.getLength(); dispatch++) {
			Node childNode = childNodesOfDispatchOfferAttributes_Xpath.item(dispatch);
				if(childNode.getNodeType() == Node.ELEMENT_NODE){
					  Element childElement = (Element)childNode;
					  String attValue =  childElement.getAttribute("Name");
					  if(attValue.equals("DPS Type")){
						  childElement.setTextContent(craType.getDPSType());
				  	  }else if(attValue.equals("ProductReturn")){
				  		  childElement.setTextContent(craType.getProductReturn());
				  	  }else if(attValue.equals("ISPActivityProcessType")){
				  		  childElement.setTextContent(craType.getISPActivityProcessType());
				  	  }else if(attValue.equals("ISPActivitySubType")){
				  		  childElement.setTextContent(craType.getISPActivitySubType());
				  	  }
				}
		}
		
		
		/*g.	Enter Customer number(Get customer number from Original Order)
			To get Customer number: Login to Omega DB for particular env and run attached query
		h.	Enter Company Number(Get company number from Original Order) -- doubt on column
		•	To get Company number: Login to Omega DB for particular env and run attached query*/
		
			for (int buid = 0; buid < BUID_Xpath.getLength(); buid++) {
				Node subNode = BUID_Xpath.item(buid);
					if(subNode.getNodeType() == Node.ELEMENT_NODE){
							if(subNode.getParentNode().getNodeName().contains("Technician")){
								continue;
							}
						subNode.setTextContent(finalXmlMapColAndKeyValues.get("DB_BUID"));
					}
			}
			CustomerNumber_Xpath.item(0).setTextContent(finalXmlMapColAndKeyValues.get("DB_Customer_number").substring(2));
			CompanyNumber_Xpath.item(0).setTextContent(finalXmlMapColAndKeyValues.get("DB_sales_channel_code"));
		
		//i.	Enter Dispute Reason Code based on process type use attached excel sheet
		
			DisputeReasonCode_Xpath.item(0).setTextContent(mapOrderColumnCRATypeColumn.get(order_number).getDisputeReasonCode());
		
		/*j.	Enter Order Dispute Amount Minus Taxes value for line item which we are going to create the credit request.
				1.	Login to Omega DB for particular env  and run attached query to get line item and price value details*/
		
			OrderDisputeAmountMinusTaxes_Xpath.setTextContent(finalXmlMapColAndKeyValues.get("OrderDisputeAmountMinusTaxes"));
			
		//k.	Enter Sales Channel of Original order
			SalesChannel_Xpath.item(0).setTextContent(finalXmlMapColAndKeyValues.get("DB_sales_channel_code"));
		
		//l.	Change the Credit freight flag either Y/N
		CreditFreightFlag_Xpath.setTextContent(finalXmlMapColAndKeyValues.get("CreditFreightFlag"));
		ForceFreightFlag_Xpath.setTextContent(finalXmlMapColAndKeyValues.get("ForceFreightFlag"));
		ForceToManualReview_Xpath.setTextContent(finalXmlMapColAndKeyValues.get("ForceToManualReview"));
		RefundOnly_Xpath.setTextContent(finalXmlMapColAndKeyValues.get("RefundOnly"));
		NoCreditMemo_Xpath.setTextContent(finalXmlMapColAndKeyValues.get("NoCreditMemo"));
		CreditDellOnly_Xpath.setTextContent(finalXmlMapColAndKeyValues.get("CreditDellOnly"));
		TaxOnly_Xpath.setTextContent(finalXmlMapColAndKeyValues.get("TaxOnly"));
		NoTaxAllowed_Xpath.setTextContent(finalXmlMapColAndKeyValues.get("NoTaxAllowed"));
		CheckThreshold_Xpath.setTextContent(finalXmlMapColAndKeyValues.get("CheckThreshold"));
		PercentageFlag_Xpath.setTextContent(finalXmlMapColAndKeyValues.get("PercentageFlag"));
        
		
		//m.	Select the line items from Original order for which we are going to return the line items
	 	   if(OrderLineItem_Xpath.item(0) != null){
			   System.out.println(OrderLineItem_Xpath.item(0).getNodeName());
			   Node parent = OrderLineItem_Xpath.item(0).getParentNode();
			   System.out.println(parent.getNodeName());
			   System.out.println(orderLineItems);
			   Node node = createNodeFromXMLString(orderLineItems);
			   for(int i=0;i<OrderLineItem_Xpath.getLength();i++){
				   parent.removeChild(OrderLineItem_Xpath.item(i));
			   }
			   System.out.println(OrderLineItem_Xpath.item(0).getNodeName());
			   Node importedNode = doc.importNode(node,true);
			   parent.getParentNode().replaceChild(importedNode, parent);  
	 	   }		
		   /*Enter the payment term description, paycode value and Amount from the Original Order
		   a.	Login to Omega DB for particular env  and run below query to get payment term description, 
		   paycode value and Amount for the Original Order
		   */
		   
	 	   if(PaymentTerm_Xpath.item(0) != null){
			   Node OldPaymentTermsNode = PaymentTerm_Xpath.item(0).getParentNode();
			   System.out.println(OldPaymentTermsNode.getNodeName());
			   Node NewPaymentTermsNode = createNodeFromXMLString(payTermAndCodeDetails[0]);
			   for(int i=0;i<PaymentTerm_Xpath.getLength();i++){
				   OldPaymentTermsNode.removeChild(PaymentTerm_Xpath.item(i));
			   }
			   Node importedNewPaymentTermsNode = doc.importNode(NewPaymentTermsNode,true);
			   OldPaymentTermsNode.getParentNode().replaceChild(importedNewPaymentTermsNode, OldPaymentTermsNode);  
	 	   }	

	 	   if(Paycode_Xpath.item(0) != null){
			   Node OldPaycodeNode = Paycode_Xpath.item(0).getParentNode();
			   System.out.println(OldPaycodeNode.getNodeName());
			   Node NewPaycodeNode = createNodeFromXMLString(payTermAndCodeDetails[1]);
			   for(int i=0;i<Paycode_Xpath.getLength();i++){
				   OldPaycodeNode.removeChild(Paycode_Xpath.item(i));
			   }
			   Node importedNewPaycodeNode = doc.importNode(NewPaycodeNode,true);
			   OldPaycodeNode.getParentNode().replaceChild(importedNewPaycodeNode, OldPaycodeNode);  
	 	   }
		   
		   //n.	Need to change the Customer cam location ID for Bill to ship to address
		   for (int addSeqNum = 0; addSeqNum < AddressSeqNumber_Xpath.getLength(); addSeqNum++) {
				Node addnode = AddressSeqNumber_Xpath.item(addSeqNum);
					if(addnode.getNodeType() == Node.ELEMENT_NODE){
						addnode.setTextContent(finalXmlMapColAndKeyValues.get("AddressSeqNumber").substring(2));
					}
			}
		   
		   NodeList childNodesCompany_Xpath = Company_Xpath.item(0).getChildNodes();
			for (int order = 0; order < childNodesCompany_Xpath.getLength(); order++) {
				Node childNode = childNodesCompany_Xpath.item(order);
					if(childNode.getNodeType() == Node.ELEMENT_NODE){
						System.out.println("Order chiled Node Name "+childNode.getNodeName());
						String nodeName = childNode.getNodeName(); 
							if(nodeName.equals("ns0:Number")){
								childNode.setTextContent(finalXmlMapColAndKeyValues.get("DB_sales_channel_code"));
							}					
					}
			}
		   
		   
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		transformer.setOutputProperty(javax.xml.transform.OutputKeys.OMIT_XML_DECLARATION, "yes");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(outLocation+File.separator+order_number+"_"+craType.getDPSType()+".xml"));
		transformer.transform(source, result);
		System.out.println("Done");
		
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	
	
	/*public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
	     
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
         * 
		    XMLModification xmlMod = new XMLModification();
		    try{
		    	System.out.println("Please enter the location of the file.");	
			    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));	
				String excelConfigFile = br.readLine();
			System.out.println(excelConfigFile);
		    mapXmlColumnKeyColumn = dataFromExcel.loadXmlAndKeyColumns(excelConfigFile);
		    mapOrderColumnCRATypeColumn = dataFromExcel.getMapOrderCRAType();
		    for(String orderNumber : mapOrderColumnCRATypeColumn.keySet()){
		    	CRAType craType = mapOrderColumnCRATypeColumn.get(orderNumber);
		    	if(craType.getPartial().equals("Y")){
		    		mapReqColumnFromDB = dataFromDb.loadOrderCustomerDetails(orderNumber,craType.getSKU(),craType.getEnvironment());
		    	}else{
		    		mapReqColumnFromDB = dataFromDb.loadOrderCustomerDetails(orderNumber,null,craType.getEnvironment());
		    	}
				orderLineItems = dataFromDb.loadOrderLineItems(orderNumber,craType.getEnvironment(),craType.getSKU());
				payTermAndCodeDetails = dataFromDb.loadPaymentTerm(orderNumber,craType.getEnvironment());
				xmlMod.createMapOfXmlElementsWithDbColumnValues();
				xmlMod.finalXmlModification(mapXmlColumnKeyColumn.get("OutputFileLocation"),orderNumber,mapOrderColumnCRATypeColumn.get(orderNumber));
		    }
			
		    }catch(Exception e){
				e.printStackTrace();
			}
			
	}*/		
}
