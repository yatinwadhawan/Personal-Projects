package com.cfcs.calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.cfcs.classes.Reminder;
import com.cfcs.dao.ReminderDao;
import com.cfcs.main.ApplicationContextProvider;
import com.cfcs.main.Config;
import com.cfcs.main.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CalendarActivity extends Activity implements
		CalendarView.OnCellTouchListener, OnClickListener {
	public static final String MIME_TYPE = "vnd.android.cursor.dir/vnd.exina.android.calendar.date";
	CalendarView mView = null;
	TextView mHit, monthText;
	Handler mHandler = new Handler();
	private Button next, previous, textDisplay, eventListButton, addEvent;
	private Config config;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar);
		mView = (CalendarView) findViewById(R.id.calendar);
		mView.setOnCellTouchListener(this);

		config = new Config();
		addEvent = (Button) findViewById(R.id.buttonEventAdd);
		eventListButton = (Button) findViewById(R.id.buttonEventList);
		textDisplay = (Button) findViewById(R.id.monthText);
		next = (Button) findViewById(R.id.next);
		previous = (Button) findViewById(R.id.previous);
		monthText = (TextView) findViewById(R.id.monthText);
		next.setOnClickListener(this);
		addEvent.setOnClickListener(this);
		previous.setOnClickListener(this);
		eventListButton.setOnClickListener(this);
		textDisplay.setText(DateUtils.getMonthString(mView.getMonth(),
				DateUtils.LENGTH_LONG) + " " + mView.getYear());
	}

	public void onTouch(Cell cell) {

		int userid = config.getSharedPreferences(
				ApplicationContextProvider.getContext(), "usersettings",
				"userid", 0);
		SimpleDateFormat Todaydf = new SimpleDateFormat("dd MMM yyyy");
		int day = cell.getDayOfMonth();
		int month = mView.getMonth();
		int year = mView.getYear();
		Calendar calCell = Calendar.getInstance();
		calCell.set(year, month, day);
		Date dateCell = calCell.getTime();
		String d = Todaydf.format(dateCell);

		Time todayTime = new Time(Time.getCurrentTimezone());
		todayTime.setToNow();
		int TodayDay = todayTime.monthDay;
		int Todaymonth = todayTime.month;
		int Todayyear = todayTime.year;
		Calendar Todaycal = Calendar.getInstance();
		Todaycal.set(Todayyear, Todaymonth, TodayDay - 1);
		Date Todaydate = Todaycal.getTime();

		if (dateCell.getTime() >= Todaydate.getTime()) {
			ReminderDao dao = new ReminderDao();
			List<Reminder> lsreminder = dao
					.getTablesValues("select * from reminder where reminderdate='"
							+ d
							+ "'  and  reminderid in (select reminderid from reminderRelation where userid="
							+ userid + ")");

			if (lsreminder != null && !lsreminder.isEmpty()) {
				Intent intent = new Intent(CalendarActivity.this,
						EventsListActivity.class);
				intent.putExtra("DAY", day);
				intent.putExtra("MONTH", month);
				intent.putExtra("YEAR", year);
				intent.putExtra("KEY", "y");
				startActivity(intent);
			}
			// monthText.setText(DateUtils.getMonthString(mView.getMonth(),
			// DateUtils.LENGTH_LONG) + " " + mView.getYear());
			// if (mView.firstDay(day))
			// mView.previousMonth();
			// else if (mView.lastDay(day))
			// mView.nextMonth();
			// else
			// return;

		}
	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.previous: {
			mView.previousMonth();
			monthText.setText(DateUtils.getMonthString(mView.getMonth(),
					DateUtils.LENGTH_LONG) + " " + mView.getYear());
			break;
		}
		case R.id.next: {
			mView.nextMonth();
			monthText.setText(DateUtils.getMonthString(mView.getMonth(),
					DateUtils.LENGTH_LONG) + " " + mView.getYear());
			break;
		}
		case R.id.buttonEventList: {
			Intent intent = new Intent(CalendarActivity.this,
					EventsListActivity.class);
			intent.putExtra("KEY", "ALL");
			startActivity(intent);
			break;
		}
		case R.id.buttonEventAdd: {
			Intent intent = new Intent(CalendarActivity.this,
					SchedulerActivity.class);
			intent.putExtra("DAY", -1);
			intent.putExtra("MONTH", mView.getMonth());
			intent.putExtra("YEAR", mView.getYear());
			startActivity(intent);

			break;
		}

		default:
			break;
		}
	}

}