package de.oninoni.PlotOrganizer;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;

public class PlotManager {
	
	PlotOrganizer plugin;
	
	ArrayList<Plot> plots;
	HashMap<OfflinePlayer, ArrayList<Integer>> playerPlots;
	
	public PlotManager(PlotOrganizer p){
		plugin = p;
		plots = new ArrayList<>();
		playerPlots = new HashMap<>();
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
