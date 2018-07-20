package com.cfcs.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.cfcs.classes.DeleteCaseUpdateIds;
import com.cfcs.classes.DeleteCaseUpdateIds;

import android.content.ContentValues;
import android.database.Cursor;

public class DeleteCaseUpdateDao {

	private String table_deletecaseupdate = "deleteCaseUpdate";
	private String[] arr = { "caseupdateId" };
	private final String query_allTable = "Select * from "
			+ table_deletecaseupdate + ";";
	private Database database;

	public DeleteCaseUpdateDao() {
		super();
		database = Database.instance();
	}

	public long insert(DeleteCaseUpdateIds m) {
		ContentValues cont = getContentValues(m);
		return database.insert(table_deletecaseupdate, cont);
	}

	public int update(DeleteCaseUpdateIds m) {
		ContentValues cont = getContentValues(m);
		return database.update(table_deletecaseupdate, cont,
				arr[0] + "='" + m.getCaseUpdateId() + "'");
	}

	public int delete(DeleteCaseUpdateIds m) {
		try {
			return database.delete(table_deletecaseupdate,
					arr[0] + "='" + m.getCaseUpdateId() + "'");
		} catch (Exception e) {
			return -1;
		}
	}

	public boolean status_database(DeleteCaseUpdateIds m) {
		boolean flag = false;
		List<DeleteCaseUpdateIds> ls = new ArrayList<DeleteCaseUpdateIds>();
		ls = this.getTablesValues("select * from " + table_deletecaseupdate
				+ " where " + arr[0] + "='" + m.getCaseUpdateId() + "';");
		Iterator array = ls.iterator();
		while (array.hasNext()) {
			DeleteCaseUpdateIds s = (DeleteCaseUpdateIds) array.next();
			if (m.getCaseUpdateId().compareTo(s.getCaseUpdateId()) == 0) {
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
		List<DeleteCaseUpdateIds> ls = new ArrayList<DeleteCaseUpdateIds>();
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					DeleteCaseUpdateIds m = new DeleteCaseUpdateIds();
					m.setCaseUpdateId(cur.getString(cur.getColumnIndex(arr[0])));
					ls.add(m);
				} while (cur.moveToNext());
			}
		}
		cur.close();
		return ls;
	}

	public List getTablesValues(String query) {

		Cursor cur = database.cursor_command(query);
		List<DeleteCaseUpdateIds> ls = new ArrayList<DeleteCaseUpdateIds>();
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					DeleteCaseUpdateIds m = new DeleteCaseUpdateIds();
					m.setCaseUpdateId(cur.getString(cur.getColumnIndex(arr[0])));
					ls.add(m);
				} while (cur.moveToNext());
			}
		}
		cur.close();
		return ls;
	}

	public ContentValues getContentValues(DeleteCaseUpdateIds m) {
		ContentValues cont = new ContentValues();
		cont.put(arr[0], m.getCaseUpdateId());
		return cont;
	}

}
