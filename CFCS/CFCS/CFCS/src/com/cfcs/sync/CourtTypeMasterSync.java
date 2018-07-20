package com.cfcs.sync;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;

import com.cfcs.classes.CourtTypeMaster;
import com.cfcs.classes.GroupRelation;
import com.cfcs.dao.CourtTypeMasterDao;
import com.cfcs.dao.GroupRelationDao;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class CourtTypeMasterSync {

	private String response;

	public CourtTypeMasterSync(Context c, String response) {
		this.response = response;
		decodeJson();
	}

	private void decodeJson() {

		try {
			CourtTypeMasterDao dao = new CourtTypeMasterDao();
			Object jsonObject = new JSONTokener(response).nextValue();
			if (jsonObject instanceof JSONObject) {
				JsonElement json = new JsonParser().parse(response);
				Gson gson = new Gson();
				CourtTypeMaster admin = gson.fromJson(json,
						CourtTypeMaster.class);
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
					CourtTypeMaster admin = gson.fromJson(json2,
							CourtTypeMaster.class);
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
