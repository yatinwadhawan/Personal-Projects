package com.cfcs.calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	public final static String ALARM_RECEIVER = "com.paad.network.ALARM_RECEIVER";

	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub
		Toast.makeText(context, "Alarm received!", Toast.LENGTH_LONG).show();
	}

}
