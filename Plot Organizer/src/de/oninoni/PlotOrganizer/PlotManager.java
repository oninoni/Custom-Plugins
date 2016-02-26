package de.oninoni.PlotOrganizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlotManager {
	
	PlotOrganizer plugin;
	
	ArrayList<Plot> plots;
	HashMap<OfflinePlayer, ArrayList<Integer>> playerPlots;
	HashMap<OfflinePlayer, Integer> favoritePlots;
	
	public PlotManager(PlotOrganizer p){
		plugin = p;
		plots = new ArrayList<>();
		playerPlots = new HashMap<>();
		favoritePlots = new HashMap<>();
		loadPlots();
	}
	
	private boolean gridPositionFree(GridPosition gp){
		for (Plot plot : plots)
			if (plot.getGridPosition().equals(gp))
				return false;
		return true;
	}
	
	public GridPosition getFreeGridPosition(){
		int x = 0, y = 0, dirStep = 0, blocksLeft = 1;
		while (!gridPositionFree(new GridPosition(x, y))){
			switch (dirStep % 4)
			{
			case 0: // pos x
				x++;
				break;
			case 1: // pos y
				y++;
				break;
			case 2: // neg x
				x--;
				break;
			case 3: // neg y
				y--;
				break;
			}
			blocksLeft--;
			if (blocksLeft <= 0){
				dirStep++;
				blocksLeft = dirStep / 2 + 1;
			}
		}
		return new GridPosition(x, y);
	}
	
	public void addPlot(OfflinePlayer p){
		GridPosition gridPosition = getFreeGridPosition();
		int plotID = plots.size();
		plots.add(new Plot(gridPosition, plugin));
		ArrayList<Integer> plots;
		if (!playerPlots.containsKey(p))
			plots = new ArrayList<>();
		else
			plots = playerPlots.get(p);
		plots.add(plotID);
		playerPlots.put(p, plots);
		favoritePlots.put(p, plotID);
		savePlots();
	}
	
	public void delPlot(int plotID){
		// check for players with that plot

	}
	
	public void tpToFavorite(Player p){
		if(playerPlots.containsKey(p.getPlayer())){
			Plot favoritePlot = plots.get(favoritePlots.get(p));
			favoritePlot.teleportTo(p);
		}
	}
	
	public void loadPlots(){
		
	}
	
	public void savePlots(){
		
	}
	
	public void playerEntered(Player p){
		if(playerPlots.containsKey(p.getPlayer())){
			tpToFavorite(p);
		}else{
			addPlot(p);
			tpToFavorite(p);
		}
	}
	
}
