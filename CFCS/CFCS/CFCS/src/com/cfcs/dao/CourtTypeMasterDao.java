package com.cfcs.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.cfcs.classes.CourtTypeMaster;
import android.content.ContentValues;
import android.database.Cursor;

public class CourtTypeMasterDao {

	private String table_courttypemaster = "courttypemaster";
	private String[] arr = { "courttypeid", "courttype", "updatedate", "active" };
	private final String query_allTable = "Select * from "
			+ table_courttypemaster + ";";
	private Database database;

	public CourtTypeMasterDao() {
		super();
		database = Database.instance();
	}

	public long insert(CourtTypeMaster m) {
		ContentValues cont = getContentValues(m);
		return database.insert(table_courttypemaster, cont);
	}

	public int update(CourtTypeMaster m) {
		ContentValues cont = getContentValues(m);
		return database.update(table_courttypemaster, cont,
				arr[0] + "=" + m.getCourtTypeId());
	}

	public int delete(CourtTypeMaster m) {
		try {
			return database.delete(table_courttypemaster,
					arr[0] + "=" + m.getCourtTypeId());
		} catch (Exception e) {
			return -1;
		}
	}

	public boolean status_database(CourtTypeMaster m) {
		boolean flag = false;
		List<CourtTypeMaster> ls = new ArrayList<CourtTypeMaster>();
		ls = this.getTablesValues("select * from " + table_courttypemaster
				+ " where " + arr[0] + "=" + m.getCourtTypeId() + ";");
		Iterator array = ls.iterator();
		while (array.hasNext()) {
			CourtTypeMaster s = (CourtTypeMaster) array.next();
			if (m.getCourtTypeId() == s.getCourtTypeId()) {
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
		List<CourtTypeMaster> ls = new ArrayList<CourtTypeMaster>();
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					CourtTypeMaster m = new CourtTypeMaster();
					m.setCourtTypeId(cur.getInt(cur.getColumnIndex(arr[0])));
					m.setCourtType(cur.getString(cur.getColumnIndex(arr[1])));
					m.setUpdateDate(cur.getString(cur.getColumnIndex(arr[2])));
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
		List<CourtTypeMaster> ls = new ArrayList<CourtTypeMaster>();
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					CourtTypeMaster m = new CourtTypeMaster();
					m.setCourtTypeId(cur.getInt(cur.getColumnIndex(arr[0])));
					m.setCourtType(cur.getString(cur.getColumnIndex(arr[1])));
					m.setUpdateDate(cur.getString(cur.getColumnIndex(arr[2])));
					m.setActive(cur.getString(cur.getColumnIndex(arr[3])));
					ls.add(m);
				} while (cur.moveToNext());
			}
		}
		cur.close();
		return ls;
	}

	public ContentValues getContentValues(CourtTypeMaster m) {
		ContentValues cont = new ContentValues();
		cont.put(arr[0], m.getCourtTypeId());
		cont.put(arr[1], m.getCourtType());
		cont.put(arr[2], m.getUpdateDate());
		cont.put(arr[3], m.getActive());
		return cont;
	}

}
