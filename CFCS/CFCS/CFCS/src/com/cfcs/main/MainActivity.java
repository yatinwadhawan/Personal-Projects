package com.cfcs.main;

import java.util.ArrayList;
import java.util.List;

import com.cfcs.anim.ActivitySwitcher;
import com.cfcs.classes.AdminUser;
import com.cfcs.dao.AdminUserDao;
import com.cfcs.dao.Database;
import com.cfcs.service.DataSyncService;
import com.cfcs.service.Reciever;
import com.cfcs.sync.SyncData;
import com.loopj.android.http.AsyncHttpClient;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	private Button login;
	private EditText username, password;
	private String user, pass;
	private Config config;
	private AsyncHttpClient client;
	private AlarmManager alarmManager;
	private PendingIntent pendingIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		client = new AsyncHttpClient();
		Config.setClient(client);
		Database database = new Database();   
		Config.setCookie(ApplicationContextProvider.getContext());
		config = new Config();
		login = (Button) findViewById(R.id.login);
		username = (EditText) findViewById(R.id.editTextusername);
		password = (EditText) findViewById(R.id.editTextpassword);
		username.clearFocus();
		password.clearFocus();
		password.setText("omsairam");
		user = "";
		pass = "";
		username.setText(config.getSharedPreferences(this, "usersettings",
				"username", ""));

		setNotificationService();
		login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				user = username.getText().toString().toLowerCase();
				pass = password.getText().toString();

				if (user.compareTo("") != 0 && pass.compareTo("") != 0) {
					boolean flag = matchCreadentials(user, pass);
					if (flag) {
						config.toastShow("Successful Login", MainActivity.this);
						username.setText("");
						password.setText("");
						Intent intent = new Intent(MainActivity.this,
								BasePageActivity.class);
						startActivity(intent);
						finish();
					} else {
						config.toastShow("Username & Password are Incorrect",
								MainActivity.this);
					}
				} else {
					if (user.compareTo("") == 0 && pass.compareTo("") == 0)
						config.alertBox("Please enter Username and Password",
								MainActivity.this);
					else if (user.compareTo("") == 0)
						config.alertBox("Please enter Username",
								MainActivity.this);
					else
						config.alertBox("Please enter Password",
								MainActivity.this);
				}
			}
		});
	}

	public boolean matchCreadentials(String user, String pass) {
		boolean flag = false;
		AdminUserDao adminDao = new AdminUserDao();
		List<AdminUser> lsAdmin = new ArrayList<AdminUser>();
		lsAdmin = adminDao
				.getTablesValues("select * from adminuser where active='true'");
		for (int i = 0; i < lsAdmin.size(); i++) {
			AdminUser admin = lsAdmin.get(i);
			if (admin.getUserName().toLowerCase().compareTo(user) == 0
					&& admin.getPassword().compareTo(pass) == 0) {
				config.putSharedPreferences(MainActivity.this, "usersettings",
						"username", admin.getUserName());
				config.putSharedPreferences(MainActivity.this, "usersettings",
						"name", admin.getName());
				config.putSharedPreferences(MainActivity.this, "usersettings",
						"password", admin.getPassword());
				config.putSharedPreferences(MainActivity.this, "usersettings",
						"userid", admin.getUserid());
				flag = true;
				break;
			} else {
				flag = false;
			}
		}
		return flag;
	}

	private void setNotificationService() {
		Intent intent = new Intent(Reciever.ACTION_REFRESH_ALARM);
		pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
				0, 120000, pendingIntent);
	}

	private void animatedStartActivity(Class<?> cls) {
		final Intent intent = new Intent(MainActivity.this, cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		ActivitySwitcher.animationOut(findViewById(R.id.maincontainer),
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
		ActivitySwitcher.animationOut(findViewById(R.id.maincontainer),
				getWindowManager(),
				new ActivitySwitcher.AnimationFinishedListener() {
					@Override
					public void onAnimationFinished() {
						MainActivity.super.finish();
						// disable default animation
						overridePendingTransition(0, 0);
					}
				});
	}

	@Override
	protected void onResume() {
		// animateIn this activity
		ActivitySwitcher.animationIn(findViewById(R.id.maincontainer),
				getWindowManager());
		super.onResume();
	}

}
