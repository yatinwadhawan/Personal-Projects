package com.cfcs.calendar;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.cfcs.classes.DeleteReminder;
import com.cfcs.classes.Reminder;
import com.cfcs.classes.ReminderAdvocate;
import com.cfcs.classes.ReminderIDToEventID;
import com.cfcs.classes.ReminderRelation;
import com.cfcs.dao.DeleteReminderDao;
import com.cfcs.dao.ReminderAdvocateDao;
import com.cfcs.dao.ReminderDao;
import com.cfcs.dao.ReminderIdToEventIdDao;
import com.cfcs.dao.ReminderRelationDao;
import com.cfcs.main.Config;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ReminderFunctinalityClass {

	public void sendReminder(final Context context) {
		AsyncHttpClient client = Config.getClient();
		ReminderDao dao = new ReminderDao();
		ReminderRelationDao relationDao = new ReminderRelationDao();
		JSONArray array = new JSONArray();
		JSONObject addBothObj = new JSONObject();
		JSONObject finalJson = new JSONObject();
		JSONArray reminderrelationArray = new JSONArray();
		JSONArray reminderAdvocateArray = new JSONArray();
		String reminderid = "";
		final List<Reminder> ls = dao
				.getTablesValues("select * from reminder where updatestamp=0");
		try {
			if (ls != null && !ls.isEmpty()) {

				reminderid = "'" + ls.get(0).getReminderID() + "'";
				Gson gson = new Gson();
				String json = gson.toJson(ls.get(0));
				JSONObject objfirst = new JSONObject(json);
				array.put(objfirst);

				for (int i = 1; i < ls.size(); i++) {
					reminderid = reminderid + ",'" + ls.get(i).getReminderID()
							+ "'";
					Gson gson1 = new Gson();
					String json1 = gson1.toJson(ls.get(i));
					JSONObject objfirst1 = new JSONObject(json1);
					array.put(objfirst1);
				}
				addBothObj.put("ReminderMaster", array);

				List<ReminderRelation> lsrelation = relationDao
						.getTablesValues("select * from reminderRelation where reminderid in ("
								+ reminderid + ");");
				if (lsrelation != null && !lsrelation.isEmpty()) {
					for (int i = 0; i < lsrelation.size(); i++) {
						JSONObject obj2 = new JSONObject();
						obj2.put("ReminderID", lsrelation.get(i)
								.getReminderId());
						obj2.put("UserID", lsrelation.get(i).getUserId());
						reminderrelationArray.put(obj2);
					}
				}
				addBothObj.put("ReminderRelation", reminderrelationArray);

				ReminderAdvocateDao advocatedao = new ReminderAdvocateDao();
				List<ReminderAdvocate> lsreminderadvocate = new ArrayList<ReminderAdvocate>();
				lsreminderadvocate = advocatedao
						.getTablesValues("select * from reminderAdvocate where reminderid in ("
								+ reminderid + ");");
				if (lsreminderadvocate != null && !lsreminderadvocate.isEmpty()) {
					for (int i = 0; i < lsreminderadvocate.size(); i++) {
						JSONObject obj3 = new JSONObject();
						obj3.put("ReminderID", lsreminderadvocate.get(i)
								.getReminderId());
						obj3.put("AdvocateID", lsreminderadvocate.get(i)
								.getAdvocateId());
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
								context);

						ReminderDao dao = new ReminderDao();
						for (int i = 0; i < ls.size(); i++) {
							Reminder r = ls.get(i);
							r.setUpdateStamp(1);
							dao.update(r);
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						Config.toastShow("Not sent. Saved to Database.",
								context);
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void deleteReminderFromServerTakenFromDatabase(final Context context) {
		try {
			AsyncHttpClient client = Config.getClient();
			DeleteReminderDao dao = new DeleteReminderDao();
			List<DeleteReminder> ls = dao.index();
			JSONArray array = new JSONArray();
			if (ls != null && !ls.isEmpty()) {
				for (int i = 0; i < ls.size(); i++) {
					JSONObject obj = new JSONObject();
					obj.put("ReminderID", ls.get(i).getReminderID());
					array.put(obj);
				}

				JSONObject addObj = new JSONObject();
				addObj.put("ReminderMaster", array);
				JSONObject finalJson = new JSONObject();
				finalJson.put("SyncData", addObj);

				RequestParams params = new RequestParams();
				params.put("DataJson", finalJson.toString());
				params.put("UserName", Config.username);
				params.put("Password", Config.password);
				String url = Config.BASE_URL + "ResponseReminderDelete";
				client.post(url, params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						Config.toastShow("All Reminders Deleted From Server.",
								context);
						// CHANGE
						DeleteReminderDao dao = new DeleteReminderDao();
						ReminderAdvocateDao reminderAdvocateDao = new ReminderAdvocateDao();
						ReminderRelationDao reminderrelationDao = new ReminderRelationDao();
						List<DeleteReminder> ls = dao.index();
						if (ls != null && !ls.isEmpty()) {
							for (int i = 0; i < ls.size(); i++) {
								dao.delete(ls.get(i));
								reminderAdvocateDao.delete(ls.get(i)
										.getReminderID());
								reminderrelationDao.delete(ls.get(i)
										.getReminderID());
							}
						}

					}

					@Override
					public void onFailure(Throwable error, String content) {
						Config.toastShow("Not Deleted from Server.", context);

					}
				});

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteReminderFromServer(final String reminderId,
			final Context context) {
		try {
			AsyncHttpClient client = Config.getClient();
			String url = Config.BASE_URL + "ResponseReminderDelete";
			JSONObject obj = new JSONObject();
			obj.put("ReminderID", reminderId);
			JSONArray array = new JSONArray();
			array.put(obj);
			JSONObject addObj = new JSONObject();
			addObj.put("ReminderMaster", array);
			JSONObject finalJson = new JSONObject();
			finalJson.put("SyncData", addObj);

			RequestParams params = new RequestParams();
			params.put("DataJson", finalJson.toString());
			params.put("UserName", Config.username);
			params.put("Password", Config.password);
			client.post(url, params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String response) {
					Config.toastShow("Reminder Deleted From Server.", context);
				}

				@Override
				public void onFailure(Throwable error, String content) {
					Config.toastShow("Not Deleted from Server.", context);

					DeleteReminder deleteremimnder = new DeleteReminder();
					DeleteReminderDao dao = new DeleteReminderDao();
					deleteremimnder.setReminderID(reminderId);
					dao.insert(deleteremimnder);
				}
			});
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static long pushAppointmentsToCalender(Context curActivity,
			String title, String addInfo, String place, int status,
			long startDate, int reminderbefore, boolean needReminder,
			boolean needMailService) {
		/***************** Event: note(without alert) *******************/

		String eventUriString = "content://com.android.calendar/events";
		ContentValues eventValues = new ContentValues();

		eventValues.put("calendar_id", 1); // id, We need to choose from
											// our mobile for primary
											// its 1
		eventValues.put("title", title);
		eventValues.put("description", addInfo);
		eventValues.put("eventLocation", place);
		eventValues.put("eventTimezone", TimeZone.getDefault().getID());
		long endDate = startDate + 1000 * 60 * 60; // For next 1hr

		eventValues.put("dtstart", startDate);
		eventValues.put("dtend", endDate);

		// values.put("allDay", 1); //If it is bithday alarm or such
		// kind (which should remind me for whole day) 0 for false, 1
		// for true
		eventValues.put("eventStatus", status); // This information is
		// sufficient for most
		// entries tentative (0),
		// confirmed (1) or canceled
		// (2):
		eventValues.put("visibility", 3); // visibility to default (0),
											// confidential (1), private
											// (2), or public (3):
		eventValues.put("transparency", 0); // You can control whether
											// an event consumes time
											// opaque (0) or transparent
											// (1).
		eventValues.put("hasAlarm", 1); // 0 for false, 1 for true

		Uri eventUri = curActivity.getApplicationContext().getContentResolver()
				.insert(Uri.parse(eventUriString), eventValues);
		long eventID = Long.parseLong(eventUri.getLastPathSegment());

		if (needReminder) {
			/***************** Event: Reminder(with alert) Adding reminder to event *******************/

			String reminderUriString = "content://com.android.calendar/reminders";

			ContentValues reminderValues = new ContentValues();

			reminderValues.put("event_id", eventID);
			reminderValues.put("minutes", reminderbefore); // Default value of
															// the
			// system. Minutes is a
			// integer
			reminderValues.put("method", 1); // Alert Methods: Default(0),
												// Alert(1), Email(2),
												// SMS(3)
			Uri reminderUri = curActivity.getApplicationContext()
					.getContentResolver()
					.insert(Uri.parse(reminderUriString), reminderValues);
		}

		/***************** Event: Meeting(without alert) Adding Attendies to the meeting *******************/

		if (needMailService) {
			String attendeuesesUriString = "content://com.android.calendar/attendees";

			/********
			 * To add multiple attendees need to insert ContentValues multiple
			 * times
			 ***********/
			ContentValues attendeesValues = new ContentValues();

			attendeesValues.put("event_id", eventID);
			attendeesValues.put("attendeeName", "xxxxx"); // Attendees name
			attendeesValues.put("attendeeEmail", "yyyy@gmail.com");// Attendee
																	// E
																	// mail
																	// id
			attendeesValues.put("attendeeRelationship", 0); // Relationship_Attendee(1),
															// Relationship_None(0),
															// Organizer(2),
															// Performer(3),
															// Speaker(4)
			attendeesValues.put("attendeeType", 0); // None(0), Optional(1),
													// Required(2), Resource(3)
			attendeesValues.put("attendeeStatus", 0); // NOne(0), Accepted(1),
														// Decline(2),
														// Invited(3),
														// Tentative(4)

			Uri attendeuesesUri = curActivity.getApplicationContext()
					.getContentResolver()
					.insert(Uri.parse(attendeuesesUriString), attendeesValues);
		}
		return eventID;
	}

	public static void insertReminderIdToEventId(Context curActivity,
			String reminderId, long eventId, boolean flag) {
		String eventUriString = "content://com.android.calendar/events";

		ReminderIdToEventIdDao dao = new ReminderIdToEventIdDao();
		ReminderIDToEventID remindertoevent = new ReminderIDToEventID();
		remindertoevent.setReminderId(reminderId);

		if (eventId > 0)
			remindertoevent.setEventId(Long.toString(eventId));
		else {
			List<ReminderIDToEventID> ls = dao
					.getTablesValues("select * from remindertoevent where reminderid='"
							+ reminderId + "'");
			if (ls != null && !ls.isEmpty()) {
				eventId = Long.parseLong(ls.get(0).getEventId());
			}
		}

		if (dao.status_database(remindertoevent)) {
			dao.delete(remindertoevent);
			Uri eventsUri = Uri.parse(eventUriString);
			Uri eventUri = Uri.withAppendedPath(eventsUri,
					Long.toString(eventId));
			int rowsDeleted = curActivity.getContentResolver().delete(eventUri,
					null, null);
			Log.e("Del", Integer.toBinaryString(rowsDeleted));
		}

		if (flag) {
			dao.insert(remindertoevent);
		}
	}
}
