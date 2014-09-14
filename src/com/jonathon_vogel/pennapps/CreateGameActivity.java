package com.jonathon_vogel.pennapps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pennapps.R;

public class CreateGameActivity extends HHActivity {
	public static Activity self;
	
	private ArrayAdapter<PlayerInfo> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_game);
		getActionBar().hide();
		self = this;

		String gameID = getIntent().getStringExtra("game_id");
		String nickname = getIntent().getStringExtra("nickname");
		
		Button startGame = (Button) findViewById(R.id.startGame);
		startGame.setEnabled(false);
		
		
		TextView gameCode = (TextView) findViewById(R.id.bigCode);
		gameCode.setText(gameID);
		
		PlayerInfo self = Game.getInstance().makeSelfPlayer(nickname);
		self.setHunter(true);
		
		adapter = new ArrayAdapter<PlayerInfo>(this, android.R.layout.simple_list_item_2, android.R.id.text1, Game.getInstance().players) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				PlayerInfo player = Game.getInstance().players.get(position);

				View view = super.getView(position, convertView, parent);
				TextView name = (TextView) view.findViewById(android.R.id.text1);
				TextView status = (TextView) view.findViewById(android.R.id.text2);

				name.setText(player.nickname);
				status.setText((player.ready ? "" : "Not ") + "Ready (tap to kick)");

				return view;
			}
		};

		super.setAdapter(adapter);

		ListView playerList = (ListView) findViewById(R.id.playerList);
		playerList.setAdapter(adapter);
	}
	
	public void startGame(View v){
		startActivity(new Intent(this, InGameActivity.class));
	}
}
