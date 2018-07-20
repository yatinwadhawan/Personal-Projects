package com.cfcs.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import com.cfcs.classes.GroupRelation;

public class GroupRelationDao {

	private String table_grouprelation = "grouprelation";
	private String[] arr = { "groupid", "memberid" };
	private Database database;
	private String query_allTable = "Select * from " + table_grouprelation
			+ ";";

	public GroupRelationDao() {
		super();
		database = Database.instance();
	}

	public long insert(GroupRelation m) {
		ContentValues cont = getContentValues(m);
		return database.insert(table_grouprelation, cont);
	}

	public int update(GroupRelation m) {
		ContentValues cont = getContentValues(m);
		return database.update(table_grouprelation, cont,
				arr[0] + "=" + m.getGroupID());
	}

	public int delete(GroupRelation m) {
		try {
			return database.delete(
					table_grouprelation,
					arr[0] + "=" + m.getGroupID() + " and " + arr[1] + "="
							+ m.getMemberID());
		} catch (Exception e) {
			return -1;
		}
	}

	public int delete(int memberID) {
		try {
			return database
					.delete(table_grouprelation, arr[1] + "=" + memberID);
		} catch (Exception e) {
			return -1;
		}
	}

	public boolean status_database(GroupRelation m) {
		boolean flag = false;
		List<GroupRelation> ls = new ArrayList<GroupRelation>();
		ls = this.getTablesValues("select * from " + table_grouprelation
				+ " where " + arr[0] + "=" + m.getGroupID() + ";");
		Iterator array = ls.iterator();
		while (array.hasNext()) {
			GroupRelation s = (GroupRelation) array.next();
			if (m.getGroupID() == s.getGroupID()
					&& m.getMemberID() == s.getMemberID()) {
				flag = true;
				break;
			}
		}
		if (flag)
			return true;
		return false;
	}

	public List index() {

		Cursor cur = database.cursor_command(query_allTable);
		List<GroupRelation> ls = new ArrayList<GroupRelation>();
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					GroupRelation m = new GroupRelation();
					m.setGroupID(cur.getInt(cur.getColumnIndex(arr[0])));
					m.setMemberID(cur.getInt(cur.getColumnIndex(arr[1])));
					ls.add(m);
				} while (cur.moveToNext());
			}
		}
		cur.close();
		return ls;
	}

	public List getTablesValues(String query) {

		Cursor cur = database.cursor_command(query);
		List<GroupRelation> ls = new ArrayList<GroupRelation>();
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					GroupRelation m = new GroupRelation();
					m.setGroupID(cur.getInt(cur.getColumnIndex(arr[0])));
					m.setMemberID(cur.getInt(cur.getColumnIndex(arr[1])));
					ls.add(m);
				} while (cur.moveToNext());
			}
		}
		cur.close();
		return ls;
	}

	public ContentValues getContentValues(GroupRelation m) {
		ContentValues cont = new ContentValues();
		cont.put(arr[0], m.getGroupID());
		cont.put(arr[1], m.getMemberID());
		return cont;
	}

}
