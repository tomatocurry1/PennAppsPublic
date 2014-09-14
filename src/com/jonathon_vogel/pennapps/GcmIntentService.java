package com.jonathon_vogel.pennapps;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmIntentService extends IntentService {
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
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				MainActivity.handler.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(GcmIntentService.this, "Got message " + extras.toString(), Toast.LENGTH_LONG).show();
					}
				});
			}
		}
		
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}
}
