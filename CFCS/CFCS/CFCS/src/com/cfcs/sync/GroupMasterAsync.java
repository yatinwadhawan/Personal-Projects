package com.cfcs.sync;

import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import android.content.Context;
import com.cfcs.classes.GroupMaster;
import com.cfcs.dao.GroupMasterDao;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class GroupMasterAsync {

	private String response;

	public GroupMasterAsync(Context c, String response) {
		this.response = response;
		decodeJson();
	}

	private void decodeJson() {

		try {
			GroupMasterDao dao = new GroupMasterDao();
			Object jsonObject = new JSONTokener(response).nextValue();
			if (jsonObject instanceof JSONObject) {
				JsonElement json = new JsonParser().parse(response);
				Gson gson = new Gson();
				GroupMaster admin = gson.fromJson(json, GroupMaster.class);
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
					GroupMaster admin = gson.fromJson(json2, GroupMaster.class);
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
