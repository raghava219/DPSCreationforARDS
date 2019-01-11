package com.dao.utility;

import java.io.Serializable;

public class CRAType implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8322822341627670466L;
	private String environment = null;
	private String DPSType = null;	
	private String ProductReturn = null;	
	private String ISPActivityProcessType = null;	
	private String ISPActivitySubType = null;	
	private String DisputeReasonCode = null;	
	private String Partial = null;	
	private String SKU = null;
	
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public String getDPSType() {
		return DPSType;
	}
	public void setDPSType(String dpsType) {
		DPSType = dpsType;
	}
	public String getProductReturn() {
		return ProductReturn;
	}
	public void setProductReturn(String productReturn) {
		ProductReturn = productReturn;
	}
	public String getISPActivityProcessType() {
		return ISPActivityProcessType;
	}
	public void setISPActivityProcessType(String ispActivityProcessType) {
		ISPActivityProcessType = ispActivityProcessType;
	}
	public String getISPActivitySubType() {
		return ISPActivitySubType;
	}
	public void setISPActivitySubType(String ispActivitySubType) {
		ISPActivitySubType = ispActivitySubType;
	}
	public String getDisputeReasonCode() {
		return DisputeReasonCode;
	}
	public void setDisputeReasonCode(String disputeReasonCode) {
		DisputeReasonCode = disputeReasonCode;
	}
	public String getPartial() {
		return Partial;
	}
	public void setPartial(String partial) {
		Partial = partial;
	}
	public String getSKU() {
		return SKU;
	}
	public void setSKU(String sku) {
		SKU = sku;
	}
}
