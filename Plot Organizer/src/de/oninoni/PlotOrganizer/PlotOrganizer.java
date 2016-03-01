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
		tabCompletion = new TabCompletion(this, plotManager);
		
		PlayerChangedWorldListener playerChangedWorldListener = new PlayerChangedWorldListener(this, plotManager);
		
		getServer().getPluginManager().registerEvents(playerChangedWorldListener, this);
	}
	
	public void onDisable() {
		plotManager.savePlots();
	}
	
	// TODO: getOfflinePlayers mit name selber schreiben
	@SuppressWarnings("deprecation")
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
					sender.sendMessage("§cUsage: /plot list [player]");
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
					sender.sendMessage("§cUsage: /plot tp [plotname] [player]");
				}
				return true;
			}
			else if(args[0].equalsIgnoreCase("add")){
				if(sender.hasPermission("plotmanager.core.add")){
					if(args.length == 3){
						OfflinePlayer p = Bukkit.getOfflinePlayer(args[1]);
						if(p.isOnline()){
							if(plotManager.addPlot(p, args[2])){
								sender.sendMessage("§6Plot §f" + args[2] + "§6 added for player §f" + args[1] + "§6!");
							}else{
								sender.sendMessage("§6Player §f" + args[1] + "§6 does already have a Plot with the name §f" + args[2] + "§6!");
							}
						}else{
							sender.sendMessage("§6Player §f" + args[1] + "§6 is not online!");
						}
					}else{
						sender.sendMessage("§cUsage: /plot add <player> <plotname>");
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
							sender.sendMessage("§6Plot deleted! (ID: §f" + id + "§6)");				
						} catch (Exception e) {
							sender.sendMessage("§cUsage: /plot del <id>");
						}
						return true;
					case 3:
						int id = plotManager.getIDByOwnerName(Bukkit.getOfflinePlayer(args[1]), args[2]);
						if (id == -1)
							sender.sendMessage("§f" + args[1] + "§6 doesn't have a plot called §f" + args[2] + "§6!");
						else{
							plotManager.delPlot(id);
							sender.sendMessage("§6Plot §f" + args[2] + "§6 of Player §f" + args[1] + "§6 deleted!");
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
					sender.sendMessage("§cUsage: /plot fav <plotname>");
				}
				return true;
			}
			else if(args[0].equalsIgnoreCase("rename")){
				if(args.length == 3){
					if(sender instanceof Player){
						plotManager.changePlotName((Player)sender, args[1], args[2]);
					}
				}else{
					sender.sendMessage("§cUsage: /plot rename <oldName> <newName>");
				}
				return true;
			}
			else if(args[0].equalsIgnoreCase("friend") || args[0].equalsIgnoreCase("friends")){
				if(args.length >= 2 && sender instanceof Player){
					if(args[1].equalsIgnoreCase("add")){
						if(Bukkit.getOfflinePlayer(args[2]).isOnline()){
							if(sender.getName().equalsIgnoreCase(args[2])){
								sender.sendMessage("&6You already own this plot!");
							}
							else{
								if(args.length == 3) {
									if(plotManager.addFriend((Player) sender, Bukkit.getOfflinePlayer(args[2]))){
										sender.sendMessage("§6Friend §f" + args[2] + "§6 added to this Plot!");
									}
									else{
										sender.sendMessage("§6Friend §f" + args[2] + "§6 already exists on this Plot!");
									}
								}
								else if(args.length == 4){
									if(plotManager.addFriend((Player) sender, args[3], Bukkit.getOfflinePlayer(args[2]))){
										sender.sendMessage("§6Friend §f" + args[2] + "§6 added to §f" + args[3] + "§6!");
									}
									else{
										sender.sendMessage("§6Friend §f" + args[2] + "§6 already exists on §f" + args[3] + "§6!");
									}
								}
								else{
									sender.sendMessage("§cUsage: /plot friend add <player> [plot]");
								}
							}
						}
						else{
							sender.sendMessage("§f" + args[2] + "§6 is not Online!");
						}
						
					}
					else if(args[1].equalsIgnoreCase("del")){
						if(Bukkit.getOfflinePlayer(args[2]).isOnline()){
							if(sender.getName().equalsIgnoreCase(args[2])){
								sender.sendMessage("&6You cant remove yourself from this plot!");
							}
							else{
								if(args.length == 3){
									if(plotManager.delFriend((Player) sender, Bukkit.getOfflinePlayer(args[2]))){
										sender.sendMessage("§6Friend §f" + args[2] + "§6 removed from this Plot!");
									}
									else{
										sender.sendMessage("§6Friend §f" + args[2] + "§6 does not exist on this Plot!");
									}
								}
								else if(args.length == 4){
									if(plotManager.delFriend((Player) sender, args[3], Bukkit.getOfflinePlayer(args[2]))){
										sender.sendMessage("§6Friend §f" + args[2] + "§6 removed from §f" + args[3] + "§6!");
									}
									else{
										sender.sendMessage("§6Friend §f" + args[2] + "§6 does not exist on §f" + args[3] + "§6!");
									}
								}
								else{
									sender.sendMessage("§cUsage: /plot friend del <player> [plot]");
								}
							}
							
						}
						else{
							sender.sendMessage("§f" + args[2] + "§6 is not Online!");
						}
					}
					else if(args[1].equalsIgnoreCase("list")){
						if(args.length == 2){
							plotManager.listFriends((Player) sender);
						}
						else if(args.length == 3){
							plotManager.listFriends((Player) sender, args[2]);
						}
						else{
							sender.sendMessage("§cUsage: /plot friend list [plot]");
						}
					}
					else{
						
					}
					return true;
				}
			}
		}
		
		return false;
	}
	
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