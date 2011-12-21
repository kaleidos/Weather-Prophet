package net.kaleidos.weatherprophet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.preference.PreferenceManager;

public class WeatherCheckService extends Service{

	private boolean raining;
	private int probRainToday;
	private String prettyDay;
	
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	private void checkWeatherProphetWeb(String day){
		SharedPreferences settings = getSharedPreferences("WEATHERPROPHET", MODE_PRIVATE);
		raining=settings.getBoolean("isRaining", false);
		
		if (WeatherUtils.checkInternetConnection(this)){
			List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);
			parameters.add(new BasicNameValuePair("authToken", settings.getString("authToken", "0")));
			parameters.add(new BasicNameValuePair("day", day));
			try {

				JSONObject json = WeatherUtils.readRemoteJson(
						WeatherUtils.URL_ALARMS_ACTIVATED, parameters, "GET");
				if (json.getBoolean("success")) {
					raining=json.getBoolean("rainAlarmActivated");
					prettyDay=json.getString("prettyDay");
					probRainToday=json.getInt("probRainToday");
					Editor editor=settings.edit();
					editor.putString("prettyDay", prettyDay);
					editor.putInt("probRainToday", probRainToday);
					editor.commit();
				} else {
					//Do nothing
				}

			} catch (Exception e) {
				//Do nothing
			}
		}
		
		
		
	}
	
	/**
	 * If updatedPreferences is false, try to save the preferences to the server
	 * @param settings
	 */
	private SharedPreferences saveOrReadPreferences(){		
		SharedPreferences settings = getSharedPreferences("WEATHERPROPHET", MODE_PRIVATE);
		if (!settings.getBoolean("updatedPreferences", true)) {
			if (PreferencesController.savePreferencesRemote(this,settings.getString("authToken", "0"))){
				Editor editor=settings.edit();
				editor.putBoolean("updatedPreferences", true);
				editor.commit();
			}
		}else{
			PreferencesController.readPreferencesRemote(this,settings.getString("authToken", "0"));
			settings = getSharedPreferences("WEATHERPROPHET", MODE_PRIVATE);
		}
		return settings;
	}
	
	
	@Override
	public void onStart(Intent intent, int startid) {
		//Get preferences
		WeatherUtils.debug(this, "WeatherCheckService");
		
		SharedPreferences settings = saveOrReadPreferences();
		
		boolean activate=settings.getBoolean("activate", false);
		//If the alarms are active
		if (activate){
			
			Calendar calendar=WeatherUtils.getAlarmCalendar(settings);
			Calendar now=Calendar.getInstance();
			
			
			long lastAlarmSound=settings.getLong("lastAlarmSound", -1);
			//If this alarm hasn't sound yet
			if (lastAlarmSound!=calendar.getTimeInMillis()){
		        String day="today";
		        if (now.get(Calendar.DAY_OF_MONTH)!=calendar.get(Calendar.DAY_OF_MONTH)){
		        	day="tomorrow";
		        }
		        
		        checkWeatherProphetWeb(day);
		        
		        
		        
		        int offset=0;
		        if (settings.getBoolean("rainWakeUp", false) && raining){
		        	//offset+1 is the number of hour quarters
		        	offset=(settings.getInt("offsetRain", 0)+1)*15;
		        }
				
				//Check API for conditions
				if (offset>0){
					//change wake up time
					Calendar calendarOffset=Calendar.getInstance();
					calendarOffset.setTimeInMillis(calendar.getTimeInMillis());
									
					calendarOffset.add(Calendar.MINUTE,-offset);
					
					//If calendar was in future, but calendarOffset in past, set alarm for now!!! (or in 5 seconds)
					if ((now.before(calendar)&&(now.after(calendarOffset)))){
						calendar.setTimeInMillis(now.getTimeInMillis());
						calendar.add(Calendar.SECOND, 5);
					}else{
						calendar=calendarOffset;
					}
				}
				
				//Set wakeupcall
				WeatherUtils.setAlarm(this, calendar.getTimeInMillis(), WakeUpReceiver.class);
			}
		}else{
			//We have received a call, but the alarms should be deactivated
			WeatherUtils.createCancelAlarm(this,false,false);
		}
	}
	
}
