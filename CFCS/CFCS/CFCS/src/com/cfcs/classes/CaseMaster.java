package com.cfcs.classes;

import com.google.gson.annotations.SerializedName;

public class CaseMaster {

	@SerializedName("CaseID")
	private int caseID;

	@SerializedName("CustomerID")
	private int customerID;

	@SerializedName("ManualCaseID")
	private String manualCaseID;

	@SerializedName("CaseTitle")
	private String caseTitle;

	@SerializedName("CaseDetail")
	private String caseDetail;

	@SerializedName("CaseStatus")
	private String caseStatus;

	@SerializedName("AddDate")
	private String adddate;

	@SerializedName("UpdateDate")
	private String updateDate;

	@SerializedName("UpdateBy")
	private String updateby;

	@SerializedName("Active")
	private String active;

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public int getCaseID() {
		return caseID;
	}

	public void setCaseID(int caseID) {
		this.caseID = caseID;
	}

	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	public String getManualCaseID() {
		return manualCaseID;
	}

	public void setManualCaseID(String manualCaseID) {
		this.manualCaseID = manualCaseID;
	}

	public String getCaseTitle() {
		return caseTitle;
	}

	public void setCaseTitle(String caseTitle) {
		this.caseTitle = caseTitle;
	}

	public String getCaseDetail() {
		return caseDetail;
	}

	public void setCaseDetail(String caseDetail) {
		this.caseDetail = caseDetail;
	}

	public String getCaseStatus() {
		return caseStatus;
	}

	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getAdddate() {
		return adddate;
	}

	public void setAdddate(String adddate) {
		this.adddate = adddate;
	}

	public String getUpdateby() {
		return updateby;
	}

	public void setUpdateby(String updateby) {
		this.updateby = updateby;
	}

}
