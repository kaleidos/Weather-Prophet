package net.kaleidos.weatherprophet;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

public class BootService extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		super.onStart(intent, startid);
		SharedPreferences settings = getSharedPreferences("WEATHERPROPHET", MODE_PRIVATE);
		WeatherUtils.createCancelAlarm(this, settings.getBoolean("activate", false), settings.getBoolean("notification", false));
	}
}
