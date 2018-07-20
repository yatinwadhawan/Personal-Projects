package com.cfcs.classes;

import com.google.gson.annotations.SerializedName;

public class GroupRelation {

	@SerializedName("GroupID")
	private int groupID;
	
	@SerializedName("MemberID")
	private int memberID;

	public int getGroupID() {
		return groupID;
	}

	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}

	public int getMemberID() {
		return memberID;
	}

	public void setMemberID(int memberID) {
		this.memberID = memberID;
	}

}
