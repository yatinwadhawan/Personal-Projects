package com.cfcs.main;

import com.cfcs.anim.ActivitySwitcher;
import com.cfcs.calendar.CalendarActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class BasePageActivity extends Activity implements OnClickListener {

	Button caseUpdate, calendar, chat, logOut;
	TextView username;
	Config config;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basepage);

		config = new Config();
		username = (TextView) findViewById(R.id.textUsername);
		caseUpdate = (Button) findViewById(R.id.caseupdate);
		calendar = (Button) findViewById(R.id.calendar);
		chat = (Button) findViewById(R.id.chat);
		logOut = (Button) findViewById(R.id.logout);
		caseUpdate.setOnClickListener(this);
		calendar.setOnClickListener(this);
		chat.setOnClickListener(this);
		logOut.setOnClickListener(this);

		String user = config.getSharedPreferences(this, "usersettings", "name",
				"User");
		username.setText("Welcome " + user);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.caseupdate:
			Intent caseUpdateIntent = new Intent(BasePageActivity.this,
					CaseUpdateActivity.class);
			startActivity(caseUpdateIntent);
			break;
		case R.id.calendar:
			Intent calendar = new Intent(BasePageActivity.this,
					CalendarActivity.class);
			startActivity(calendar);
			break;
		case R.id.chat:
			Intent sms = new Intent(BasePageActivity.this, SendSMS.class);
			startActivity(sms);
			break;
		case R.id.logout:
			logout();
			break;
		default:
			break;
		}
	}

	public void logout() {
		Intent sms = new Intent(BasePageActivity.this, MainActivity.class);
		startActivity(sms);
		Config.toastShow("Successfully Loged Out", this);
		finish();
	}

	@Override
	protected void onResume() {
		// animateIn this activity
		ActivitySwitcher.animationIn(findViewById(R.id.basecontainer),
				getWindowManager());
		super.onResume();
	}

	private void animatedStartActivity(Class<?> cls) {
		final Intent intent = new Intent(BasePageActivity.this, cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		ActivitySwitcher.animationOut(findViewById(R.id.basecontainer),
				getWindowManager(),
				new ActivitySwitcher.AnimationFinishedListener() {
					@Override
					public void onAnimationFinished() {
						startActivity(intent);
					}
				});
	}

	@Override
	public void finish() {
		// we need to override this to performe the animtationOut on each
		// finish.
		ActivitySwitcher.animationOut(findViewById(R.id.basecontainer),
				getWindowManager(),
				new ActivitySwitcher.AnimationFinishedListener() {
					@Override
					public void onAnimationFinished() {
						BasePageActivity.super.finish();
						// disable default animation
						overridePendingTransition(0, 0);
					}
				});
	}

}
