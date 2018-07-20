package com.cfcs.classes;

import com.google.gson.annotations.SerializedName;

public class CaseUpdate {

	@SerializedName("CaseUpdateID")
	private String caseUpdateID;

	@SerializedName("UpdateTypeID")
	private int updateTypeID;

	@SerializedName("CustomerID")
	private int customerID;

	@SerializedName("CaseID")
	private int caseID;

	@SerializedName("AdvocateID")
	private int advocateID;

	@SerializedName("CourtTypeID")
	private int courtTypeID;

	@SerializedName("Remark")
	private String remark;

	@SerializedName("Attachment")
	private String attachment;

	@SerializedName("WorkDoneBy")
	private int workDoneBy;

	@SerializedName("AppStatus")
	private int appStatus;// 1

	@SerializedName("AppBy")
	private int appBy;

	@SerializedName("AppDateTime")
	private String appDateTime;

	@SerializedName("AddDate")
	private String addDate;

	@SerializedName("UpdateDate")
	private String updateDate;

	@SerializedName("Active")
	private String active;

	@SerializedName("IsChargable")
	private String isChargeable;

	@SerializedName("Amount")
	private String amount;

	@SerializedName("SrAdvFee")
	private String advocateFee;

	@SerializedName("CaseUpdateDate")
	private String caseupdatedate;

	@SerializedName("MinutSpend")
	private int minuteSpend;

	@SerializedName("IsAdvChargeable")
	private String isAdvChargeable;

	private int updateStamp;

	public String getCaseUpdateID() {
		return caseUpdateID;
	}

	public void setCaseUpdateID(String caseUpdateID) {
		this.caseUpdateID = caseUpdateID;
	}

	public int getUpdateTypeID() {
		return updateTypeID;
	}

	public void setUpdateTypeID(int updateTypeID) {
		this.updateTypeID = updateTypeID;
	}

	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	public int getCaseID() {
		return caseID;
	}

	public void setCaseID(int caseID) {
		this.caseID = caseID;
	}

	public int getAdvocateID() {
		return advocateID;
	}

	public void setAdvocateID(int advocateID) {
		this.advocateID = advocateID;
	}

	public int getCourtTypeID() {
		return courtTypeID;
	}

	public void setCourtTypeID(int courtTypeID) {
		this.courtTypeID = courtTypeID;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public int getWorkDoneBy() {
		return workDoneBy;
	}

	public void setWorkDoneBy(int workDoneBy) {
		this.workDoneBy = workDoneBy;
	}

	public int getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(int appStatus) {
		this.appStatus = appStatus;
	}

	public int getAppBy() {
		return appBy;
	}

	public void setAppBy(int appBy) {
		this.appBy = appBy;
	}

	public String getAppDateTime() {
		return appDateTime;
	}

	public void setAppDateTime(String appDateTime) {
		this.appDateTime = appDateTime;
	}

	public String getAddDate() {
		return addDate;
	}

	public void setAddDate(String addDate) {
		this.addDate = addDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public int getUpdateStamp() {
		return updateStamp;
	}

	public void setUpdateStamp(int updateStamp) {
		this.updateStamp = updateStamp;
	}

	public String getIsChargeable() {
		return isChargeable;
	}

	public void setIsChargeable(String isChargeable) {
		this.isChargeable = isChargeable;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getAdvocateFee() {
		return advocateFee;
	}

	public void setAdvocateFee(String advocateFee) {
		this.advocateFee = advocateFee;
	}

	public String getCaseupdatedate() {
		return caseupdatedate;
	}

	public void setCaseupdatedate(String caseupdatedate) {
		this.caseupdatedate = caseupdatedate;
	}

	public int getMinuteSpend() {
		return minuteSpend;
	}

	public void setMinuteSpend(int minuteSpend) {
		this.minuteSpend = minuteSpend;
	}

	public String getIsAdvChargeable() {
		return isAdvChargeable;
	}

	public void setIsAdvChargeable(String isAdvChargeable) {
		this.isAdvChargeable = isAdvChargeable;
	}

}
