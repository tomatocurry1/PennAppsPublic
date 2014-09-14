package com.jonathon_vogel.pennapps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pennapps.R;

public class InGameActivity extends HHActivity implements CreateNdefMessageCallback {
	private ArrayAdapter<PlayerInfo> adapter;
	Button special;
	private NfcAdapter nfcAdapter;

	Intent vibrateIntent;
	Intent gpsIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_in_game);
		((TextView) findViewById(R.id.identity)).setText(Game.getInstance().self.isHunter() ? "Hunter" : "Hunted");
		
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (nfcAdapter == null) {
			Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		nfcAdapter.setNdefPushMessageCallback(this, this);

		vibrateIntent = new Intent(this, VibrateService.class);
		startService(vibrateIntent);

		gpsIntent = new Intent(this, GpsService.class);
		startService(gpsIntent);

		adapter = new ArrayAdapter<PlayerInfo>(this, android.R.layout.simple_list_item_2, android.R.id.text1, Game.getInstance().players) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				PlayerInfo player = Game.getInstance().players.get(position);

				View view = super.getView(position, convertView, parent);
				TextView name = (TextView) view.findViewById(android.R.id.text1);
				TextView status = (TextView) view.findViewById(android.R.id.text2);

				name.setText(player.nickname);
				status.setText(player.hunter ? "Hunter" : "Hider");

				return view;
			}
		};

		super.setAdapter(adapter);

		ListView playerList = (ListView) findViewById(R.id.playerList);
		playerList.setAdapter(adapter);
	}

	@Override
	public NdefMessage createNdefMessage(NfcEvent event) {
		String text = MainActivity.gcmRegistrationId;
		return new NdefMessage(new NdefRecord[] { NdefRecord.createMime("application/vnd.com.example.android.beam", text.getBytes()) });
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
			processIntent(getIntent());
		}
	}

	void processIntent(Intent intent) {
		Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		NdefMessage msg = (NdefMessage) rawMsgs[0]; // record 0 contains the
													// MIME type, record 1 is
													// the AAR, if present
		final String otherID = new String(msg.getRecords()[0].getPayload());
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				try {
					AndroidHttpClient http = AndroidHttpClient.newInstance("Hide and Hunt App");
					List<NameValuePair> queries = new ArrayList<NameValuePair>(1);
					queries.add(new BasicNameValuePair("reg_id1", MainActivity.gcmRegistrationId));
					queries.add(new BasicNameValuePair("reg_id2", otherID));
					HttpPost req = new HttpPost(MainActivity.SERVER + "/tag/" + Game.getInstance().gameID + "?" + URLEncodedUtils.format(queries, "UTF-8"));
					HttpResponse resp = http.execute(req);

					if (resp.getStatusLine().getStatusCode() != 200) {
						throw new IOException("HTTP error");
					}
				} catch (IOException e) {
					e.printStackTrace();
					MainActivity.handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(MainActivity.handler.context, "Couldn't reach server :(", Toast.LENGTH_LONG).show();
						}
					});
				}
				return null;
			}
		}.execute();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopService(vibrateIntent);
		stopService(gpsIntent);
	}
}
