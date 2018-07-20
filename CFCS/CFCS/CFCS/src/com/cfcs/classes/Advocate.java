package com.cfcs.classes;

import com.google.gson.annotations.SerializedName;

public class Advocate {

	@SerializedName("AdvocateID")
	private int advocateID;

	@SerializedName("Title")
	private String title;

	@SerializedName("AdvocateName")
	private String advocateName;

	@SerializedName("MobileNo")
	private String mobileNo;

	@SerializedName("MailID")
	private String mailID;

	@SerializedName("Type")
	private int type;

	@SerializedName("UpdateDate")
	private String updateDate;

	private int timestamp;

	@SerializedName("Active")
	private String active;

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public int getAdvocateID() {
		return advocateID;
	}

	public void setAdvocateID(int advocateID) {
		this.advocateID = advocateID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAdvocateName() {
		return advocateName;
	}

	public void setAdvocateName(String advocateName) {
		this.advocateName = advocateName;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getMailID() {
		return mailID;
	}

	public void setMailID(String mailID) {
		this.mailID = mailID;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

}
