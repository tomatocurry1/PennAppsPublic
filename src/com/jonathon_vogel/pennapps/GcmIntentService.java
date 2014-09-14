package com.jonathon_vogel.pennapps;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.example.pennapps.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmIntentService extends IntentService {
	public static BaseAdapter adapter;

	public GcmIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		final Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);

		Log.d("gcm", "Got GCM message: " + messageType + ", " + extras.toString());
		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
				Log.e("gcm", "send error");
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType) && extras != null) {
				String type = extras.getString("event");
				if (type.equals("player_join")) {
					Game.getInstance().players.add(new PlayerInfo(extras.getString("reg_id"), extras.getString("nickname"), false, false));
				} else if (type.equals("player_kick")) {
					Game.getInstance().players.remove(Game.getInstance().getPlayerById(extras.getString("reg_id")));
				} else if (type.equals("player_ready")) {
					Game.getInstance().getPlayerById(extras.getString("reg_id")).ready = true;
				} else if (type.equals("game_start")) {
					Game.getInstance().gameStarted = true;
				} else if (type.equals("player_tagged")) {
					Game.getInstance().getPlayerById(extras.getString("reg_id")).hunter = true;
				} else if (type.equals("all_players_ready")) {
					MainActivity.handler.post(new Runnable() {
						@Override
						public void run() {
							((Button) CreateGameActivity.self.findViewById(R.id.startGame)).setEnabled(true);
						}
					});
				} else {
					Log.w("gcm", "Got unknown event type " + type + "!");
				}

				MainActivity.handler.post(new Runnable() {
					@Override
					public void run() {
						if (adapter != null)
							adapter.notifyDataSetChanged();
					}
				});
			}
		}

		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}
}
