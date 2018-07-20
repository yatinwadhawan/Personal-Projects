package com.cfcs.sync;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;

import com.cfcs.classes.AdminUser;
import com.cfcs.classes.CustomerMaster;
import com.cfcs.dao.AdminUserDao;
import com.cfcs.dao.CustomerMasterDao;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class CustomerMasterAsyn {

	private String response;
	private Context context;
	public CustomerMasterAsyn(Context c, String response) {
		this.response = response;
		this.context=c;
		decodeJson();
	}

	private void decodeJson() {

		try {
			CustomerMasterDao dao = new CustomerMasterDao();
			Object jsonObject = new JSONTokener(response).nextValue();
			if (jsonObject instanceof JSONObject) {
				JsonElement json = new JsonParser().parse(response);
				Gson gson = new Gson();
				CustomerMaster admin = gson.fromJson(json, CustomerMaster.class);
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
					CustomerMaster admin = gson.fromJson(json2, CustomerMaster.class);
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
