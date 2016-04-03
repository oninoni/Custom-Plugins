package de.oninoni.MiniGames;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Lobby {
	
	private Location location;
	private List<Player> players;
	
	public Lobby(Location location) {
		this.location = location;
	}
	
}
