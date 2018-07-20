package com.cfcs.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import com.cfcs.anim.ActivitySwitcher;
import com.cfcs.classes.Advocate;
import com.cfcs.classes.CaseMaster;
import com.cfcs.classes.CaseUpdate;
import com.cfcs.classes.CourtTypeMaster;
import com.cfcs.classes.CustomerMaster;
import com.cfcs.classes.UpdateTypeMaster;
import com.cfcs.dao.AdvocateDao;
import com.cfcs.dao.CaseMasterDao;
import com.cfcs.dao.CaseUpdateDao;
import com.cfcs.dao.CourtTypeMasterDao;
import com.cfcs.dao.CustomerMasterDao;
import com.cfcs.dao.UpdateTypeMasterDao;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.sax.RootElement;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class CaseUpdateActivity extends Activity implements OnClickListener,
		OnItemSelectedListener {

	// {"CaseUpdate":[{}]}

	private Button submit, viewList, caseUpdateDateButton;
	private Spinner workupdateSpinner, clientSpinner, caseSpinner,
			seniorAdvocateSpinner, courtSpinner, minuteSpinner;
	// List<String> workUpdateList, clientList, caseList, senioAdvocateList,
	// courtList;
	private EditText remark, clientNameEdit, chargeableAmountEdit, advocateFee,
			caseUpdateDateEdit;
	private String updateType = "", remarkValue = "", caseupdateId = "",
			CASEUPDATEUID = "", chargeableAmount = "0", advocateFeeValue = "0",
			caseupdateDateValue = "";
	private int caseId = 0, updateTypeId = 0, clientId = 0, advocateId = 0,
			courtId = 0, minuteSpend = 0;
	private List<Advocate> lsAdvocate;
	private List<CourtTypeMaster> lsCourt;
	private List<CaseMaster> lsCase;
	private List<CustomerMaster> lsCustomer, totalListCustomer;
	private List<UpdateTypeMaster> lsUpdate;
	private AsyncHttpClient client;
	private LinearLayout clubLayout, amountLayout, layoutIsCharagable,
			layoutIsAdvChargeable;
	private RadioButton radioYes, radioNo, radioAdvChargYes, radioAdvChargNo;
	private RadioGroup radioGroup, radioAdvChargGroup;
	private boolean isAdvocateSelected = false, dateflag = false;
	private ProgressBar progessBar;
	private DatePicker caseUpdatedatePicker;
	private List<String> minuteKey;
	private HashMap<String, Integer> minuteKeyToValue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.caseupdateactivity);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		client = Config.getClient();
		layoutIsAdvChargeable = (LinearLayout) findViewById(R.id.isAdvChargeableLayout);
		caseUpdatedatePicker = (DatePicker) findViewById(R.id.datePickerCaseUpdateDate);
		caseUpdateDateEdit = (EditText) findViewById(R.id.editTextCaseUpdateDate);
		clientNameEdit = (EditText) findViewById(R.id.editTextclientName);
		advocateFee = (EditText) findViewById(R.id.editTextadvocatefee);
		clubLayout = (LinearLayout) findViewById(R.id.layoutcustomercourtetc);
		minuteSpinner = (Spinner) findViewById(R.id.spinnerMinuteSpend);
		workupdateSpinner = (Spinner) findViewById(R.id.spinnerworkupdate);
		clientSpinner = (Spinner) findViewById(R.id.spinnerclient);
		caseSpinner = (Spinner) findViewById(R.id.spinnercase);
		seniorAdvocateSpinner = (Spinner) findViewById(R.id.spinnersenioradvocate);
		courtSpinner = (Spinner) findViewById(R.id.spinnercourt);
		workupdateSpinner.setOnItemSelectedListener(this);
		clientSpinner.setOnItemSelectedListener(this);
		caseSpinner.setOnItemSelectedListener(this);
		seniorAdvocateSpinner.setOnItemSelectedListener(this);
		courtSpinner.setOnItemSelectedListener(this);
		minuteSpinner.setOnItemSelectedListener(this);
		caseUpdateDateButton = (Button) findViewById(R.id.buttonCaseUpdatedate);
		submit = (Button) findViewById(R.id.submitupdates);
		viewList = (Button) findViewById(R.id.listupdates);
		remark = (EditText) findViewById(R.id.remarkupdate);
		submit.setOnClickListener(this);
		viewList.setOnClickListener(this);
		caseUpdateDateButton.setOnClickListener(this);
		radioYes = (RadioButton) findViewById(R.id.radioyes);
		radioNo = (RadioButton) findViewById(R.id.radiono);
		radioAdvChargNo = (RadioButton) findViewById(R.id.radioAdvChargno);
		radioAdvChargYes = (RadioButton) findViewById(R.id.radioAdvChargyes);
		radioAdvChargGroup = (RadioGroup) findViewById(R.id.radioGroupIsAdvChargeable);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroupchargables);
		chargeableAmountEdit = (EditText) findViewById(R.id.editTextAmount);
		layoutIsCharagable = (LinearLayout) findViewById(R.id.layoutRadioIsCharagable);
		amountLayout = (LinearLayout) findViewById(R.id.amountLayout);
		lsAdvocate = new ArrayList<Advocate>();
		lsCourt = new ArrayList<CourtTypeMaster>();
		lsCustomer = new ArrayList<CustomerMaster>();
		lsCase = new ArrayList<CaseMaster>();
		lsUpdate = new ArrayList<UpdateTypeMaster>();
		minuteKey = new ArrayList<String>();
		minuteKeyToValue = new HashMap<String, Integer>();
		progessBar = (ProgressBar) findViewById(R.id.progressBarCaseUpdate);
		totalListCustomer = new ArrayList<CustomerMaster>();
		caseUpdateDateEdit.setEnabled(false);
		fillList();
		radioYes.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				chargeableAmount = "0";
				chargeableAmountEdit.setText("");
				advocateFee.setText("");
				advocateFeeValue = "0";

				if (isChecked) {
					amountLayout.setVisibility(View.VISIBLE);
					layoutIsAdvChargeable.setVisibility(View.VISIBLE);
					if (isAdvocateSelected && radioAdvChargYes.isChecked())
						advocateFee.setVisibility(View.VISIBLE);
					else
						advocateFee.setVisibility(View.GONE);
				} else {
					amountLayout.setVisibility(View.GONE);
					advocateFee.setVisibility(View.GONE);
					layoutIsAdvChargeable.setVisibility(View.GONE);
				}
			}
		});

		radioAdvChargYes
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						advocateFee.setText("");
						advocateFeeValue = "0";
						if (isChecked) {
							if (isAdvocateSelected)
								advocateFee.setVisibility(View.VISIBLE);
							else
								advocateFee.setVisibility(View.GONE);
						} else {
							advocateFee.setVisibility(View.GONE);
						}
					}
				});

		clientNameEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

				if (arg0.toString().compareTo("") != 0) {
					lsCustomer.clear();
					lsCustomer = searchClientName(arg0.toString());
					if (lsCustomer != null) {
						lsCase.clear();
						clientId = 0;
						caseId = 0;
						caseSpinner.setAdapter(new SpinnerAdapter(lsCase));
						clientSpinner
								.setAdapter(new SpinnerAdapter(lsCustomer));
					}
				} else {
					lsCustomer.clear();
					clientId = 0;
					caseId = 0;
					CustomerMasterDao customerdao = new CustomerMasterDao();
					List<CustomerMaster> listCustomer = customerdao
							.getTablesValues("select * from customermaster where active='true' and activestatus=1 Order by customername COLLATE NOCASE ASC;");
					List<CustomerMaster> listCoustomerWithCases = new ArrayList<CustomerMaster>();
					CaseMasterDao casedao = new CaseMasterDao();
					if (listCustomer != null && !listCustomer.isEmpty()) {
						for (int i = 0; i < listCustomer.size(); i++) {
							List<CaseMaster> listCase = casedao
									.getTablesValues("select * from casemaster where customerid="
											+ listCustomer.get(i)
													.getCustomerID()
											+ " and active='true' and casestatus='true' Order by casetitle COLLATE NOCASE ASC");
							if (listCase != null && !listCase.isEmpty()) {
								listCoustomerWithCases.add(listCustomer.get(i));
							}
						}
						CustomerMaster customerMaster = new CustomerMaster();
						customerMaster.setCustomerName("Select");
						lsCustomer.add(customerMaster);
						for (int i = 0; i < listCoustomerWithCases.size(); i++) {
							lsCustomer.add(listCoustomerWithCases.get(i));
						}
						clientSpinner
								.setAdapter(new SpinnerAdapter(lsCustomer));
					}

				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	public ArrayAdapter<String> getAdapterForSpinner(List<String> list) {
		ArrayAdapter<String> adapter = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item, list);
		return adapter;
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

	private void fillList() {
		/*
		 * Advocate**************************************************************
		 * **
		 */
		AdvocateDao advocatedao = new AdvocateDao();
		List<Advocate> listAdvocate = advocatedao
				.getTablesValues("select * from otheradvocate where type=1 and active='true' Order by advocatename COLLATE NOCASE ASC");
		Advocate advocae = new Advocate();
		if (listAdvocate != null && listAdvocate.size() > 0) {
			advocae.setAdvocateName("Select");
			lsAdvocate.add(advocae);
			for (int i = 0; i < listAdvocate.size(); i++) {
				lsAdvocate.add(listAdvocate.get(i));
			}
			seniorAdvocateSpinner.setAdapter(new SpinnerAdapter(lsAdvocate));
		} else {
			seniorAdvocateSpinner.setAdapter(new SpinnerAdapter(lsAdvocate));
		}

		/*
		 * CourtTypeMaster*******************************************************
		 * *********
		 */
		CourtTypeMasterDao courtdao = new CourtTypeMasterDao();
		List<CourtTypeMaster> listCourt = courtdao
				.getTablesValues("select * from courttypemaster where active='true' Order by courttype COLLATE NOCASE ASC;");
		if (listCourt != null && !listCourt.isEmpty()) {
			CourtTypeMaster court = new CourtTypeMaster();
			court.setCourtType("Select");
			lsCourt.add(court);
			for (int i = 0; i < listCourt.size(); i++) {
				lsCourt.add(listCourt.get(i));
			}
			courtSpinner.setAdapter(new SpinnerAdapter(lsCourt));
		} else {
			courtSpinner.setAdapter(new SpinnerAdapter(lsCourt));
		}

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
			clientSpinner.setAdapter(new SpinnerAdapter(lsCustomer));
		} else {
			clientSpinner.setAdapter(new SpinnerAdapter(lsCustomer));
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
			lsUpdate.add(updatemaster);
			for (int i = 0; i < listupdatetype.size(); i++) {
				lsUpdate.add(listupdatetype.get(i));

			}
			workupdateSpinner.setAdapter(new SpinnerAdapter(lsUpdate));
		} else {
			workupdateSpinner.setAdapter(new SpinnerAdapter(lsUpdate));
		}

		minuteKey.add("30 Minutes");
		minuteKey.add("1 Hour");
		minuteKey.add("1 Hour 30 Minutes");
		minuteKey.add("2 Hour");
		minuteKey.add("2 Hour 30 Minutes");
		minuteKey.add("3 Hour");
		minuteKey.add("3 Hour 30 Minutes");
		minuteKey.add("4 Hour");
		minuteKey.add("4 Hour 30 Minutes");
		minuteKey.add("5 Hour");
		minuteKey.add("5 Hour 30 Minutes");
		minuteKey.add("6 Hour");
		minuteKey.add("6 Hour 30 Minutes");
		minuteKey.add("7 Hour");
		minuteKey.add("7 Hour 30 Minutes");
		minuteKey.add("8 Hour");

		minuteKeyToValue.put("30 Minutes", 30);
		minuteKeyToValue.put("1 Hour", 60);
		minuteKeyToValue.put("1 Hour 30 Minutes", 90);
		minuteKeyToValue.put("2 Hour", 120);
		minuteKeyToValue.put("2 Hour 30 Minutes", 150);
		minuteKeyToValue.put("3 Hour", 180);
		minuteKeyToValue.put("3 Hour 30 Minutes", 210);
		minuteKeyToValue.put("4 Hour", 240);
		minuteKeyToValue.put("4 Hour 30 Minutes", 270);
		minuteKeyToValue.put("5 Hour", 300);
		minuteKeyToValue.put("5 Hour 30 Minutes", 330);
		minuteKeyToValue.put("6 Hour", 360);
		minuteKeyToValue.put("6 Hour 30 Minutes", 390);
		minuteKeyToValue.put("7 Hour", 420);
		minuteKeyToValue.put("7 Hour 30 Minutes", 450);
		minuteKeyToValue.put("8 Hour", 480);

		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_dropdown_item, minuteKey);
		minuteSpinner.setAdapter(spinnerArrayAdapter);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.submitupdates: {

			remarkValue = remark.getText().toString();
			chargeableAmount = chargeableAmountEdit.getText().toString();
			advocateFeeValue = advocateFee.getText().toString();
			if (chargeableAmount.compareTo("") == 0)
				chargeableAmount = "0";
			if (advocateFeeValue.compareTo("") == 0)
				advocateFeeValue = "0";

			if (caseupdateDateValue.compareTo("") != 0) {
				if (updateType.compareTo("0") != 0 && updateTypeId != 0) {
					if (updateType.compareTo("Miscellaneous") == 0) {
						if (minuteSpend != 0) {
							if (remarkValue.compareTo("") != 0) {
								submit.setEnabled(false);
								progessBar.setVisibility(View.VISIBLE);
								makeJsonAndSend();
							} else {
								alert("Please fill the remark.");
							}
						} else {
							alert("Please fill the Minutes Spend.");
						}
					} else {
						if (clientId != 0) {
							if (caseId != 0) {
								if (minuteSpend != 0) {
									submit.setEnabled(false);
									progessBar.setVisibility(View.VISIBLE);
									makeJsonAndSend();
								} else {
									alert("Please fill the Minutes Spend.");
								}
							} else {
								alert("Please fill the Case Details.");
							}
						} else {
							alert("Please fill the Client Details.");
						}
					}
				} else {
					alert("Please fill the Work Update type");
				}
			} else {
				alert("Please fill the Case Update Date");
			}
		}
			break;

		case R.id.listupdates:
			animatedStartActivity(CaseUpdates.class);
			break;

		case R.id.buttonCaseUpdatedate: {
			if (!dateflag) {
				caseUpdatedatePicker.setVisibility(View.VISIBLE);
				dateflag = true;
				caseUpdateDateEdit.setText("");
				caseupdateDateValue = "";
			} else {
				dateflag = false;
				caseUpdatedatePicker.setVisibility(View.GONE);
				int day = caseUpdatedatePicker.getDayOfMonth();
				int month = caseUpdatedatePicker.getMonth();
				int year = caseUpdatedatePicker.getYear();
				Calendar calendar = Calendar.getInstance();
				calendar.set(year, month, day);
				Date date = calendar.getTime();
				SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
				String finaldate = df.format(date);

				Time todayTime = new Time(Time.getCurrentTimezone());
				todayTime.setToNow();
				int TodayDay = todayTime.monthDay;
				int Todaymonth = todayTime.month;
				int Todayyear = todayTime.year;

				if (day <= TodayDay && day >= TodayDay - 2) {
					caseupdateDateValue = finaldate;
					caseUpdateDateEdit.setText(finaldate);
				} else {
					Config.alertBox("Please fill valid date",
							CaseUpdateActivity.this);
				}

			}
			break;
		}

		default:
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {

		switch (arg0.getId()) {
		// startActivity(intent);
		case R.id.spinnerworkupdate: {
			if (position > 0) {
				updateType = lsUpdate.get(position).getUpdateType();
				updateTypeId = lsUpdate.get(position).getUpdateTypeID();
				if (lsUpdate.get(position).getUpdateType()
						.compareTo("Miscellaneous") == 0) {
					clubLayout.setVisibility(View.GONE);
					layoutIsCharagable.setVisibility(View.GONE);
					amountLayout.setVisibility(View.GONE);
					layoutIsAdvChargeable.setVisibility(View.GONE);
				} else {
					clubLayout.setVisibility(View.VISIBLE);
					layoutIsCharagable.setVisibility(View.VISIBLE);
					amountLayout.setVisibility(View.VISIBLE);
					layoutIsAdvChargeable.setVisibility(View.VISIBLE);
				}
			} else {
				updateTypeId = 0;
				updateType = "0";
				if (lsUpdate.get(position).getUpdateType()
						.compareTo("Miscellaneous") == 0) {
					clubLayout.setVisibility(View.GONE);
					layoutIsCharagable.setVisibility(View.GONE);
					amountLayout.setVisibility(View.GONE);
					layoutIsAdvChargeable.setVisibility(View.GONE);
				} else {
					clubLayout.setVisibility(View.VISIBLE);
					layoutIsCharagable.setVisibility(View.VISIBLE);
					amountLayout.setVisibility(View.VISIBLE);
					layoutIsAdvChargeable.setVisibility(View.VISIBLE);
				}
			}
			break;
		}

		case R.id.spinnerclient: {
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

				caseSpinner.setVisibility(View.VISIBLE);
				seniorAdvocateSpinner.setVisibility(View.VISIBLE);
				clientSpinner.setVisibility(View.VISIBLE);
				courtSpinner.setVisibility(View.VISIBLE);
			} else {
				clientId = 0;
				caseId = 0;
				lsCase.clear();
				caseSpinner.setAdapter(new SpinnerAdapter(lsCase));

				caseSpinner.setVisibility(View.VISIBLE);
				seniorAdvocateSpinner.setVisibility(View.VISIBLE);
				clientSpinner.setVisibility(View.VISIBLE);
				courtSpinner.setVisibility(View.VISIBLE);
			}
			break;
		}
		case R.id.spinnercase:
			if (lsCase != null && !lsCase.isEmpty())
				caseId = lsCase.get(position).getCaseID();

			break;
		case R.id.spinnersenioradvocate:
			if (position > 0 && lsAdvocate != null && !lsAdvocate.isEmpty()) {
				isAdvocateSelected = true;
				advocateId = lsAdvocate.get(position).getAdvocateID();
				if (radioYes.isChecked()) {
					advocateFee.setVisibility(View.VISIBLE);
					advocateFeeValue = "0";
				}
			} else {
				advocateId = 0;
				isAdvocateSelected = false;
				advocateFee.setVisibility(View.GONE);
				advocateFeeValue = "0";
			}
			break;
		case R.id.spinnercourt:
			if (position > 0 && lsCourt != null && !lsCourt.isEmpty())
				courtId = lsCourt.get(position).getCourtTypeId();
			else
				courtId = 0;
			break;

		case R.id.spinnerMinuteSpend: {
			minuteSpend = minuteKeyToValue.get(minuteKey.get(position));
			break;
		}
		default:
			break;
		}
	}

	private void makeJsonAndSend() {

		try {
			Gson gson = new Gson();
			UUID uuid = UUID.randomUUID();
			CASEUPDATEUID = uuid.toString();
			final CaseUpdate caseupdate = getCaseUpdateObjectFilled(CASEUPDATEUID);
			// if (caseupdate.getCaseID() != -1) {
			String case_json = gson.toJson(caseupdate);
			JSONObject objfirst = new JSONObject(case_json);
			JSONArray array = new JSONArray();
			array.put(objfirst);
			JSONObject obj = new JSONObject();
			obj.put("CaseUpdate", array);
			JSONObject finalobj1 = new JSONObject();
			finalobj1.put("SyncData", obj);
			JSONObject finalObj = new JSONObject(finalobj1.toString());

			String url = Config.BASE_URL + "ResponseCaseUpdate";

			RequestParams params = new RequestParams();
			params.put("DataJson", finalObj.toString());
			params.put("UserName", Config.username);
			params.put("Password", Config.password);
			client.post(url, params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String response) {
					Config.toastShow("sent", CaseUpdateActivity.this);
					saveInDatabaseAndResetSpinner(caseupdate, 1);
					clientNameEdit.setText("");
					submit.setEnabled(true);
					progessBar.setVisibility(View.GONE);
				}

				@Override
				public void onFailure(Throwable error, String content) {
					Config.toastShow("Not sent. Saved to Database.",
							CaseUpdateActivity.this);
					saveInDatabaseAndResetSpinner(caseupdate, 0);
					submit.setEnabled(true);
					progessBar.setVisibility(View.GONE);

				}
			});

			// } else {
			// Config.alertBox("Invalid Case Id.", CaseUpdateActivity.this);
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveInDatabaseAndResetSpinner(CaseUpdate caseUpdate, int i) {
		CaseUpdateDao dao = new CaseUpdateDao();
		caseUpdate.setUpdateStamp(i);
		dao.insert(caseUpdate);
		caseSpinner.setAdapter(new SpinnerAdapter(lsCase));
		seniorAdvocateSpinner.setAdapter(new SpinnerAdapter(lsAdvocate));
		workupdateSpinner.setAdapter(new SpinnerAdapter(lsUpdate));
		courtSpinner.setAdapter(new SpinnerAdapter(lsCourt));
		clientSpinner.setAdapter(new SpinnerAdapter(lsCustomer));
		remark.setText("");
		CASEUPDATEUID = "";
		radioYes.setChecked(true);
		chargeableAmountEdit.setText("");
		chargeableAmount = "";
		advocateFeeValue = "";
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_dropdown_item, minuteKey);
		minuteSpinner.setAdapter(spinnerArrayAdapter);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

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
			if (object.get(postion) instanceof Advocate) {
				Advocate advocate = (Advocate) object.get(postion);
				name = advocate.getAdvocateName();
			} else if (object.get(0) instanceof CourtTypeMaster) {
				CourtTypeMaster court = (CourtTypeMaster) object.get(postion);
				name = court.getCourtType();
			} else if (object.get(0) instanceof CaseMaster) {
				CaseMaster casemaster = (CaseMaster) object.get(postion);
				name = casemaster.getCaseTitle();
			} else if (object.get(0) instanceof CustomerMaster) {
				CustomerMaster customer = (CustomerMaster) object.get(postion);
				name = customer.getCustomerName();
			} else if (object.get(0) instanceof UpdateTypeMaster) {
				UpdateTypeMaster update = (UpdateTypeMaster) object
						.get(postion);
				name = update.getUpdateType();
			}

			TextView t = new TextView(CaseUpdateActivity.this);
			t.setText(name);
			t.setTextSize(15);

			return t;
		}
	}

	private CaseUpdate getCaseUpdateObjectFilled(String caseUpdateId) {
		int workDoneBy = Config.getSharedPreferences(CaseUpdateActivity.this,
				"usersettings", "userid", -1);
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dateTimef = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = df.format(cal.getTime());
		String dateTime = dateTimef.format(cal.getTime());

		CaseUpdate caseUpdate = new CaseUpdate();
		caseUpdate.setCaseUpdateID(caseUpdateId);
		caseUpdate.setUpdateTypeID(updateTypeId);
		if (updateType.compareTo("Miscellaneous") != 0) {
			caseUpdate.setCustomerID(clientId);
			caseUpdate.setCaseID(caseId);
			caseUpdate.setAdvocateID(advocateId);
			caseUpdate.setCourtTypeID(courtId);
			if (radioYes.isChecked()) {
				caseUpdate.setIsChargeable("true");
				caseUpdate.setAmount(chargeableAmount);
			} else {
				caseUpdate.setIsChargeable("false");
				caseUpdate.setAmount("0");
			}
		} else {
			caseUpdate.setCustomerID(0);
			caseUpdate.setCaseID(0);
			caseUpdate.setAdvocateID(0);
			caseUpdate.setCourtTypeID(0);
			caseUpdate.setIsChargeable("false");
			caseUpdate.setAmount("0");
		}
		caseUpdate.setAdvocateFee(advocateFeeValue);
		caseUpdate.setRemark(remarkValue);
		caseUpdate.setAttachment("");
		caseUpdate.setWorkDoneBy(workDoneBy);
		caseUpdate.setAppStatus(1);
		caseUpdate.setAppBy(0);
		caseUpdate.setAddDate(date);
		caseUpdate.setUpdateDate(dateTime);
		caseUpdate.setActive("true");
		caseUpdate.setUpdateStamp(1);

		caseUpdate.setCaseupdatedate(caseUpdateDateEdit.getText().toString());
		caseUpdate.setMinuteSpend(minuteSpend);
		if (radioAdvChargYes.isChecked())
			caseUpdate.setIsAdvChargeable("true");
		else
			caseUpdate.setIsAdvChargeable("false");

		return caseUpdate;
	}

	@Override
	protected void onResume() {
		// animateIn this activity
		ActivitySwitcher.animationIn(findViewById(R.id.casecontainer),
				getWindowManager());
		super.onResume();
	}

	private void animatedStartActivity(Class<?> cls) {
		final Intent intent = new Intent(CaseUpdateActivity.this, cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		ActivitySwitcher.animationOut(findViewById(R.id.casecontainer),
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
		ActivitySwitcher.animationOut(findViewById(R.id.casecontainer),
				getWindowManager(),
				new ActivitySwitcher.AnimationFinishedListener() {
					@Override
					public void onAnimationFinished() {
						CaseUpdateActivity.super.finish();
						// disable default animation
						overridePendingTransition(0, 0);
					}
				});
	}

	public void alert(String alert) {
		Config.alertBox(alert, CaseUpdateActivity.this);
	}

}

// if (updateType.compareTo("Miscellaneous") != 0) {
// if (updateType.compareTo("") != 0 && clientId != 0
// && advocateId != 0 && updateType.compareTo("") != 0
// && courtId != 0) {
//
// if (radioYes.isChecked()) {
// if (chargeableAmount.compareTo("") != 0) {
// if (remarkValue.compareTo("") != 0) {
// makeJsonAndSend();
// } else
// Config.alertBox("Please fill the remark.",
// CaseUpdateActivity.this);
// } else
// Config.alertBox("Please fill the amount",
// CaseUpdateActivity.this);
// } else {
// if (remarkValue.compareTo("") != 0) {
// makeJsonAndSend();
// } else {
// Config.alertBox("Please fill the remark.",
// CaseUpdateActivity.this);
// }
// }
// } else
// Config.alertBox("Please fill all the fields.",
// CaseUpdateActivity.this);
// } else {
// if (remarkValue.compareTo("") != 0) {
// makeJsonAndSend();
// } else {
// Config.alertBox("Please fill the remark.",
// CaseUpdateActivity.this);
// }
// }
