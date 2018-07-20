package com.cfcs.classes;

import com.google.gson.annotations.SerializedName;

public class UpdateTypeMaster {

	@SerializedName("UpdateTypeID")
	private int updateTypeID;
	
	@SerializedName("UpdateType")
	private String updateType;

	@SerializedName("UpdateDate")
	private String updateStamp;

	@SerializedName("Active")
	private String active;

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public int getUpdateTypeID() {
		return updateTypeID;
	}

	public void setUpdateTypeID(int updateTypeID) {
		this.updateTypeID = updateTypeID;
	}

	public String getUpdateType() {
		return updateType;
	}

	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}

	public String getUpdateStamp() {
		return updateStamp;
	}

	public void setUpdateStamp(String updateStamp) {
		this.updateStamp = updateStamp;
	}

}
