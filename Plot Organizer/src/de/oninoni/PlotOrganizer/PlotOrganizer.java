package de.oninoni.PlotOrganizer;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import de.oninoni.PlotOrganizer.Listener.PlayerChangedWorldListener;

public class PlotOrganizer extends JavaPlugin{
	
	private WorldEditPlugin worldEdit;
	private WorldGuardPlugin worldGuard;
	
	private PlotManager plotManager;
	private World plotWorld;
	private TabCompletion tabCompletion;
	
	public void onEnable() {
		worldEdit = (WorldEditPlugin) getServer().getPluginManager().getPlugin("WorldEdit");
		if(worldEdit != null){
			getLogger().info("World Edit detected!");
		}else{
			getLogger().warning("World Edit missing!");
		}
		
		worldGuard = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
		if(worldGuard != null){
			getLogger().info("World Guard detected!");
		}else{
			getLogger().warning("World Guard missing!");
		}
		
		if(!(getConfig().getKeys(false).size() > 0)){
			getLogger().info("Loading Default Config!");
			saveDefaultConfig();
		}
		
		plotWorld = getServer().getWorld(getConfig().getString("world"));		
		plotManager = new PlotManager(this);		
		tabCompletion = new TabCompletion(this);
		
		PlayerChangedWorldListener playerChangedWorldListener = new PlayerChangedWorldListener(this, plotManager);
		
		getServer().getPluginManager().registerEvents(playerChangedWorldListener, this);
	}
	
	public void onDisable() {
		plotManager.savePlots();
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("plot") && args.length > 0){
			if(args[0].equalsIgnoreCase("list")){
				switch (args.length)
				{
				case 1:
					if (sender instanceof Player){
						plotManager.showList((Player) sender);
					}
					break;
				case 2:
					plotManager.showList(args[1], sender);
					break;
				default:
					sender.sendMessage("�cUsage: /plot list [player]");
				}				
				return true;
			}
			else if(args[0].equalsIgnoreCase("tp")){
				switch(args.length)
				{
				case 1:
					if(sender instanceof Player){
						plotManager.tpToFavorite((Player) sender);
					}
					break;
				case 2:
					if(sender instanceof Player){
						plotManager.tpToName((Player) sender, args[1]);
					}
					break;
				case 3:
					if(sender instanceof Player){
						plotManager.tpToName((Player) sender, args[1], Bukkit.getOfflinePlayer(args[2]));
					}
					break;
				default:
					sender.sendMessage("�cUsage: /plot tp [plotname] [player]");
				}
				return true;
			}
			else if(args[0].equalsIgnoreCase("add")){
				if(sender.hasPermission("plotmanager.core.add")){
					if(args.length == 3){
						OfflinePlayer p = Bukkit.getOfflinePlayer(args[1]);
						if(p.isOnline()){
							if(plotManager.addPlot(p, args[2])){
								sender.sendMessage("�6Plot added!");
							}else{
								sender.sendMessage("�6A Plot with that name does already exist!");
							}
						}else{
							sender.sendMessage("�6That player is not online!");
						}
					}else{
						sender.sendMessage("�cUsage: /plot add <player> <plotname>");
						return true;
					}
					return true;
				}
			}
			else if(args[0].equalsIgnoreCase("del")){
				if(sender.hasPermission("plotmanager.core.del")){
					switch(args.length){
					case 2:
						try {
							int id = Integer.parseInt(args[1]);
							plotManager.delPlot(id);
							sender.sendMessage("�6Plot deleted!");							
						} catch (Exception e) {
							sender.sendMessage("�cUsage: /plot del <id>");
						}
						return true;
					case 3:
						int id = plotManager.getIDByOwnerName(Bukkit.getOfflinePlayer(args[1]), args[2]);
						if (id == -1)
							sender.sendMessage("�6" + args[1] + " doesn't have a plot called " + args[2]);
						else{
							plotManager.delPlot(id);
							sender.sendMessage("�6Plot deleted!");
						}
						return true;
					}
				}
			}
			else if (args[0].equalsIgnoreCase("fav")){
				if (args.length == 2){
					if (sender instanceof Player){
						plotManager.setFavorite((Player) sender, args[1]);
					}
				}else{
					sender.sendMessage("�cUsage: /plot fav <plotname>");
				}
				return true;
			}
		}
		
		return false;
	}
	
	// TODO: aus momentanem onTabComplete eigene Klasse machen, �bersichtlicher
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return tabCompletion.complete(sender, command, args);
	}
	
	public WorldEditPlugin getWorldEdit() {
		return worldEdit;
	}
	
	public WorldGuardPlugin getWorldGuard() {
		return worldGuard;
	}
	
	public World getPlotWorld() {
		return plotWorld;
	}
}