package com.jonathon_vogel.pennapps;

import java.util.ArrayList;
import java.util.List;

public class Game {
	private static Game INSTANCE;
	
	public List<PlayerInfo> players;
	public boolean gameStarted;
	
	private Game() {
		players = new ArrayList<PlayerInfo>();
		gameStarted = false;
	}
	
	public PlayerInfo getPlayerById(String regID) {
		for (PlayerInfo p : players) {
			if (p.regID.equals(regID))
				return p;
		}
		return null;
	}
	
	public static void initialize() {
		INSTANCE = new Game();
	}
	
	public static Game getInstance() {
		return INSTANCE;
	}
}
