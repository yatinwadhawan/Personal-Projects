package com.cfcs.sync;

import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import android.content.Context;
import com.cfcs.classes.AdminUser;
import com.cfcs.dao.AdminUserDao;
import com.cfcs.dao.GroupRelationDao;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class AdminUserSync extends AsyncHttpResponseHandler {

	private String response;

	public AdminUserSync(Context c, String response) {
		this.response = response;
		decodeJson();
	}

	private void decodeJson() {

		try {
			AdminUserDao dao = new AdminUserDao();
			GroupRelationDao relationDao = new GroupRelationDao();
			Object jsonObject = new JSONTokener(response).nextValue();
			if (jsonObject instanceof JSONObject) {
				JsonElement json = new JsonParser().parse(response);
				Gson gson = new Gson();
				AdminUser admin = gson.fromJson(json, AdminUser.class);
				if (dao.status_database(admin))
					dao.update(admin);
				else
					dao.insert(admin);

				relationDao.delete(admin.getUserid());

			} else if (jsonObject instanceof JSONArray) {
				JsonElement json = new JsonParser().parse(response);
				JsonArray array = json.getAsJsonArray();
				Iterator iterator = array.iterator();
				while (iterator.hasNext()) {
					JsonElement json2 = (JsonElement) iterator.next();
					Gson gson = new Gson();
					AdminUser admin = gson.fromJson(json2, AdminUser.class);
					if (dao.status_database(admin))
						dao.update(admin);
					else
						dao.insert(admin);

					relationDao.delete(admin.getUserid());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
