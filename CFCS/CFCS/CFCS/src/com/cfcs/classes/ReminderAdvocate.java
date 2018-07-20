package com.cfcs.classes;

import com.google.gson.annotations.SerializedName;

public class ReminderAdvocate {

	@SerializedName("ReminderID")
	private String reminderId;

	@SerializedName("AdvocateID")
	private int advocateId;

	public String getReminderId() {
		return reminderId;
	}

	public void setReminderId(String reminderId) {
		this.reminderId = reminderId;
	}

	public int getAdvocateId() {
		return advocateId;
	}

	public void setAdvocateId(int advocateId) {
		this.advocateId = advocateId;
	}

}
