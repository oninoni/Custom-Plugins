package de.oninoni.PlotOrganizer;

import java.util.HashMap;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;

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
	
	public ProtectedCuboidRegion getNewPlot(Player p){
		
	}
	
	public void playerEntered(Player p){
		if(plots.containsKey(p.getPlayer())){
			// Has a Plot already
		}else{
			// Has no Plot
			
		}
	}
	
}
