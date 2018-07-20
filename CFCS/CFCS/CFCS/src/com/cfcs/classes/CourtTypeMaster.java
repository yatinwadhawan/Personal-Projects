package com.cfcs.classes;

import com.google.gson.annotations.SerializedName;

public class CourtTypeMaster {

	@SerializedName("CourtTypeID")
	private int courtTypeId;

	@SerializedName("CourtType")
	private String courtType;

	@SerializedName("UpdateDate")
	private String updateDate;

	@SerializedName("Active")
	private String active;

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public int getCourtTypeId() {
		return courtTypeId;
	}

	public void setCourtTypeId(int courtTypeId) {
		this.courtTypeId = courtTypeId;
	}

	public String getCourtType() {
		return courtType;
	}

	public void setCourtType(String courtType) {
		this.courtType = courtType;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

}
