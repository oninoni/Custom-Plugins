package de.oninoni.PlotOrganizer;

import java.util.HashMap;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlotManager {
	
	PlotOrganizer plugin;
	
	HashMap<OfflinePlayer, PlayerPlots> plots = new HashMap<>();
	
	public PlotManager(PlotOrganizer p){
		plugin = p;
		loadPlots();
	}
	
	public void loadPlots(){
		
	}
	
	public void savePlots(){
		
	}
	
	public void playerEntered(Player p){
		if(plots.containsKey(p.getPlayer())){
			
		}else{
			
		}
	}
	
}
