package de.oninoni.MiniGames.Games;

import java.util.List;

import org.bukkit.entity.Player;

public abstract class Game {

	private Lobby lobby;
	private List<Player> players;
	
	public Game(Lobby lobby) {
		this.lobby = lobby;
	}
	
	
	
}
