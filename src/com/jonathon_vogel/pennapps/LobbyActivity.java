package com.jonathon_vogel.pennapps;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pennapps.R;

public class LobbyActivity extends HHActivity {
	private ArrayAdapter<PlayerInfo> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.activity_lobby);

		adapter = new ArrayAdapter<PlayerInfo>(this, android.R.layout.simple_list_item_2, android.R.id.text1, Game.getInstance().players) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				final PlayerInfo player = Game.getInstance().players.get(position);

				View view = super.getView(position, convertView, parent);
				TextView name = (TextView) view.findViewById(android.R.id.text1);
				final TextView status = (TextView) view.findViewById(android.R.id.text2);

				name.setText(player.nickname);
				if (player.isSelf) {
					status.setText((player.ready ? "" : "Not ") + "Ready (tap to ready up)");
					view.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if (!player.isReady()) {
								player.markReadyToServer(v);
								status.setText("Ready");
							}
						}
					});
				} else {
					status.setText(player.ready ? "Not Ready" : "Ready");
				}
				return view;
			}
		};
		super.setAdapter(adapter);

		ListView playerList = (ListView) findViewById(R.id.playerList);
		playerList.setAdapter(adapter);
	}
}
