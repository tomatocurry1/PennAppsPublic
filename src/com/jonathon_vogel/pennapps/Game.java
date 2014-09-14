package com.jonathon_vogel.pennapps;

import java.util.ArrayList;
import java.util.List;

public class Game {
	private static Game INSTANCE;
	
	public List<PlayerInfo> players;
	public PlayerInfo self;
	public String gameID;
	
	private Game() {
		players = new ArrayList<PlayerInfo>();
	}
	
	public PlayerInfo getPlayerById(String regID) {
		for (PlayerInfo p : players) {
			if (p.regID.equals(regID))
				return p;
		}
		return null;
	}
	
	public PlayerInfo makeSelfPlayer(String nick){
		self = new PlayerInfo(MainActivity.gcmRegistrationId, nick, false, false);
		self.isSelf = true;
		players.add(self);
		return self;
	}
	
	public static void initialize() {
		INSTANCE = new Game();
	}
	
	public static Game getInstance() {
		return INSTANCE;
	}
	
	
}
