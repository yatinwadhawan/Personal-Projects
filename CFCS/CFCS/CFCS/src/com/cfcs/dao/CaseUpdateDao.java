package com.cfcs.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import android.content.ContentValues;
import android.database.Cursor;
import com.cfcs.classes.CaseUpdate;

public class CaseUpdateDao {

	private String table_caseupdate = "caseupdate";
	private String[] arr = { "caseupdateid", "updatetypeid", "customerid",
			"caseid", "advocateid", "courttypeid", "remark", "attachment",
			"workdoneby", "appstatus", "appby", "appdatetime", "adddate",
			"updatedate", "active", "updatestamp", "ischargeable", "amount",
			"advocatefee", "caseupdatedate", "minutespend", "isadvchargeable" };
	private Database database;
	private String query_allTable = "select * from " + table_caseupdate + ";";

	public CaseUpdateDao() {
		super();
		database = Database.instance();
	}

	public long insert(CaseUpdate m) {
		ContentValues cont = getContentValues(m);
		return database.insert(table_caseupdate, cont);
	}

	public int update(CaseUpdate m) {
		ContentValues cont = getContentValues(m);
		return database.update(table_caseupdate, cont,
				arr[0] + "='" + m.getCaseUpdateID() + "'");
	}

	public int delete(CaseUpdate m) {
		try {
			return database.delete(table_caseupdate,
					arr[0] + "='" + m.getCaseUpdateID() + "'");
		} catch (Exception e) {
			return -1;
		}
	}

	public boolean status_database(CaseUpdate m) {
		boolean flag = false;
		List<CaseUpdate> ls = new ArrayList<CaseUpdate>();
		ls = this.getTablesValues("select * from " + table_caseupdate
				+ " where " + arr[0] + "='" + m.getCaseUpdateID() + "';");
		Iterator array = ls.iterator();
		while (array.hasNext()) {
			CaseUpdate s = (CaseUpdate) array.next();
			if (m.getCaseUpdateID().compareTo(s.getCaseUpdateID()) == 0) {
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
		List<CaseUpdate> ls = new ArrayList<CaseUpdate>();
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					CaseUpdate m = new CaseUpdate();
					m.setCaseUpdateID(cur.getString(cur.getColumnIndex(arr[0])));
					m.setUpdateTypeID(cur.getInt(cur.getColumnIndex(arr[1])));
					m.setCustomerID(cur.getInt(cur.getColumnIndex(arr[2])));
					m.setCaseID(cur.getInt(cur.getColumnIndex(arr[3])));
					m.setAdvocateID(cur.getInt(cur.getColumnIndex(arr[4])));
					m.setCourtTypeID(cur.getInt(cur.getColumnIndex(arr[5])));
					m.setRemark(cur.getString(cur.getColumnIndex(arr[6])));
					m.setAttachment(cur.getString(cur.getColumnIndex(arr[7])));
					m.setWorkDoneBy(cur.getInt(cur.getColumnIndex(arr[8])));
					m.setAppStatus(cur.getInt(cur.getColumnIndex(arr[9])));
					m.setAppBy(cur.getInt(cur.getColumnIndex(arr[10])));
					m.setAppDateTime(cur.getString(cur.getColumnIndex(arr[11])));
					m.setAddDate(cur.getString(cur.getColumnIndex(arr[12])));
					m.setUpdateDate(cur.getString(cur.getColumnIndex(arr[13])));
					m.setActive(cur.getString(cur.getColumnIndex(arr[14])));
					m.setUpdateStamp(cur.getInt(cur.getColumnIndex(arr[15])));
					m.setIsChargeable(cur.getString(cur.getColumnIndex(arr[16])));
					m.setAmount(cur.getString(cur.getColumnIndex(arr[17])));
					m.setAdvocateFee(cur.getString(cur.getColumnIndex(arr[18])));
					m.setCaseupdatedate(cur.getString(cur
							.getColumnIndex(arr[19])));
					m.setMinuteSpend(cur.getInt(cur.getColumnIndex(arr[20])));
					m.setIsAdvChargeable(cur.getString(cur
							.getColumnIndex(arr[21])));
					ls.add(m);
				} while (cur.moveToNext());
			}
		}
		cur.close();
		return ls;
	}

	public List getTablesValues(String query) {

		Cursor cur = database.cursor_command(query);
		List<CaseUpdate> ls = new ArrayList<CaseUpdate>();
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					CaseUpdate m = new CaseUpdate();
					m.setCaseUpdateID(cur.getString(cur.getColumnIndex(arr[0])));
					m.setUpdateTypeID(cur.getInt(cur.getColumnIndex(arr[1])));
					m.setCustomerID(cur.getInt(cur.getColumnIndex(arr[2])));
					m.setCaseID(cur.getInt(cur.getColumnIndex(arr[3])));
					m.setAdvocateID(cur.getInt(cur.getColumnIndex(arr[4])));
					m.setCourtTypeID(cur.getInt(cur.getColumnIndex(arr[5])));
					m.setRemark(cur.getString(cur.getColumnIndex(arr[6])));
					m.setAttachment(cur.getString(cur.getColumnIndex(arr[7])));
					m.setWorkDoneBy(cur.getInt(cur.getColumnIndex(arr[8])));
					m.setAppStatus(cur.getInt(cur.getColumnIndex(arr[9])));
					m.setAppBy(cur.getInt(cur.getColumnIndex(arr[10])));
					m.setAppDateTime(cur.getString(cur.getColumnIndex(arr[11])));
					m.setAddDate(cur.getString(cur.getColumnIndex(arr[12])));
					m.setUpdateDate(cur.getString(cur.getColumnIndex(arr[13])));
					m.setActive(cur.getString(cur.getColumnIndex(arr[14])));
					m.setUpdateStamp(cur.getInt(cur.getColumnIndex(arr[15])));
					m.setIsChargeable(cur.getString(cur.getColumnIndex(arr[16])));
					m.setAmount(cur.getString(cur.getColumnIndex(arr[17])));
					m.setAdvocateFee(cur.getString(cur.getColumnIndex(arr[18])));
					m.setCaseupdatedate(cur.getString(cur
							.getColumnIndex(arr[19])));
					m.setMinuteSpend(cur.getInt(cur.getColumnIndex(arr[20])));
					m.setIsAdvChargeable(cur.getString(cur
							.getColumnIndex(arr[21])));
					ls.add(m);
				} while (cur.moveToNext());
			}
		}
		cur.close();
		return ls;
	}

	public ContentValues getContentValues(CaseUpdate m) {
		ContentValues cont = new ContentValues();
		cont.put(arr[0], m.getCaseUpdateID());
		cont.put(arr[1], m.getUpdateTypeID());
		cont.put(arr[2], m.getCustomerID());
		cont.put(arr[3], m.getCaseID());
		cont.put(arr[4], m.getAdvocateID());
		cont.put(arr[5], m.getCourtTypeID());
		cont.put(arr[6], m.getRemark());
		cont.put(arr[7], m.getAttachment());
		cont.put(arr[8], m.getWorkDoneBy());
		cont.put(arr[9], m.getAppStatus());
		cont.put(arr[10], m.getAppBy());
		cont.put(arr[11], m.getAppDateTime());
		cont.put(arr[12], m.getAddDate());
		cont.put(arr[13], m.getUpdateDate());
		cont.put(arr[14], m.getActive());
		cont.put(arr[15], m.getUpdateStamp());
		cont.put(arr[16], m.getIsChargeable());
		cont.put(arr[17], m.getAmount());
		cont.put(arr[18], m.getAdvocateFee());
		cont.put(arr[19], m.getCaseupdatedate());
		cont.put(arr[20], m.getMinuteSpend());
		cont.put(arr[21], m.getIsAdvChargeable());
		return cont;
	}

}
