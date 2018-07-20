package com.cfcs.sync;

import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import android.content.Context;

import com.cfcs.classes.ReminderRelation;
import com.cfcs.classes.UpdateTypeMaster;
import com.cfcs.dao.ReminderRelationDao;
import com.cfcs.dao.UpdateTypeMasterDao;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class ReminderRelationSync {

	private String response;

	public ReminderRelationSync(Context c, String response) {
		this.response = response;
		decodeJson();
	}

	private void decodeJson() {

		try {
			ReminderRelationDao dao = new ReminderRelationDao();
			Object jsonObject = new JSONTokener(response).nextValue();
			if (jsonObject instanceof JSONObject) {
				JsonElement json = new JsonParser().parse(response);
				Gson gson = new Gson();
				ReminderRelation admin = gson.fromJson(json,
						ReminderRelation.class);
				if (dao.status_database(admin)) {

				} else
					dao.insert(admin);
			} else if (jsonObject instanceof JSONArray) {
				JsonElement json = new JsonParser().parse(response);
				JsonArray array = json.getAsJsonArray();
				Iterator iterator = array.iterator();
				while (iterator.hasNext()) {
					JsonElement json2 = (JsonElement) iterator.next();
					Gson gson = new Gson();
					ReminderRelation admin = gson.fromJson(json2,
							ReminderRelation.class);
					if (dao.status_database(admin)) {

					} else
						dao.insert(admin);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
