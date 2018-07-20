package com.cfcs.main;

import java.util.ArrayList;
import java.util.List;
import com.cfcs.anim.ActivitySwitcher;
import com.cfcs.classes.AdminUser;
import com.cfcs.classes.Advocate;
import com.cfcs.dao.AdminUserDao;
import com.cfcs.dao.AdvocateDao;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

public class AddReminderAdvocateActivity extends Activity implements
		OnClickListener {

	Button addTeam;
	ListView listView;
	List<Advocate> listAdvocate, listSelected;
	TextView textHeading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teammember);

		listAdvocate = new ArrayList<Advocate>();
		listSelected = new ArrayList<Advocate>();
		addTeam = (Button) findViewById(R.id.buttonTeamMembers);
		listView = (ListView) findViewById(R.id.listViewTeamMembers);
		addTeam.setOnClickListener(this);
		textHeading = (TextView) findViewById(R.id.textViewTeamMembers);
		textHeading.setText("Select Advocate");

		AdvocateDao dao = new AdvocateDao();
		listAdvocate = dao
				.getTablesValues("select * from otheradvocate where type=1 and active='true' Order by advocatename COLLATE NOCASE ASC");
		int userid = Config.getSharedPreferences(this, "usersettings",
				"userid", -1);
		if (userid != -1 && listAdvocate != null && !listAdvocate.isEmpty()) {
			for (int i = 0; i < listAdvocate.size(); i++) {
				Advocate user = listAdvocate.get(i);
				if (userid == user.getAdvocateID()) {
					listAdvocate.remove(i);
					break;
				}
			}
			listView.setAdapter(new ListAdapter());
		} else {
			Config.toastShow("Not Available", this);
		}
	}

	private class ListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listAdvocate.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View arg1, ViewGroup arg2) {

			final Advocate user = listAdvocate.get(position);

			LinearLayout layout = new LinearLayout(
					AddReminderAdvocateActivity.this);
			CheckBox check = new CheckBox(AddReminderAdvocateActivity.this);
			check.setText(user.getAdvocateName());
			check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (isChecked)
						listSelected.add(user);
					else
						listSelected.remove(user);
				}
			});

			if (position % 2 == 0)
				layout.setBackgroundColor(Color.parseColor("#D1D0CE"));

			layout.addView(check);

			return layout;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonTeamMembers: {
			Intent myIntent = new Intent();
			ArrayList<Integer> ls = new ArrayList<Integer>();
			for (int i = 0; i < listSelected.size(); i++) {
				ls.add(listSelected.get(i).getAdvocateID());
			}
			myIntent.putIntegerArrayListExtra("SELECTED", ls);
			setResult(2, myIntent);
			finish();
			break;
		}
		default:
			break;
		}

	}

	@Override
	protected void onResume() {
		// animateIn this activity
		ActivitySwitcher.animationIn(findViewById(R.id.teammembedrcontainer),
				getWindowManager());
		super.onResume();
	}

	private void animatedStartActivity(Class<?> cls) {
		final Intent intent = new Intent(AddReminderAdvocateActivity.this, cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		ActivitySwitcher.animationOut(findViewById(R.id.teammembedrcontainer),
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
		ActivitySwitcher.animationOut(findViewById(R.id.teammembedrcontainer),
				getWindowManager(),
				new ActivitySwitcher.AnimationFinishedListener() {
					@Override
					public void onAnimationFinished() {
						AddReminderAdvocateActivity.super.finish();
						// disable default animation
						overridePendingTransition(0, 0);
					}
				});
	}

}
