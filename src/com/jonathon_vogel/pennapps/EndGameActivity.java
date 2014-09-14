package com.jonathon_vogel.pennapps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pennapps.R;

public class EndGameActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_end_game);
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				try {
					AndroidHttpClient http = AndroidHttpClient.newInstance("Hide and Hunt App");
					List<NameValuePair> queries = new ArrayList<NameValuePair>(1);
					queries.add(new BasicNameValuePair("reg_id", MainActivity.gcmRegistrationId));
					HttpGet req = new HttpGet(MainActivity.SERVER + "/endgame/" + Game.getInstance().gameID);
					HttpResponse resp = http.execute(req);

					if (resp.getStatusLine().getStatusCode() != 200 || resp.getEntity() == null) {
						throw new IOException("HTTP error! " + resp.getStatusLine().getStatusCode());
					} else {
						String content = IOUtils.toString(resp.getEntity().getContent());
						Log.d("json", content);
						JSONObject jobj = new JSONObject(content);
						if (jobj.getString("status").equals("error")) {
							throw new IOException("Got error from the server :( " + content);
						}
						JSONArray tags = jobj.getJSONArray("tags");
						final List<JSONObject> tagList = new ArrayList<JSONObject>(tags.length());
						for (int i = 0; i < tags.length(); i++) {
							tagList.add(tags.getJSONObject(i));
						}
						
						MainActivity.handler.post(new Runnable() {
							@Override
							public void run() {
								ArrayAdapter<JSONObject> adapter = new ArrayAdapter<JSONObject>(EndGameActivity.this, android.R.layout.simple_list_item_2, android.R.id.text1, tagList) {
									@Override
									public View getView(int position, View convertView, ViewGroup parent) {
										final JSONObject tag = tagList.get(position);

										View view = super.getView(position, convertView, parent);
										TextView hunter = (TextView) view.findViewById(android.R.id.text1);
										TextView hider = (TextView) view.findViewById(android.R.id.text2);

										try {
											hunter.setText(tag.getString("hunter") + " took out");
											hider.setText(tag.getString("hider"));
										} catch (JSONException e) {
											e.printStackTrace();
										}
										
										return view;										
									}
								};
							}
						});						
					}
				} catch (IOException e) {
					e.printStackTrace();
					MainActivity.handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(EndGameActivity.this, "Couldn't reach server :(", Toast.LENGTH_LONG).show();
						}
					});
				} catch (JSONException e) {
					e.printStackTrace();
					MainActivity.handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(EndGameActivity.this, "JSON was malformed", Toast.LENGTH_LONG).show();
						}
					});
				}
				return null;
			}
		}.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.end_game, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
