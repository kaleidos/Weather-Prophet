package net.kaleidos.weatherprophet;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

public class About extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		//Avoid reset activity at screen rotate
		super.onConfigurationChanged(newConfig);
	}
	
	
	public void close(View view){
		finish();
	}
	
}