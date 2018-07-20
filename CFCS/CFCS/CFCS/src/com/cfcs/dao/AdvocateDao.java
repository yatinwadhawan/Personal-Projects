package com.cfcs.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import android.content.ContentValues;
import android.database.Cursor;
import com.cfcs.classes.Advocate;

public class AdvocateDao {

	private String table_otheradvocate = "otheradvocate";
	private String[] arr = { "advocateid", "title", "advocatename", "mobile",
			"mailid", "type", "updatedate", "timestamp", "active" };
	private Database database;
	private String query_allTable = "Select * from " + table_otheradvocate
			+ ";";

	public AdvocateDao() {
		database = Database.instance();
	}

	public long insert(Advocate m) {
		ContentValues cont = getContentValues(m);
		return database.insert(table_otheradvocate, cont);
	}

	public int update(Advocate m) {
		ContentValues cont = getContentValues(m);
		return database.update(table_otheradvocate, cont,
				arr[0] + "=" + m.getAdvocateID());
	}

	public int delete(Advocate m) {
		try {
			return database.delete(table_otheradvocate,
					arr[0] + "=" + m.getAdvocateID());
		} catch (Exception e) {
			return -1;
		}
	}

	public boolean status_database(Advocate m) {
		boolean flag = false;
		List<Advocate> ls = new ArrayList<Advocate>();
		ls = this.getTablesValues("select * from " + table_otheradvocate
				+ " where " + arr[0] + "=" + m.getAdvocateID() + ";");
		Iterator array = ls.iterator();
		while (array.hasNext()) {
			Advocate s = (Advocate) array.next();
			if (m.getAdvocateID() == s.getAdvocateID()) {
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
		List<Advocate> ls = new ArrayList<Advocate>();
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					Advocate m = new Advocate();
					m.setAdvocateID(cur.getInt(cur.getColumnIndex(arr[0])));
					m.setTitle(cur.getString(cur.getColumnIndex(arr[1])));
					m.setAdvocateName(cur.getString(cur.getColumnIndex(arr[2])));
					m.setMobileNo(cur.getString(cur.getColumnIndex(arr[3])));
					m.setMailID(cur.getString(cur.getColumnIndex(arr[4])));
					m.setType(cur.getInt(cur.getColumnIndex(arr[5])));
					m.setUpdateDate(cur.getString(cur.getColumnIndex(arr[6])));
					m.setTimestamp(cur.getInt(cur.getColumnIndex(arr[7])));
					m.setActive(cur.getString(cur.getColumnIndex(arr[8])));
					ls.add(m);
				} while (cur.moveToNext());
			}
		}
		cur.close();
		return ls;
	}

	public List getTablesValues(String query) {

		Cursor cur = database.cursor_command(query);
		List<Advocate> ls = new ArrayList<Advocate>();
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					Advocate m = new Advocate();
					m.setAdvocateID(cur.getInt(cur.getColumnIndex(arr[0])));
					m.setTitle(cur.getString(cur.getColumnIndex(arr[1])));
					m.setAdvocateName(cur.getString(cur.getColumnIndex(arr[2])));
					m.setMobileNo(cur.getString(cur.getColumnIndex(arr[3])));
					m.setMailID(cur.getString(cur.getColumnIndex(arr[4])));
					m.setType(cur.getInt(cur.getColumnIndex(arr[5])));
					m.setUpdateDate(cur.getString(cur.getColumnIndex(arr[6])));
					m.setTimestamp(cur.getInt(cur.getColumnIndex(arr[7])));
					m.setActive(cur.getString(cur.getColumnIndex(arr[8])));
					ls.add(m);
				} while (cur.moveToNext());
			}
		}
		cur.close();
		return ls;
	}

	public ContentValues getContentValues(Advocate m) {
		ContentValues cont = new ContentValues();
		cont.put(arr[0], m.getAdvocateID());
		cont.put(arr[1], m.getTitle());
		cont.put(arr[2], m.getAdvocateName());
		cont.put(arr[3], m.getMobileNo());
		cont.put(arr[4], m.getMailID());
		cont.put(arr[5], m.getType());
		cont.put(arr[6], m.getUpdateDate());
		cont.put(arr[7], m.getTimestamp());
		cont.put(arr[8], m.getActive());
		return cont;
	}

}
