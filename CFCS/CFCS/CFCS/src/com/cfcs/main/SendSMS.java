package com.cfcs.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cfcs.anim.ActivitySwitcher;
import com.cfcs.classes.AdminUser;
import com.cfcs.classes.GroupMaster;
import com.cfcs.classes.GroupRelation;
import com.cfcs.dao.AdminUserDao;
import com.cfcs.dao.Database;
import com.cfcs.dao.GroupMasterDao;
import com.cfcs.dao.GroupRelationDao;
import com.cfcs.main.R.id;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

public class SendSMS extends Activity implements OnItemSelectedListener {

	List<String> types, lsgrouptype, groupmobileNumbers, allNumbers;
	List<AdminUser> checkedAdmin;
	String individualNumber = "";
	EditText messages, selectedEmployees;
	LinearLayout allLayout, groupLayout, individualLayout, selectedLayout,
			selectedcheckBoxLayout;
	Spinner messageTypesSpinner, groupTypeSpinner, individualSpinner;
	Button send, selectedButton;
	Map<Integer, String> adminIdToMobile;
	Map<String, List<String>> groupNameToMobile;
	Map<String, List<AdminUser>> groupNameToAdminUser;
	int count;
	private Database database;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendsms);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		database = Database.instance();
		types = new ArrayList<String>();
		types.add("Group");
		types.add("All");
		types.add("Individual");
		types.add("Selected");
		checkedAdmin = new ArrayList<AdminUser>();
		adminIdToMobile = new HashMap<Integer, String>();
		progressBar = (ProgressBar) findViewById(R.id.progressBarSMS);
		selectedcheckBoxLayout = (LinearLayout) findViewById(R.id.selectedCheckBoxLayout);
		selectedButton = (Button) findViewById(R.id.selectedadd);
		groupNameToAdminUser = new HashMap<String, List<AdminUser>>();
		groupNameToMobile = new HashMap<String, List<String>>();
		allNumbers = new ArrayList<String>();
		groupmobileNumbers = new ArrayList<String>();
		messages = (EditText) findViewById(R.id.meesage);
		selectedEmployees = (EditText) findViewById(R.id.selectedemployees);
		send = (Button) findViewById(R.id.send);
		groupLayout = (LinearLayout) findViewById(R.id.groupLayout);
		allLayout = (LinearLayout) findViewById(R.id.allLayout);
		individualLayout = (LinearLayout) findViewById(R.id.individualLayout);
		individualSpinner = (Spinner) findViewById(R.id.spinnerIndividual);
		selectedLayout = (LinearLayout) findViewById(R.id.selectedLayout);
		groupTypeSpinner = (Spinner) findViewById(R.id.spinnergroupTypes);
		messageTypesSpinner = (Spinner) findViewById(R.id.spinnerMessageTypes);
		filldata();
		messageTypesSpinner.setOnItemSelectedListener(this);
		groupTypeSpinner.setOnItemSelectedListener(this);
		selectedButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectedcheckBoxLayout.removeAllViews();
				selectedButton.setVisibility(View.GONE);
				selectedcheckBoxLayout.setVisibility(View.VISIBLE);
				allLayout.setVisibility(View.GONE);
				selectedEmployees.setVisibility(View.GONE);
				selectedEmployees.setText("");
				checkedAdmin.clear();
				selectedcheckBoxLayout.addView(getcheckBoxLayout());
			}
		});

		send.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				String message = messages.getText().toString();
				if (message.length() > 0) {
					switch (count) {
					case 1: {// group
						if (!groupmobileNumbers.isEmpty()) {
							progressBar.setVisibility(View.VISIBLE);
							for (int i = 0; i < groupmobileNumbers.size(); i++) {
								boolean flag = sendSMS(
										groupmobileNumbers.get(i), message);
								if (!flag)
									break;
							}
							progressBar.setVisibility(View.GONE);
						}
						break;
					}
					case 2:// individual
						if (individualNumber.compareTo("") != 0) {
							progressBar.setVisibility(View.VISIBLE);
							sendSMS(individualNumber, message);
							progressBar.setVisibility(View.GONE);
						}
						break;

					case 3:// All
						if (!allNumbers.isEmpty()) {
							for (int i = 0; i < allNumbers.size(); i++) {
								progressBar.setVisibility(View.VISIBLE);
								sendSMS(allNumbers.get(i), message);
								progressBar.setVisibility(View.GONE);
							}
						}
						break;
					case 4:
						if (!checkedAdmin.isEmpty()) {
							progressBar.setVisibility(View.VISIBLE);
							for (int i = 0; i < checkedAdmin.size(); i++) {
								String number = adminIdToMobile
										.get(checkedAdmin.get(i).getUserid());
								sendSMS(number, message);
							}
							checkedAdmin.clear();
							progressBar.setVisibility(View.GONE);
						}
						break;

					default:
						break;
					}
				} else {
					Config.alertBox("Please enter some message.", SendSMS.this);
				}
			}
		});
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {

		switch (arg0.getId()) {
		case R.id.spinnerMessageTypes: {

			messages.setText("");
			switch (position) {
			case 0: {// Group
				groupLayout.setVisibility(View.VISIBLE);
				individualLayout.setVisibility(View.GONE);
				selectedLayout.setVisibility(View.GONE);
				allLayout.setVisibility(View.VISIBLE);
				count = 1;
				break;
			}
			case 1: {// All
				groupLayout.setVisibility(View.GONE);
				individualLayout.setVisibility(View.GONE);
				selectedLayout.setVisibility(View.GONE);
				allLayout.setVisibility(View.VISIBLE);
				count = 3;
				break;
			}
			case 2: {// Individual
				individualLayout.setVisibility(View.VISIBLE);
				groupLayout.setVisibility(View.GONE);
				selectedLayout.setVisibility(View.GONE);
				allLayout.setVisibility(View.VISIBLE);
				count = 2;
				break;
			}
			case 3: {// Selected
				checkedAdmin.clear();
				if (selectedcheckBoxLayout.getChildCount() > 0)
					selectedcheckBoxLayout.removeAllViews();
				selectedLayout.setVisibility(View.VISIBLE);
				groupLayout.setVisibility(View.GONE);
				individualLayout.setVisibility(View.GONE);
				allLayout.setVisibility(View.GONE);
				selectedcheckBoxLayout.setVisibility(View.VISIBLE);
				selectedcheckBoxLayout.addView(getcheckBoxLayout());
				selectedButton.setVisibility(View.GONE);
				count = 4;
				break;
			}
			default:
				break;
			}
			break;
		}
		case R.id.spinnergroupTypes: {
			groupmobileNumbers = groupNameToMobile.get(lsgrouptype
					.get(position));
			break;
		}

		case R.id.spinnerIndividual: {
			individualNumber = allNumbers.get(position);
			count = 2;
			break;
		}
		default:
			count = -1;
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	public LinearLayout getcheckBoxLayout() {
		LinearLayout addLayout = new LinearLayout(this);
		addLayout.setOrientation(LinearLayout.VERTICAL);
		for (int i = 0; i < lsgrouptype.size(); i++) {
			LinearLayout maincheckLayout = new LinearLayout(this);
			maincheckLayout.setOrientation(LinearLayout.VERTICAL);
			final LinearLayout checkLayout = new LinearLayout(this);
			checkLayout.setOrientation(LinearLayout.VERTICAL);
			CheckBox main = new CheckBox(this);
			main.setText(lsgrouptype.get(i));
			checkLayout.setVisibility(View.GONE);
			main.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (isChecked) {
						checkLayout.setVisibility(View.VISIBLE);
					} else {
						checkLayout.setVisibility(View.GONE);
					}
				}
			});
			List<AdminUser> lsName = groupNameToAdminUser.get(lsgrouptype
					.get(i));
			if (!lsName.isEmpty())
				for (int j = 0; j < lsName.size(); j++) {
					CheckBox check = new CheckBox(this);
					check.setText(lsName.get(j).getName());
					// + " - "+ // adminNameToMobile.get(lsName.get(j)));
					check.setTag(lsName.get(j));
					checkLayout.addView(check);
					LinearLayout.LayoutParams progressparams = new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					progressparams.setMargins(30, 5, 0, 10);
					checkLayout.setLayoutParams(progressparams);
					check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if (isChecked) {
								checkedAdmin.add((AdminUser) buttonView
										.getTag());
							} else {
								checkedAdmin.remove((AdminUser) buttonView
										.getTag());
							}
						}
					});
				}
			else
				main.setVisibility(View.GONE);
			maincheckLayout.addView(main);
			maincheckLayout.addView(checkLayout);
			addLayout.addView(maincheckLayout);
		}

		Button done = new Button(this);
		done.setText("Submit");
		done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String s = "";
				for (int i = 0; i < checkedAdmin.size(); i++) {
					s = s + "; " + checkedAdmin.get(i).getName();
				}
				selectedcheckBoxLayout.removeAllViews();
				selectedcheckBoxLayout.setVisibility(View.GONE);
				selectedLayout.setVisibility(View.VISIBLE);
				selectedButton.setVisibility(View.VISIBLE);
				selectedEmployees.setText(s);
				selectedEmployees.setVisibility(View.VISIBLE);
				allLayout.setVisibility(View.VISIBLE);
			}
		});

		addLayout.addView(done);
		return addLayout;

	}

	public void filldata() {
		GroupMasterDao courtDao = new GroupMasterDao();
		List<GroupMaster> lsgroup = courtDao.index();
		// Name of Groups
		lsgrouptype = new ArrayList<String>();
		for (int i = 0; i < lsgroup.size(); i++) {
			AdminUserDao dao = new AdminUserDao();
			lsgrouptype.add(lsgroup.get(i).getGroupName());
			List<String> mobileNo = new ArrayList<String>();
			List<AdminUser> listAdminUserNames = new ArrayList<AdminUser>();
			List<AdminUser> adminUser = new ArrayList<AdminUser>();
			GroupRelationDao d = new GroupRelationDao();
			List<GroupRelation> lsr = d.index();
			listAdminUserNames = dao
					.getTablesValues("select * from adminuser where active='true' and userid in"
							+ "(select memberid from grouprelation where groupid='"
							+ lsgroup.get(i).getGroupID()
							+ "') Order by name COLLATE NOCASE ASC;");
			groupNameToMobile.put(lsgroup.get(i).getGroupName(), mobileNo);
			groupNameToAdminUser.put(lsgroup.get(i).getGroupName(),
					listAdminUserNames);
		}
		if (lsgrouptype.size() > 0)
			groupmobileNumbers = groupNameToMobile.get(lsgrouptype.get(0));
		groupTypeSpinner.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, lsgrouptype));

		AdminUserDao admindao = new AdminUserDao();
		List<AdminUser> lsAdmin = new ArrayList<AdminUser>();
		lsAdmin = admindao
				.getTablesValues("select * from adminuser where active='true' Order by name COLLATE NOCASE ASC;");
		List<String> lsAdminUserName = new ArrayList<String>();
		if (lsAdmin != null) {
			for (int i = 0; i < lsAdmin.size(); i++) {
				lsAdminUserName.add(lsAdmin.get(i).getName());
				allNumbers.add(lsAdmin.get(i).getMobileNo());
			}
			individualSpinner.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, lsAdminUserName));
		}

		messageTypesSpinner.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, types));
		if (allNumbers.size() > 0)
			individualNumber = allNumbers.get(0);
	}

	public boolean sendSMS(String phoneNo, String message) {
		try {
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(phoneNo, null, message, null, null);
			Toast.makeText(getApplicationContext(), "Message Sent",
					Toast.LENGTH_LONG).show();
			return true;
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"SMS faild, please try again later!", Toast.LENGTH_LONG)
					.show();
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void finish() {
		// we need to override this to performe the animtationOut on each
		// finish.
		ActivitySwitcher.animationOut(findViewById(R.id.linearLayout1),
				getWindowManager(),
				new ActivitySwitcher.AnimationFinishedListener() {
					@Override
					public void onAnimationFinished() {
						SendSMS.super.finish();
						// disable default animation
						overridePendingTransition(0, 0);
					}
				});
	}

	@Override
	protected void onResume() {
		// animateIn this activity
		ActivitySwitcher.animationIn(findViewById(R.id.linearLayout1),
				getWindowManager());
		super.onResume();
	}
}

// btnSend.setOnClickListener(new OnClickListener() {
//
// @Override
// public void onClick(View v) {
// // TODO Auto-generated method stub
// String phoneNo = txtPhoneNo.getText().toString();
// String SMS = txtSMS.getText().toString();
//
// try {
// SmsManager smsManager = SmsManager.getDefault();
// smsManager.sendTextMessage(phoneNo, null, SMS, null, null);
// Toast.makeText(getApplicationContext(), " SMS Sent!...",
// Toast.LENGTH_LONG).show();
//
// } catch (Exception e) {
// Toast.makeText(getApplicationContext(),
// "SMS faild, please try again later!",
// Toast.LENGTH_LONG).show();
// e.printStackTrace();
// }
// }
// });
