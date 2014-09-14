package com.jonathon_vogel.pennapps;

import android.app.Activity;
import android.widget.BaseAdapter;

public abstract class HHActivity extends Activity {
	private BaseAdapter adapter;

	public void setAdapter(BaseAdapter adapter) {
		this.adapter = adapter;
	}

	@Override
	protected void onResume() {
		super.onResume();
		GcmIntentService.adapter = adapter;
	}
}