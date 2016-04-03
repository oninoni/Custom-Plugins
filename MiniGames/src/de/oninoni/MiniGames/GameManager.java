package de.oninoni.MiniGames;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;

import de.oninoni.MiniGames.Games.Game;
import de.oninoni.MiniGames.Games.Lobby;

public class GameManager {
	
	private Lobby lobby;
	private World world;
	
	private List<Game> games;
	
	public GameManager(){
		games = new ArrayList<Game>();
	}
	
	public World getWorld() {
		return world;
	}
	
	public Lobby getLobby() {
		return lobby;
	}
	
}
