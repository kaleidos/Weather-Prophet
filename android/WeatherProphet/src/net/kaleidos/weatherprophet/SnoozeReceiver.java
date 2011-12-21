package net.kaleidos.weatherprophet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SnoozeReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
    	 Intent i = new Intent(context, ShowAlarm.class);  
         i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
         context.startActivity(i);  
    }
}
