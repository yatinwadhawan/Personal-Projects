package com.cfcs.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import android.content.ContentValues;
import android.database.Cursor;
import com.cfcs.classes.Reminder;

public class ReminderDao {

	private String table_reminder = "reminder";
	private String[] arr = { "reminderid", "reminderdate", "remindertime",
			"customerid", "caseid", "advocateid", "updatetypeid", "title",
			"details", "status", "addby", "adddate", "updatedate", "active",
			"reminderbefore", "updatestamp", "reminderalarm" };
	private Database database;
	private String query_allTable = "select * from " + table_reminder + ";";

	public ReminderDao() {
		super();
		database = Database.instance();
	}

	public long insert(Reminder m) {
		ContentValues cont = getContentValues(m);
		return database.insert(table_reminder, cont);
	}

	public int update(Reminder m) {
		ContentValues cont = getContentValues(m);
		return database.update(table_reminder, cont,
				arr[0] + "='" + m.getReminderID() + "'");
	}

	public int delete(Reminder m) {
		try {
			return database.delete(table_reminder,
					arr[0] + "='" + m.getReminderID() + "'");
		} catch (Exception e) {
			return -1;
		}
	}

	public boolean status_database(Reminder m) {
		boolean flag = false;
		List<Reminder> ls = new ArrayList<Reminder>();
		ls = this.getTablesValues("select * from " + table_reminder + " where "
				+ arr[0] + "='" + m.getReminderID() + "';");
		Iterator array = ls.iterator();
		while (array.hasNext()) {
			Reminder s = (Reminder) array.next();
			if (m.getReminderID().compareTo(s.getReminderID()) == 0) {
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
		List<Reminder> ls = new ArrayList<Reminder>();
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					Reminder m = new Reminder();
					m.setReminderID(cur.getString(cur.getColumnIndex(arr[0])));
					m.setReminderdate((cur.getString(cur.getColumnIndex(arr[1]))));
					m.setTime(cur.getString(cur.getColumnIndex(arr[2])));
					m.setCustomerid(cur.getInt(cur.getColumnIndex(arr[3])));
					m.setCaseid(cur.getInt(cur.getColumnIndex(arr[4])));
					m.setAdvocateid(cur.getInt(cur.getColumnIndex(arr[5])));
					m.setUpdatetypeid(cur.getInt(cur.getColumnIndex(arr[6])));
					m.setTitle(cur.getString(cur.getColumnIndex(arr[7])));
					m.setDetail(cur.getString(cur.getColumnIndex(arr[8])));
					m.setStatus(cur.getInt(cur.getColumnIndex(arr[9])));
					m.setAddBy(cur.getInt(cur.getColumnIndex(arr[10])));
					m.setAddDate(cur.getString(cur.getColumnIndex(arr[11])));
					m.setUpdatedate(cur.getString(cur.getColumnIndex(arr[12])));
					m.setActive(cur.getString(cur.getColumnIndex(arr[13])));
					m.setReminderBefore(cur.getInt(cur.getColumnIndex(arr[14])));
					m.setUpdateStamp(cur.getInt(cur.getColumnIndex(arr[15])));
					m.setReminderAlarm(cur.getString(cur
							.getColumnIndex(arr[16])));
					ls.add(m);
				} while (cur.moveToNext());
			}
		}
		cur.close();
		return ls;
	}

	public List getTablesValues(String query) {

		Cursor cur = database.cursor_command(query);
		List<Reminder> ls = new ArrayList<Reminder>();
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					Reminder m = new Reminder();
					m.setReminderID(cur.getString(cur.getColumnIndex(arr[0])));
					m.setReminderdate((cur.getString(cur.getColumnIndex(arr[1]))));
					m.setTime(cur.getString(cur.getColumnIndex(arr[2])));
					m.setCustomerid(cur.getInt(cur.getColumnIndex(arr[3])));
					m.setCaseid(cur.getInt(cur.getColumnIndex(arr[4])));
					m.setAdvocateid(cur.getInt(cur.getColumnIndex(arr[5])));
					m.setUpdatetypeid(cur.getInt(cur.getColumnIndex(arr[6])));
					m.setTitle(cur.getString(cur.getColumnIndex(arr[7])));
					m.setDetail(cur.getString(cur.getColumnIndex(arr[8])));
					m.setStatus(cur.getInt(cur.getColumnIndex(arr[9])));
					m.setAddBy(cur.getInt(cur.getColumnIndex(arr[10])));
					m.setAddDate(cur.getString(cur.getColumnIndex(arr[11])));
					m.setUpdatedate(cur.getString(cur.getColumnIndex(arr[12])));
					m.setActive(cur.getString(cur.getColumnIndex(arr[13])));
					m.setReminderBefore(cur.getInt(cur.getColumnIndex(arr[14])));
					m.setUpdateStamp(cur.getInt(cur.getColumnIndex(arr[15])));
					m.setReminderAlarm(cur.getString(cur
							.getColumnIndex(arr[16])));
					ls.add(m);
				} while (cur.moveToNext());
			}
		}
		cur.close();
		return ls;
	}

	public ContentValues getContentValues(Reminder m) {
		ContentValues cont = new ContentValues();
		cont.put(arr[0], m.getReminderID());
		cont.put(arr[1], m.getReminderdate());
		cont.put(arr[2], m.getTime());
		cont.put(arr[3], m.getCustomerid());
		cont.put(arr[4], m.getCaseid());
		cont.put(arr[5], m.getAdvocateid());
		cont.put(arr[6], m.getUpdatetypeid());
		cont.put(arr[7], m.getTitle());
		cont.put(arr[8], m.getDetail());
		cont.put(arr[9], m.getStatus());
		cont.put(arr[10], m.getAddBy());
		cont.put(arr[11], m.getAddDate());
		cont.put(arr[12], m.getUpdatedate());
		cont.put(arr[13], m.getActive());
		cont.put(arr[14], m.getReminderBefore());
		cont.put(arr[15], m.getUpdateStamp());
		cont.put(arr[16], m.getReminderAlarm());
		return cont;
	}

}
