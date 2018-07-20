package com.cfcs.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cfcs.classes.AdminUser;
import com.cfcs.classes.Advocate;
import com.cfcs.classes.CaseMaster;
import com.cfcs.classes.CaseUpdate;
import com.cfcs.classes.CustomerMaster;
import com.cfcs.classes.DeleteReminder;
import com.cfcs.classes.Reminder;
import com.cfcs.classes.ReminderAdvocate;
import com.cfcs.classes.ReminderRelation;
import com.cfcs.classes.UpdateTypeMaster;
import com.cfcs.dao.AdminUserDao;
import com.cfcs.dao.AdvocateDao;
import com.cfcs.dao.CaseMasterDao;
import com.cfcs.dao.CustomerMasterDao;
import com.cfcs.dao.DeleteReminderDao;
import com.cfcs.dao.ReminderAdvocateDao;
import com.cfcs.dao.ReminderDao;
import com.cfcs.dao.ReminderRelationDao;
import com.cfcs.dao.UpdateTypeMasterDao;
import com.cfcs.main.CaseDetails;
import com.cfcs.main.Config;
import com.cfcs.main.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class EventListDetails extends Activity {

	private LinearLayout layoutAddedMembers, clubLayout, caselayout,
			clientlayout, advocatelayout, remarkLayout,
			layoutAddTeamMemberText, layoutAddReminderAdvocate;

	private TextView eventDateText, eventTitleText, eventTimeText,
			eventWorkUpdateTypeText, eventClientText, eventCaseText,
			eventReminderBeforeText, eventRemarkText, eventDetailNumber,
			scheduledByText, createDateText;
	private List<Advocate> advocateListAdded;
	private ImageView editIcon, deleteIcon;
	private Button back, front;
	private List<ReminderAdvocate> lsRmdAdv;
	private List<Reminder> lsReminder;
	private List<CustomerMaster> lsCustomer;
	private List<Advocate> lsAdvocate;
	Map<String, CustomerMaster> caseToCustomer;
	Map<String, Advocate> caseToAdvocate;
	private Reminder currentReminder;
	private int position = 0;
	private List<ReminderRelation> lsrelation;
	private AsyncHttpClient client;
	private Config config;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventdetails);

		client = Config.getClient();
		config = new Config();
		clientlayout = (LinearLayout) findViewById(R.id.reminderdetailClientLayout);
		layoutAddTeamMemberText = (LinearLayout) findViewById(R.id.layouttextaddTeamMembers);
		clubLayout = (LinearLayout) findViewById(R.id.reminderclublayout);
		caselayout = (LinearLayout) findViewById(R.id.reminderlayoutcase);
		advocatelayout = (LinearLayout) findViewById(R.id.reminderlayoutadvocate);
		layoutAddedMembers = (LinearLayout) findViewById(R.id.reminderdetailsaddMemberlayout);
		layoutAddReminderAdvocate = (LinearLayout) findViewById(R.id.reminderdetailAdvocate);
		remarkLayout = (LinearLayout) findViewById(R.id.layoutEventRemark);

		createDateText = (TextView) findViewById(R.id.reminderCreateDate);
		scheduledByText = (TextView) findViewById(R.id.reminderSchedulerBy);
		eventTitleText = (TextView) findViewById(R.id.reminderDetailsTitle);
		eventDateText = (TextView) findViewById(R.id.reminderdetailDate);
		eventTimeText = (TextView) findViewById(R.id.reminderdetailTime);
		eventWorkUpdateTypeText = (TextView) findViewById(R.id.reminderdetailWorkUpdate);
		eventClientText = (TextView) findViewById(R.id.reminderdetailClient);
		eventCaseText = (TextView) findViewById(R.id.reminderdetailcase);
		eventReminderBeforeText = (TextView) findViewById(R.id.reminderdetailreminderbefore);
		eventRemarkText = (TextView) findViewById(R.id.remindedetailremark);
		eventDetailNumber = (TextView) findViewById(R.id.reminderdetailnumber);

		editIcon = (ImageView) findViewById(R.id.remindereditIcon);
		deleteIcon = (ImageView) findViewById(R.id.reminderDeleteIcon);
		front = (Button) findViewById(R.id.reminderfront);
		back = (Button) findViewById(R.id.reminderback);
		back.setOnClickListener(onClickListener);
		front.setOnClickListener(onClickListener);
		deleteIcon.setOnClickListener(onClickListener);
		editIcon.setOnClickListener(onClickListener);
		lsrelation = new ArrayList<ReminderRelation>();
		lsAdvocate = new ArrayList<Advocate>();
		lsRmdAdv = new ArrayList<ReminderAdvocate>();
		lsCustomer = new ArrayList<CustomerMaster>();
		caseToAdvocate = new HashMap<String, Advocate>();
		caseToCustomer = new HashMap<String, CustomerMaster>();
		advocateListAdded = new ArrayList<Advocate>();
		position = getIntent().getIntExtra("reminderIndex", 0);

		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();
		int day = today.monthDay;
		int month = today.month;
		int year = today.year;
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day - 1);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = cal.getTime();
		String finaldate = df.format(date);

		ReminderDao dao = new ReminderDao();
		lsReminder = new ArrayList<Reminder>();
		int userid = config.getSharedPreferences(EventListDetails.this,
				"usersettings", "userid", 0);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
		if (getIntent().getExtras().getString("KEY").compareTo("ALL") == 0) {
			List<Reminder> ls = dao
					.getTablesValues("select * from reminder where reminderid in (select reminderid from reminderRelation where userid="
							+ userid + ")");
			if (ls != null && !ls.isEmpty()) {
				for (int i = 0; i < ls.size(); i++) {
					Reminder reminder = ls.get(i);
					String reminderdate = reminder.getReminderdate();
					try {
						Date datereminder = dateFormat.parse(reminderdate);
						if (datereminder.getTime() > date.getTime()) {
							lsReminder.add(reminder);
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		} else {
			String clickeddate = getIntent().getExtras().getString("DATE");

			lsReminder = dao
					.getTablesValues("select * from reminder where reminderdate='"
							+ clickeddate
							+ "' and  reminderid in (select reminderid from reminderRelation where userid="
							+ userid + ") Order by DATETIME(reminderdate) ASC");

		}

		if (lsReminder != null && !lsReminder.isEmpty()) {
			for (int i = 0; i < lsReminder.size(); i++) {
				for (int j = i; j < lsReminder.size(); j++) {
					Reminder reminderI = lsReminder.get(i);
					Reminder reminderJ = lsReminder.get(j);
					String reminderdateI = reminderI.getReminderdate();
					String reminderdateJ = reminderJ.getReminderdate();
					try {
						Date datereminderI = dateFormat.parse(reminderdateI);
						Date datereminderJ = dateFormat.parse(reminderdateJ);
						if (datereminderI.getTime() > datereminderJ.getTime()) {
							lsReminder.set(j, reminderI);
							lsReminder.set(i, reminderJ);
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
			lsCustomer = getCustomerOfReminder();
			// lsAdvocate = getAdvocateOfReminder();
			setReminderDetails();
		} else
			Config.alertBox("Sorry, No Reminder.", this);

	}

	public void setReminderDetails() {
		if (position < lsReminder.size()) {
			int workDoneBy = Config.getSharedPreferences(EventListDetails.this,
					"usersettings", "userid", -1);

			CustomerMaster customer = new CustomerMaster();
			Advocate advocateObj = new Advocate();
			currentReminder = lsReminder.get(position);
			if (caseToCustomer.containsKey(currentReminder.getReminderID()))
				customer = caseToCustomer.get(currentReminder.getReminderID());
			if (caseToAdvocate.containsKey(currentReminder.getReminderID()))
				advocateObj = caseToAdvocate.get(currentReminder
						.getReminderID());
			eventTitleText.setText(currentReminder.getTitle());
			eventTimeText.setText(currentReminder.getTime());
			eventDateText.setText(currentReminder.getReminderdate());

			createDateText.setText(currentReminder.getAddDate());

			AdminUserDao admibD = new AdminUserDao();
			int createdBy = currentReminder.getAddBy();
			List<AdminUser> lsAdmin = new ArrayList<AdminUser>();
			lsAdmin = admibD
					.getTablesValues("select * from adminuser where userid="
							+ createdBy);
			if (lsAdmin != null && !lsAdmin.isEmpty()) {
				scheduledByText.setText(lsAdmin.get(0).getName());
			}

			UpdateTypeMasterDao updatedao = new UpdateTypeMasterDao();
			List<UpdateTypeMaster> lsupdate = new ArrayList<UpdateTypeMaster>();
			lsupdate = updatedao
					.getTablesValues("select * from updatetypemaster where updatetypeid="
							+ currentReminder.getUpdatetypeid());
			if (lsupdate != null && !lsupdate.isEmpty())
				eventWorkUpdateTypeText
						.setText(lsupdate.get(0).getUpdateType());
			else
				eventWorkUpdateTypeText.setText("");

			if (caseToCustomer.containsKey(currentReminder.getReminderID())) {
				eventClientText.setText(customer.getCustomerName());
				clientlayout.setVisibility(View.VISIBLE);
			} else {
				eventClientText.setText("");
				clientlayout.setVisibility(View.GONE);
			}

			CaseMasterDao casedao = new CaseMasterDao();
			List<CaseMaster> lscaseMaster = new ArrayList<CaseMaster>();
			lscaseMaster = casedao
					.getTablesValues("select * from casemaster where caseid="
							+ currentReminder.getCaseid());

			if (lscaseMaster != null && lscaseMaster.size() > 0) {
				caselayout.setVisibility(View.VISIBLE);
				eventCaseText.setText(lscaseMaster.get(0).getCaseTitle());
			} else {
				caselayout.setVisibility(View.GONE);
				eventCaseText.setText("");
			}

			if (currentReminder.getDetail().length() > 0) {
				eventRemarkText.setText(currentReminder.getDetail());
				remarkLayout.setVisibility(View.VISIBLE);
			} else {
				remarkLayout.setVisibility(View.GONE);
			}

			eventReminderBeforeText.setText(Integer.toString(currentReminder
					.getReminderBefore()));

			eventDetailNumber.setText(Integer.toString(position + 1));

			/** For Admin Added Members ************************************************************************************/
			ReminderRelationDao relationDao = new ReminderRelationDao();
			lsrelation.clear();
			lsrelation = relationDao
					.getTablesValues("select * from reminderRelation where reminderid='"
							+ currentReminder.getReminderID() + "';");
			if (lsrelation != null && !lsrelation.isEmpty()) {
				String id = "";
				int y = 0;
				if (workDoneBy != lsrelation.get(y).getUserId()) {
					id = Integer.toString(lsrelation.get(y).getUserId());
					y++;
				} else {
					y++;
					if (y < lsrelation.size()) {
						id = Integer.toString(lsrelation.get(y).getUserId());
						y++;
					}
				}
				for (int i = y; i < lsrelation.size(); i++) {
					if (workDoneBy != lsrelation.get(i).getUserId())
						id = id + "," + lsrelation.get(i).getUserId();
				}
				AdminUserDao adminDao = new AdminUserDao();
				List<AdminUser> lsadmin = adminDao
						.getTablesValues("select * from adminuser where userid in ("
								+ id + ") Order by name COLLATE NOCASE ASC");
				if (lsadmin != null && !lsadmin.isEmpty()) {
					layoutAddTeamMemberText.removeAllViews();
					for (int i = 0; i < lsadmin.size(); i++) {
						TextView text = new TextView(this);
						text.setText(lsadmin.get(i).getName());
						text.setTextSize(15);
						layoutAddTeamMemberText.addView(text);
					}
					layoutAddedMembers.setVisibility(View.VISIBLE);
				} else {
					layoutAddTeamMemberText.removeAllViews();
					layoutAddedMembers.setVisibility(View.GONE);
				}
			} else {
				layoutAddTeamMemberText.removeAllViews();
				layoutAddedMembers.setVisibility(View.GONE);
			}

			// if (caseToAdvocate.containsKey(currentReminder.getReminderID()))
			// {
			ReminderAdvocateDao reminderAdvocateDao = new ReminderAdvocateDao();
			lsRmdAdv.clear();
			layoutAddReminderAdvocate.removeAllViews();
			lsRmdAdv = reminderAdvocateDao
					.getTablesValues("select * from reminderAdvocate where reminderid='"
							+ currentReminder.getReminderID() + "';");
			if (lsRmdAdv != null && !lsRmdAdv.isEmpty()) {
				String id = "";
				id = Integer.toString(lsRmdAdv.get(0).getAdvocateId());
				for (int i = 1; i < lsRmdAdv.size(); i++) {
					id = id + "," + lsRmdAdv.get(i).getAdvocateId();
				}
				AdvocateDao advDao = new AdvocateDao();
				advocateListAdded = advDao
						.getTablesValues("select * from otheradvocate where advocateid in ("
								+ id
								+ ") Order by advocatename COLLATE NOCASE ASC");
				if (advocateListAdded != null && !advocateListAdded.isEmpty()) {
					layoutAddReminderAdvocate.removeAllViews();
					for (int i = 0; i < advocateListAdded.size(); i++) {
						TextView text = new TextView(this);
						text.setText(advocateListAdded.get(i).getAdvocateName());
						text.setTextSize(15);
						layoutAddReminderAdvocate.addView(text);
					}
					advocatelayout.setVisibility(View.VISIBLE);
				} else {
					advocatelayout.setVisibility(View.GONE);
					layoutAddReminderAdvocate.removeAllViews();
				}
			} else {
				advocatelayout.setVisibility(View.GONE);
				layoutAddReminderAdvocate.removeAllViews();
			}

			// Only user who has added this reminder can modify it. Others can
			// only see it who are added
			int userid = config.getSharedPreferences(EventListDetails.this,
					"usersettings", "userid", 0);
			if (userid == currentReminder.getAddBy()) {
				editIcon.setVisibility(View.VISIBLE);
				deleteIcon.setVisibility(View.VISIBLE);
			} else {
				editIcon.setVisibility(View.GONE);
				deleteIcon.setVisibility(View.GONE);
			}
		}
	}

	View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.reminderback:
				if (position > 0)
					position--;
				setReminderDetails();
				break;

			case R.id.reminderfront:
				if (position < lsReminder.size() - 1)
					position++;
				setReminderDetails();
				break;

			case R.id.reminderDeleteIcon:
				deleteReminder();
				break;

			case R.id.remindereditIcon:
				Intent intent = new Intent(EventListDetails.this,
						EditSchedulerActivity.class);
				intent.putExtra("ReminderID", currentReminder.getReminderID());
				startActivity(intent);
				break;

			default:
				break;
			}
		}
	};

	public void deleteReminder() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this)
				.setMessage("Are you sure want to delete Reminder?")
				.setPositiveButton("Yes", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						ReminderDao dao = new ReminderDao();
						dao.delete(currentReminder);
						ReminderRelationDao relationdao = new ReminderRelationDao();
						for (int i = 0; i < lsrelation.size(); i++) {
							relationdao.delete(lsrelation.get(i));
						}
						//CHANGE
						ReminderAdvocateDao reminderAdvocateDao = new ReminderAdvocateDao();
						reminderAdvocateDao.delete(currentReminder
								.getReminderID());
						//
						
						ReminderFunctinalityClass f = new ReminderFunctinalityClass();
						f.deleteReminderFromServer(
								currentReminder.getReminderID(),
								EventListDetails.this);

						ReminderFunctinalityClass.insertReminderIdToEventId(
								EventListDetails.this,
								currentReminder.getReminderID(), -1, false);

						Config.toastShow("Reminder Deleted",
								EventListDetails.this);
						Intent intent = new Intent(EventListDetails.this,
								CalendarActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}
				}).setNegativeButton("No", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				});
		alert.show();
	}

	public List<CustomerMaster> getCustomerOfReminder() {
		CustomerMasterDao dao = new CustomerMasterDao();
		List<CustomerMaster> lsCustomer = new ArrayList<CustomerMaster>();
		for (int i = 0; i < lsReminder.size(); i++) {
			Reminder reminder = lsReminder.get(i);
			List<CustomerMaster> lscust = dao
					.getTablesValues("select * from customermaster where customerid="
							+ reminder.getCustomerid());
			if (lscust != null && !lscust.isEmpty()) {
				lsCustomer.add(lscust.get(0));
				caseToCustomer.put(reminder.getReminderID(), lscust.get(0));
			}
		}
		return lsCustomer;
	}

	// public List<Advocate> getAdvocateOfReminder() {
	// AdvocateDao dao = new AdvocateDao();
	// List<Advocate> lsadvocate = new ArrayList<Advocate>();
	// for (int i = 0; i < lsReminder.size(); i++) {
	// Reminder reminder = lsReminder.get(i);
	// List<Advocate> lscust = dao
	// .getTablesValues("select * from otheradvocate where advocateid="
	// + reminder.getAdvocateid());
	// if (lscust != null && !lscust.isEmpty()) {
	// lsadvocate.add(lscust.get(0));
	// caseToAdvocate.put(reminder.getReminderID(), lscust.get(0));
	// }
	// }
	// return lsadvocate;
	// }

	private class SpinnerAdapter extends BaseAdapter {

		private List<AdminUser> lsAminUser;

		public SpinnerAdapter(List<AdminUser> lsAminUser) {
			this.lsAminUser = lsAminUser;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return lsAminUser.size();
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
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			TextView text = new TextView(EventListDetails.this);
			text.setText(lsAminUser.get(arg0).getName());
			return text;
		}

	}
}
