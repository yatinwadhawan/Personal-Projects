package com.cfcs.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.cfcs.classes.ReminderIDToEventID;
import com.cfcs.classes.ReminderIDToEventID;

import android.content.ContentValues;
import android.database.Cursor;

public class ReminderIdToEventIdDao {

	private final static String table_reminderIdtoeventId = "remindertoevent";
	private String[] arr = { "reminderid", "eventid" };
	private String query_allTable = "select * from "
			+ table_reminderIdtoeventId + ";";
	private Database database;

	public ReminderIdToEventIdDao() {
		super();
		database = Database.instance();
	}

	public long insert(ReminderIDToEventID m) {
		ContentValues cont = getContentValues(m);
		return database.insert(table_reminderIdtoeventId, cont);
	}

	public int update(ReminderIDToEventID m) {
		ContentValues cont = getContentValues(m);
		return database.update(table_reminderIdtoeventId, cont, arr[0] + "='"
				+ m.getReminderId() + "'");
	}

	public int delete(ReminderIDToEventID m) {
		try {
			return database.delete(table_reminderIdtoeventId,
					arr[0] + "='" + m.getReminderId() + "'");
		} catch (Exception e) {
			return -1;
		}
	}

	public boolean status_database(ReminderIDToEventID m) {
		boolean flag = false;
		List<ReminderIDToEventID> ls = new ArrayList<ReminderIDToEventID>();
		ls = this.getTablesValues("select * from " + table_reminderIdtoeventId
				+ " where " + arr[0] + "='" + m.getReminderId() + "';");
		Iterator array = ls.iterator();
		while (array.hasNext()) {
			ReminderIDToEventID s = (ReminderIDToEventID) array.next();
			if (m.getReminderId().compareTo(s.getReminderId()) == 0) {
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
		List<ReminderIDToEventID> ls = new ArrayList<ReminderIDToEventID>();
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					ReminderIDToEventID m = new ReminderIDToEventID();
					m.setReminderId(cur.getString(cur.getColumnIndex(arr[0])));
					m.setEventId(cur.getString(cur.getColumnIndex(arr[1])));
					ls.add(m);
				} while (cur.moveToNext());
			}
		}
		cur.close();
		return ls;
	}

	public List getTablesValues(String query) {

		Cursor cur = database.cursor_command(query);
		List<ReminderIDToEventID> ls = new ArrayList<ReminderIDToEventID>();
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					ReminderIDToEventID m = new ReminderIDToEventID();
					m.setReminderId(cur.getString(cur.getColumnIndex(arr[0])));
					m.setEventId(cur.getString(cur.getColumnIndex(arr[1])));
					ls.add(m);
				} while (cur.moveToNext());
			}
		}
		cur.close();
		return ls;
	}

	public ContentValues getContentValues(ReminderIDToEventID m) {
		ContentValues cont = new ContentValues();
		cont.put(arr[0], m.getReminderId());
		cont.put(arr[1], m.getEventId());
		return cont;
	}

}
