package net.kaleidos.weatherprophet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WeatherCheckReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
    	 Intent weatherCheckService = new Intent( context, WeatherCheckService.class );
    	 context.startService( weatherCheckService );
    }
}
