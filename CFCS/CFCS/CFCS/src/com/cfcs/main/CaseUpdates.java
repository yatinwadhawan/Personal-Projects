package com.cfcs.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cfcs.anim.ActivitySwitcher;
import com.cfcs.classes.Advocate;
import com.cfcs.classes.CaseUpdate;
import com.cfcs.classes.CustomerMaster;
import com.cfcs.dao.AdvocateDao;
import com.cfcs.dao.CaseUpdateDao;
import com.cfcs.dao.CustomerMasterDao;
import com.cfcs.dao.Database;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class CaseUpdates extends Activity implements OnItemClickListener {

	ListView listView;
	Database db;
	Map<String, CustomerMaster> caseToCustomer;
	Map<String, Advocate> caseToAdvocate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.caseupdates);
		db = Database.instance();
		listView = (ListView) findViewById(R.id.listViewcaseupdates);
		caseToAdvocate = new HashMap<String, Advocate>();
		caseToCustomer = new HashMap<String, CustomerMaster>();
		CaseUpdateDao casedao = new CaseUpdateDao();
		List<CaseUpdate> lsCase = getTop10CaseUpdate();
		if (lsCase != null && !lsCase.isEmpty()) {
			List<CustomerMaster> lsCustomer = getCustomerOfCaseUpdate(lsCase);
			List<Advocate> lsAdvocate = getAdvocateOfCaseUpdate(lsCase);
			listView.setOnItemClickListener(this);
			listView.setAdapter(new ListAdapterUpdates(lsCase, caseToCustomer,
					caseToAdvocate));
		} else {
			Config.toastShow("Sorry, No Case Updates are available", this);
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

	private class ListAdapterUpdates extends BaseAdapter {

		List<CaseUpdate> lsCaseUpdate;
		Map<String, CustomerMaster> caseToCustomer;
		Map<String, Advocate> caseToAdvocate;

		public ListAdapterUpdates(List<CaseUpdate> lsCaseUpdate,
				Map<String, CustomerMaster> caseToCustomer,
				Map<String, Advocate> caseToAdvocate) {
			this.lsCaseUpdate = lsCaseUpdate;
			this.caseToCustomer = caseToCustomer;
			this.caseToAdvocate = caseToAdvocate;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return lsCaseUpdate.size();
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

			CustomerMaster customer = new CustomerMaster();
			Advocate advocate = new Advocate();
			CaseUpdate caseUpdate = lsCaseUpdate.get(position);
			if (caseToCustomer.containsKey(caseUpdate.getCaseUpdateID()))
				customer = caseToCustomer.get(caseUpdate.getCaseUpdateID());
			if (caseToAdvocate.containsKey(caseUpdate.getCaseUpdateID()))
				advocate = caseToAdvocate.get(caseUpdate.getCaseUpdateID());

			LayoutInflater layoutInflater1 = (LayoutInflater) getBaseContext()
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			LinearLayout basepage = (LinearLayout) layoutInflater1.inflate(
					R.layout.caseupdatecustomer, null);
			LinearLayout layout = (LinearLayout) basepage
					.findViewById(R.id.caseupdatelistLayout);
			LinearLayout secondlayout = (LinearLayout) layout
					.findViewById(R.id.caseupdatelistLayout);

			if (position % 2 == 0)
				basepage.setBackgroundColor(Color.parseColor("#D1D0CE"));

			TextView count = (TextView) layout.findViewById(R.id.textViewcount);
			count.setText(Integer.toString(position + 1));

			TextView name = (TextView) layout
					.findViewById(R.id.textViewcustomername);
			if (caseToCustomer.containsKey(caseUpdate.getCaseUpdateID()))
				name.setText(customer.getCustomerName());
			else
				name.setText("Miscellaneous");

			TextView dateText = (TextView) secondlayout
					.findViewById(R.id.textViewcasedate);
			String dateValue = caseUpdate.getUpdateDate();
			Date date = new Date();
			SimpleDateFormat dateTimef = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			try {
				date = dateTimef.parse(dateValue);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"dd MMM yyyy HH:mm:ss");
			// dateText.setText(dateFormat.format(date));
			dateText.setText(caseUpdate.getCaseupdatedate());

			TextView advocateName = (TextView) secondlayout
					.findViewById(R.id.textViewadvocatename);
			if (caseToAdvocate.containsKey(caseUpdate.getCaseUpdateID()))
				advocateName.setText(advocate.getAdvocateName());
			else
				advocateName.setText("-");

			ImageView imageView = (ImageView) layout
					.findViewById(R.id.imageViewcasestatus);

			switch (caseUpdate.getAppStatus()) {
			case 1:
				imageView.setImageResource(R.drawable.ideal);
				break;
			case 2:
				imageView.setImageResource(R.drawable.approved);
				break;
			case 3:
				imageView.setImageResource(R.drawable.rejected);
				break;
			case 4:
				imageView.setImageResource(R.drawable.ignored);
				break;
			case 5:
				imageView.setImageResource(R.drawable.resubmited);
				break;
			default:
				imageView.setImageResource(R.drawable.ideal);
				break;
			}

			return basepage;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		animatedStartActivity(CaseDetails.class, arg2);
	}

	@Override
	protected void onResume() {
		// animateIn this activity
		ActivitySwitcher.animationIn(
				findViewById(R.id.caseupdateslayoutcontainer),
				getWindowManager());
		super.onResume();
	}

	private void animatedStartActivity(Class<?> cls, int position) {
		final Intent intent = new Intent(CaseUpdates.this, cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		intent.putExtra("index", position);
		ActivitySwitcher.animationOut(
				findViewById(R.id.caseupdateslayoutcontainer),
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
		ActivitySwitcher.animationOut(
				findViewById(R.id.caseupdateslayoutcontainer),
				getWindowManager(),
				new ActivitySwitcher.AnimationFinishedListener() {
					@Override
					public void onAnimationFinished() {
						CaseUpdates.super.finish();
						// disable default animation
						overridePendingTransition(0, 0);
					}
				});
	}
}
