package net.kaleidos.weatherprophet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.widget.Toast;

public abstract class WeatherUtils {
	private static final boolean DEBUG_ENABLED=false;
	private static final int SIMPLE_NOTFICATION_ID = 63457;
	public static final String SERVER = "http://weatherprophet.kaleidos.net";
	public static final String URL_LOGIN = SERVER+"/j_spring_security_check";
	public static final String URL_ALARMS = SERVER+"/api/user/alarms";
	public static final String URL_ALARMS_UPDATE = SERVER+"/api/user/alarms/update";
	public static final String URL_ALARMS_ACTIVATED = SERVER+"/api/user/alarms/activated";

	public static int[] splitTimeFromNow(long millis) {
		long now = System.currentTimeMillis();
		long delta = millis - now;

		int hours = Math.round(delta / 3600000l);
		delta = delta % 3600000l;

		int minutes = Math.round(delta / 60000l);
		delta = delta % 60000l;

		int seconds = Math.round(delta / 1000);

		int[] result = { hours, minutes, seconds };

		return result;
	}


	public static JSONObject readRemoteJson(String urlString,
			List<NameValuePair> parameters, String method) throws IOException,
			JSONException, URISyntaxException {

		String data;
		if ("POST".equals(method)) {
			data = postData(urlString, parameters);
		} else {
			data = getData(urlString, parameters);
		}

		JSONObject jsonObject = new JSONObject(data);
		return jsonObject;

	}

	public static String postData(String url, List<NameValuePair> nameValuePairs)
			throws ClientProtocolException, IOException {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		httppost.setHeader("X-Requested-With", "XMLHttpRequest");

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		// Execute HTTP Post Request
		HttpResponse httpResponse = httpclient.execute(httppost);
		HttpEntity entity = httpResponse.getEntity();
		StringBuffer response = new StringBuffer();
		// Read all the text returned by the server
		if (entity != null) {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					entity.getContent()));
			String str;
			while ((str = in.readLine()) != null) {
				// str is one line of text; readLine() strips the newline
				// character(s)
				response.append(str);
			}
			in.close();
		}

		return response.toString();
	}

	public static String getData(String url, List<NameValuePair> nameValuePairs)
			throws ClientProtocolException, IOException, URISyntaxException {
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet();

		boolean firstParameter = true;
		for (Iterator<NameValuePair> it = nameValuePairs.iterator(); it
				.hasNext();) {
			NameValuePair nvp = it.next();
			if (firstParameter) {
				url += "?";
				firstParameter = false;
			} else {
				url += "&";
			}
			url += nvp.getName();
			url += "=";
			url += nvp.getValue();
		}

		request.setURI(new URI(url));
		HttpResponse response = client.execute(request);
		BufferedReader in = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		StringBuffer sb = new StringBuffer("");
		String str = "";
		while ((str = in.readLine()) != null) {
			sb.append(str);
		}
		in.close();

		return sb.toString();
	}

	/*
	 * If create==True Create an alarm every hour to call WeatherCheckService
	 * through WeatherCheckReceiver, and call WeatherCheckService now else
	 * Delete hourly invocation to WeatherCheckService through
	 * WeatherCheckReceiver. Delete invocation to WakeUpReceiver
	 */
	public static void createCancelAlarm(Context context, boolean create,
			boolean notify) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		// Cancel any previous wake up alarms
		Intent intent = new Intent(context, WakeUpReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(
				context.getApplicationContext(), 1, intent, 0);

		am.cancel(sender);

		// Weather alarm
		intent = new Intent(context, WeatherCheckReceiver.class);
		sender = PendingIntent.getBroadcast(context, 0, intent, 0);

		// Cancel any previous weather alarms
		am.cancel(sender);

		// Create new alarms
		if (create) {
			// Check every hour
			am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
					SystemClock.elapsedRealtime(), AlarmManager.INTERVAL_HOUR,
					sender);

			// Check now
			 Intent weatherCheckService = new Intent( context,
			 WeatherCheckService.class );
			 context.startService( weatherCheckService );

			if (notify) {
				WeatherUtils.showNotificationIcon(context, true);
			}
		}

		if (!notify) {
			WeatherUtils.showNotificationIcon(context, false);
		}
	}

	private static void showNotificationIcon(Context context, boolean activate) {
		String ns = WeatherProphetActivity.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(ns);
		mNotificationManager.cancel(SIMPLE_NOTFICATION_ID);

		if (activate) {
			int icon = R.drawable.ic_weather_prophet;
			CharSequence tickerText = "Weather Prophet";
			long when = System.currentTimeMillis();

			Notification notification = new Notification(icon, tickerText, when);
			notification.flags |= Notification.FLAG_NO_CLEAR
					| Notification.FLAG_ONGOING_EVENT;

			CharSequence contentTitle = "Weather Prophet";
			CharSequence contentText = "Click to change your preferences";
			Intent notificationIntent = new Intent(context,
					WeatherProphetActivity.class);
			PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
					notificationIntent, 0);
			notification.setLatestEventInfo(context, contentTitle, contentText,
					contentIntent);

			mNotificationManager.notify(SIMPLE_NOTFICATION_ID, notification);
		}

	}

	public static void debug(Context context, String str) {
		if (DEBUG_ENABLED){
			Toast.makeText(context, "DEBUG: " + str, Toast.LENGTH_SHORT).show();
		}
	}

	//Are we conected to the net?
	public static boolean checkInternetConnection(Context context) {
		ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (conMgr.getActiveNetworkInfo() != null
				&& conMgr.getActiveNetworkInfo().isAvailable()
				&& conMgr.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			return false;

		}
	}
	
	
	public static void setAlarm(Context context, long timeMillis, Class receiver){
		Intent intent = new Intent(context, receiver);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				context.getApplicationContext(), 1, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		//Cancel any previous alarms
		alarmManager.cancel(pendingIntent);
		
		//Create new alarms
		alarmManager.set(AlarmManager.RTC_WAKEUP, timeMillis, pendingIntent);
	}
	
	public static Calendar getAlarmCalendar(SharedPreferences settings){
		Calendar now=Calendar.getInstance();
		int hour = settings.getInt("baseHour", 0);
		int minute = settings.getInt("baseMinute", 0);
		
		Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        
        
        // If past time, set to tomorrow
        if (now.after(calendar)){
        	calendar.add(Calendar.DAY_OF_YEAR, 1);	        	
        }
        
        
        return calendar;
	}

}