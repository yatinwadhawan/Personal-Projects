package com.cfcs.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.cfcs.classes.ReminderRelation;

import android.content.ContentValues;
import android.database.Cursor;

public class ReminderRelationDao {

	private String table_reminderRelation = "reminderRelation";
	private String[] arr = { "reminderid", "userid" };
	private String query_allTable = "select * from " + table_reminderRelation
			+ ";";
	private Database database;

	public ReminderRelationDao() {
		super();
		database = Database.instance();
	}

	public long insert(ReminderRelation m) {
		ContentValues cont = getContentValues(m);
		return database.insert(table_reminderRelation, cont);
	}

	public int update(ReminderRelation m) {
		ContentValues cont = getContentValues(m);
		return database.update(table_reminderRelation, cont,
				arr[0] + "='" + m.getReminderId() + "'");
	}

	public int delete(ReminderRelation m) {
		try {
			return database.delete(table_reminderRelation,
					arr[0] + "='" + m.getReminderId() + "'");
		} catch (Exception e) {
			return -1;
		}
	}

	public int delete(String reminderId) {
		try {
			return database.delete(table_reminderRelation, arr[0] + "='"
					+ reminderId + "'");
		} catch (Exception e) {
			return -1;
		}
	}

	public boolean status_database(ReminderRelation m) {
		boolean flag = false;
		List<ReminderRelation> ls = new ArrayList<ReminderRelation>();
		ls = this.getTablesValues("select * from " + table_reminderRelation
				+ " where " + arr[0] + "='" + m.getReminderId() + "';");
		Iterator array = ls.iterator();
		while (array.hasNext()) {
			ReminderRelation s = (ReminderRelation) array.next();
			if (m.getReminderId().compareTo(s.getReminderId()) == 0
					&& m.getUserId() == s.getUserId()) {
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
		List<ReminderRelation> ls = new ArrayList<ReminderRelation>();
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					ReminderRelation m = new ReminderRelation();
					m.setReminderId(cur.getString(cur.getColumnIndex(arr[0])));
					m.setUserId(cur.getInt(cur.getColumnIndex(arr[1])));
					ls.add(m);
				} while (cur.moveToNext());
			}
		}
		cur.close();
		return ls;
	}

	public List getTablesValues(String query) {

		Cursor cur = database.cursor_command(query);
		List<ReminderRelation> ls = new ArrayList<ReminderRelation>();
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					ReminderRelation m = new ReminderRelation();
					m.setReminderId(cur.getString(cur.getColumnIndex(arr[0])));
					m.setUserId(cur.getInt(cur.getColumnIndex(arr[1])));
					ls.add(m);
				} while (cur.moveToNext());
			}
		}
		cur.close();
		return ls;
	}

	public ContentValues getContentValues(ReminderRelation m) {
		ContentValues cont = new ContentValues();
		cont.put(arr[0], m.getReminderId());
		cont.put(arr[1], m.getUserId());
		return cont;
	}

}
