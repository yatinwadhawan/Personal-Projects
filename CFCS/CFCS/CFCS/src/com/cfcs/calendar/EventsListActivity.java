package com.cfcs.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.cfcs.anim.ActivitySwitcher;
import com.cfcs.classes.AdminUser;
import com.cfcs.classes.CaseMaster;
import com.cfcs.classes.CustomerMaster;
import com.cfcs.classes.Reminder;
import com.cfcs.classes.ReminderRelation;
import com.cfcs.dao.AdminUserDao;
import com.cfcs.dao.CaseMasterDao;
import com.cfcs.dao.CustomerMasterDao;
import com.cfcs.dao.ReminderDao;
import com.cfcs.dao.ReminderRelationDao;
import com.cfcs.main.Config;
import com.cfcs.main.R;
import com.cfcs.main.R.id;
import com.cfcs.main.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class EventsListActivity extends Activity implements OnItemClickListener {

	private ListView listView;
	private String type;
	private String currentDate;
	private Config config;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventslist);

		listView = (ListView) findViewById(R.id.listViewEventList);
		config = new Config();
		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();
		int day = today.monthDay;
		int month = today.month;
		int year = today.year;
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day - 1);
		SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
		Date date = cal.getTime();

		ReminderDao dao = new ReminderDao();
		List<Reminder> lsreminder = new ArrayList<Reminder>();

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");

		if (getIntent().getExtras().getString("KEY").compareTo("ALL") == 0) {
			int userid = config.getSharedPreferences(EventsListActivity.this,
					"usersettings", "userid", 0);
			List<Reminder> ls = dao
					.getTablesValues("select * from reminder where reminderid in (select reminderid from reminderRelation where userid="
							+ userid + ")");
			// Order by DATETIME(reminderdate)

			if (!ls.isEmpty()) {
				for (int i = 0; i < ls.size(); i++) {
					Reminder reminder = ls.get(i);
					String reminderdate = reminder.getReminderdate();
					try {
						Date datereminder = dateFormat.parse(reminderdate);
						if (datereminder.getTime() > date.getTime()) {
							lsreminder.add(reminder);
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				type = "ALL";
				listView.setAdapter(new ListAdapter(lsreminder));
			}
		} else {
			int clickedday = getIntent().getExtras().getInt("DAY");
			int clickedmonth = getIntent().getExtras().getInt("MONTH");
			int clickedYear = getIntent().getExtras().getInt("YEAR");
			Calendar clickedcal = Calendar.getInstance();
			clickedcal.set(clickedYear, clickedmonth, clickedday);
			currentDate = df.format(clickedcal.getTime());
			int userid = config.getSharedPreferences(EventsListActivity.this,
					"usersettings", "userid", 0);
			lsreminder = dao
					.getTablesValues("select * from reminder where reminderdate='"
							+ currentDate
							+ "' and  reminderid in (select reminderid from reminderRelation where userid="
							+ userid
							+ ")   Order by DATETIME(reminderdate) ASC");
			type = "y";
		}
		if (lsreminder != null && !lsreminder.isEmpty()) {
			for (int i = 0; i < lsreminder.size(); i++) {
				for (int j = i; j < lsreminder.size(); j++) {
					Reminder reminderI = lsreminder.get(i);
					Reminder reminderJ = lsreminder.get(j);
					String reminderdateI = reminderI.getReminderdate();
					String reminderdateJ = reminderJ.getReminderdate();
					try {
						Date datereminderI = dateFormat.parse(reminderdateI);
						Date datereminderJ = dateFormat.parse(reminderdateJ);
						if (datereminderI.getTime() > datereminderJ.getTime()) {
							lsreminder.set(j, reminderI);
							lsreminder.set(i, reminderJ);
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
			listView.setAdapter(new ListAdapter(lsreminder));
		}
		listView.setOnItemClickListener(this);
	}

	private class ListAdapter extends BaseAdapter {

		private List<Reminder> lsreminder;

		public ListAdapter(List<Reminder> lsreminder) {
			this.lsreminder = lsreminder;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return lsreminder.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return lsreminder.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View arg1, ViewGroup arg2) {

			Reminder reminder = lsreminder.get(position);

			LayoutInflater layoutInflater1 = (LayoutInflater) getBaseContext()
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			LinearLayout basepage = (LinearLayout) layoutInflater1.inflate(
					R.layout.event, null);
			LinearLayout layout = (LinearLayout) basepage
					.findViewById(R.id.singleeventlayout);

			TextView countEvent = (TextView) layout
					.findViewById(R.id.textViewSingleEventcount);
			countEvent.setText(Integer.toString(position + 1));

			TextView nameEvent = (TextView) layout
					.findViewById(R.id.textViewSingleEventTitle);
			nameEvent.setText("Title : " + reminder.getTitle());

			TextView dateEvent = (TextView) layout
					.findViewById(R.id.textViewSingleEventdate);
			dateEvent.setText("Date : " + reminder.getReminderdate());

			TextView timeEvent = (TextView) layout
					.findViewById(R.id.textViewSingleEventTime);
			timeEvent.setText("Time : " + reminder.getTime());

			CaseMasterDao casedao = new CaseMasterDao();
			List<CaseMaster> lscaseMaster = new ArrayList<CaseMaster>();
			lscaseMaster = casedao
					.getTablesValues("select * from casemaster where caseid="
							+ reminder.getCaseid());
			TextView caseName = (TextView) layout
					.findViewById(R.id.textViewSingleEventCaseName);
			if (lscaseMaster != null && lscaseMaster.size() > 0) {
				caseName.setText("Case : " + lscaseMaster.get(0).getCaseTitle());
			} else {
				caseName.setVisibility(View.GONE);
			}

			TextView clientname = (TextView) layout
					.findViewById(R.id.textViewSingleEventClientName);
			CustomerMasterDao dao = new CustomerMasterDao();
			List<CustomerMaster> lscust = dao
					.getTablesValues("select * from customermaster where customerid="
							+ reminder.getCustomerid());
			if (lscust != null && !lscust.isEmpty()) {
				clientname.setVisibility(View.VISIBLE);
				clientname.setTextColor(Color.BLUE);
				clientname
						.setText("Client :" + lscust.get(0).getCustomerName());
			} else {
				clientname.setVisibility(View.GONE);
			}

			AdminUserDao admibD = new AdminUserDao();
			int createdBy = reminder.getAddBy();
			TextView scheduledByText = (TextView) layout
					.findViewById(R.id.textViewSingleEventScheduledBy);
			List<AdminUser> lsAdmin = new ArrayList<AdminUser>();
			lsAdmin = admibD
					.getTablesValues("select * from adminuser where userid="
							+ createdBy);
			if (lsAdmin != null && !lsAdmin.isEmpty()) {
				scheduledByText.setText("By : " + lsAdmin.get(0).getName());
				scheduledByText.setTextColor(Color.RED);
			}

			ReminderRelationDao relationDao = new ReminderRelationDao();
			TextView options = (TextView) layout
					.findViewById(R.id.textViewSingleEventOptions);
			List<ReminderRelation> lsrelation = relationDao
					.getTablesValues("select * from reminderRelation where reminderid='"
							+ reminder.getReminderID() + "';");
			if (lsrelation != null && !lsrelation.isEmpty()) {
				int size = lsrelation.size() - 1;
				options.setText("Y(" + size + ")");
				options.setVisibility(View.VISIBLE);
				options.setTextColor(Color.BLUE);
			} else {
				options.setVisibility(View.GONE);
			}

			if (position % 2 == 0)
				layout.setBackgroundColor(Color.parseColor("#D1D0CE"));

			return basepage;
		}
	}

	@Override
	protected void onResume() {
		// animateIn this activity
		ActivitySwitcher.animationIn(findViewById(R.id.eventlistContainer),
				getWindowManager());
		super.onResume();
	}

	private void animatedStartActivity(Class<?> cls, int position) {
		final Intent intent = new Intent(EventsListActivity.this, cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		intent.putExtra("reminderIndex", position);
		intent.putExtra("KEY", type);
		intent.putExtra("DATE", currentDate);
		ActivitySwitcher.animationOut(findViewById(R.id.eventlistContainer),
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
		ActivitySwitcher.animationOut(findViewById(R.id.eventlistContainer),
				getWindowManager(),
				new ActivitySwitcher.AnimationFinishedListener() {
					@Override
					public void onAnimationFinished() {
						EventsListActivity.super.finish();
						// disable default animation
						overridePendingTransition(0, 0);
					}
				});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		animatedStartActivity(EventListDetails.class, arg2);
	}

}
