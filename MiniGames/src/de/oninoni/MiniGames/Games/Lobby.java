package de.oninoni.MiniGames.Games;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Lobby {
	
	private Location location;
	private List<Player> players;
	
	public Lobby(Location location) {
		this.location = location;
	}
	
	public void addPlayer(Player player) {
		players.add(player);
	}
	
	public void delPlayer(Player player) {
		players.remove(player);
	}
	
}
