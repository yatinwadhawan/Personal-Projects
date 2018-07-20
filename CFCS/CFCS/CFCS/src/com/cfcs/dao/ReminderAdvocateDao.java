package com.cfcs.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.cfcs.classes.ReminderAdvocate;
import android.content.ContentValues;
import android.database.Cursor;

public class ReminderAdvocateDao {

	private String table_reminderAdvocate = "reminderAdvocate";
	private String[] arr = { "reminderid", "advocateId" };
	private String query_allTable = "select * from " + table_reminderAdvocate
			+ ";";
	private Database database;

	public ReminderAdvocateDao() {
		super();
		database = Database.instance();
	}

	public long insert(ReminderAdvocate m) {
		ContentValues cont = getContentValues(m);
		return database.insert(table_reminderAdvocate, cont);
	}

	public int update(ReminderAdvocate m) {
		ContentValues cont = getContentValues(m);
		return database.update(table_reminderAdvocate, cont,
				arr[0] + "='" + m.getReminderId() + "'");
	}

	public int delete(ReminderAdvocate m) {
		try {
			return database.delete(table_reminderAdvocate,
					arr[0] + "='" + m.getReminderId() + "'");
		} catch (Exception e) {
			return -1;
		}
	}

	public int delete(String reminderId) {
		try {
			return database.delete(table_reminderAdvocate, arr[0] + "='"
					+ reminderId + "'");
		} catch (Exception e) {
			return -1;
		}

	}

	public boolean status_database(ReminderAdvocate m) {
		boolean flag = false;
		List<ReminderAdvocate> ls = new ArrayList<ReminderAdvocate>();
		ls = this.getTablesValues("select * from " + table_reminderAdvocate
				+ " where " + arr[0] + "='" + m.getReminderId() + "';");
		Iterator array = ls.iterator();
		while (array.hasNext()) {
			ReminderAdvocate s = (ReminderAdvocate) array.next();
			if (m.getReminderId().compareTo(s.getReminderId()) == 0
					&& m.getAdvocateId() == s.getAdvocateId()) {
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
		List<ReminderAdvocate> ls = new ArrayList<ReminderAdvocate>();
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					ReminderAdvocate m = new ReminderAdvocate();
					m.setReminderId(cur.getString(cur.getColumnIndex(arr[0])));
					m.setAdvocateId(cur.getInt(cur.getColumnIndex(arr[1])));
					ls.add(m);
				} while (cur.moveToNext());
			}
		}
		cur.close();
		return ls;
	}

	public List getTablesValues(String query) {

		Cursor cur = database.cursor_command(query);
		List<ReminderAdvocate> ls = new ArrayList<ReminderAdvocate>();
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					ReminderAdvocate m = new ReminderAdvocate();
					m.setReminderId(cur.getString(cur.getColumnIndex(arr[0])));
					m.setAdvocateId(cur.getInt(cur.getColumnIndex(arr[1])));
					ls.add(m);
				} while (cur.moveToNext());
			}
		}
		cur.close();
		return ls;
	}

	public ContentValues getContentValues(ReminderAdvocate m) {
		ContentValues cont = new ContentValues();
		cont.put(arr[0], m.getReminderId());
		cont.put(arr[1], m.getAdvocateId());
		return cont;
	}

}
