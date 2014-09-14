package com.jonathon_vogel.pennapps;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pennapps.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class MainActivity extends HHActivity {
	static final String PROPERTY_REG_ID = "registration_id";
	static final String PROPERTY_APP_VERSION = "app_version";
	static final int APP_VERSION = 0;
	static final String SENDER_ID = "472922563262";
	public static final String SERVER = "http://66.228.36.36:44207";
	
	public class MainActivityHandler extends Handler {
		public Context context;
		
		public MainActivityHandler() {
			super();
			this.context = MainActivity.this;
		}
	}
	
	public static MainActivityHandler handler;

	GoogleCloudMessaging gcm;
	static String gcmRegistrationId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		Game.initialize();
		
		handler = new MainActivityHandler();
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		if (!checkPlayServices()) {
			Toast.makeText(MainActivity.this, "Please get a valid Play Services APK / update! Sorry :(", Toast.LENGTH_LONG).show();
			finish();
		}

		gcm = GoogleCloudMessaging.getInstance(this);
		gcmRegistrationId = getRegId(this);
		if (gcmRegistrationId.isEmpty()) {
			Toast.makeText(this, "Waiting for Google Cloud Messaging Servers...", Toast.LENGTH_LONG).show();
			Button create = (Button) findViewById(R.id.createGame);
			Button join = (Button) findViewById(R.id.joinGame);
			create.setEnabled(false);
			join.setEnabled(false);
			registerInBackground();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Game.getInstance().players.clear();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

	public void createGameClick(View v) {
		new AsyncTask<Void, Void, Void>() {
			IOException exc;
			JSONObject serverData;
			
			@Override
			protected Void doInBackground(Void... params) {
				try {
					AndroidHttpClient http = AndroidHttpClient.newInstance("Hide and Hunt App");
					List<NameValuePair> queries = new ArrayList<NameValuePair>(1);
					queries.add(new BasicNameValuePair("reg_id", gcmRegistrationId));
					HttpPost req = new HttpPost(SERVER + "/create" + "?" + URLEncodedUtils.format(queries, "UTF-8"));
					HttpResponse resp = http.execute(req);
					if (resp.getStatusLine().getStatusCode() != 200 || resp.getEntity() == null) {
						throw new IOException("HTTP error! " + resp.getStatusLine().getStatusCode());
					}
					String content = IOUtils.toString(resp.getEntity().getContent());
					serverData = new JSONObject(content);
				} catch (IOException e) {
					exc = e;
				} catch (JSONException e) {}
				return null;
			}
			
			protected void onPostExecute(Void result) {
				try {
					if (serverData != null && serverData.getString("status").equals("success")) {
						Intent intent = new Intent(MainActivity.this, CreateGameActivity.class);
						intent.putExtra("game_id", serverData.getString("game_id"));
						intent.putExtra("nickname", serverData.getString("nickname"));
						startActivity(intent);
					} else {
						Toast.makeText(MainActivity.this, "Couldn't reach server :( " + exc.getMessage(), Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(MainActivity.this, "Crazy JSON over here tho", Toast.LENGTH_LONG).show();
				}
			};
		}.execute();
	}

	public void joinGameClick(View v) {
		Intent intent = new Intent(this, JoinGameActivity.class);
		startActivity(intent);
	}

	private boolean checkPlayServices() {
		int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (result != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(result)) {
				GooglePlayServicesUtil.getErrorDialog(result, this, 9000).show(); // 9000
																					// =
																					// PLAY_SERVICES_RESOLUTION_REQUEST
			} else {
				Toast.makeText(MainActivity.this, "Can't find google play services or recover, aborting! " + result, Toast.LENGTH_LONG).show();
				finish();
			}
			return false;
		}
		return true;
	}

	private String getRegId(Context context) {
		SharedPreferences prefs = getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
		String regId = prefs.getString(PROPERTY_REG_ID, "");
		if (regId.isEmpty()) {
			return "";
		}
		int regVersion = prefs.getInt(PROPERTY_APP_VERSION, -1);
		if (regVersion != APP_VERSION) {
			return "";
		}
		return regId;
	}

	private void registerInBackground() {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				try {
					gcmRegistrationId = gcm.register(SENDER_ID);
					SharedPreferences prefs = getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString(PROPERTY_REG_ID, gcmRegistrationId);
					editor.putInt(PROPERTY_APP_VERSION, APP_VERSION);
					editor.commit();
				} catch (IOException e) {
					finish();
				}
				return null;
			}
			
			protected void onPostExecute(Void result) {
				Button create = (Button) findViewById(R.id.createGame);
				Button join = (Button) findViewById(R.id.joinGame);
				create.setEnabled(true);
				join.setEnabled(true);
			};
		}.execute();
	}
}
