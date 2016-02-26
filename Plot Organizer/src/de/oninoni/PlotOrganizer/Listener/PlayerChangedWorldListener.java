package de.oninoni.PlotOrganizer.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerChangedWorldListener implements Listener{
	
	String worldName = "Plot";
	
	@EventHandler
	public void onPlayerChangeWorld(PlayerChangedWorldEvent event){
		if(event.getPlayer().getWorld().getName().equalsIgnoreCase(worldName)){
			
		}
	}
}
