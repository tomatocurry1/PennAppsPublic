package com.jonathon_vogel.pennapps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.pennapps.R;

public class JoinGameActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join_game);
	}
	
	
	public void joinButtonClick(View view) {
		Intent intent = new Intent(this, LobbyActivity.class);
		//TODO: pass nickname into dis
        startActivity(intent);
	}
}
