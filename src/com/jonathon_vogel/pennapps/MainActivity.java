package com.jonathon_vogel.pennapps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pennapps.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class MainActivity extends Activity {
	static final String PROPERTY_REG_ID = "registration_id";
	static final String PROPERTY_APP_VERSION = "app_version";
	static final int APP_VERSION = 0;
	static final String SENDER_ID = "472922563262";
	public static final String SERVER = "http://66.228.36.36:44207";

	public static Handler handler;

	GoogleCloudMessaging gcm;
	String gcmRegistrationId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		handler = new Handler();
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
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

	public void createGameClick(View v) {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				try {
					HttpClient http = HttpClients.createDefault();
					HttpPost req = new HttpPost(SERVER + "/create");
					List<NameValuePair> queries = new ArrayList<NameValuePair>(1);
					queries.add(new BasicNameValuePair("reg_id", gcmRegistrationId));
					req.setEntity(new UrlEncodedFormEntity(queries));
					HttpResponse resp = http.execute(req);
					if (resp.getEntity() != null) {
						//String content = IOUtils.toString(resp.getEntity().getContent());
					}
				} catch (IOException e) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(MainActivity.this, "Couldn't reach server :(", Toast.LENGTH_LONG).show();
						}
					});
				}
				return (Void) null;
			}
		};
		Intent intent = new Intent(this, CreateGameActivity.class);
		startActivity(intent);
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
					handler.post(new Runnable() {
						@Override
						public void run() {
							Button create = (Button) findViewById(R.id.createGame);
							Button join = (Button) findViewById(R.id.joinGame);
							create.setEnabled(true);
							join.setEnabled(true);
						}
					});
				} catch (IOException e) {
					finish();
				}
				return (Void) null;
			}
		}.execute();
	}
}
