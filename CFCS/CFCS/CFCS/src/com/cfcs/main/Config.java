package com.cfcs.main;

import com.cfcs.anim.ActivitySwitcher;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.TextView;
import android.widget.Toast;

public class Config {

	public static AsyncHttpClient client;
	public static PersistentCookieStore myCookieStore;
	public final static String username = "Vijay";
	// public final static String password = "#ala@cfcs208";
	public final static String password = "cfcs";

	// public static String BASE_URL =
	// "http://182.18.176.22/Office/MobileServices/SyncService.asmx/";

	public static String BASE_URL = "http://182.18.142.12:456/Advocate/MobileServices/SyncService.asmx/";

	public static void putSharedPreferences(Context context, String preference,
			String key, String value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				preference, context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void putSharedPreferences(Context context, String preference,
			String key, int value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				preference, context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static String getSharedPreferences(Context context,
			String preference, String key, String defValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				preference, context.MODE_PRIVATE);
		String value = sharedPreferences.getString(key, defValue);
		return value;
	}

	public static int getSharedPreferences(Context context, String preference,
			String key, int defValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				preference, context.MODE_PRIVATE);
		int value = sharedPreferences.getInt(key, defValue);
		return value;
	}

	public static void alertBox(String s, Context c) {
		AlertDialog.Builder altDialog;
		altDialog = new AlertDialog.Builder(c);
		altDialog.setMessage(s); // here add your message
		altDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		altDialog.show();
	}

	public static void toastShow(String s, Context c) {
		Toast toast = Toast.makeText(c, s, 15);
		TextView v = (TextView) toast.getView().findViewById(
				android.R.id.message);
		v.setTextSize(20);
		toast.show();
	}

	public static String getDB_PATH() {
		String DB_PATH = "/data/data/"
				+ ApplicationContextProvider.getContext().getPackageName()
				+ "/databases/";
		return DB_PATH;
	}

	public static String getDB_NAME() {
		String DB_NAME = "pwrms_law";
		return DB_NAME;
	}

	public static void setCookie(Context c) {
		myCookieStore = new PersistentCookieStore(c);
		client.setCookieStore(myCookieStore);
	}

	public static AsyncHttpClient getClient() {
		return client;
	}

	public static void setClient(AsyncHttpClient client) {
		Config.client = client;
	}

}
