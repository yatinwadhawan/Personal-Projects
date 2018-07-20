package com.cfcs.classes;

import android.R.bool;

import com.google.gson.annotations.SerializedName;

public class Reminder {

	@SerializedName("ReminderID")
	private String reminderID;

	@SerializedName("ReminderDate")
	private String reminderdate;

	@SerializedName("ReminderTime")
	private String time;

	@SerializedName("CustomerID")
	private int customerid;

	@SerializedName("CaseID")
	private int caseid;

	// @SerializedName("AdvocateID")
	private int advocateid;

	@SerializedName("UpdateTypeID")
	private int updatetypeid;

	@SerializedName("ReminderTitle")
	private String title;

	@SerializedName("ReminderDetail")
	private String detail;

	@SerializedName("Status")
	private int status;

	@SerializedName("AddBy")
	private int addBy;

	@SerializedName("UpdateDate")
	private String updatedate;

	@SerializedName("AddDate")
	private String addDate;

	@SerializedName("Active")
	private String active;

	@SerializedName("RemindAlarm")
	private String reminderAlarm;

	@SerializedName("RemindBefore")
	private int reminderBefore;

	private int updateStamp;

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getReminderID() {
		return reminderID;
	}

	public void setReminderID(String reminderID) {
		this.reminderID = reminderID;
	}

	public String getReminderdate() {
		return reminderdate;
	}

	public void setReminderdate(String reminderdate) {
		this.reminderdate = reminderdate;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getCustomerid() {
		return customerid;
	}

	public void setCustomerid(int customerid) {
		this.customerid = customerid;
	}

	public int getCaseid() {
		return caseid;
	}

	public void setCaseid(int caseid) {
		this.caseid = caseid;
	}

	public int getAdvocateid() {
		return advocateid;
	}

	public void setAdvocateid(int advocateid) {
		this.advocateid = advocateid;
	}

	public int getUpdatetypeid() {
		return updatetypeid;
	}

	public void setUpdatetypeid(int updatetypeid) {
		this.updatetypeid = updatetypeid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getAddBy() {
		return addBy;
	}

	public void setAddBy(int addBy) {
		this.addBy = addBy;
	}

	public String getAddDate() {
		return addDate;
	}

	public void setAddDate(String addDate) {
		this.addDate = addDate;
	}

	public String getUpdatedate() {
		return updatedate;
	}

	public void setUpdatedate(String updatedate) {
		this.updatedate = updatedate;
	}

	public int getReminderBefore() {
		return reminderBefore;
	}

	public void setReminderBefore(int reminderBefore) {
		this.reminderBefore = reminderBefore;
	}

	public int getUpdateStamp() {
		return updateStamp;
	}

	public void setUpdateStamp(int updateStamp) {
		this.updateStamp = updateStamp;
	}

	public String getReminderAlarm() {
		return reminderAlarm;
	}

	public void setReminderAlarm(String reminderAlarm) {
		this.reminderAlarm = reminderAlarm;
	}

}
