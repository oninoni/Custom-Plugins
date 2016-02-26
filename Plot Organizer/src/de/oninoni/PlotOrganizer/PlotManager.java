package de.oninoni.PlotOrganizer;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class PlotManager {
	
	private PlotOrganizer plugin;
	
	private PlotManagerData plotManagerData;
	
	private ArrayList<Plot> plots;
	private HashMap<OfflinePlayer, ArrayList<Integer>> playerPlots;
	private HashMap<OfflinePlayer, Integer> favoritePlots;
	
	public PlotManager(PlotOrganizer pl){
		plugin = pl;
		plotManagerData = new PlotManagerData(pl);
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
	
	public void addPlot(OfflinePlayer p, String name){
		GridPosition gridPosition = getFreeGridPosition();
		int plotID = plots.size();
		Plot plot = new Plot(gridPosition, plugin, p, plotID);
		plot.setName(name);
		plots.add(plot);
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
		OfflinePlayer player = plots.get(plotID).getOwner();
		plots.remove(plotID);
		playerPlots.remove(player);
		favoritePlots.remove(player);
		savePlots();
	}
	
	public void tpToFavorite(Player p){
		if(favoritePlots.containsKey(p.getPlayer())){
			Plot favoritePlot = plots.get(favoritePlots.get(p));
			favoritePlot.teleportTo(p);
		}
	}
	
	public void tpToName(Player p, String name){
		tpToName(p, name, (OfflinePlayer) p);
	}
	
	public void tpToName(Player p, String name, OfflinePlayer owner){
		for (Plot plot : plots) {
			if(plot.getOwner() == owner && plot.getName() == name){
				plot.teleportTo(p);
				return;
			}
		}
	}
	
	public void loadPlots(){
		
	}
	
	public void savePlots(){
		FileConfiguration config = plotManagerData.getConfig();
		
		for (Plot plot : plots) {
			int id = plots.indexOf(plot);
			GridPosition gp = plot.getGridPosition();
			config.set("plots." + id + ".pos.x", gp.getX());
			config.set("plots." + id + ".pos.y", gp.getY());
			config.set("plots." + id + ".name", plot.getName());
		}
		
		for (OfflinePlayer key : playerPlots.keySet()){
			String playerUUID = key.getUniqueId().toString();
			for (int i = 0; i < playerPlots.get(key).size(); i++)
				config.set("player." + playerUUID + ".allPlots." + i, playerPlots.get(key).get(i));
			config.set("player." + playerUUID + ".favPlot", favoritePlots.get(key));
		}
		
		plotManagerData.saveConfig();
	}
	
	public void playerEntered(Player p){
		if(playerPlots.containsKey(p.getPlayer())){
			tpToFavorite(p);
		}else{
			addPlot(p, "Default");
			tpToFavorite(p);
		}
	}
	
}
