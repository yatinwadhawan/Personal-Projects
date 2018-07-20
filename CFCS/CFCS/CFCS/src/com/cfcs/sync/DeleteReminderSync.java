package com.cfcs.sync;

import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import android.content.Context;

import com.cfcs.classes.DeleteReminder;
import com.cfcs.classes.GroupRelation;
import com.cfcs.classes.Reminder;
import com.cfcs.classes.ReminderRelation;
import com.cfcs.dao.DeleteReminderDao;
import com.cfcs.dao.GroupRelationDao;
import com.cfcs.dao.ReminderAdvocateDao;
import com.cfcs.dao.ReminderDao;
import com.cfcs.dao.ReminderRelationDao;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class DeleteReminderSync {

	private String response;

	public DeleteReminderSync(Context c, String response) {
		this.response = response;
		decodeJson();
	}

	private void decodeJson() {

		try {
			DeleteReminderDao dao = new DeleteReminderDao();
			ReminderAdvocateDao reminderadocateDao = new ReminderAdvocateDao();
			ReminderRelationDao relationdao = new ReminderRelationDao();
			ReminderDao reminderdao = new ReminderDao();
			Object jsonObject = new JSONTokener(response).nextValue();
			if (jsonObject instanceof JSONObject) {
				JsonElement json = new JsonParser().parse(response);
				Gson gson = new Gson();
				DeleteReminder admin = gson
						.fromJson(json, DeleteReminder.class);

				Reminder r = new Reminder();
				r.setReminderID(admin.getReminderID());
				reminderdao.delete(r);
				relationdao.delete(admin.getReminderID());
				reminderadocateDao.delete(admin.getReminderID());

			} else if (jsonObject instanceof JSONArray) {
				JsonElement json = new JsonParser().parse(response);
				JsonArray jsonArray = json.getAsJsonArray();
				Iterator iterator = jsonArray.iterator();
				while (iterator.hasNext()) {
					JsonElement json2 = (JsonElement) iterator.next();
					Gson gson = new Gson();
					DeleteReminder admin = gson.fromJson(json2,
							DeleteReminder.class);
					Reminder r = new Reminder();
					r.setReminderID(admin.getReminderID());
					reminderdao.delete(r);
					relationdao.delete(admin.getReminderID());
					reminderadocateDao.delete(admin.getReminderID());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
