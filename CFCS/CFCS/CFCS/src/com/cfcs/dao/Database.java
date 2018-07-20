package com.cfcs.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.cfcs.main.ApplicationContextProvider;
import com.cfcs.main.Config;
import android.content.ContentValues;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

	private static SQLiteDatabase mDataBase;
	private static Database sInstance = null;
	private static int flag = 0;

	public Database() {
		super(ApplicationContextProvider.getContext(), Config.getDB_NAME(),
				null, 1);

		try {
			createDataBase();
			openDataBase();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Database instance() {
		if (sInstance == null) {
			sInstance = new Database();
		}
		return sInstance;
	}

	public static void openDataBase() {
		// Open the database
		String myPath = Config.getDB_PATH().concat(Config.getDB_NAME());
		if (flag != 1) {
			mDataBase = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
			flag = 1;
		}
	}

	private void createDataBase() {
		boolean dbExist = checkDataBase();
		if (dbExist) {

		} else {
			mDataBase = this.getWritableDatabase();
			createAllTables();
			// copydatabase();
		}
	}

	public static void copydatabase() {
		AssetManager am = ApplicationContextProvider.getContext().getAssets();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					am.open("dump.sql")));
			String mLine = reader.readLine();
			while (mLine != null) {
				mDataBase.execSQL(mLine);
				mLine = reader.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = Config.getDB_PATH().concat(Config.getDB_NAME());
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
			e.getStackTrace();
			// database doesn't exist yet.
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null;
	}

	public void createTable(String sql) {
		mDataBase.execSQL(sql);
	}

	public long insert(String table, ContentValues values) throws SQLException {
		try {
			return mDataBase.insert(table, null, values);
		} catch (Exception e) {
			e.getStackTrace();
			return 0;
		}
	}

	public int delete(String table, String where) throws SQLException {
		return mDataBase.delete(table, where, null);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	public int update(String table, ContentValues values, String where) {
		return mDataBase.update(table, values, where, null);
	}

	public void sqlCommand(String command) {
		mDataBase.execSQL(command);
	}

	public Cursor cursor_command(String query) {
		return mDataBase.rawQuery(query, null);
	}

	public final static String table_adminuser = "adminuser";
	private String[] arr1 = { "userid", "roleid", "name", "mobileno",
			"username", "password", "emailid", "lastupdate", "updatestamp",
			"active" };
	private final String sql_table1 = "Create table " + table_adminuser + "("
			+ arr1[0] + " integer primary key," + arr1[1] + " integer,"
			+ arr1[2] + " TEXT," + arr1[3] + " TEXT," + arr1[4] + " TEXT,"
			+ arr1[5] + " TEXT," + arr1[6] + " TEXT," + arr1[7] + " integer,"
			+ arr1[8] + " integer," + arr1[9] + " TEXT);";

	public final static String table_groupmaster = "groupmaster";
	private String[] arr2 = { "groupid", "groupname", "updatedate",
			"timestamp", "active" };
	private final String sql_table2 = "Create table " + table_groupmaster + "("
			+ arr2[0] + " integer primary key," + arr2[1] + " TEXT," + arr2[2]
			+ " TEXT," + arr2[3] + " integer," + arr2[4] + " TEXT);";

	public final static String table_grouprelation = "grouprelation";
	private String[] arr3 = { "groupid", "memberid" };
	private final String sql_table3 = "Create table " + table_grouprelation
			+ "(" + arr3[0] + " integer," + arr3[1] + " integer);";

	public final static String table_customermaster = "customermaster";
	private String[] arr4 = { "customerid", "customername", "mobileno",
			"updatedate", "updatetimestamp", "active", "activestatus" };
	private final String sql_table4 = "Create table " + table_customermaster
			+ "(" + arr4[0] + " integer primary key," + arr4[1] + " TEXT,"
			+ arr4[2] + " TEXT," + arr4[3] + " TEXT," + arr4[4] + " integer,"
			+ arr4[5] + " TEXT," + arr4[6] + " integer);";

	public final static String table_otheradvocate = "otheradvocate";
	private String[] arr5 = { "advocateid", "title", "advocatename", "mobile",
			"mailid", "type", "updatedate", "timestamp", "active" };
	private final String sql_table5 = "Create table " + table_otheradvocate
			+ "(" + arr5[0] + " integer primary key," + arr5[1] + " TEXT,"
			+ arr5[2] + " TEXT," + arr5[3] + " TEXT," + arr5[4] + " TEXT,"
			+ arr5[5] + " integer," + arr5[6] + " TEXT," + arr5[7]
			+ " integer," + arr5[8] + " TEXT);";

	public final static String table_updatetypemaster = "updatetypemaster";
	private String[] arr6 = { "updatetypeid", "updatetype", "timestamp",
			"active" };
	private final String sql_table6 = "Create table " + table_updatetypemaster
			+ "(" + arr6[0] + " integer primary key," + arr6[1] + " TEXT,"
			+ arr6[2] + " integer," + arr6[3] + " TEXT);";

	public final static String table_casemaster = "casemaster";
	private String[] arr7 = { "caseid", "customerid", "manualcaseid",
			"casetitle", "casedetail", "casestatus", "adddate", "updatedate",
			"updateby", "active" };
	private final String sql_table7 = "Create table " + table_casemaster + "("
			+ arr7[0] + " integer primary key," + arr7[1] + " integer,"
			+ arr7[2] + " TEXT," + arr7[3] + " TEXT," + arr7[4] + " integer,"
			+ arr7[5] + " TEXT," + arr7[6] + " TEXT," + arr7[7] + " integer,"
			+ arr7[8] + " integer," + arr7[9] + " TEXT);";

	public final static String table_caseupdate = "caseupdate";
	private String[] arr8 = { "caseupdateid", "updatetypeid", "customerid",
			"caseid", "advocateid", "courttypeid", "remark", "attachment",
			"workdoneby", "appstatus", "appby", "appdatetime", "adddate",
			"updatedate", "active", "updatestamp", "ischargeable", "amount",
			"advocatefee", "caseupdatedate", "minutespend", "isadvchargeable" };
	private final String sql_table8 = "Create table " + table_caseupdate + "("
			+ arr8[0] + " TEXT," + arr8[1] + " integer," + arr8[2]
			+ " integer," + arr8[3] + " integer," + arr8[4] + " integer,"
			+ arr8[5] + " integer," + arr8[6] + " TEXT," + arr8[7] + " TEXT,"
			+ arr8[8] + " integer," + arr8[9] + " integer," + arr8[10]
			+ " integer," + arr8[11] + " TEXT," + arr8[12] + " TEXT,"
			+ arr8[13] + " TEXT," + arr8[14] + " TEXT," + arr8[15]
			+ " integer," + arr8[16] + " TEXT NOT NULL," + arr8[17] + " TEXT,"
			+ arr8[18] + " TEXT," + arr8[19] + " TEXT," + arr8[20]
			+ " integer," + arr8[21] + " TEXT);";

	public final static String table_reminder = "reminder";
	private String[] arr9 = { "reminderid", "reminderdate", "remindertime",
			"customerid", "caseid", "advocateid", "updatetypeid", "title",
			"details", "status", "addby", "adddate", "updatedate", "active",
			"reminderbefore", "updatestamp", "reminderalarm" };
	private final String sql_table9 = "Create table " + table_reminder + "("
			+ arr9[0] + " TEXT," + arr9[1] + " TEXT," + arr9[2] + " TEXT,"
			+ arr9[3] + " integer," + arr9[4] + " integer," + arr9[5]
			+ " integer," + arr9[6] + " integer," + arr9[7] + " TEXT,"
			+ arr9[8] + " TEXT," + arr9[9] + " integer," + arr9[10]
			+ " integer," + arr9[11] + " TEXT," + arr9[12] + " TEXT,"
			+ arr9[13] + " TEXT," + arr9[14] + " integer," + arr9[15]
			+ " integer," + arr9[16] + " integer);";

	private final static String table_reminderRelation = "reminderRelation";
	private String[] arr12 = { "reminderid", "userid" };
	private final String sql_table12 = "Create table " + table_reminderRelation
			+ "(" + arr12[0] + " TEXT," + arr12[1] + " integer);";

	public final static String table_courttypemaster = "courttypemaster";
	private String[] arr10 = { "courttypeid", "courttype", "updatedate",
			"active" };
	private final String sql_table10 = "Create table " + table_courttypemaster
			+ "(" + arr10[0] + " integer primary key," + arr10[1] + " TEXT,"
			+ arr10[2] + " TEXT," + arr10[3] + " TEXT);";

	public final static String table_deletecaseupdate = "deleteCaseUpdate";
	private final static String[] arr11 = { "caseupdateId" };
	private final String sql_table11 = "Create table " + table_deletecaseupdate
			+ "(" + arr11[0] + " TEXT);";

	public final static String table_deletereminder = "deleteReminder";
	private final static String[] arr13 = { "reminderId" };
	private final String sql_table13 = "Create table " + table_deletereminder
			+ "(" + arr13[0] + " TEXT);";

	private final static String table_reminderIdtoeventId = "remindertoevent";
	private String[] arr14 = { "reminderid", "eventid" };
	private final String sql_table14 = "Create table "
			+ table_reminderIdtoeventId + "(" + arr14[0] + " TEXT," + arr14[1]
			+ " TEXT);";

	private final static String table_reminderAdvocate = "reminderAdvocate";
	private String[] arr15 = { "reminderid", "advocateId" };
	private final String sql_table15 = "Create table " + table_reminderAdvocate
			+ "(" + arr15[0] + " TEXT," + arr15[1] + " integer);";

	public void createAllTables() {
		this.createTable(sql_table1);
		this.createTable(sql_table2);
		this.createTable(sql_table3);
		this.createTable(sql_table4);
		this.createTable(sql_table5);
		this.createTable(sql_table6);
		this.createTable(sql_table7);
		this.createTable(sql_table8);
		this.createTable(sql_table9);
		this.createTable(sql_table10);
		this.createTable(sql_table11);
		this.createTable(sql_table12);
		this.createTable(sql_table13);
		this.createTable(sql_table14);
		this.createTable(sql_table15);
	}

	public void deleteAllTables() {
		this.sqlCommand("drop table " + table_adminuser + " ;");
		this.sqlCommand("drop table " + table_groupmaster + " ;");
		this.sqlCommand("drop table " + table_grouprelation + " ;");
		this.sqlCommand("drop table " + table_customermaster + " ;");
		this.sqlCommand("drop table " + table_otheradvocate + " ;");
		this.sqlCommand("drop table " + table_updatetypemaster + " ;");
		this.sqlCommand("drop table " + table_casemaster + " ;");
		this.sqlCommand("drop table " + table_caseupdate + " ;");
		this.sqlCommand("drop table " + table_reminder + " ;");
		this.sqlCommand("drop table " + table_courttypemaster + " ;");
		this.sqlCommand("drop table " + table_deletecaseupdate + " ;");
		this.sqlCommand("drop table " + table_reminderRelation + " ;");
		this.sqlCommand("drop table " + table_deletereminder + " ;");
		this.sqlCommand("drop table " + table_reminderIdtoeventId + " ;");
		this.sqlCommand("drop table " + table_reminderAdvocate + " ;");
	}

}
