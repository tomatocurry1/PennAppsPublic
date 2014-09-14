
package com.jonathon_vogel.pennapps;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.example.pennapps.R;

public class CountdownActivity extends HHActivity {
	private TextView count;
	final private long TIME = 10000;
	final private long TICK = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_countdown);
		count = (TextView) findViewById(R.id.countdown);
		new CountDownTimer(TIME, TICK) {

			public void onTick(long millisUntilFinished) {
				count.setText("" + millisUntilFinished / TICK);
			}

			public void onFinish() {
				// count.setText("GO!");
				CountdownActivity.this.startActivity(new Intent());
			}
		}.start();
	}
}
