package com.cfcs.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Reciever extends BroadcastReceiver{

	public static final String ACTION_REFRESH_ALARM ="com.paad.network.ACTION_REFRESH_ALARM";
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent myIntent=new Intent(context, DataSyncService.class);
		context.startService(myIntent);

	}
	

}
