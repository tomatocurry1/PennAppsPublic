package com.jonathon_vogel.pennapps;

import com.example.pennapps.R;
import com.example.pennapps.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
