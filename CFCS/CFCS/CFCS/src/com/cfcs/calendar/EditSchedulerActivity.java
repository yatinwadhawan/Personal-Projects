package com.cfcs.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.cfcs.anim.ActivitySwitcher;
import com.cfcs.classes.AdminUser;
import com.cfcs.classes.Advocate;
import com.cfcs.classes.CaseMaster;
import com.cfcs.classes.CustomerMaster;
import com.cfcs.classes.Reminder;
import com.cfcs.classes.ReminderAdvocate;
import com.cfcs.classes.ReminderIDToEventID;
import com.cfcs.classes.ReminderRelation;
import com.cfcs.classes.UpdateTypeMaster;
import com.cfcs.dao.AdminUserDao;
import com.cfcs.dao.AdvocateDao;
import com.cfcs.dao.CaseMasterDao;
import com.cfcs.dao.CustomerMasterDao;
import com.cfcs.dao.ReminderAdvocateDao;
import com.cfcs.dao.ReminderDao;
import com.cfcs.dao.ReminderIdToEventIdDao;
import com.cfcs.dao.ReminderRelationDao;
import com.cfcs.dao.UpdateTypeMasterDao;
import com.cfcs.main.AddReminderAdvocateActivity;
import com.cfcs.main.AddTeamMemberActivity;
import com.cfcs.main.Config;
import com.cfcs.main.R;
import com.cfcs.main.R.id;
import com.cfcs.main.R.layout;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class EditSchedulerActivity extends Activity implements OnClickListener,
		OnItemSelectedListener {

	EditText eventdateEdit, eventTitleEdit, eventTimeEdit, eventRemarkEdit,
			remindBeforeEdit, eventClientEdit;
	private DatePicker eventdatePicker;
	private Button submitButton, addMembersButton, addEventDateButton,
			addEventTimeButton, schedulerDelete, buttonAddAdvocateList;
	private Spinner customerSpinner, caseSpinner, advocateSpinner,
			updateTypeSpinner, teamMembersSpinner;
	private List<Advocate> advocateListAdded;
	private TimePicker timePicker;
	// private List<Advocate> lsAdvocate;
	private List<CaseMaster> lsCase;
	private List<CustomerMaster> lsCustomer, totalListCustomer;
	private List<UpdateTypeMaster> lsUpdateType;
	private List<AdminUser> adminUsersAdded;
	List<ReminderAdvocate> lsRemAdv;
	private LinearLayout clubLayout;
	private String eventDate = "", eventTime = "", eventTitle = "",
			updateType = "", remark = "", remiderBefore = "";
	private int clientId = 0, caseId = 0, advocateId = 0, updateTypeId = 0;
	private boolean dateflag = false, timeflag = false;
	private AsyncHttpClient client;
	private Reminder currentReminderObject;
	private List<ReminderRelation> lsrelation;
	private ProgressBar progressBar;
	private boolean titleflag = false;
	private RadioButton radioYes, radioNo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scheduler);

		/* Remove Comment from SyncData class */

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		client = Config.getClient();
		lsRemAdv = new ArrayList<ReminderAdvocate>();
		buttonAddAdvocateList = (Button) findViewById(R.id.buttonaddAdvocate);
		advocateListAdded = new ArrayList<Advocate>();
		timePicker = (TimePicker) findViewById(R.id.timePickerevent);
		eventdatePicker = (DatePicker) findViewById(R.id.datePickerevent);
		eventClientEdit = (EditText) findViewById(R.id.editTextEventClient);
		eventdateEdit = (EditText) findViewById(R.id.editTextEventDate);
		eventRemarkEdit = (EditText) findViewById(R.id.editTextRemarkEvent);
		eventTimeEdit = (EditText) findViewById(R.id.editTextEventTime);
		eventTitleEdit = (EditText) findViewById(R.id.editTextEventTitle);
		submitButton = (Button) findViewById(R.id.buttonEventSubmit);
		addEventTimeButton = (Button) findViewById(R.id.buttonaddeventtime);
		addEventDateButton = (Button) findViewById(R.id.buttonaddeventdate);
		customerSpinner = (Spinner) findViewById(R.id.spinnerCustomerEvent);
		caseSpinner = (Spinner) findViewById(R.id.spinnerCaseEvent);
		advocateSpinner = (Spinner) findViewById(R.id.spinnerAdvocateEvent);
		updateTypeSpinner = (Spinner) findViewById(R.id.spinnerUpdateTypeEvent);
		teamMembersSpinner = (Spinner) findViewById(R.id.spinnerTeamMembersEvent);
		remindBeforeEdit = (EditText) findViewById(R.id.ediTextReminderBefore);
		addMembersButton = (Button) findViewById(R.id.buttonaddTeamMember);
		clubLayout = (LinearLayout) findViewById(R.id.schedulerclubLayout);
		radioNo = (RadioButton) findViewById(R.id.radioReminderno);
		radioYes = (RadioButton) findViewById(R.id.radioReminderyes);
		addMembersButton.setOnClickListener(this);
		schedulerDelete = (Button) findViewById(R.id.schedulerdelete);
		schedulerDelete.setVisibility(View.VISIBLE);
		addEventDateButton.setOnClickListener(this);
		addEventTimeButton.setOnClickListener(this);
		buttonAddAdvocateList.setOnClickListener(this);
		submitButton.setOnClickListener(this);
		schedulerDelete.setOnClickListener(this);
		caseSpinner.setOnItemSelectedListener(this);
		customerSpinner.setOnItemSelectedListener(this);
		advocateSpinner.setOnItemSelectedListener(this);
		updateTypeSpinner.setOnItemSelectedListener(this);
		// lsAdvocate = new ArrayList<Advocate>();
		adminUsersAdded = new ArrayList<AdminUser>();
		lsCase = new ArrayList<CaseMaster>();
		lsCustomer = new ArrayList<CustomerMaster>();
		lsUpdateType = new ArrayList<UpdateTypeMaster>();
		totalListCustomer = new ArrayList<CustomerMaster>();
		progressBar = (ProgressBar) findViewById(R.id.progressBarScheduler);
		eventClientEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if (s.toString().compareTo("") != 0) {
					lsCustomer.clear();
					lsCustomer = searchClientName(s.toString());
					if (lsCustomer != null && !lsCustomer.isEmpty()) {
						{
							customerSpinner.setAdapter(new SpinnerAdapter(
									lsCustomer));

						}
					}
				} else {
					lsCustomer.clear();
					CustomerMasterDao customerdao = new CustomerMasterDao();
					lsCustomer = customerdao
							.getTablesValues("select * from customermaster where active='true' and activestatus=1 Order by customername COLLATE NOCASE ASC;");
					customerSpinner.setAdapter(new SpinnerAdapter(lsCustomer));
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		fillData();
		fillAllData();
		eventdateEdit.setEnabled(false);
		eventTimeEdit.setEnabled(false);

		radioYes.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					remindBeforeEdit.setVisibility(View.VISIBLE);
				} else {
					remindBeforeEdit.setVisibility(View.GONE);
				}
			}
		});

	}

	public List<CustomerMaster> searchClientName(String name) {
		List<CustomerMaster> listCustomer = new ArrayList<CustomerMaster>();
		for (int i = 0; i < totalListCustomer.size(); i++) {
			CustomerMaster m = totalListCustomer.get(i);
			String regx = "(?i:.*" + name + ".*)";
			if (m.getCustomerName().matches(regx)) {
				listCustomer.add(m);
			}
		}
		return listCustomer;
	}

	public void fillAllData() {
		String remiderId = getIntent().getExtras().getString("ReminderID");
		ReminderDao dao = new ReminderDao();
		List<Reminder> ls = dao
				.getTablesValues("select * from reminder where reminderid='"
						+ remiderId + "'");
		currentReminderObject = new Reminder();
		if (ls != null && !ls.isEmpty()) {
			currentReminderObject = ls.get(0);
			eventDate = currentReminderObject.getReminderdate();
			eventdateEdit.setText(currentReminderObject.getReminderdate());
			eventTime = currentReminderObject.getTime();
			eventTimeEdit.setText(currentReminderObject.getTime());
			eventTitle = currentReminderObject.getTitle();
			eventTitleEdit.setText(currentReminderObject.getTitle());
			remark = currentReminderObject.getDetail();
			eventRemarkEdit.setText(currentReminderObject.getDetail());

			if (currentReminderObject.getReminderAlarm().compareTo("true") == 0) {
				remiderBefore = Integer.toString(currentReminderObject
						.getReminderBefore());
				remindBeforeEdit.setText(remiderBefore);
				remindBeforeEdit.setVisibility(View.VISIBLE);
			} else {
				remindBeforeEdit.setVisibility(View.GONE);
			}

			for (int i = 0; i < lsUpdateType.size(); i++) {
				if (currentReminderObject.getUpdatetypeid() == lsUpdateType
						.get(i).getUpdateTypeID()) {
					updateTypeId = currentReminderObject.getUpdatetypeid();
					updateTypeSpinner.setSelection(i);
					break;
				}
			}

			for (int i = 0; i < lsCustomer.size(); i++) {
				if (currentReminderObject.getCustomerid() == lsCustomer.get(i)
						.getCustomerID()) {
					clientId = currentReminderObject.getCustomerid();
					customerSpinner.setSelection(i);
					break;
				}
			}

			for (int i = 0; i < lsCase.size(); i++) {
				if (currentReminderObject.getCaseid() == lsCase.get(i)
						.getCaseID()) {
					caseId = currentReminderObject.getCaseid();
					caseSpinner.setSelection(i);
					break;
				}
			}

			// for (int i = 0; i < lsAdvocate.size(); i++) {
			// if (currentReminderObject.getAdvocateid() == lsAdvocate.get(i)
			// .getAdvocateID()) {
			// advocateId = currentReminderObject.getAdvocateid();
			// advocateSpinner.setSelection(i);
			// break;
			// }
			// }

			/** For Admin Added Members ************************************************************************************/
			ReminderRelationDao relationDao = new ReminderRelationDao();
			lsrelation = new ArrayList<ReminderRelation>();
			lsrelation = relationDao
					.getTablesValues("select * from reminderRelation where reminderid='"
							+ currentReminderObject.getReminderID() + "';");
			int workDoneBy = Config.getSharedPreferences(
					EditSchedulerActivity.this, "usersettings", "userid", -1);
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
				for (int i = 1; i < lsrelation.size(); i++) {
					if (workDoneBy != lsrelation.get(i).getUserId())
						id = id + "," + lsrelation.get(i).getUserId();
				}
				AdminUserDao adminDao = new AdminUserDao();
				adminUsersAdded = adminDao
						.getTablesValues("select * from adminuser where userid in ("
								+ id + ") Order by name COLLATE NOCASE ASC;");
				if (adminUsersAdded != null && !adminUsersAdded.isEmpty()) {
					teamMembersSpinner.setAdapter(new SpinnerAdapter(
							adminUsersAdded));
				}
			}

			// For Adding Advocate List
			ReminderAdvocateDao remAdvocatedao = new ReminderAdvocateDao();
			lsRemAdv = new ArrayList<ReminderAdvocate>();
			lsRemAdv = remAdvocatedao
					.getTablesValues("select * from reminderAdvocate where reminderid='"
							+ currentReminderObject.getReminderID() + "';");
			String id = "";
			id = Integer.toString(lsRemAdv.get(0).getAdvocateId());
			for (int i = 1; i < lsRemAdv.size(); i++) {
				id = id + "," + lsRemAdv.get(i).getAdvocateId();
			}
			AdvocateDao advDao = new AdvocateDao();
			advocateListAdded = advDao
					.getTablesValues("select * from otheradvocate where advocateid in ("
							+ id + ") Order by advocatename COLLATE NOCASE ASC");
			if (advocateListAdded != null && !advocateListAdded.isEmpty()) {
				advocateSpinner
						.setAdapter(new SpinnerAdapter(advocateListAdded));
			}
		}
	}

	public void fillData() {

		/*
		 * Advocate**************************************************************
		 * **
		 */
		// AdvocateDao advocatedao = new AdvocateDao();
		// List<Advocate> listAdvocate = advocatedao
		// .getTablesValues("select * from otheradvocate where type=1 and active='true' Order by advocatename COLLATE NOCASE ASC");
		// Advocate advocae = new Advocate();
		// if (listAdvocate != null && listAdvocate.size() > 0) {
		// advocae.setAdvocateName("Select");
		// lsAdvocate.add(advocae);
		// for (int i = 0; i < listAdvocate.size(); i++) {
		// lsAdvocate.add(listAdvocate.get(i));
		// }
		// advocateSpinner.setAdapter(new SpinnerAdapter(lsAdvocate));
		// } else {
		// advocateSpinner.setAdapter(new SpinnerAdapter(lsAdvocate));
		// }

		/*
		 * CustomerTypeMaster****************************************************
		 * *** *********
		 */
		CustomerMasterDao customerdao = new CustomerMasterDao();
		List<CustomerMaster> listCustomer = customerdao
				.getTablesValues("select * from customermaster where active='true' and activestatus=1 Order by customername COLLATE NOCASE ASC;");
		List<CustomerMaster> listCoustomerWithCases = new ArrayList<CustomerMaster>();
		CaseMasterDao casedao = new CaseMasterDao();
		if (listCustomer != null && !listCustomer.isEmpty()) {
			for (int i = 0; i < listCustomer.size(); i++) {
				List<CaseMaster> listCase = casedao
						.getTablesValues("select * from casemaster where customerid="
								+ listCustomer.get(i).getCustomerID()
								+ " and active='true' and casestatus='true' Order by casetitle COLLATE NOCASE ASC");
				if (listCase != null && !listCase.isEmpty()) {
					listCoustomerWithCases.add(listCustomer.get(i));
				}
			}
			CustomerMaster customerMaster = new CustomerMaster();
			customerMaster.setCustomerName("Select");
			lsCustomer.add(customerMaster);
			totalListCustomer.add(customerMaster);
			for (int i = 0; i < listCoustomerWithCases.size(); i++) {
				lsCustomer.add(listCoustomerWithCases.get(i));
				totalListCustomer.add(listCoustomerWithCases.get(i));
			}
			customerSpinner.setAdapter(new SpinnerAdapter(lsCustomer));
		} else {
			customerSpinner.setAdapter(new SpinnerAdapter(lsCustomer));
		}

		/*
		 * UpdateTypeMaster******************************************************
		 * * *********
		 */
		UpdateTypeMasterDao updatedao = new UpdateTypeMasterDao();
		List<UpdateTypeMaster> listupdatetype = updatedao
				.getTablesValues("select * from updatetypemaster where active='true';");
		if (listupdatetype != null && !listupdatetype.isEmpty()) {
			UpdateTypeMaster updatemaster = new UpdateTypeMaster();
			updatemaster.setUpdateType("Select");
			lsUpdateType.add(updatemaster);
			for (int i = 0; i < listupdatetype.size(); i++) {
				lsUpdateType.add(listupdatetype.get(i));

			}
			updateTypeSpinner.setAdapter(new SpinnerAdapter(lsUpdateType));
		} else {
			updateTypeSpinner.setAdapter(new SpinnerAdapter(lsUpdateType));
		}
	}

	private class SpinnerAdapter extends BaseAdapter {

		private List<?> object;

		public SpinnerAdapter(List<?> ls) {
			this.object = ls;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return object.size();
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
		public View getView(int postion, View arg1, ViewGroup arg2) {

			String name = "";
			if (postion < object.size()) {
				if (object.get(postion) instanceof Advocate) {
					Advocate advocate = (Advocate) object.get(postion);
					name = advocate.getAdvocateName();
				} else if (object.get(0) instanceof CaseMaster) {
					CaseMaster casemaster = (CaseMaster) object.get(postion);
					name = casemaster.getCaseTitle();
				} else if (object.get(0) instanceof CustomerMaster) {
					CustomerMaster customer = (CustomerMaster) object
							.get(postion);
					name = customer.getCustomerName();
				} else if (object.get(0) instanceof UpdateTypeMaster) {
					UpdateTypeMaster update = (UpdateTypeMaster) object
							.get(postion);
					name = update.getUpdateType();
				} else if (object.get(0) instanceof AdminUser) {
					AdminUser update = (AdminUser) object.get(postion);
					name = update.getName();
				}
			}

			TextView t = new TextView(EditSchedulerActivity.this);
			t.setText(name);
			t.setTextSize(15);

			return t;

		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {

		switch (arg0.getId()) {
		case R.id.spinnerCustomerEvent: {
			CustomerMaster customerMaster = lsCustomer.get(position);
			if (customerMaster.getCustomerName().compareTo("Select") != 0
					&& customerMaster.getCustomerName().compareTo("None") != 0) {
				lsCase.clear();
				clientId = lsCustomer.get(position).getCustomerID();
				CaseMasterDao casemasterdao = new CaseMasterDao();
				lsCase = casemasterdao
						.getTablesValues("select * from casemaster where customerid="
								+ clientId
								+ " and active='true' and casestatus='true' Order by casetitle COLLATE NOCASE ASC");
				if (lsCase != null && !lsCase.isEmpty())
					caseId = lsCase.get(0).getCaseID();
				else
					caseId = 0;

				caseSpinner.setAdapter(new SpinnerAdapter(lsCase));
			} else {
				clientId = 0;
				caseId = 0;
				lsCase.clear();
				caseSpinner.setAdapter(new SpinnerAdapter(lsCase));
			}
			break;
		}

		case R.id.spinnerCaseEvent: {
			if (lsCase != null && !lsCase.isEmpty())
				caseId = lsCase.get(position).getCaseID();
			break;
		}

		// case R.id.spinnerAdvocateEvent: {
		// if (position > 0 && lsAdvocate != null && !lsAdvocate.isEmpty()) {
		// advocateId = lsAdvocate.get(position).getAdvocateID();
		// } else {
		// advocateId = 0;
		// }
		// break;
		// }

		case R.id.spinnerUpdateTypeEvent: {
			if (position > 0) {
				updateType = lsUpdateType.get(position).getUpdateType();
				updateTypeId = lsUpdateType.get(position).getUpdateTypeID();
				if (lsUpdateType.get(position).getUpdateType()
						.compareTo("Miscellaneous") == 0) {
					clubLayout.setVisibility(View.GONE);
				} else {
					clubLayout.setVisibility(View.VISIBLE);
				}
				if (titleflag) {
					eventTitle = updateType;
					eventTitleEdit.setText(updateType);
				}
				titleflag = true;
			} else {
				updateTypeId = 0;
				updateType = "0";
				if (lsUpdateType.get(position).getUpdateType()
						.compareTo("Miscellaneous") == 0) {
					clubLayout.setVisibility(View.GONE);
				} else {
				}
			}
			break;
		}

		default:
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.buttonaddTeamMember: {
			Intent myIntent = new Intent(EditSchedulerActivity.this,
					AddTeamMemberActivity.class);
			startActivityForResult(myIntent, 1);
			break;
		}
		case R.id.buttonEventSubmit: {
			eventDate = eventdateEdit.getText().toString();
			eventTitle = eventTitleEdit.getText().toString();
			remark = eventRemarkEdit.getText().toString();
			remiderBefore = remindBeforeEdit.getText().toString();
			eventTime = eventTimeEdit.getText().toString();

			SimpleDateFormat form = new SimpleDateFormat("dd MMM yyyy");
			Date date = new Date();
			boolean flag = false;
			try {
				date = form.parse(eventDate);
				flag = true;
			} catch (Exception e) {
				flag = false;
				e.printStackTrace();
			}

			SimpleDateFormat df = new SimpleDateFormat("HH:mm");
			Date dTime = new Date();
			boolean flagTime = false;
			try {
				dTime = df.parse(eventTime);
				flagTime = true;
			} catch (Exception e) {
				flagTime = false;
				e.printStackTrace();
			}

			if (eventDate.compareTo("") != 0) {
				if (flag) {
					if (eventTime.compareTo("") != 0) {
						if (flagTime) {
							if (eventTitle.compareTo("") != 0) {
								if (updateTypeId != 0) {
									if (updateType.compareTo("Miscellaneous") == 0) {
										if (remiderBefore.compareTo("") != 0) {
											submitButton.setEnabled(false);
											progressBar
													.setVisibility(View.VISIBLE);
											makeJsonAndSend();
										} else {
											alert("Please fill Event Remider Before.");
										}
									} else {
										if (clientId != 0) {
											if (caseId != 0) {
												if (remiderBefore.compareTo("") != 0) {
													submitButton
															.setEnabled(false);
													progressBar
															.setVisibility(View.VISIBLE);
													makeJsonAndSend();
												} else {
													alert("Please fill Event Remider Before.");
												}
											} else {
												alert("Please fill the Case Details.");
											}
										} else {
											alert("Please fill the Client Details.");
										}
									}
								} else {
									alert("Please fill Event WorkUpdateType.");
								}
							} else {
								alert("Please fill Event Title.");
							}
						} else {
							alert("Please fill Correct Event Time.");
						}
					} else {
						alert("Please fill Event Time.");
					}
				} else {
					alert("Please fill Correct Event Date.");
				}
			} else {
				alert("Please fill Event Date.");
			}
			break;
		}
		case R.id.buttonaddeventdate: {
			if (!dateflag) {
				eventdatePicker.setVisibility(View.VISIBLE);
				dateflag = true;
				eventdateEdit.setText("");
			} else {
				dateflag = false;
				eventdatePicker.setVisibility(View.GONE);
				int day = eventdatePicker.getDayOfMonth();
				int month = eventdatePicker.getMonth();
				int year = eventdatePicker.getYear();
				Calendar calendar = Calendar.getInstance();
				calendar.set(year, month, day);
				Date date = calendar.getTime();
				SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
				String finaldate = df.format(date);
				eventdateEdit.setText(finaldate);
			}
			break;
		}

		case R.id.buttonaddeventtime: {
			if (!timeflag) {
				eventTime = "";
				eventTimeEdit.setText("");
				timePicker.setVisibility(View.VISIBLE);
				timeflag = true;
			} else {
				timeflag = false;
				timePicker.setVisibility(View.GONE);
				int hour = timePicker.getCurrentHour();
				int min = timePicker.getCurrentMinute();
				SimpleDateFormat df = new SimpleDateFormat("HH:mm");
				Date date = new Date();
				date.setHours(hour);
				date.setMinutes(min);
				eventTime = df.format(date);

				SimpleDateFormat datef = new SimpleDateFormat("hh:mm");
				String finaltime = datef.format(date) + " ";
				eventTimeEdit.setText(finaltime);
				View amPmView = ((ViewGroup) timePicker.getChildAt(0))
						.getChildAt(2);
				if (amPmView instanceof Button) {
					String val = ((Button) amPmView).getText().toString();
					finaltime = finaltime + val;
				}
				eventTimeEdit.setText(finaltime);
			}
			break;
		}

		case R.id.buttonaddAdvocate: {
			Intent myIntent = new Intent(EditSchedulerActivity.this,
					AddReminderAdvocateActivity.class);
			startActivityForResult(myIntent, 2);
			break;
		}

		case R.id.schedulerdelete:
			deleteReminder();
			break;
		default:
			break;
		}
	}

	public void deleteReminder() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this)
				.setMessage("Are you sure want to delete Reminder?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								ReminderDao dao = new ReminderDao();
								dao.delete(currentReminderObject);
								ReminderRelationDao relationdao = new ReminderRelationDao();
								for (int i = 0; i < lsrelation.size(); i++) {
									relationdao.delete(lsrelation.get(i));
								}

								ReminderFunctinalityClass f = new ReminderFunctinalityClass();
								f.deleteReminderFromServer(
										currentReminderObject.getReminderID(),
										EditSchedulerActivity.this);

								ReminderFunctinalityClass
										.insertReminderIdToEventId(
												EditSchedulerActivity.this,
												currentReminderObject
														.getReminderID(), -1,
												false);

								Config.toastShow("Reminder Deleted",
										EditSchedulerActivity.this);
								Intent intent = new Intent(
										EditSchedulerActivity.this,
										CalendarActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		alert.show();
	}

	public void makeJsonAndSend() {
		try {
			Gson gson = new Gson();
			final Reminder reminder = getReminderObjectFilled();
			JSONObject addBothObj = new JSONObject();
			JSONObject finalJson = new JSONObject();
			String reminderJson = gson.toJson(reminder);
			JSONObject obj1 = new JSONObject(reminderJson);
			JSONArray array = new JSONArray();
			array.put(obj1);
			addBothObj.put("ReminderMaster", array);

			JSONArray reminderrelationArray = new JSONArray();
			if (adminUsersAdded != null && !adminUsersAdded.isEmpty()) {
				for (int i = 0; i < adminUsersAdded.size(); i++) {
					JSONObject obj2 = new JSONObject();
					obj2.put("ReminderID", reminder.getReminderID());
					obj2.put("UserID", adminUsersAdded.get(i).getUserid());
					reminderrelationArray.put(obj2);
				}
			}

			int workDoneBy = Config.getSharedPreferences(
					EditSchedulerActivity.this, "usersettings", "userid", -1);
			JSONObject obj2 = new JSONObject();
			obj2.put("ReminderID", reminder.getReminderID());
			obj2.put("UserID", workDoneBy);
			reminderrelationArray.put(obj2);
			addBothObj.put("ReminderRelation", reminderrelationArray);

			JSONArray reminderAdvocateArray = new JSONArray();
			if (advocateListAdded != null && !advocateListAdded.isEmpty()) {
				for (int i = 0; i < advocateListAdded.size(); i++) {
					JSONObject obj3 = new JSONObject();
					obj3.put("ReminderID", reminder.getReminderID());
					obj3.put("AdvocateID", advocateListAdded.get(i)
							.getAdvocateID());
					reminderAdvocateArray.put(obj3);
				}
			}
			addBothObj.put("ReminderAdvocate", reminderAdvocateArray);
			finalJson.put("SyncData", addBothObj);

			String url = Config.BASE_URL + "ResponseReminderUpdate";
			RequestParams params = new RequestParams();
			params.put("DataJson", finalJson.toString());
			params.put("UserName", Config.username);
			params.put("Password", Config.password);
			client.post(url, params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String response) {
					Config.toastShow("Reminder Stored and Sent to Server.",
							EditSchedulerActivity.this);
					saveRemiderIntoDatabase(reminder, 1);
					submitButton.setEnabled(true);
					progressBar.setVisibility(View.GONE);
				}

				@Override
				public void onFailure(Throwable error, String content) {
					Config.toastShow("Not sent. Saved to Database.",
							EditSchedulerActivity.this);
					saveRemiderIntoDatabase(reminder, 0);
					submitButton.setEnabled(true);
					progressBar.setVisibility(View.GONE);
				}
			});

			Intent intent = new Intent(EditSchedulerActivity.this,
					CalendarActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Reminder getReminderObjectFilled() {
		int workDoneBy = Config.getSharedPreferences(
				EditSchedulerActivity.this, "usersettings", "userid", -1);
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dateTimef = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = df.format(cal.getTime());
		String dateTime = dateTimef.format(cal.getTime());

		Reminder reminder = new Reminder();
		reminder.setUpdatedate(dateTime);
		reminder.setAddDate(currentReminderObject.getAddDate());
		reminder.setReminderID(currentReminderObject.getReminderID());
		reminder.setReminderdate(eventDate);
		reminder.setTime(eventTime);
		reminder.setTitle(eventTitle);
		reminder.setAddBy(workDoneBy);
		reminder.setActive("true");
		reminder.setDetail(remark);
		reminder.setStatus(1);
		reminder.setAdvocateid(0);
		if (radioYes.isChecked()) {
			reminder.setReminderAlarm("true");
		} else {
			reminder.setReminderAlarm("false");
		}

		if (remiderBefore.length() > 0 && radioYes.isChecked())
			reminder.setReminderBefore(Integer.parseInt(remiderBefore));
		else
			reminder.setReminderBefore(Integer.parseInt("0"));
		reminder.setUpdatetypeid(updateTypeId);
		if (updateType.compareTo("Miscellaneous") != 0) {
			reminder.setCustomerid(clientId);
			reminder.setCaseid(caseId);
		} else {
			reminder.setCustomerid(0);
			reminder.setCaseid(0);
		}
		return reminder;
	}

	public void saveRemiderIntoDatabase(Reminder reminder, int updateStamp) {
		ReminderDao dao = new ReminderDao();
		reminder.setUpdateStamp(updateStamp);
		dao.update(reminder);

		if (radioYes.isChecked())
			addEventToAndroidCalendar(reminder);

		ReminderRelationDao relationdao = new ReminderRelationDao();
		for (int i = 0; i < lsrelation.size(); i++) {
			relationdao.delete(lsrelation.get(i));
		}

		// All Added partners
		for (int i = 0; i < adminUsersAdded.size(); i++) {
			AdminUser user = adminUsersAdded.get(i);
			ReminderRelation relation = new ReminderRelation();
			relation.setReminderId(reminder.getReminderID());
			relation.setUserId(user.getUserid());
			if (relationdao.status_database(relation)) {

			} else
				relationdao.insert(relation);
		}

		ReminderAdvocateDao reminderAdvocateDao = new ReminderAdvocateDao();
		for (int i = 0; i < lsRemAdv.size(); i++) {
			reminderAdvocateDao.delete(lsRemAdv.get(i));
		}

		for (int i = 0; i < advocateListAdded.size(); i++) {
			Advocate adv = advocateListAdded.get(i);
			ReminderAdvocate remadv = new ReminderAdvocate();
			remadv.setReminderId(reminder.getReminderID());
			remadv.setAdvocateId(adv.getAdvocateID());
			if (reminderAdvocateDao.status_database(remadv)) {

			} else
				reminderAdvocateDao.insert(remadv);
		}

		// Insert the user who has created this reminder
		int workDoneBy = Config.getSharedPreferences(
				EditSchedulerActivity.this, "usersettings", "userid", -1);
		ReminderRelation relation = new ReminderRelation();
		relation.setReminderId(reminder.getReminderID());
		relation.setUserId(workDoneBy);
		if (relationdao.status_database(relation)) {

		} else
			relationdao.insert(relation);

		caseSpinner.setAdapter(new SpinnerAdapter(lsCase));
		// advocateSpinner.setAdapter(new SpinnerAdapter(lsAdvocate));
		updateTypeSpinner.setAdapter(new SpinnerAdapter(lsUpdateType));
		customerSpinner.setAdapter(new SpinnerAdapter(lsCustomer));
		eventClientEdit.setText("");
		eventRemarkEdit.setText("");
		eventdateEdit.setText("");
		eventTitleEdit.setText("");
		eventTimeEdit.setText("");
		remindBeforeEdit.setText("60");
		eventDate = "";
		eventTitle = "";
		remark = "";
		remiderBefore = "";
		clientId = 0;
		advocateId = 0;
		caseId = 0;
		updateType = "";
		updateTypeId = 0;
		adminUsersAdded.clear();
		advocateListAdded.clear();
		teamMembersSpinner.setAdapter(new SpinnerAdapter(adminUsersAdded));
		caseSpinner.setVisibility(View.VISIBLE);
		advocateSpinner.setVisibility(View.VISIBLE);
		customerSpinner.setVisibility(View.VISIBLE);

	}

	public void addEventToAndroidCalendar(Reminder reminder) {

		String date = reminder.getReminderdate() + " " + reminder.getTime();
		SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy hh:mm aa");

		try {
			int before = reminder.getReminderBefore();
			Date dateObj = df.parse(date);
			long id = ReminderFunctinalityClass.pushAppointmentsToCalender(
					EditSchedulerActivity.this, reminder.getTitle(),
					reminder.getDetail(), "", 1, dateObj.getTime(), before,
					true, false);

			ReminderFunctinalityClass.insertReminderIdToEventId(
					EditSchedulerActivity.this, reminder.getReminderID(), id,
					true);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && data != null) {
			adminUsersAdded.clear();
			AdminUserDao dao = new AdminUserDao();
			ArrayList<Integer> lsID = new ArrayList<Integer>();
			lsID = data.getExtras().getIntegerArrayList("SELECTED");
			String id = "";
			if (lsID.size() > 0)
				id = Integer.toString(lsID.get(0));
			for (int i = 1; i < lsID.size(); i++) {
				id = id + "," + Integer.toString(lsID.get(i));
			}
			adminUsersAdded = dao
					.getTablesValues("select * from adminuser where userid in ("
							+ id + ") Order by name COLLATE NOCASE ASC");
			if (adminUsersAdded != null && !adminUsersAdded.isEmpty())
				teamMembersSpinner.setAdapter(new SpinnerAdapter(
						adminUsersAdded));
		} else if (resultCode == 2 && data != null) {
			advocateListAdded.clear();
			AdvocateDao dao = new AdvocateDao();
			ArrayList<Integer> lsID = new ArrayList<Integer>();
			lsID = data.getExtras().getIntegerArrayList("SELECTED");
			String id = "";
			if (lsID.size() > 0)
				id = Integer.toString(lsID.get(0));
			for (int i = 1; i < lsID.size(); i++) {
				id = id + "," + Integer.toString(lsID.get(i));
			}
			advocateListAdded = dao
					.getTablesValues("select * from otheradvocate where advocateid in ("
							+ id + ") Order by advocatename COLLATE NOCASE ASC");
			if (advocateListAdded != null && !advocateListAdded.isEmpty()) {
				advocateSpinner
						.setAdapter(new SpinnerAdapter(advocateListAdded));
			}
		}
	}

	@Override
	protected void onResume() {
		// animateIn this activity
		ActivitySwitcher.animationIn(findViewById(R.id.schedulercontainer),
				getWindowManager());
		super.onResume();
	}

	private void animatedStartActivity(Class<?> cls) {
		final Intent intent = new Intent(EditSchedulerActivity.this, cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		ActivitySwitcher.animationOut(findViewById(R.id.schedulercontainer),
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
		ActivitySwitcher.animationOut(findViewById(R.id.schedulercontainer),
				getWindowManager(),
				new ActivitySwitcher.AnimationFinishedListener() {
					@Override
					public void onAnimationFinished() {
						EditSchedulerActivity.super.finish();
						// disable default animation
						overridePendingTransition(0, 0);
					}
				});
	}

	public void alert(String str) {
		Config.alertBox(str, EditSchedulerActivity.this);
	}

}
