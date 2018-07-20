package com.cfcs.sync;

import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import android.content.Context;
import com.cfcs.classes.CaseUpdate;
import com.cfcs.dao.CaseUpdateDao;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class CaseUpdatesSync {

	private String response;

	public CaseUpdatesSync(Context c, String response) {
		this.response = response;
		decodeJson();
	}

	private void decodeJson() {

		try {
			CaseUpdateDao dao = new CaseUpdateDao();
			Object jsonObject = new JSONTokener(response).nextValue();
			if (jsonObject instanceof JSONObject) {
				JsonElement json = new JsonParser().parse(response);
				Gson gson = new Gson();
				CaseUpdate admin = gson.fromJson(json, CaseUpdate.class);
				admin.setUpdateStamp(1);
				if (dao.status_database(admin))
					dao.update(admin);
				else
					dao.insert(admin);
			} else if (jsonObject instanceof JSONArray) {

				JsonElement json = new JsonParser().parse(response);
				JsonArray jsonArray = json.getAsJsonArray();
				Iterator iterator = jsonArray.iterator();
				while (iterator.hasNext()) {
					JsonElement json2 = (JsonElement) iterator.next();
					Gson gson = new Gson();
					CaseUpdate admin = gson.fromJson(json2, CaseUpdate.class);
					admin.setUpdateStamp(1);
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
