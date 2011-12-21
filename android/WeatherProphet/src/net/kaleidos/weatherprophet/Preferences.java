package net.kaleidos.weatherprophet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Preferences {
	int hour;
	int minutes;
	boolean activate;
	boolean notification;
	
	int probRain;
	boolean rainWakeUp;
	int offsetRain;
	boolean rainEmail;
	

	public Preferences(int hour, int minutes, boolean activate,
			boolean notification, int probRain, boolean rainWakeUp,
			int offsetRain, boolean rainEmail) {
		super();
		this.hour = hour;
		this.minutes = minutes;
		this.activate = activate;
		this.notification = notification;
		this.probRain = probRain;
		this.rainWakeUp = rainWakeUp;
		this.offsetRain = offsetRain;
		this.rainEmail = rainEmail;

	}

	public static Preferences createFromJson(JSONObject prefs) {
		JSONArray alarms;
		try {
			alarms = prefs.getJSONArray("alarms");

			JSONObject rain = alarms.getJSONObject(0);
			
			return new Preferences(
					0, //hour - Do not save at server 
					0, //minutes - Do not save at server 
					false, //activate - Do not save at server 
					false, //notification - Do not save at server
					rain.getInt("probability"),
					false, // rainWakeUp - Do not save at server 
					0,//offsetRain - Do not save at server
					rain.getBoolean("notifyEmail"));
		} catch (JSONException e) {
			return null;
		}
	}
}
