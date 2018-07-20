package com.cfcs.classes;

import com.google.gson.annotations.SerializedName;

public class AdminUser {

	@SerializedName("UserID")
	private int userid;

	@SerializedName("RoleID")
	private int roleID;

	@SerializedName("Name")
	private String name;

	@SerializedName("MobileNo")
	private String mobileNo;

	@SerializedName("UserName")
	private String UserName;

	@SerializedName("Password")
	private String password;

	@SerializedName("EmailID")
	private String emailID;

	@SerializedName("LastUpdate")
	private String lastUpdate;

	@SerializedName("Active")
	private String active;

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	private int timestamp;

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getRoleID() {
		return roleID;
	}

	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}
	
	

}
