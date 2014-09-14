package com.jonathon_vogel.pennapps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pennapps.R;

public class InGameActivity extends HHActivity {
	private ArrayAdapter<PlayerInfo> adapter;
	Button special;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_in_game);
		((TextView)findViewById(R.id.identity)).setText(Game.getInstance().self.isHunter()? "Hunter" : "Hunted");
		Button cloaker  = (Button)findViewById(R.id.cloakerButton);
		Button Scanner = (Button)findViewById(R.id.scannerButton);
		special = Game.getInstance().self.isHunter() ?  Scanner : cloaker;
		(!Game.getInstance().self.isHunter() ?  Scanner : cloaker).setEnabled(false);
		
		
		Intent vibrateIntent = new Intent(this, VibrateService.class);
		startService(vibrateIntent);
		
		Intent GpsIntent = new Intent(this, GpsService.class);
		startService(GpsIntent);
		
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
		
		super.setAdapter(adapter);
		
        ListView playerList = (ListView) findViewById(R.id.playerList);
        playerList.setAdapter(adapter);
	}
}
