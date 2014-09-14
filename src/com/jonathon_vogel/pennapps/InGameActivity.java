package com.jonathon_vogel.pennapps;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pennapps.R;

public class InGameActivity extends Activity {
	private ArrayAdapter<PlayerInfo> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_in_game);
		
		adapter = new ArrayAdapter<PlayerInfo>(this, android.R.layout.simple_list_item_2, android.R.id.text1, Game.getInstance().players) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				PlayerInfo player = Game.getInstance().players.get(position);
				
				View view = super.getView(position, convertView, parent);
				TextView name = (TextView) view.findViewById(android.R.id.text1);
				TextView status = (TextView) view.findViewById(android.R.id.text2);
				
				name.setText(player.nickname);
				status.setText(player.hunter ? "Hunter" : "Hider");

				return view;
			}
		};
        ListView playerList = (ListView) findViewById(R.id.playerList);
        playerList.setAdapter(adapter);
	}
}
