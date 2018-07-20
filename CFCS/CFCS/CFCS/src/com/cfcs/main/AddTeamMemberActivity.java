package com.cfcs.main;

import java.util.ArrayList;
import java.util.List;
import com.cfcs.anim.ActivitySwitcher;
import com.cfcs.classes.AdminUser;
import com.cfcs.dao.AdminUserDao;
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
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

public class AddTeamMemberActivity extends Activity implements OnClickListener {

	Button addTeam;
	ListView listView;
	List<AdminUser> listAdminUser, listSelected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teammember);

		listAdminUser = new ArrayList<AdminUser>();
		listSelected = new ArrayList<AdminUser>();
		addTeam = (Button) findViewById(R.id.buttonTeamMembers);
		listView = (ListView) findViewById(R.id.listViewTeamMembers);
		addTeam.setOnClickListener(this);

		AdminUserDao dao = new AdminUserDao();
		listAdminUser = dao
				.getTablesValues("select * from adminuser where active='true' Order by name COLLATE NOCASE ASC;");
		int userid = Config.getSharedPreferences(this, "usersettings",
				"userid", -1);
		if (userid != -1 && listAdminUser != null && !listAdminUser.isEmpty()) {
			for (int i = 0; i < listAdminUser.size(); i++) {
				AdminUser user = listAdminUser.get(i);
				if (userid == user.getUserid()) {
					listAdminUser.remove(i);
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
			return listAdminUser.size();
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

			final AdminUser user = listAdminUser.get(position);

			LinearLayout layout = new LinearLayout(AddTeamMemberActivity.this);
			CheckBox check = new CheckBox(AddTeamMemberActivity.this);
			check.setText(user.getName());
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
				ls.add(listSelected.get(i).getUserid());
			}
			myIntent.putIntegerArrayListExtra("SELECTED", ls);
			setResult(1, myIntent);
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
		final Intent intent = new Intent(AddTeamMemberActivity.this, cls);
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
						AddTeamMemberActivity.super.finish();
						// disable default animation
						overridePendingTransition(0, 0);
					}
				});
	}

}
