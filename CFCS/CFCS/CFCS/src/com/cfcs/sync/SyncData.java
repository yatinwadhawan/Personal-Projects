package com.cfcs.sync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cfcs.calendar.ReminderFunctinalityClass;
import com.cfcs.classes.CaseUpdate;
import com.cfcs.classes.DeleteCaseUpdateIds;
import com.cfcs.dao.CaseUpdateDao;
import com.cfcs.dao.Database;
import com.cfcs.dao.DeleteCaseUpdateDao;
import com.cfcs.main.ApplicationContextProvider;
import com.cfcs.main.CaseUpdateActivity;
import com.cfcs.main.CaseUpdates;
import com.cfcs.main.Config;
import com.cfcs.main.EditCaseUpdateActivity;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.provider.ContactsContract.Contacts.Data;

public class SyncData extends AsyncHttpResponseHandler {

	private AsyncHttpClient client;
	private Context context;
	private String url = Config.BASE_URL + "RequestSyncData?" + "UserName="
			+ Config.username + "&Password=" + Config.password
			+ "&SyncDateTime=1-jun-2013";
	private Database db;

	public SyncData(Context c, String time) {
		db = Database.instance();
		this.client = Config.getClient();
		this.context = c;
		url = Config.BASE_URL + "RequestSyncData";

		RequestParams params = new RequestParams();
		params.put("Username", Config.username);
		params.put("Password", Config.password);
		params.put("SyncDateTime", Config.getSharedPreferences(context,
				"updateTime", "time", "1-Jun-2012"));
		this.client.get(url, params, this);
		// sync(copydatabase());
	}

	public void sync(String response) {
		AsyncTaskForSyncData sync = new AsyncTaskForSyncData();
		sync.execute(response);
	}

	public void sendCaseUpdates() {

		String url = Config.BASE_URL + "ResponseCaseUpdate";
		String datajson = getCaseUpdateObjectFilled();
		if (datajson != null && datajson.compareTo("") != 0
				&& datajson.length() > 3) {
			RequestParams params = new RequestParams();
			params.put("DataJson", datajson);
			params.put("UserName", Config.username);
			params.put("Password", Config.password);
			client.post(url, params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String response) {
					Config.toastShow("Successfully Sent CaseUpdates to Server",
							context);
				}

				@Override
				public void onFailure(Throwable error, String content) {
					Config.toastShow("UnSuccessfully CaseUpdates to Server",
							context);
				}
			});
		}
	}

	public String getCaseUpdateObjectFilled() {
		CaseUpdateDao dao = new CaseUpdateDao();
		String query = "select * from caseupdate where updatestamp=0";
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		JSONObject finalobj1 = new JSONObject();
		List<CaseUpdate> lscase = dao.getTablesValues(query);
		try {
			if (lscase != null && !lscase.isEmpty()) {
				for (int i = 0; i < lscase.size(); i++) {
					Gson gson = new Gson();
					String json = gson.toJson(lscase.get(i));
					JSONObject objfirst = new JSONObject(json);
					array.put(objfirst);
				}
				obj.put("CaseUpdate", array);
				finalobj1.put("SyncData", obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return finalobj1.toString();
	}

	private class AsyncTaskForSyncData extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			try {
				sendCaseUpdates();
				ReminderFunctinalityClass rf = new ReminderFunctinalityClass();
				rf.sendReminder(context);
				rf.deleteReminderFromServerTakenFromDatabase(context);

				JSONObject sync = new JSONObject(params[0]);
				CaseMasterSync caseSync = new CaseMasterSync(context,
						sync.getString("CaseMaster"));
				CaseUpdatesSync caseUpdateSync = new CaseUpdatesSync(context,
						sync.getString("CaseUpdates"));
				AdminUserSync adminsync = new AdminUserSync(context,
						sync.getString("AdminUser"));
				GroupMasterAsync groupmaster = new GroupMasterAsync(context,
						sync.getString("GroupMaster"));
				GroupRelationSync grouprelationsync = new GroupRelationSync(
						context, sync.getString("GroupRelation"));
				CustomerMasterAsyn customerSync = new CustomerMasterAsyn(
						context, sync.getString("CustomerMaster"));
				AdvocateSync advocateSync = new AdvocateSync(context,
						sync.getString("OtherAdvocate"));
				UpdateTypeMasterSync updateSync = new UpdateTypeMasterSync(
						context, sync.getString("UpdateTypeMaster"));
				CourtTypeMasterSync courttypesync = new CourtTypeMasterSync(
						context, sync.getString("CourtTypeMaster"));

				DeleteReminderSync deletereminder = new DeleteReminderSync(
						context, sync.getString("ReminderDeletedLog"));
				ReminderSync reminderSync = new ReminderSync(context,
						sync.getString("ReminderMaster"));
				ReminderRelationSync reminderrelationsync = new ReminderRelationSync(
						context, sync.getString("ReminderRelation"));
				ReminderAdvocateSync remideradvocateSync = new ReminderAdvocateSync(
						context, sync.getString("ReminderAdvocate"));

				SimpleDateFormat df = new SimpleDateFormat(
						"dd-MMM-yyyy hh:mm:ss");
				String date = df.format(Calendar.getInstance().getTime());
				Config.putSharedPreferences(context, "updateTime", "time", date);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	@Override
	public void onSuccess(String arg0) {
		super.onSuccess(arg0);
		try {
			if (arg0 != null) {
				if (arg0.length() > 75) {
					String response = arg0.substring(75, arg0.length() - 9);
					sync(response);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onFailure(Throwable arg0) {
		super.onFailure(arg0);
	}

	public static String copydatabase() {
		String response = "";
		AssetManager am = ApplicationContextProvider.getContext().getAssets();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					am.open("sync.txt")));
			String mLine = reader.readLine();
			while (mLine != null) {
				response = response + mLine;
				mLine = reader.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	// private void sendCaseUpdateDeleted() {
	// JSONArray array = new JSONArray();
	// JSONObject caseupdateJSON = new JSONObject();
	// JSONObject syncdate = new JSONObject();
	//
	// DeleteCaseUpdateDao dao = new DeleteCaseUpdateDao();
	// List<DeleteCaseUpdateIds> lsDelete = new
	// ArrayList<DeleteCaseUpdateIds>();
	// try {
	// if (lsDelete != null && !lsDelete.isEmpty()) {
	// for (int i = 0; i < lsDelete.size(); i++) {
	// DeleteCaseUpdateIds delete = lsDelete.get(i);
	// JSONObject obj = new JSONObject();
	// obj.put("CaseUpdateID", delete.getCaseUpdateId());
	// array.put(obj);
	// }
	// caseupdateJSON.put("CaseUpdate", array);
	// syncdate.put("SyncData", caseupdateJSON);
	//
	// String url =
	// "http://182.18.142.12:456/Advocate/MobileServices/SyncService.asmx/ResponseCaseDelete";
	// RequestParams params = new RequestParams();
	// params.put("DataJson", syncdate.toString());
	// params.put("UserName", Config.username);
	// params.put("Password", Config.password);
	// client.post(url, params, new AsyncHttpResponseHandler() {
	// @Override
	// public void onSuccess(String response) {
	//
	// }
	//
	// @Override
	// public void onFailure(Throwable error, String content) {
	// }
	// });
	// }
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

}
