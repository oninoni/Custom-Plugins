package de.oninoni.PlotOrganizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.domains.DefaultDomain;

public class PlotManager {
	
	static int MAX_PLOTS = Integer.MAX_VALUE;
	
	private PlotOrganizer plugin;
	
	private PlotManagerData plotManagerData;
	
	private HashMap<Integer, Plot> plots;	
	private HashMap<OfflinePlayer, ArrayList<Integer>> playerPlots;
	private HashMap<OfflinePlayer, Integer> favoritePlots;
	
	public PlotManager(PlotOrganizer pl){
		plugin = pl;
		plotManagerData = new PlotManagerData(pl);
		plots = new HashMap<>();
		playerPlots = new HashMap<>();
		favoritePlots = new HashMap<>();
		loadPlots();
	}
	
	private boolean gridPositionFree(GridPosition gp){
		for (Integer key : plots.keySet())
			if (plots.get(key).getGridPosition().equals(gp))
				return false;
		return true;
	}

	private GridPosition getFreeGridPosition(){
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
	
	private int getFreePlotID(){
		int i = -1;
		while (i < MAX_PLOTS) {
			if (!plots.containsKey(++i))
				return i;			
		}
		return -1;
	}
	
	private boolean isPlotNameFree(OfflinePlayer p, String name){
		ArrayList<Integer> playerPlotIDs = playerPlots.get(p);
		if(playerPlotIDs != null){
			for (Integer key : playerPlotIDs) {
				Plot plot = plots.get(key);
				if(plot.getName().equalsIgnoreCase(name))
					return false;
			}
		}
		return true;
	}
	
	public void changePlotName(Player p, String oldName, String newName){
		int plotId = getIDByOwnerName((OfflinePlayer) p, oldName);
		if(plotId == -1){
			p.sendMessage("§6No such Plot with the name " + oldName + "!");
			return;
		}
		if(!isPlotNameFree(p, newName)){
			p.sendMessage("§6A Plot with the Name " + newName + " does already exist!");
			return;
		}
		Plot plot = plots.get(plotId);
		plot.setName(newName);
		p.sendMessage("§6Plot " + oldName + " is now called " + newName);
	}
	
	public boolean addPlot(OfflinePlayer p, String name){
		if(isPlotNameFree(p, name)){
			plugin.getLogger().info("Position searching...");
			GridPosition gridPosition = getFreeGridPosition();
			plugin.getLogger().info("Position found: " + gridPosition.getX() + " / " + gridPosition.getY());
			int plotID = getFreePlotID();
			plugin.getLogger().info("Plot ID found: " + plotID);
			Plot plot = new Plot(gridPosition, plugin, p, plotID);
			plot.setName(name);
			plots.put(plotID, plot);
			ArrayList<Integer> plotlist;
			if (!playerPlots.containsKey(p))
				plotlist = new ArrayList<>();
			else
				plotlist = playerPlots.get(p);
			plotlist.add(plotID);
			playerPlots.put(p, plotlist);
			if (!favoritePlots.containsKey(p)) // only the first plot should be favorited
				favoritePlots.put(p, plotID);
			
			savePlots();
			
			return true;
		}
		return false;
	}
	
	public void delPlot(int plotID){
		OfflinePlayer player = plots.get(plotID).getOwner();
		plots.get(plotID).getProtectedCuboidRegion().setOwners(new DefaultDomain());
		plots.remove(plotID);
		
		ArrayList<Integer> list = playerPlots.get(player);		
		for(int i = 0; i < list.size(); i++){
			if (list.get(i) == plotID){
				if (favoritePlots.get(player) == list.remove(i)) {
					if (list.size() > 0){
						favoritePlots.put(player, list.get(0));
					}
					else{
						playerPlots.remove(player);
						favoritePlots.remove(player);
					}
					break;
				}
			}
		}
		
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
		for (Integer key : plots.keySet()) {
			Plot plot = plots.get(key);
			if(plot.getOwner().getUniqueId() == owner.getUniqueId() && plot.getName().equalsIgnoreCase(name)){
				plot.teleportTo(p);
				return;
			}
		}
	}
	
	public void loadPlots(){		
		plotManagerData.reloadConfig();
		FileConfiguration config = plotManagerData.getConfig();
		
		if (!config.contains("plots"))
			return;
		
		for (String key : config.getConfigurationSection("plots").getKeys(false)){
			ConfigurationSection cs = config.getConfigurationSection("plots." + key);
			
			GridPosition gp = new GridPosition(
				cs.getInt("pos.x"),
				cs.getInt("pos.y")
			);
			int id = Integer.parseInt(key);
			
			OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(cs.getString("UUID")));
			plots.put(id, new Plot(gp, plugin, p, id));
			plots.get(id).setName(cs.getString("name"));
		}
		
		for (String key : config.getConfigurationSection("player").getKeys(false)) {
			ConfigurationSection cs = config.getConfigurationSection("player." + key);
			ArrayList<Integer> list = new ArrayList<>();
			for (String index : cs.getConfigurationSection("allPlots").getKeys(false))
				list.add(Integer.parseInt(index));
			playerPlots.put(Bukkit.getOfflinePlayer(UUID.fromString(key)), list);
			favoritePlots.put(Bukkit.getOfflinePlayer(UUID.fromString(key)), cs.getInt("favPlot"));
		}
	}
	
	public void savePlots(){
		FileConfiguration config = plotManagerData.getConfig();
		
		config.set("plots", null);
		config.set("player", null);
		
		for (Integer id : plots.keySet()) {
			Plot plot = plots.get(id);
			GridPosition gp = plot.getGridPosition();
			config.set("plots." + id + ".pos.x", gp.getX());
			config.set("plots." + id + ".pos.y", gp.getY());
			config.set("plots." + id + ".name", plot.getName());
			config.set("plots." + id + ".UUID", plot.getOwner().getUniqueId().toString());
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
	
	private void listPlots(OfflinePlayer player, CommandSender sender) {
		ArrayList<Integer> list = playerPlots.get(player);
		for (int i = 0; i < list.size(); i++){
			plugin.getLogger().info("i: " + i + " / " + list.get(i));
			Plot plot = plots.get(list.get(i));
			GridPosition l = plot.getGridPosition();
			String msg = 
				"§b " + (i + 1) + ") " + 
				plot.getName() + 
				" at [ " + l.getX() + " | " + l.getY() + " ]";
			if (list.get(i) == favoritePlots.get(player))
				msg += " §a(favorited)";
			sender.sendMessage(msg);
		}	
	}

	// show own list
	public void showList(Player player) {			
		if (!playerPlots.containsKey(player)){
			player.sendMessage("§6You don't have any plots yet!");
			player.sendMessage("§6Enter the plot world to create one");
			return;
		}
		player.sendMessage("§6Your own Plots:");
		listPlots(player, player);
	}
	
	// show others list
	@SuppressWarnings("deprecation")
	public void showList(String from, CommandSender sender) {
		OfflinePlayer player = Bukkit.getOfflinePlayer(from);
		if (!playerPlots.containsKey(player)){
			sender.sendMessage("§6" + from + " doesn't have any plots!");
			return;
		}
		sender.sendMessage("§6" + from + "'s Plots:");
		listPlots(player, sender);
	}
	
	public int getIDByOwnerName(OfflinePlayer owner, String name){
		for (Integer id : plots.keySet()) {
			Plot plot = plots.get(id);

			plugin.getLogger().info(Boolean.toString(plot.getOwner().getUniqueId() == owner.getUniqueId()));
			
			if (plot.getOwner().getUniqueId() == owner.getUniqueId() &&
				plot.getName().equalsIgnoreCase(name)) {
				return id;
			}
		}
		return -1;
	}
	
	public Plot getPlotByOwnerName(OfflinePlayer owner, String name){
		int id = getIDByOwnerName(owner, name);
		if (id == -1)
			return null;
		return plots.get(id);
	}

	public void setFavorite(Player owner, String name) {
		int id = getIDByOwnerName(owner, name);
		if (id == -1){
			owner.sendMessage("§6You don't have a plot called '" + name + "'!");
			owner.sendMessage("§6Use /plot list to get a list of all your plots");
			return;
		}
		favoritePlots.put(owner, id);
		owner.sendMessage("§6" + name + " is now your favorite plot!");
		savePlots();
	}
}
