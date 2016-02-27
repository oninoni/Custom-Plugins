package de.oninoni.PlotOrganizer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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
		
		PlayerChangedWorldListener playerChangedWorldListener = new PlayerChangedWorldListener(this, plotManager);
		
		getServer().getPluginManager().registerEvents(playerChangedWorldListener, this);
	}
	
	public void onDisable() {
		plotManager.savePlots();
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("plot") && args.length > 0){
			if(args[0].equalsIgnoreCase("list")){
				//TODO Add the Command /plot list
			}
			else if(args[0].equalsIgnoreCase("tp")){
				if(args.length == 1){
					if(sender instanceof Player){
						plotManager.tpToFavorite((Player) sender);
						return true;
					}
				}
				else if(args.length == 2){
					if(sender instanceof Player){
						plotManager.tpToName((Player) sender, args[1]);
						return true;
					}
				}
				else if(args.length == 3){
					if(sender instanceof Player){
						plotManager.tpToName((Player) sender, args[1], Bukkit.getOfflinePlayer(args[2]));
						return true;
					}
				}
				else{
					sender.sendMessage("/plot tp [Plot Name] [Owner]");
					return true;
				}
			}
			else if(args[0].equalsIgnoreCase("add")){
				if(sender.hasPermission("plotmanager.core.add")){
					if(args.length == 3){
						OfflinePlayer p = Bukkit.getOfflinePlayer(args[2]);
						if(p.isOnline()){
							plotManager.addPlot(p, args[2]);
							sender.sendMessage("Plot added!");
							return true;
						}
					}else{
						sender.sendMessage("/plot add <Plot Name> <Player>");
						return true;
					}
				}
			}
			else if(args[0].equalsIgnoreCase("del")){
				if(sender.hasPermission("plotmanager.core.del")){
					if(args.length == 2){
						plotManager.delPlot(Integer.parseInt(args[1]));
						sender.sendMessage("Plot deleted!");
						return true;
					}
				}
			}
		}
		
		/* Commands to add:
		 * 
		 * 
		 * 
		 * 
		 */
		
		
		return false;
	}
	
	// TODO: aus momentanem onTabComplete eigene Klasse machen, übersichtlicher
	private void addIfStartsWith(String fullArg, String partArg, List<String> list){
		if (fullArg.startsWith(partArg))
			list.add(fullArg);
	}
	
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> result = new ArrayList<String>();
		Logger l = getLogger();
		l.info("TabCompletion Request by " + sender.getName());
		l.info("Command:    " + command.getName());
		l.info("Alias:      " + alias);
		l.info("Parameters: " + args.length);
		l.info("Parameter0: " + args[0]);
		if (command.getName().equalsIgnoreCase("plot"))
		{
			switch (args.length)
			{
			case 1:
				addIfStartsWith("add", args[0], result);
				addIfStartsWith("del", args[0], result);
				addIfStartsWith("list", args[0], result);
				addIfStartsWith("tp", args[0], result);
				/*
				if (("add").startsWith(args[0]))
					result.add("add");
				result.add("del");
				result.add("list");
				result.add("tp");
				*/
				break;
			}
		}
		
		return result;
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