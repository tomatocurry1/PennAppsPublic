package com.jonathon_vogel.pennapps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class GpsService extends Service {
	private LocationManager locationManager;
	private LocationListener locationListener;

	@Override
	public void onCreate() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				try {
					AndroidHttpClient http = AndroidHttpClient.newInstance("Hide and Hunt App");
					List<NameValuePair> queries = new ArrayList<NameValuePair>(1);
					queries.add(new BasicNameValuePair("reg_id", MainActivity.gcmRegistrationId));
					queries.add(new BasicNameValuePair("longitude", "" + location.getLongitude()));
					queries.add(new BasicNameValuePair("latitude", "" + location.getLatitude()));
					queries.add(new BasicNameValuePair("heading", "" + location.getBearing()));
					HttpPost req = new HttpPost(MainActivity.SERVER + "/create" + "?" + URLEncodedUtils.format(queries, "UTF-8"));
					HttpResponse resp = http.execute(req);
					
					if (resp.getStatusLine().getStatusCode() != 200) {
						Log.w("gps", "Got non-200 code attempting to punt GPS: " + resp.getStatusLine().getStatusCode());
					}
				} catch (final IOException e) {
					MainActivity.handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(GpsService.this, "Error in GPS service: " + e.getMessage(), Toast.LENGTH_LONG).show();
							e.printStackTrace();
						}
					});
				}
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
