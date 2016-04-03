package de.oninoni.MiniGames.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import de.oninoni.MiniGames.MiniGames;

public class PlayerListerner implements Listener{
	
	private static MiniGames plugin = MiniGames.get();
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		
	}
	
	@EventHandler
	public void onLeave(PlayerJoinEvent e){
		
	}
	
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent e){
		
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e){
		
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e){
		
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent e){
		
	}
}
