package de.oninoni.PlotOrganizer.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import de.oninoni.PlotOrganizer.PlotManager;
import de.oninoni.PlotOrganizer.PlotOrganizer;

public class PlayerChangedWorldListener implements Listener{
	
	PlotOrganizer plugin;
	PlotManager plotManager;
	
	String worldName = "PlotWorld";
	
	public PlayerChangedWorldListener(PlotOrganizer pO, PlotManager pM){
		plugin = pO;
		plotManager = pM;
	}
	
	@EventHandler
	public void onPlayerChangeWorld(PlayerChangedWorldEvent event){
		if(event.getPlayer().getWorld() == plugin.getPlotWorld()){
			plugin.getLogger().info("Player " + event.getPlayer().getName() + " has entered PlotWorld!");
			plotManager.playerEntered(event.getPlayer());
		}
	}
}
