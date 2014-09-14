package com.jonathon_vogel.pennapps;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Vibrator;

public class VibrateService extends Service {
	public static VibrateService staticInstance;
	
	final private double MAXDIST = 40.0;
	final private double MAXPAUSE = 5000.0;
	public double currentDistance = -1;

	private VibrateThread thread;
	private boolean threadShouldRun = true;


	public VibrateService() {
		super();
		staticInstance = this;
	}

	public static VibrateService getStatic() {
		return staticInstance;
	}

	public class VibrateThread extends Thread {
		Vibrator vibrator;

		public VibrateThread() {
			vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		}

		@Override
		public void run() {
			while (threadShouldRun) {
				double dist = currentDistance;
				if (dist == 0)
					dist = 0.0001;

				if (dist >= 0 && dist < MAXDIST) {
					vibrator.vibrate(500);
					VibrateService.sleep((long) (MAXPAUSE * dist / MAXDIST));
				} else {
					VibrateService.sleep((long) (MAXPAUSE));
				}
			}
		}

	}

	private static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		thread = new VibrateThread();
		

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		thread.start();
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		threadShouldRun = false;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
