package de.oninoni.MiniGames.Games;

import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.regions.CuboidRegion;

public abstract class Game {

	private Lobby lobby;
	private GameManager gameManager;
	private World world;
	private List<Player> players;
	private CuboidRegion area;
	
	public Game(Lobby lobby, GameManager gameManager, CuboidRegion area) {
		this.lobby = lobby;
		this.gameManager = gameManager;
		this.world = gameManager.getWorld;
	}
	
	// put player in from main lobby to local lobby
	public void playerJoin(Player player) {
		lobby.addPlayer(player);
	}
	
	// player returns to main lobby
	public void playerLeave(Player player) {
		players.remove(player);
		gameManager.getLobby().addPlayer();
	}
	
	// all players in local lobby into game and start the game logic
	public void startGame() {
		
	}

}
