package com.cfcs.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.cfcs.classes.UpdateTypeMaster;

import android.content.ContentValues;
import android.database.Cursor;

public class UpdateTypeMasterDao {

	private String table_updatetypemaster = "updatetypemaster";
	private String[] arr = { "updatetypeid", "updatetype", "timestamp",
			"active" };
	private String query_allTable = "select * from " + table_updatetypemaster
			+ ";";
	private Database database;

	public UpdateTypeMasterDao() {
		super();
		database = Database.instance();
	}

	public long insert(UpdateTypeMaster m) {
		ContentValues cont = getContentValues(m);
		return database.insert(table_updatetypemaster, cont);
	}

	public int update(UpdateTypeMaster m) {
		ContentValues cont = getContentValues(m);
		return database.update(table_updatetypemaster, cont,
				arr[0] + "=" + m.getUpdateTypeID());
	}

	public int delete(UpdateTypeMaster m) {
		try {
			return database.delete(table_updatetypemaster,
					arr[0] + "=" + m.getUpdateTypeID());
		} catch (Exception e) {
			return -1;
		}
	}

	public boolean status_database(UpdateTypeMaster m) {
		boolean flag = false;
		List<UpdateTypeMaster> ls = new ArrayList<UpdateTypeMaster>();
		ls = this.getTablesValues("select * from " + table_updatetypemaster
				+ " where " + arr[0] + "=" + m.getUpdateTypeID() + ";");
		Iterator array = ls.iterator();
		while (array.hasNext()) {
			UpdateTypeMaster s = (UpdateTypeMaster) array.next();
			if (m.getUpdateTypeID() == s.getUpdateTypeID()) {
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
		List<UpdateTypeMaster> ls = new ArrayList<UpdateTypeMaster>();
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					UpdateTypeMaster m = new UpdateTypeMaster();
					m.setUpdateTypeID(cur.getInt(cur.getColumnIndex(arr[0])));
					m.setUpdateType(cur.getString(cur.getColumnIndex(arr[1])));
					m.setUpdateStamp(cur.getString(cur.getColumnIndex(arr[2])));
					m.setActive(cur.getString(cur.getColumnIndex(arr[3])));
					ls.add(m);
				} while (cur.moveToNext());
			}
		}
		cur.close();
		return ls;
	}

	public List getTablesValues(String query) {

		Cursor cur = database.cursor_command(query);
		List<UpdateTypeMaster> ls = new ArrayList<UpdateTypeMaster>();
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					UpdateTypeMaster m = new UpdateTypeMaster();
					m.setUpdateTypeID(cur.getInt(cur.getColumnIndex(arr[0])));
					m.setUpdateType(cur.getString(cur.getColumnIndex(arr[1])));
					m.setUpdateStamp(cur.getString(cur.getColumnIndex(arr[2])));
					m.setActive(cur.getString(cur.getColumnIndex(arr[3])));
					ls.add(m);
				} while (cur.moveToNext());
			}
		}
		cur.close();
		return ls;
	}

	public ContentValues getContentValues(UpdateTypeMaster m) {
		ContentValues cont = new ContentValues();
		cont.put(arr[0], m.getUpdateTypeID());
		cont.put(arr[1], m.getUpdateType());
		cont.put(arr[2], m.getUpdateStamp());
		cont.put(arr[3], m.getActive());
		return cont;
	}

}
