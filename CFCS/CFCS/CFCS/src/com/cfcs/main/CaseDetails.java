package com.cfcs.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CaseDetails extends Activity {

	private LinearLayout amountdetailsLayout, caselayout, clientlayout,
			advocatelayout, courtlayout, remarkLayout, advocateFeeLayout;
	private ImageView casestatusImageView, editIcon, deleteIcon;
	private Button back, front;
	private TextView workupdate, client, court, advocate, casedetails, remark,
			detailNumber, amountdetials, advocatefee, caseUpdateDatetext,
			minuteSpendText;
	private int position;
	private AsyncHttpClient clientAsync;
	private List<CaseUpdate> lsCase;
	private List<CustomerMaster> lsCustomer;
	private List<Advocate> lsAdvocate;
	Map<String, CustomerMaster> caseToCustomer;
	Map<String, Advocate> caseToAdvocate;
	private CaseUpdate currentCaseUpdate;
	private List<String> minuteKey;
	private HashMap<String, Integer> minuteKeyToValue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.casedetails);

		clientAsync = Config.getClient();
		caseToAdvocate = new HashMap<String, Advocate>();
		caseToCustomer = new HashMap<String, CustomerMaster>();
		position = getIntent().getIntExtra("index", 0);
		advocatefee = (TextView) findViewById(R.id.advocatefeedetails);
		caselayout = (LinearLayout) findViewById(R.id.layoutcase);
		clientlayout = (LinearLayout) findViewById(R.id.layoutclient);
		advocatelayout = (LinearLayout) findViewById(R.id.layoutadvocate);
		courtlayout = (LinearLayout) findViewById(R.id.layoutcourt);
		amountdetailsLayout = (LinearLayout) findViewById(R.id.amountdetailsLayout);
		advocateFeeLayout = (LinearLayout) findViewById(R.id.layoutadvocatefee);
		remarkLayout = (LinearLayout) findViewById(R.id.layoutRemark);
		amountdetials = (TextView) findViewById(R.id.amountdetails);
		workupdate = (TextView) findViewById(R.id.workupdatedetails);
		casedetails = (TextView) findViewById(R.id.casedetails);
		remark = (TextView) findViewById(R.id.remarkdetails);
		court = (TextView) findViewById(R.id.courtdetails);
		advocate = (TextView) findViewById(R.id.advocatedetails);
		detailNumber = (TextView) findViewById(R.id.detailnumber);
		client = (TextView) findViewById(R.id.clientdetails);
		minuteSpendText = (TextView) findViewById(R.id.minuteSpendText);
		caseUpdateDatetext = (TextView) findViewById(R.id.textviewCaseUpdateDate);
		casestatusImageView = (ImageView) findViewById(R.id.casedetailsstatus);
		back = (Button) findViewById(R.id.back);
		front = (Button) findViewById(R.id.front);
		editIcon = (ImageView) findViewById(R.id.editIcon);
		deleteIcon = (ImageView) findViewById(R.id.imageViewDeleteIcon);
		back.setOnClickListener(onClickListener);
		front.setOnClickListener(onClickListener);
		deleteIcon.setOnClickListener(onClickListener);
		CaseUpdateDao casedao = new CaseUpdateDao();
		currentCaseUpdate = new CaseUpdate();
		minuteKey = new ArrayList<String>();
		minuteKeyToValue = new HashMap<String, Integer>();
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
		lsCase = getTop10CaseUpdate();
		if (lsCase != null && !lsCase.isEmpty()) {
			lsCustomer = getCustomerOfCaseUpdate(lsCase);
			lsAdvocate = getAdvocateOfCaseUpdate(lsCase);
			setDetialsOfCase();
		}
	}

	View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.back:
				if (position > 0)
					position--;
				setDetialsOfCase();
				break;

			case R.id.front:
				if (position < 24)
					position++;
				setDetialsOfCase();
				break;

			case R.id.imageViewDeleteIcon:
				deleteCurrentUpdate();
				break;

			default:
				break;
			}
		}
	};

	public void setDetialsOfCase() {
		try {
			CustomerMaster customer = new CustomerMaster();
			Advocate advocateObj = new Advocate();
			if (position < lsCase.size()) {
				currentCaseUpdate = lsCase.get(position);
				if (caseToCustomer.containsKey(currentCaseUpdate
						.getCaseUpdateID()))
					customer = caseToCustomer.get(currentCaseUpdate
							.getCaseUpdateID());
				if (caseToAdvocate.containsKey(currentCaseUpdate
						.getCaseUpdateID()))
					advocateObj = caseToAdvocate.get(currentCaseUpdate
							.getCaseUpdateID());

				UpdateTypeMasterDao updatedao = new UpdateTypeMasterDao();
				List<UpdateTypeMaster> lsupdate = new ArrayList<UpdateTypeMaster>();
				lsupdate = updatedao
						.getTablesValues("select * from updatetypemaster where active ='true' and updatetypeid="
								+ currentCaseUpdate.getUpdateTypeID());
				if (lsupdate != null && !lsupdate.isEmpty())
					workupdate.setText(lsupdate.get(0).getUpdateType());
				else
					workupdate.setText("");

				CourtTypeMasterDao dao = new CourtTypeMasterDao();
				List<CourtTypeMaster> lscourt = new ArrayList<CourtTypeMaster>();
				lscourt = dao
						.getTablesValues("select * from courttypemaster where active ='true' and courttypeid="
								+ currentCaseUpdate.getCourtTypeID());

				if (lscourt != null && !lscourt.isEmpty()) {
					courtlayout.setVisibility(View.VISIBLE);
					court.setText(lscourt.get(0).getCourtType());
				} else {
					courtlayout.setVisibility(View.GONE);
					court.setText("");
				}

				if (caseToAdvocate.containsKey(currentCaseUpdate
						.getCaseUpdateID())) {
					if (advocateObj.getAdvocateName().length() > 0) {
						advocatelayout.setVisibility(View.VISIBLE);
						advocate.setText(advocateObj.getAdvocateName());
					} else {
						advocatelayout.setVisibility(View.GONE);
						advocate.setText("");
					}
				} else {
					advocatelayout.setVisibility(View.GONE);
					advocate.setText("");
				}

				if (caseToCustomer.containsKey(currentCaseUpdate
						.getCaseUpdateID())) {
					client.setText(customer.getCustomerName());
					clientlayout.setVisibility(View.VISIBLE);
				} else {
					client.setText("");
					clientlayout.setVisibility(View.GONE);
				}

				if (currentCaseUpdate.getRemark().length() > 0) {
					remark.setText(currentCaseUpdate.getRemark());
					remarkLayout.setVisibility(View.VISIBLE);
				} else {
					remarkLayout.setVisibility(View.GONE);
				}

				detailNumber.setText(Integer.toString(position + 1));

				CaseMasterDao casedao = new CaseMasterDao();
				List<CaseMaster> lscaseMaster = new ArrayList<CaseMaster>();
				lscaseMaster = casedao
						.getTablesValues("select * from casemaster where active ='true' and caseid="
								+ currentCaseUpdate.getCaseID());

				if (lscaseMaster != null && lscaseMaster.size() > 0) {
					caselayout.setVisibility(View.VISIBLE);
					casedetails.setText(lscaseMaster.get(0).getCaseTitle());
				} else {
					caselayout.setVisibility(View.GONE);
					casedetails.setText("");
				}

				if (currentCaseUpdate.getIsChargeable().compareTo("true") == 0) {
					if (currentCaseUpdate.getAmount().compareTo("0") == 0)
						amountdetials.setText("Non Chargeable");
					else
						amountdetials.setText(currentCaseUpdate.getAmount());
					amountdetailsLayout.setVisibility(View.VISIBLE);

					if (currentCaseUpdate.getIsAdvChargeable()
							.compareTo("true") == 0) {
						if (currentCaseUpdate.getAdvocateFee().compareTo("0") == 0)
							advocatefee.setText("Non Chargeable");
						else
							advocatefee.setText(currentCaseUpdate
									.getAdvocateFee());
						advocateFeeLayout.setVisibility(View.VISIBLE);
					} else {
						advocateFeeLayout.setVisibility(View.VISIBLE);
						advocatefee.setText("Non Chargeable");
					}
				} else {
					amountdetailsLayout.setVisibility(View.VISIBLE);
					amountdetials.setText("Non Chargeable");
					advocatefee.setText("Non Chargeable");
					advocateFeeLayout.setVisibility(View.VISIBLE);
				}

				editIcon.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {

						String caseUpdateId = currentCaseUpdate
								.getCaseUpdateID();
						Intent myIntent = new Intent(CaseDetails.this,
								EditCaseUpdateActivity.class);
						myIntent.putExtra("CASEUPDATEID", caseUpdateId);
						startActivity(myIntent);
					}
				});

				caseUpdateDatetext.setText(currentCaseUpdate
						.getCaseupdatedate());
				for (int i = 0; i < minuteKey.size(); i++) {
					if (minuteKeyToValue.get(minuteKey.get(i)) == currentCaseUpdate
							.getMinuteSpend()) {
						minuteSpendText.setText(minuteKey.get(i));
						break;
					}
				}

				switch (currentCaseUpdate.getAppStatus()) {
				case 1:
					casestatusImageView.setImageResource(R.drawable.ideal);
					editIcon.setVisibility(View.VISIBLE);
					deleteIcon.setVisibility(View.VISIBLE);
					break;
				case 2:
					casestatusImageView.setImageResource(R.drawable.approved);
					deleteIcon.setVisibility(View.GONE);
					editIcon.setVisibility(View.GONE);
					break;
				case 3:
					casestatusImageView.setImageResource(R.drawable.rejected);
					editIcon.setVisibility(View.VISIBLE);
					deleteIcon.setVisibility(View.VISIBLE);
					break;
				case 4:
					casestatusImageView.setImageResource(R.drawable.ignored);
					editIcon.setVisibility(View.VISIBLE);
					deleteIcon.setVisibility(View.VISIBLE);
					break;
				case 5:
					casestatusImageView.setImageResource(R.drawable.resubmited);
					editIcon.setVisibility(View.VISIBLE);
					deleteIcon.setVisibility(View.VISIBLE);
					break;
				default:
					casestatusImageView.setImageResource(R.drawable.ideal);
					editIcon.setVisibility(View.VISIBLE);
					deleteIcon.setVisibility(View.VISIBLE);
					break;
				}

			} else {
				position--;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<CaseUpdate> getTop10CaseUpdate() {
		List<CaseUpdate> lsCase = new ArrayList<CaseUpdate>();
		CaseUpdateDao dao = new CaseUpdateDao();
		int userid = Config.getSharedPreferences(this, "usersettings",
				"userid", -1);
		String quer = "select * from caseupdate where workdoneby="
				+ userid
				+ " and active='true' Order by DATETIME(updatedate) DESC limit 25";
		lsCase = dao.getTablesValues(quer);
		return lsCase;
	}

	public List<CustomerMaster> getCustomerOfCaseUpdate(List<CaseUpdate> lsCase) {
		CustomerMasterDao dao = new CustomerMasterDao();
		List<CustomerMaster> lsCustomer = new ArrayList<CustomerMaster>();
		for (int i = 0; i < lsCase.size(); i++) {
			CaseUpdate caseUpdate = lsCase.get(i);
			List<CustomerMaster> lscust = dao
					.getTablesValues("select * from customermaster where active ='true' and customerid="
							+ caseUpdate.getCustomerID());
			if (lscust != null && !lscust.isEmpty()) {
				lsCustomer.add(lscust.get(0));
				caseToCustomer.put(caseUpdate.getCaseUpdateID(), lscust.get(0));
			}
		}
		return lsCustomer;
	}

	public List<Advocate> getAdvocateOfCaseUpdate(List<CaseUpdate> lsCase) {
		AdvocateDao dao = new AdvocateDao();
		List<Advocate> lsadvocate = new ArrayList<Advocate>();
		for (int i = 0; i < lsCase.size(); i++) {
			CaseUpdate caseUpdate = lsCase.get(i);
			List<Advocate> lscust = dao
					.getTablesValues("select * from otheradvocate where active ='true' and advocateid="
							+ caseUpdate.getAdvocateID());
			if (lscust != null && !lscust.isEmpty()) {
				lsadvocate.add(lscust.get(0));
				caseToAdvocate.put(caseUpdate.getCaseUpdateID(), lscust.get(0));
			}
		}
		return lsadvocate;
	}

	public void deleteCurrentUpdate() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this)
				.setMessage("Do you want to delete this case?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								currentCaseUpdate.setActive("false");
								makeJsonAndSend(currentCaseUpdate);
								Config.toastShow("CaseUpdate Deleted",
										CaseDetails.this);
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		alert.show();
	}

	private void makeJsonAndSend(final CaseUpdate caseupdateJson) {
		try {
			Gson gson = new Gson();
			if (caseupdateJson.getCaseID() != -1) {
				String case_json = gson.toJson(caseupdateJson);
				JSONObject objfirst = new JSONObject(case_json);
				JSONArray array = new JSONArray();
				array.put(objfirst);
				JSONObject obj = new JSONObject();
				obj.put("CaseUpdate", array);
				JSONObject finalobj1 = new JSONObject();
				finalobj1.put("SyncData", obj);
				JSONObject finalObj = new JSONObject(finalobj1.toString());

				StringEntity se_info = new StringEntity(finalObj.toString());
				String url = Config.BASE_URL + "ResponseCaseUpdate";

				RequestParams params = new RequestParams();
				params.put("DataJson", finalObj.toString());
				params.put("UserName", Config.username);
				params.put("Password", Config.password);
				clientAsync.post(url, params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						Config.toastShow("Case Updated and Sent",
								CaseDetails.this);
						saveInDatabaseAndResetSpinner(caseupdateJson, 1);
						Intent intent = new Intent(CaseDetails.this,
								CaseUpdateActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}

					@Override
					public void onFailure(Throwable error, String content) {
						Config.toastShow("Not sent. Saved to Database.",
								CaseDetails.this);
						saveInDatabaseAndResetSpinner(caseupdateJson, 0);
						Intent intent = new Intent(CaseDetails.this,
								CaseUpdateActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}
				});

			} else {
				Config.alertBox("Invalid Case Id.", CaseDetails.this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveInDatabaseAndResetSpinner(CaseUpdate caseupdateJson, int i) {
		CaseUpdateDao dao = new CaseUpdateDao();
		caseupdateJson.setUpdateStamp(i);
		dao.update(caseupdateJson);
	}

	@Override
	public void finish() {
		// we need to override this to performe the animtationOut on each
		// finish.
		ActivitySwitcher.animationOut(findViewById(R.id.casedetailscontainer),
				getWindowManager(),
				new ActivitySwitcher.AnimationFinishedListener() {
					@Override
					public void onAnimationFinished() {
						CaseDetails.super.finish();
						// disable default animation
						overridePendingTransition(0, 0);
					}
				});
	}

	@Override
	protected void onResume() {
		// animateIn this activity
		ActivitySwitcher.animationIn(findViewById(R.id.casedetailscontainer),
				getWindowManager());
		super.onResume();
	}

}
