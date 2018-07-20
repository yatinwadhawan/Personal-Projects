package com.cfcs.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.cfcs.classes.AdminUser;
import android.content.ContentValues;
import android.database.Cursor;

public class AdminUserDao {

	private String table_adminuser = "adminuser";
	private String[] arr = { "userid", "roleid", "name", "mobileno",
			"username", "password", "emailid", "lastupdate", "updatestamp",
			"active" };
	private final String query_allTable = "Select * from " + table_adminuser
			+ ";";
	private Database database;

	public AdminUserDao() {
		super();
		database = Database.instance();
	}

	public long insert(AdminUser m) {
		ContentValues cont = getContentValues(m);
		return database.insert(table_adminuser, cont);
	}

	public int update(AdminUser m) {
		ContentValues cont = getContentValues(m);
		return database.update(table_adminuser, cont,
				arr[0] + "=" + m.getUserid());
	}

	public int delete(AdminUser m) {
		try {
			return database.delete(table_adminuser,
					arr[0] + "=" + m.getUserid());
		} catch (Exception e) {
			return -1;
		}
	}

	public boolean status_database(AdminUser m) {
		boolean flag = false;
		List<AdminUser> ls = new ArrayList<AdminUser>();
		ls = this.getTablesValues("select * from " + table_adminuser
				+ " where " + arr[0] + "=" + m.getUserid() + ";");
		Iterator array = ls.iterator();
		while (array.hasNext()) {
			AdminUser s = (AdminUser) array.next();
			if (m.getUserid() == s.getUserid()) {
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
		List<AdminUser> ls = new ArrayList<AdminUser>();
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					AdminUser m = new AdminUser();
					m.setUserid(cur.getInt(cur.getColumnIndex(arr[0])));
					m.setRoleID(cur.getInt(cur.getColumnIndex(arr[1])));
					m.setName(cur.getString(cur.getColumnIndex(arr[2])));
					m.setMobileNo(cur.getString(cur.getColumnIndex(arr[3])));
					m.setUserName(cur.getString(cur.getColumnIndex(arr[4])));
					m.setPassword(cur.getString(cur.getColumnIndex(arr[5])));
					m.setEmailID(cur.getString(cur.getColumnIndex(arr[6])));
					m.setLastUpdate(cur.getString(cur.getColumnIndex(arr[7])));
					m.setTimestamp(cur.getInt(cur.getColumnIndex(arr[8])));
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
		List<AdminUser> ls = new ArrayList<AdminUser>();
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					AdminUser m = new AdminUser();
					m.setUserid(cur.getInt(cur.getColumnIndex(arr[0])));
					m.setRoleID(cur.getInt(cur.getColumnIndex(arr[1])));
					m.setName(cur.getString(cur.getColumnIndex(arr[2])));
					m.setMobileNo(cur.getString(cur.getColumnIndex(arr[3])));
					m.setUserName(cur.getString(cur.getColumnIndex(arr[4])));
					m.setPassword(cur.getString(cur.getColumnIndex(arr[5])));
					m.setEmailID(cur.getString(cur.getColumnIndex(arr[6])));
					m.setLastUpdate(cur.getString(cur.getColumnIndex(arr[7])));
					m.setTimestamp(cur.getInt(cur.getColumnIndex(arr[8])));
					m.setActive(cur.getString(cur.getColumnIndex(arr[9])));
					ls.add(m);
				} while (cur.moveToNext());
			}
		}
		cur.close();
		return ls;
	}

	public ContentValues getContentValues(AdminUser m) {
		ContentValues cont = new ContentValues();
		cont.put(arr[0], m.getUserid());
		cont.put(arr[1], m.getRoleID());
		cont.put(arr[2], m.getName());
		cont.put(arr[3], m.getMobileNo());
		cont.put(arr[4], m.getUserName());
		cont.put(arr[5], m.getPassword());
		cont.put(arr[6], m.getEmailID());
		cont.put(arr[7], m.getLastUpdate());
		cont.put(arr[8], m.getTimestamp());
		cont.put(arr[9], m.getActive());
		return cont;
	}

}
