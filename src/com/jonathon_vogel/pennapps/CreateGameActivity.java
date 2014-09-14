package com.jonathon_vogel.pennapps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pennapps.R;

public class CreateGameActivity extends HHActivity {
	public static Activity self;

	private ArrayAdapter<PlayerInfo> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_game);
		getActionBar().hide();
		self = this;

		String gameID = getIntent().getStringExtra("game_id");
		String nickname = getIntent().getStringExtra("nickname");

		Button startGame = (Button) findViewById(R.id.startGame);
		startGame.setEnabled(false);

		TextView gameCode = (TextView) findViewById(R.id.bigCode);
		gameCode.setText(gameID);

		PlayerInfo self = Game.getInstance().makeSelfPlayer(nickname);
		self.setHunter(true);

		adapter = new ArrayAdapter<PlayerInfo>(this, android.R.layout.simple_list_item_2, android.R.id.text1, Game.getInstance().players) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				final PlayerInfo player = Game.getInstance().players.get(position);

				View view = super.getView(position, convertView, parent);
				TextView name = (TextView) view.findViewById(android.R.id.text1);
				TextView status = (TextView) view.findViewById(android.R.id.text2);

				name.setText(player.nickname);
				if (player.isSelf) {
					status.setText("You're the boss and always ready");
				} else {
					if (player.ready) {
						status.setText("Ready! (Tap to kick)");
					} else {
						status.setText("Not ready... (Tap to kick)");
					}

					view.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							new AsyncTask<Void, Void, Void>() {
								IOException exc;

								@Override
								protected Void doInBackground(Void... params) {
									try {
										AndroidHttpClient http = AndroidHttpClient.newInstance("Hide and Hunt App");
										List<NameValuePair> queries = new ArrayList<NameValuePair>(1);
										queries.add(new BasicNameValuePair("reg_id", MainActivity.gcmRegistrationId));
										HttpPost req = new HttpPost(MainActivity.SERVER + "/ready/" + Game.getInstance().gameID + "/" + player.regID + "?"
												+ URLEncodedUtils.format(queries, "UTF-8"));
										HttpResponse resp = http.execute(req);

										if (resp.getStatusLine().getStatusCode() != 200) {
											throw new IOException("HTTP error");
										}
									} catch (IOException e) {
										exc = e;
									}
									return null;
								}

								protected void onPostExecute(Void result) {
									if (exc != null) {
										exc.printStackTrace();
										Toast.makeText(MainActivity.handler.context, "Couldn't reach server :(", Toast.LENGTH_LONG).show();

									}
								};
							}.execute();
						}

					});
				}

				return view;
			}
		};

		super.setAdapter(adapter);

		ListView playerList = (ListView) findViewById(R.id.playerList);
		playerList.setAdapter(adapter);
	}

	public void startGame(View v) {
		new AsyncTask<Void, Void, Void>() {
			boolean failure;
			
			@Override
			protected Void doInBackground(Void... params) {
				try {
					AndroidHttpClient http = AndroidHttpClient.newInstance("Hide and Hunt App");
					List<NameValuePair> queries = new ArrayList<NameValuePair>(1);
					queries.add(new BasicNameValuePair("reg_id", MainActivity.gcmRegistrationId));
					HttpPost req = new HttpPost(MainActivity.SERVER + "/start/" + Game.getInstance().gameID + "?" + URLEncodedUtils.format(queries, "UTF-8"));
					HttpResponse resp = http.execute(req);

					if (resp.getStatusLine().getStatusCode() != 200) {
						failure = true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
			
			protected void onPostExecute(Void result) {
				if (failure) {
					Toast.makeText(CreateGameActivity.this, "Couldn't start game! :(", Toast.LENGTH_LONG).show();
				}
			};
		}.execute();
	}
}
