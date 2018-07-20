package com.cfcs.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.cfcs.classes.DeleteReminder;
import com.cfcs.classes.DeleteReminder;
import com.cfcs.classes.DeleteReminder;

import android.content.ContentValues;
import android.database.Cursor;

public class DeleteReminderDao {

	private String table_deletereminder = "deleteReminder";
	private String[] arr = { "reminderId" };
	private final String query_allTable = "Select * from "
			+ table_deletereminder + ";";
	private Database database;

	public DeleteReminderDao() {
		super();
		database = Database.instance();
	}

	public long insert(DeleteReminder m) {
		ContentValues cont = getContentValues(m);
		return database.insert(table_deletereminder, cont);
	}

	public int update(DeleteReminder m) {
		ContentValues cont = getContentValues(m);
		return database.update(table_deletereminder, cont,
				arr[0] + "='" + m.getReminderID() + "'");
	}

	public int delete(DeleteReminder m) {
		try {
			return database.delete(table_deletereminder,
					arr[0] + "='" + m.getReminderID() + "'");
		} catch (Exception e) {
			return -1;
		}
	}

	public boolean status_database(DeleteReminder m) {
		boolean flag = false;
		List<DeleteReminder> ls = new ArrayList<DeleteReminder>();
		ls = this.getTablesValues("select * from " + table_deletereminder
				+ " where " + arr[0] + "='" + m.getReminderID() + "';");
		Iterator array = ls.iterator();
		while (array.hasNext()) {
			DeleteReminder s = (DeleteReminder) array.next();
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
		List<DeleteReminder> ls = new ArrayList<DeleteReminder>();
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					DeleteReminder m = new DeleteReminder();
					m.setReminderID(cur.getString(cur.getColumnIndex(arr[0])));
					ls.add(m);
				} while (cur.moveToNext());
			}
		}
		cur.close();
		return ls;
	}

	public List getTablesValues(String query) {

		Cursor cur = database.cursor_command(query);
		List<DeleteReminder> ls = new ArrayList<DeleteReminder>();
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					DeleteReminder m = new DeleteReminder();
					m.setReminderID(cur.getString(cur.getColumnIndex(arr[0])));
					ls.add(m);
				} while (cur.moveToNext());
			}
		}
		cur.close();
		return ls;
	}

	public ContentValues getContentValues(DeleteReminder m) {
		ContentValues cont = new ContentValues();
		cont.put(arr[0], m.getReminderID());
		return cont;
	}

}
