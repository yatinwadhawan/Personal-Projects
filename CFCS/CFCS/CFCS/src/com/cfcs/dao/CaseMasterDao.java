package com.cfcs.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import android.content.ContentValues;
import android.database.Cursor;
import com.cfcs.classes.CaseMaster;

public class CaseMasterDao {

	private String table_casemaster = "casemaster";
	private String[] arr = { "caseid", "customerid", "manualcaseid",
			"casetitle", "casedetail", "casestatus", "adddate", "updatedate",
			"updateby", "active" };
	private Database database;
	public String query_allTable = "select * from " + table_casemaster + ";";

	public CaseMasterDao() {
		database = Database.instance();
	}

	public long insert(CaseMaster m) {
		ContentValues cont = getContentValues(m);
		return database.insert(table_casemaster, cont);
	}

	public int update(CaseMaster m) {
		ContentValues cont = getContentValues(m);
		return database.update(table_casemaster, cont,
				arr[0] + "=" + m.getCaseID());
	}

	public int delete(CaseMaster m) {
		try {
			return database.delete(table_casemaster,
					arr[0] + "=" + m.getCaseID());
		} catch (Exception e) {
			return -1;
		}
	}

	public boolean status_database(CaseMaster m) {
		boolean flag = false;
		List<CaseMaster> ls = new ArrayList<CaseMaster>();
		ls = this.getTablesValues("select * from " + table_casemaster
				+ " where " + arr[0] + "=" + m.getCaseID() + ";");
		Iterator array = ls.iterator();
		while (array.hasNext()) {
			CaseMaster s = (CaseMaster) array.next();
			if (m.getCaseID() == s.getCaseID()) {
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
		List<CaseMaster> ls = new ArrayList<CaseMaster>();
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					CaseMaster m = new CaseMaster();
					m.setCaseID(cur.getInt(cur.getColumnIndex(arr[0])));
					m.setCustomerID(cur.getInt(cur.getColumnIndex(arr[1])));
					m.setManualCaseID(cur.getString(cur.getColumnIndex(arr[2])));
					m.setCaseTitle(cur.getString(cur.getColumnIndex(arr[3])));
					m.setCaseDetail(cur.getString(cur.getColumnIndex(arr[4])));
					m.setCaseStatus(cur.getString(cur.getColumnIndex(arr[5])));
					m.setAdddate(cur.getString(cur.getColumnIndex(arr[6])));
					m.setUpdateDate(cur.getString(cur.getColumnIndex(arr[7])));
					m.setUpdateby(cur.getString(cur.getColumnIndex(arr[8])));
					m.setActive(cur.getString(cur.getColumnIndex(arr[9])));
					ls.add(m);
				} while (cur.moveToNext());
			}
		}
		cur.close();
		return ls;
	}

	public List getTablesValues(String query) {

		Cursor cur = database.cursor_command(query);
		List<CaseMaster> ls = new ArrayList<CaseMaster>();
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					CaseMaster m = new CaseMaster();
					m.setCaseID(cur.getInt(cur.getColumnIndex(arr[0])));
					m.setCustomerID(cur.getInt(cur.getColumnIndex(arr[1])));
					m.setManualCaseID(cur.getString(cur.getColumnIndex(arr[2])));
					m.setCaseTitle(cur.getString(cur.getColumnIndex(arr[3])));
					m.setCaseDetail(cur.getString(cur.getColumnIndex(arr[4])));
					m.setCaseStatus(cur.getString(cur.getColumnIndex(arr[5])));
					m.setAdddate(cur.getString(cur.getColumnIndex(arr[6])));
					m.setUpdateDate(cur.getString(cur.getColumnIndex(arr[7])));
					m.setUpdateby(cur.getString(cur.getColumnIndex(arr[8])));
					m.setActive(cur.getString(cur.getColumnIndex(arr[9])));
					ls.add(m);
				} while (cur.moveToNext());
			}
		}
		cur.close();
		return ls;
	}

	public ContentValues getContentValues(CaseMaster m) {
		ContentValues cont = new ContentValues();
		cont.put(arr[0], m.getCaseID());
		cont.put(arr[1], m.getCustomerID());
		cont.put(arr[2], m.getManualCaseID());
		cont.put(arr[3], m.getCaseTitle());
		cont.put(arr[4], m.getCaseDetail());
		cont.put(arr[5], m.getCaseStatus());
		cont.put(arr[6], m.getAdddate());
		cont.put(arr[7], m.getUpdateDate());
		cont.put(arr[8], m.getUpdateby());
		cont.put(arr[9], m.getActive());
		return cont;
	}

}
