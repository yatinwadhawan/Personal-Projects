package com.cfcs.service;

import com.cfcs.main.Config;
import com.cfcs.sync.SyncData;
import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;

public class DataSyncService extends Service {

	private final IBinder binder = new MyBinder();

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (isNetworkAvailable()) {
			String date = Config.getSharedPreferences(this, "updateTime",
					"time", "1-Jun-2013");
			SyncData sync = new SyncData(this, date);
		}
		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {

		return binder;
	}

	public class MyBinder extends Binder {
		public DataSyncService getService() {
			return DataSyncService.this;
		}
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}
}
