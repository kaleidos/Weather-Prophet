package net.kaleidos.weatherprophet;

import java.util.Iterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

public class WeatherProphetActivity extends Activity {

	String authToken = null;
	PreferencesController preferencesController;

	// ////////////////////////////////////////////////////////
	// /// GRAPHIC ELEMENTS ////////
	// ////////////////////////////////////////////////////////
	TimePicker timePicker = null;
	CheckBox chkActivate = null;
	CheckBox chkNotification = null;

	// Rain
	SeekBar seekRain = null;
	TextView txtRain = null;
	CheckBox chkRainWakeUp = null;
	Spinner spnRainOffset = null;
	CheckBox chkRainEmail = null;

	// //////////////////////////////////////////////////////

	public TimePicker getTimePicker() {
		if (timePicker == null) {
			timePicker = (TimePicker) findViewById(R.id.timeBase);
		}
		return timePicker;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public CheckBox getChkActivate() {
		if (chkActivate == null) {
			chkActivate = (CheckBox) findViewById(R.id.chkActivate);
		}
		return chkActivate;
	}

	public CheckBox getChkNotification() {
		if (chkNotification == null) {
			chkNotification = (CheckBox) findViewById(R.id.chkNotification);
		}
		return chkNotification;
	}

	public SeekBar getSeekRain() {
		if (seekRain == null) {
			seekRain = (SeekBar) findViewById(R.id.seekRain);
		}
		return seekRain;
	}

	public TextView getTxtRain() {
		if (txtRain == null) {
			txtRain = (TextView) findViewById(R.id.txtRain);
		}
		return txtRain;
	}

	public CheckBox getChkRainWakeUp() {
		if (chkRainWakeUp == null) {
			chkRainWakeUp = (CheckBox) findViewById(R.id.chkRainWakeUp);
		}
		return chkRainWakeUp;
	}

	public Spinner getSpnRainOffset() {
		if (spnRainOffset == null) {
			spnRainOffset = (Spinner) findViewById(R.id.spnRainOffset);
		}
		return spnRainOffset;
	}

	public CheckBox getChkRainEmail() {
		if (chkRainEmail == null) {
			chkRainEmail = (CheckBox) findViewById(R.id.chkRainEmail);
		}
		return chkRainEmail;
	}

	public PreferencesController getPreferencesController() {
		if (preferencesController == null) {
			preferencesController = new PreferencesController(this);
		}
		return preferencesController;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// Avoid reset activity at screen rotate
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onResume() {
		super.onResume();
		Preferences preferences = getPreferencesController().loadPreferences();
		if (preferences != null) {
			getTimePicker().setIs24HourView(true);
			getTimePicker().setCurrentHour(preferences.hour);
			getTimePicker().setCurrentMinute(preferences.minutes);

			getChkActivate().setChecked(preferences.activate);
			getChkNotification().setChecked(preferences.notification);

			// Rain
			getSeekRain().setProgress(preferences.probRain);
			getChkRainWakeUp().setChecked(preferences.rainWakeUp);
			getSpnRainOffset().setSelection(preferences.offsetRain);
			getTxtRain().setText(preferences.probRain + "%");
			getChkRainEmail().setChecked(preferences.rainEmail);

			activateWakeUp(null);
		}

	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		preferencesController = new PreferencesController(this);

		ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
				R.array.offsets, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		getSpnRainOffset().setAdapter(adapter);

		getSeekRain().setOnSeekBarChangeListener(
				new SeekBar.OnSeekBarChangeListener() {

					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						getTxtRain().setText(String.valueOf(progress) + "%");
					}

					public void onStartTrackingTouch(SeekBar seekBar) {

					}

					public void onStopTrackingTouch(SeekBar seekBar) {

					}

				});

	}

	public void activateWakeUp(View view) {
		if (getChkActivate().isChecked()) {
			getChkNotification().setEnabled(true);
			getChkRainWakeUp().setEnabled(true);
			getSpnRainOffset().setEnabled(true);
		} else {
			getChkNotification().setChecked(false);
			getChkNotification().setEnabled(false);
			getChkRainWakeUp().setChecked(false);
			getChkRainWakeUp().setEnabled(false);
			getSpnRainOffset().setEnabled(false);
		}
	}

	public void savePreferences(View view) {

		Preferences preferences = new Preferences(getTimePicker()
				.getCurrentHour(), getTimePicker().getCurrentMinute(),
				getChkActivate().isChecked(), getChkNotification().isChecked(),
				getSeekRain().getProgress(), getChkRainWakeUp().isChecked(),
				getSpnRainOffset().getSelectedItemPosition(), getChkRainEmail()
						.isChecked());

		PreferencesController.savePreferences(this, preferences, true, true,
				getAuthToken());

		WeatherUtils.createCancelAlarm(this, this.getChkActivate().isChecked(),
				this.getChkNotification().isChecked());

		finish();
	}

	public boolean logIn() {
		SharedPreferences settings = getSharedPreferences("WEATHERPROPHET",
				MODE_PRIVATE);

		String authToken = getAuthToken();

		if (authToken == null) {
			authToken = settings.getString("authToken", null);
			setAuthToken(authToken);
		}
		// If there is no authToken, show Login Screen
		if (authToken == null) {
			Intent i = new Intent(this, Login.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			return false;
		}
		return true;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.btnSetSound:
			setRingtone();
			break;
		case R.id.btnAbout:
			Intent i = new Intent(this, About.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			break;

		case R.id.btnWipe:

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.wipe_confirm)
					.setCancelable(false)
					.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									WeatherProphetActivity.this.wipe();
								}
							})
					.setNegativeButton(R.string.no,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			builder.create().show();

			break;

		}

		return true;
	}

	public void wipe() {
		SharedPreferences settings = getSharedPreferences("WEATHERPROPHET",
				MODE_PRIVATE);
		Editor editor = settings.edit();
		for (Iterator<String> it = settings.getAll().keySet().iterator(); it
				.hasNext();) {
			String key = it.next();
			editor.remove(key);
		}
		editor.commit();
		setAuthToken(null);

		this.onResume();
	}
	
	
	private void setRingtone() {
		SharedPreferences settings = getSharedPreferences("WEATHERPROPHET",
  				MODE_PRIVATE);
		String sound=settings.getString("sound", "content://settings/system/ringtone");
		Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, R.string.set_sound);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,  Uri.parse(sound));
		this.startActivityForResult(intent, 5);
	}
	
	@Override
	 protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent)
	 {
	     if (resultCode == Activity.RESULT_OK && requestCode == 5)
	     {
	          Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
	          String sound="content://settings/system/ringtone";
	          if (uri!=null){
	        	  sound=uri.toString();
	          }
	          SharedPreferences settings = getSharedPreferences("WEATHERPROPHET",
	  				MODE_PRIVATE);
	  		Editor editor = settings.edit();
	  		editor.putString("sound", sound);
	  		editor.commit();

	      }            
	  }
}