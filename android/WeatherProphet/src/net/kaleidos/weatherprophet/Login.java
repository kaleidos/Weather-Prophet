package net.kaleidos.weatherprophet;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {
	
	//private static final String URL_LOGIN="http://terra.es";
	TextView txtUserName=null;
	TextView txtPassword=null;
	ProgressBar progressBar=null;
	Button btnSubmit=null;
	
	
	public TextView getTxtUserName() {
		if (txtUserName==null){
			txtUserName = (TextView) findViewById(R.id.txtUserName);
		}
		return txtUserName;
	}

	public TextView getTxtPassword() {
		if (txtPassword==null){
			txtPassword = (TextView) findViewById(R.id.txtPassword);
		}
		return txtPassword;
	}

	public ProgressBar getProgressBar() {
		if (progressBar==null){
			progressBar = (ProgressBar) findViewById(R.id.progressBar);
		}
		return progressBar;
	}

	public Button getBtnSubmit() {
		if (btnSubmit==null){
			btnSubmit = (Button) findViewById(R.id.btnSubmit);
		}
		return btnSubmit;
	}


	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		//Avoid reset activity at screen rotate
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
	}
	
	
	public boolean logIn(View view){
		
		if (WeatherUtils.checkInternetConnection(this)) {
			enableAll(false);
			
			String userName=getTxtUserName().getText().toString();
			String password=getTxtPassword().getText().toString();
			
			
			List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);
			parameters.add(new BasicNameValuePair("j_username", userName));
			parameters.add(new BasicNameValuePair("j_password", password));
			
			
			try {
				JSONObject json=WeatherUtils.readRemoteJson(WeatherUtils.URL_LOGIN,parameters,"POST");
				//{"success":true,"username":"admin","authToken":"abcde"}
				if (json.getBoolean("success")){
					loginSuccess(json);
				}else{
					loginFail();
				}
			} catch (Exception e) {
				Toast.makeText(this, "There was a problem connecting with the server", Toast.LENGTH_LONG).show();
				finish();
			}
		}else{
			Toast.makeText(this, "You don't have internet conexion", Toast.LENGTH_LONG).show();
			finish();
		}
		
		return false;
	}
	
	private void loginSuccess(JSONObject json){
		WeatherUtils.debug(this,"loginSuccess "+json.toString());
		String authToken;
		try {
			authToken = json.getString("authToken");
			SharedPreferences settings = getSharedPreferences("WEATHERPROPHET", MODE_PRIVATE);
			SharedPreferences.Editor editor = settings.edit();

			editor.putString("authToken", authToken);
			editor.commit();
			finish();
		} catch (JSONException e) {
			loginFail();
		}
		
	}
	
	private void loginFail(){	
		enableAll(true);
		Toast.makeText(this, "User/Password fail", Toast.LENGTH_LONG).show();
	}
	
	private void enableAll(boolean enable){
		if (enable){
			getProgressBar().setVisibility(ProgressBar.INVISIBLE);
		}else{
			getProgressBar().setVisibility(ProgressBar.VISIBLE);
		}
		getTxtUserName().setEnabled(enable);
		getTxtPassword().setEnabled(enable);
		getBtnSubmit().setEnabled(enable);
	}
	
}