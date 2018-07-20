package com.cfcs.classes;

import com.google.gson.annotations.SerializedName;

public class DeleteReminder {

	@SerializedName("ReminderID")
	private String reminderID;

	public String getReminderID() {
		return reminderID;
	}

	public void setReminderID(String reminderID) {
		this.reminderID = reminderID;
	}
}
