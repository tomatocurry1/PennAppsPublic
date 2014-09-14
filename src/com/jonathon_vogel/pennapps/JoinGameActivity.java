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

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pennapps.R;

public class JoinGameActivity extends HHActivity {
	public static final String SERVER = "http://66.228.36.36:44207";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.activity_join_game);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

	}

	public void joinButtonClick(View view) {
		final EditText gameCode = (EditText) findViewById(R.id.gameCode);
		final EditText nickname = (EditText) findViewById(R.id.nickname);
		Game.getInstance().gameID = gameCode.getText().toString();
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				Looper.prepare();
				try {
					AndroidHttpClient http = AndroidHttpClient.newInstance("Hide and Hunt App");
					List<NameValuePair> queries = new ArrayList<NameValuePair>(2);
					queries.add(new BasicNameValuePair("reg_id", MainActivity.gcmRegistrationId));
					queries.add(new BasicNameValuePair("nickname", nickname.getText().toString()));
					HttpPost req = new HttpPost(SERVER + "/join/" + gameCode.getText().toString() + "?" + URLEncodedUtils.format(queries, "UTF-8"));
					HttpResponse resp = http.execute(req);
					if (resp.getStatusLine().getStatusCode() != 200) {
						String msg = "Server error occurred (" + resp.getStatusLine().getStatusCode() + ")";
						if (resp.getEntity() != null) {
							msg = IOUtils.toString(resp.getEntity().getContent());
						}
						Toast.makeText(JoinGameActivity.this, msg, Toast.LENGTH_LONG).show();
					} else {
						MainActivity.handler.post(new Runnable() {
							@Override
							public void run() {
								Game.getInstance().makeSelfPlayer(nickname.getText().toString());
								Intent intent = new Intent(JoinGameActivity.this, LobbyActivity.class);
								startActivity(intent);
							}
						});
					}
				} catch (IOException e) {
					MainActivity.handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(JoinGameActivity.this, "Couldn't reach server :(", Toast.LENGTH_LONG).show();
						}
					});
				}
				return null;
			}
		}.execute();
	}

}
