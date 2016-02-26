package de.oninoni.PlotOrganizer;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

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
	
	public void playerEntered(Player p){
		if(plots.containsKey(p.getPlayer())){
			
		}else{
			
		}
	}
	
}
