package com.cfcs.sync;

import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import android.content.Context;
import com.cfcs.classes.CaseMaster;
import com.cfcs.dao.CaseMasterDao;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class CaseMasterSync {

	private String response;

	public CaseMasterSync(Context c, String response) {
		this.response = response;
		decodeJson();
	}

	private void decodeJson() {

		try {
			CaseMasterDao dao = new CaseMasterDao();
			Object jsonObject = new JSONTokener(response).nextValue();
			if (jsonObject instanceof JSONObject) {
				JsonElement json = new JsonParser().parse(response);
				Gson gson = new Gson();
				CaseMaster admin = gson.fromJson(json, CaseMaster.class);
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
					CaseMaster admin = gson.fromJson(json2, CaseMaster.class);
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
