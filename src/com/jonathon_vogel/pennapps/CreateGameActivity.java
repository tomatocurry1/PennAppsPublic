package com.jonathon_vogel.pennapps;

import java.util.ArrayList;
import java.util.List;

import com.example.pennapps.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CreateGameActivity extends Activity {
	private List<PlayerInfo> players;
	private ArrayAdapter<PlayerInfo> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_game);
		getActionBar().hide();
		
		players = new ArrayList<PlayerInfo>();
		
		players.add(new PlayerInfo("Rotten194", true));
		players.add(new PlayerInfo("bob_the_cookie", false));
		players.add(new PlayerInfo("Dankesputz", true));
		players.add(new PlayerInfo("Here_Comes_The_King", true));
		players.add(new PlayerInfo("BasedGodLilB", false));
		players.add(new PlayerInfo("Rotten194", true));
		players.add(new PlayerInfo("bob_the_cookie", false));
		players.add(new PlayerInfo("Dankesputz", true));
		players.add(new PlayerInfo("Here_Comes_The_King", true));
		players.add(new PlayerInfo("BasedGodLilB", false));
		players.add(new PlayerInfo("Rotten194", true));
		players.add(new PlayerInfo("bob_the_cookie", false));
		players.add(new PlayerInfo("Dankesputz", true));
		players.add(new PlayerInfo("Here_Comes_The_King", true));
		players.add(new PlayerInfo("BasedGodLilB", false));
		
		adapter = new ArrayAdapter<PlayerInfo>(this, android.R.layout.simple_list_item_2, android.R.id.text1, players) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				PlayerInfo player = players.get(position);
				
				View view = super.getView(position, convertView, parent);
				TextView name = (TextView) view.findViewById(android.R.id.text1);
				TextView status = (TextView) view.findViewById(android.R.id.text2);
				
				name.setText(player.nickname);
				status.setText((player.ready ? "" : "Not ") + "Ready (tap to kick)");
				
				return view;
			}
		};
        ListView playerList = (ListView) findViewById(R.id.playerList);
        playerList.setAdapter(adapter);
	}
}