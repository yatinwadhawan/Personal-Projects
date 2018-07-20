package com.cfcs.sync;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;

import com.cfcs.classes.AdminUser;
import com.cfcs.classes.Advocate;
import com.cfcs.dao.AdminUserDao;
import com.cfcs.dao.AdvocateDao;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class AdvocateSync {

	private String response;

	public AdvocateSync(Context c, String response) {
		this.response = response;
		decodeJson();
	}

	private void decodeJson() {

		try {
			AdvocateDao dao = new AdvocateDao();
			Object jsonObject = new JSONTokener(response).nextValue();
			if (jsonObject instanceof JSONObject) {
				JsonElement json = new JsonParser().parse(response);
				Gson gson = new Gson();
				Advocate admin = gson.fromJson(json, Advocate.class);
				if (dao.status_database(admin))
					dao.update(admin);
				else
					dao.insert(admin);
			} else if (jsonObject instanceof JSONArray) {
				JsonElement json = new JsonParser().parse(response);
				JsonArray array = json.getAsJsonArray();
				Iterator iterator = array.iterator();
				while (iterator.hasNext()) {
					JsonElement json2 = (JsonElement) iterator.next();
					Gson gson = new Gson();
					Advocate admin = gson.fromJson(json2, Advocate.class);
					if (dao.status_database(admin))
						dao.update(admin);
					else
						dao.insert(admin);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
