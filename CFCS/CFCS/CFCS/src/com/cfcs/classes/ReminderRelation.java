package com.cfcs.classes;

import com.google.gson.annotations.SerializedName;

public class ReminderRelation {

	@SerializedName("ReminderID")
	private String reminderId;

	@SerializedName("UserID")
	private int userId;

	public String getReminderId() {
		return reminderId;
	}

	public void setReminderId(String reminderId) {
		this.reminderId = reminderId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
