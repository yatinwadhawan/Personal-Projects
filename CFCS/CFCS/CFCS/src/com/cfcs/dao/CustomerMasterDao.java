package com.cfcs.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import com.cfcs.classes.CustomerMaster;

public class CustomerMasterDao {

	private String table_customermaster = "customermaster";
	private String[] arr = { "customerid", "customername", "mobileno",
			"updatedate", "updatetimestamp", "active", "activestatus" };
	private Database database;
	private String query_allTable = "select * from " + table_customermaster
			+ ";";

	public CustomerMasterDao() {
		super();
		database = Database.instance();
	}

	public long insert(CustomerMaster m) {
		ContentValues cont = getContentValues(m);
		return database.insert(table_customermaster, cont);
	}

	public int update(CustomerMaster m) {
		ContentValues cont = getContentValues(m);
		return database.update(table_customermaster, cont,
				arr[0] + "=" + m.getCustomerID());
	}

	public int delete(CustomerMaster m) {
		try {
			return database.delete(table_customermaster,
					arr[0] + "=" + m.getCustomerID());
		} catch (Exception e) {
			return -1;
		}
	}

	public boolean status_database(CustomerMaster m) {
		boolean flag = false;
		List<CustomerMaster> ls = new ArrayList<CustomerMaster>();
		ls = this.getTablesValues("select * from " + table_customermaster
				+ " where " + arr[0] + "=" + m.getCustomerID() + ";");
		Iterator array = ls.iterator();
		while (array.hasNext()) {
			CustomerMaster s = (CustomerMaster) array.next();
			if (m.getCustomerID() == s.getCustomerID()) {
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
		List<CustomerMaster> ls = new ArrayList<CustomerMaster>();
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					CustomerMaster m = new CustomerMaster();
					m.setCustomerID(cur.getInt(cur.getColumnIndex(arr[0])));
					m.setCustomerName(cur.getString(cur.getColumnIndex(arr[1])));
					m.setMobileNo(cur.getString(cur.getColumnIndex(arr[2])));
					m.setUpdateDate(cur.getString(cur.getColumnIndex(arr[3])));
					m.setUpdateStamp(cur.getInt(cur.getColumnIndex(arr[4])));
					m.setActive(cur.getString(cur.getColumnIndex(arr[5])));
					m.setActiveStatus(cur.getInt(cur.getColumnIndex(arr[6])));
					ls.add(m);
				} while (cur.moveToNext());
			}
		}
		cur.close();
		return ls;
	}

	public List getTablesValues(String query) {

		Cursor cur = database.cursor_command(query);
		List<CustomerMaster> ls = new ArrayList<CustomerMaster>();
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					CustomerMaster m = new CustomerMaster();
					m.setCustomerID(cur.getInt(cur.getColumnIndex(arr[0])));
					m.setCustomerName(cur.getString(cur.getColumnIndex(arr[1])));
					m.setMobileNo(cur.getString(cur.getColumnIndex(arr[2])));
					m.setUpdateDate(cur.getString(cur.getColumnIndex(arr[3])));
					m.setUpdateStamp(cur.getInt(cur.getColumnIndex(arr[4])));
					m.setActive(cur.getString(cur.getColumnIndex(arr[5])));
					m.setActiveStatus(cur.getInt(cur.getColumnIndex(arr[6])));
					ls.add(m);
				} while (cur.moveToNext());
			}
		}
		cur.close();
		return ls;
	}

	public ContentValues getContentValues(CustomerMaster m) {
		ContentValues cont = new ContentValues();
		cont.put(arr[0], m.getCustomerID());
		cont.put(arr[1], m.getCustomerName());
		cont.put(arr[2], m.getMobileNo());
		cont.put(arr[3], m.getUpdateDate());
		cont.put(arr[4], m.getUpdateStamp());
		cont.put(arr[5], m.getActive());
		cont.put(arr[6], m.getActiveStatus());
		return cont;
	}

}
