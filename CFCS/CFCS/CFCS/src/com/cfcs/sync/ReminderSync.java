package com.cfcs.sync;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import android.content.Context;
import com.cfcs.classes.Reminder;
import com.cfcs.dao.ReminderAdvocateDao;
import com.cfcs.dao.ReminderDao;
import com.cfcs.dao.ReminderRelationDao;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class ReminderSync {

	private String response;

	public ReminderSync(Context c, String response) {
		this.response = response;
		decodeJson();
	}

	private void decodeJson() {

		try {
			ReminderDao dao = new ReminderDao();
			ReminderAdvocateDao reminderadocateDao = new ReminderAdvocateDao();
			ReminderRelationDao relationdao = new ReminderRelationDao();
			Object jsonObject = new JSONTokener(response).nextValue();
			if (jsonObject instanceof JSONObject) {
				JsonElement json = new JsonParser().parse(response);
				Gson gson = new Gson();
				Reminder admin = gson.fromJson(json, Reminder.class);
				admin.setUpdateStamp(1);
				admin.setAdvocateid(0);
				if (dao.status_database(admin))
					dao.update(admin);
				else
					dao.insert(admin);

				reminderadocateDao.delete(admin.getReminderID());
				relationdao.delete(admin.getReminderID());
			} else if (jsonObject instanceof JSONArray) {

				JsonElement json = new JsonParser().parse(response);
				JsonArray jsonArray = json.getAsJsonArray();
				Iterator iterator = jsonArray.iterator();
				while (iterator.hasNext()) {
					JsonElement json2 = (JsonElement) iterator.next();
					Gson gson = new Gson();
					Reminder admin = gson.fromJson(json2, Reminder.class);
					admin.setUpdateStamp(1);
					admin.setAdvocateid(0);
					if (dao.status_database(admin))
						dao.update(admin);
					else
						dao.insert(admin);
					reminderadocateDao.delete(admin.getReminderID());
					relationdao.delete(admin.getReminderID());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
