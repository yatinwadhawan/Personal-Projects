package com.cfcs.classes;

import com.google.gson.annotations.SerializedName;

public class GroupMaster {

	@SerializedName("GroupID")
	private int groupID;

	@SerializedName("GroupName")
	private String groupName;

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

	public int getGroupID() {
		return groupID;
	}

	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
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
