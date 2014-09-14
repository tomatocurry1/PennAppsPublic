package com.jonathon_vogel.pennapps;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class GpsService extends Service {

	private LocationManager locationManager;
	private LocationListener locationListener;

	@Override
	public void onCreate() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				// Log.d("gps", "GPS updated " + location);

				HttpClient http = HttpClients.createDefault();
				HttpPost req = new HttpPost(MainActivity.SERVER + "/create");
				List<NameValuePair> queries = new ArrayList<NameValuePair>(1);
				queries.add(new BasicNameValuePair("reg_id", MainActivity.gcmRegistrationId));
				queries.add(new BasicNameValuePair("longitude", "" + location.getLongitude()));
				queries.add(new BasicNameValuePair("latitude", "" + location.getLatitude()));

				// might want to double check if right variable
				queries.add(new BasicNameValuePair("heading", "" + location.getBearing()));
				try {
					req.setEntity(new UrlEncodedFormEntity(queries));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				HttpResponse resp = null;
				try {
					resp = http.execute(req);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (resp.getEntity() != null) {
					// String content =
					// IOUtils.toString(resp.getEntity().getContent());
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
		// TODO Auto-generated method stub
		return null;
	}

}