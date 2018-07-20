package com.cfcs.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import com.cfcs.classes.GroupMaster;

public class GroupMasterDao {

	private String table_groupmaster = "groupmaster";
	private String[] arr = { "groupid", "groupname", "updatedate", "timestamp",
			"active" };
	private Database database;
	private String query_allTable = "select * from " + table_groupmaster + ";";

	public GroupMasterDao() {
		super();
		database = Database.instance();
	}

	public long insert(GroupMaster m) {
		ContentValues cont = getContentValues(m);
		return database.insert(table_groupmaster, cont);
	}

	public int update(GroupMaster m) {
		ContentValues cont = getContentValues(m);
		return database.update(table_groupmaster, cont,
				arr[0] + "=" + m.getGroupID());
	}

	public int delete(GroupMaster m) {
		try {
			return database.delete(table_groupmaster,
					arr[0] + "=" + m.getGroupID());
		} catch (Exception e) {
			return -1;
		}
	}

	public boolean status_database(GroupMaster m) {
		boolean flag = false;
		List<GroupMaster> ls = new ArrayList<GroupMaster>();
		ls = this.getTablesValues("select * from " + table_groupmaster
				+ " where " + arr[0] + "=" + m.getGroupID() + ";");
		Iterator array = ls.iterator();
		while (array.hasNext()) {
			GroupMaster s = (GroupMaster) array.next();
			if (m.getGroupID() == s.getGroupID()) {
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
		List<GroupMaster> ls = new ArrayList<GroupMaster>();
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					GroupMaster m = new GroupMaster();
					m.setGroupID(cur.getInt(cur.getColumnIndex(arr[0])));
					m.setGroupName(cur.getString(cur.getColumnIndex(arr[1])));
					m.setUpdateDate(cur.getString(cur.getColumnIndex(arr[2])));
					m.setTimestamp(cur.getInt(cur.getColumnIndex(arr[3])));
					m.setActive(cur.getString(cur.getColumnIndex(arr[4])));
					ls.add(m);
				} while (cur.moveToNext());
			}
		}
		cur.close();
		return ls;
	}

	public List getTablesValues(String query) {

		Cursor cur = database.cursor_command(query);
		List<GroupMaster> ls = new ArrayList<GroupMaster>();
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					GroupMaster m = new GroupMaster();
					m.setGroupID(cur.getInt(cur.getColumnIndex(arr[0])));
					m.setGroupName(cur.getString(cur.getColumnIndex(arr[1])));
					m.setUpdateDate(cur.getString(cur.getColumnIndex(arr[2])));
					m.setTimestamp(cur.getInt(cur.getColumnIndex(arr[3])));
					m.setActive(cur.getString(cur.getColumnIndex(arr[4])));
					ls.add(m);
				} while (cur.moveToNext());
			}
		}
		cur.close();
		return ls;
	}

	public ContentValues getContentValues(GroupMaster m) {
		ContentValues cont = new ContentValues();
		cont.put(arr[0], m.getGroupID());
		cont.put(arr[1], m.getGroupName());
		cont.put(arr[2], m.getUpdateDate());
		cont.put(arr[3], m.getTimestamp());
		cont.put(arr[4], m.getActive());
		return cont;
	}

}
