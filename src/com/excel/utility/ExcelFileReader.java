package com.excel.utility;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.dao.utility.CRAType;

public class ExcelFileReader{

	//private static final String  ExcelConfigFile = "C:\\Raghava\\Development\\A_SelfThoughProject\\ARDS_DPS_Creation\\Product_return_flag_Reason_Code.xlsx";
	private final Map<String,String> mapXmlColumnKeyColumn;
	private final Map<String,CRAType> mapOrderCRAType;
	
	CRAType craType;
	
	public ExcelFileReader(){
		mapXmlColumnKeyColumn = new ConcurrentHashMap<String,String>();
		mapOrderCRAType = new ConcurrentHashMap<String,CRAType>();
	}

	public Map<String, CRAType> getMapOrderCRAType() {
		return mapOrderCRAType;
	}

	
	private Map<String,CRAType> loadOrderNumberCRAType(String order_number, String DpsInfoCommaSep) throws Exception{

		String[] dpsInfo = DpsInfoCommaSep.split("/+");
		System.out.println(dpsInfo.toString());
		CRAType craType = new CRAType();
		
				for(int i=0; i<dpsInfo.length; i++){
					if(i == 0){
				    	order_number = dpsInfo[0];
						System.out.println("order_number "+order_number);
					}else if(i == 1){
	    				craType.setEnvironment(dpsInfo[1]);				
					}else if(i == 2){
    					craType.setDPSType(dpsInfo[2]);
    					this.loadDPSTypeFlags(dpsInfo[2], mapXmlColumnKeyColumn.get(dpsInfo[2]));
					}else if(i == 3){
						craType.setProductReturn(dpsInfo[3]);						
					}else if(i == 4){
						craType.setISPActivityProcessType(dpsInfo[4]);						
					}else if(i == 5){
						craType.setISPActivitySubType(dpsInfo[5]);
					}else if(i == 6){
						craType.setDisputeReasonCode(dpsInfo[6]);
					}else if(i == 7){
						craType.setPartial(dpsInfo[7]);
					}else if(i == 8){
						craType.setSKU(dpsInfo[8]);
					}
				}//end of for
				
			mapOrderCRAType.put(order_number, craType);

		return mapOrderCRAType;
	}

	private void loadDPSTypeFlags(String dps_type, String DpsFlagInfo) throws Exception{

		String[] dpsFlagInfo = DpsFlagInfo.split(",");
		System.out.println(dpsFlagInfo.toString());
		
				for(int i=0; i<dpsFlagInfo.length; i++){
					if(i == 0){
						mapXmlColumnKeyColumn.put("CreditFreightFlag",dpsFlagInfo[i]);
					}else if(i == 1){
						mapXmlColumnKeyColumn.put("ForceFreightFlag",dpsFlagInfo[i]);
					}else if(i == 2){
						mapXmlColumnKeyColumn.put("ForceToManualReview",dpsFlagInfo[i]);
					}else if(i == 3){
						mapXmlColumnKeyColumn.put("RefundOnly",dpsFlagInfo[i]);
					}else if(i == 4){
						mapXmlColumnKeyColumn.put("NoCreditMemo",dpsFlagInfo[i]);
					}else if(i == 5){
						mapXmlColumnKeyColumn.put("CreditDellOnly",dpsFlagInfo[i]);
					}else if(i == 6){
						mapXmlColumnKeyColumn.put("TaxOnly",dpsFlagInfo[i]);
					}else if(i == 7){
						mapXmlColumnKeyColumn.put("NoTaxAllowed",dpsFlagInfo[i]);
					}else if(i == 8){
						mapXmlColumnKeyColumn.put("CheckThreshold",dpsFlagInfo[i]);
					}else if(i == 9){
						mapXmlColumnKeyColumn.put("PercentageFlag",dpsFlagInfo[i]);
					}
				}//end of for
	}
	
	public Map<String,String> loadXmlAndKeyColumns(String ExcelConfigFile) throws Exception{
		
		XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(new File(ExcelConfigFile)));
		XSSFSheet XML_Key_Map_Sheet = myExcelBook.getSheet("XML_Key_Map");
		for (Row row : XML_Key_Map_Sheet) {
			if(row.getRowNum()>0){
				Cell cell_zero = row.getCell(0);
				Cell cell_one = row.getCell(1);
				CellType cellType = cell_one.getCellTypeEnum();
				String str_zero = cell_zero.toString().trim();
				String str_one = null;
				if(cellType == CellType.NUMERIC){
				   int temp = (int) cell_one.getNumericCellValue();
				   str_one = String.valueOf(temp);
				} else if(cellType == CellType.STRING){
					   str_one = cell_one.getStringCellValue().trim();
				}
			    mapXmlColumnKeyColumn.put(str_zero, str_one);
		    }
		}
		
		for(String finalKey: mapXmlColumnKeyColumn.keySet()){
			System.out.println("XmlKey "+finalKey+ " XmlValue "+ mapXmlColumnKeyColumn.get(finalKey));
	    }
		
		for(String finalKey: mapXmlColumnKeyColumn.keySet()){
			if(finalKey.contains("_")){
				String[] orderInfo = finalKey.split("_");
				String finalString = mapXmlColumnKeyColumn.get(finalKey);
				this.loadOrderNumberCRAType(String.valueOf(orderInfo[1]), finalString);
				System.out.println("XmlKey "+String.valueOf(orderInfo[1])+ " XmlValue "+ finalString);
			}
	    }
		
		myExcelBook.close();
		return mapXmlColumnKeyColumn;
		
	}


}
