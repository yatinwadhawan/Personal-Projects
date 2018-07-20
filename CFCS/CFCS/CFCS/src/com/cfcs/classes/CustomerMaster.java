package com.cfcs.classes;

import com.google.gson.annotations.SerializedName;

public class CustomerMaster {

	@SerializedName("CustomerID")
	private int customerID;

	@SerializedName("CustomerName")
	private String customerName;

	@SerializedName("MobileNo")
	private String mobileNo;

	@SerializedName("UpdateDate")
	private String updateDate;

	private int updateStamp;

	@SerializedName("Active")
	private String active;

	@SerializedName("ActiveStatus")
	private int activeStatus;

	public int getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(int activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public int getUpdateStamp() {
		return updateStamp;
	}

	public void setUpdateStamp(int updateStamp) {
		this.updateStamp = updateStamp;
	}

}
