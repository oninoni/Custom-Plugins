package de.oninoni.PlotOrganizer;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import de.oninoni.PlotOrganizer.External.UUIDFetcher;
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
					printUsage(sender, "list");
				}				
				return true;
			}
			else if(args[0].equalsIgnoreCase("info")){
				if(sender instanceof Player){
					plotManager.sendPlotInfo((Player) sender);
				}
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
						UUID uuid;
						try {
							uuid = UUIDFetcher.getUUIDOf(args[2]);
						} catch (Exception e) {
							e.printStackTrace();
							sender.sendMessage("&6Player: " + args[2] + " does not exist!");
							return true;
						}
						plotManager.tpToName((Player) sender, args[1], Bukkit.getOfflinePlayer(uuid));
					}
					break;
				default:
					printUsage(sender, "tp");
				}
				return true;
			}
			else if(args[0].equalsIgnoreCase("add")){
				if(sender.hasPermission("plotmanager.core.add")){
					if(args.length == 3){
						UUID uuid;
						try {
							uuid = UUIDFetcher.getUUIDOf(args[1]);
						} catch (Exception e) {
							e.printStackTrace();
							sender.sendMessage("&6Player: " + args[1] + " does not exist!");
							return true;
						}
						OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
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
						printUsage(sender, "add");
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
							printUsage(sender, "deli");
						}
						return true;
					case 3:
						UUID uuid;
						try {
							uuid = UUIDFetcher.getUUIDOf(args[1]);
						} catch (Exception e) {
							e.printStackTrace();
							sender.sendMessage("&6Player: " + args[1] + " does not exist!");
							return true;
						}
						int id = plotManager.getIDByOwnerName(Bukkit.getOfflinePlayer(uuid), args[2]);
						if (id == -1)
							sender.sendMessage("§f" + args[1] + "§6 doesn't have a plot called §f" + args[2] + "§6!");
						else{
							plotManager.delPlot(id);
							sender.sendMessage("§6Plot §f" + args[2] + "§6 of Player §f" + args[1] + "§6 deleted!");
						}
						return true;
					default:
						printUsage(sender, "del");
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
					printUsage(sender, "fav");
				}
				return true;
			}
			else if(args[0].equalsIgnoreCase("rename")){
				if(args.length == 3){
					if(sender instanceof Player){
						plotManager.changePlotName((Player)sender, args[1], args[2]);
					}
				}else{
					printUsage(sender, "rename");
				}
				return true;
			}
			else if(args[0].equalsIgnoreCase("help")){
				if(args.length == 1){
					sender.sendMessage("§6Available commands: §b/plot...");
					sender.sendMessage("§b list §9- §aGet a list of all of your or another players plots");
					sender.sendMessage("§b tp §9- §aTeleport to one of your or another players plot");
					sender.sendMessage("§b fav §9- §aFavorite one of your plots as spawnpoint into this world");
					sender.sendMessage("§b friends §9- §aAdd friends to help you build in your own plot");
					sender.sendMessage("§b rename §9- §aGive your plots a new name");
					sender.sendMessage("§6Use /plot help <topic> to check the usage");
				}else if(args.length == 2){
					printUsage(sender, args[1]);
				}else if(args.length == 3){
					if (args[1].equalsIgnoreCase("friend") || args[1].equalsIgnoreCase("friends")){
						printUsage(sender, "friends " + args[2]);
					}
				}else
					sender.sendMessage("§cWhat?");
				return true;
			}
			else if(args[0].equalsIgnoreCase("friend") || args[0].equalsIgnoreCase("friends")){
				if(sender instanceof Player){
					if(args[1].equalsIgnoreCase("add")){
						if(args.length >= 3 && args.length <= 4){
							UUID uuid;
							try {
								uuid = UUIDFetcher.getUUIDOf(args[2]);
							} catch (Exception e) {
								e.printStackTrace();
								sender.sendMessage("&6Player: " + args[2] + " does not exist!");
								return true;
							}
							if(Bukkit.getOfflinePlayer(uuid).isOnline()){
								if(sender.getName().equalsIgnoreCase(args[2])){
									sender.sendMessage("&6You already own this plot!");
								}
								else{
									if(args.length == 3) {
										if(plotManager.getPlotByPosition((Player) sender) != null){
											if(plotManager.addFriend((Player) sender, Bukkit.getOfflinePlayer(uuid))){
												sender.sendMessage("§6Friend §f" + args[2] + "§6 added to this Plot!");
											}
											else{
												sender.sendMessage("§6Friend §f" + args[2] + "§6 already exists on this Plot!");
											}
										}
										else{
											sender.sendMessage("&6You are not inside a Plot!");
										}
									}
									else if(args.length == 4){
										if(plotManager.addFriend((Player) sender, args[3], Bukkit.getOfflinePlayer(uuid))){
											sender.sendMessage("§6Friend §f" + args[2] + "§6 added to §f" + args[3] + "§6!");
										}
										else{
											sender.sendMessage("§6Friend §f" + args[2] + "§6 already exists on §f" + args[3] + "§6!");
										}
									}
								}
							}
							else{
								sender.sendMessage("§6" + args[2] + "§6 is not online!");
							}
						}
						else{
							printUsage(sender, "friends add");
						}
					}
					else if(args[1].equalsIgnoreCase("del")){
						if(args.length >= 3 && args.length <= 4){
							UUID uuid;
							try {
								uuid = UUIDFetcher.getUUIDOf(args[2]);
							} catch (Exception e) {
								e.printStackTrace();
								sender.sendMessage("&6Player: " + args[2] + " does not exist!");
								return true;
							}
							if(Bukkit.getOfflinePlayer(uuid).isOnline()){
								if(sender.getName().equalsIgnoreCase(args[2])){
									sender.sendMessage("&6You cant remove yourself from this plot!");
								}
								else{
									if(args.length == 3){
										if(plotManager.getPlotByPosition((Player) sender) != null){
											if(plotManager.delFriend((Player) sender, Bukkit.getOfflinePlayer(uuid))){
												sender.sendMessage("§6Friend §f" + args[2] + "§6 removed from this Plot!");
											}
											else{
												sender.sendMessage("§6Friend §f" + args[2] + "§6 does not exist on this Plot!");
											}
										}
										else{
											sender.sendMessage("&6You are not inside a Plot!");
										}
									}
									else if(args.length == 4){
										if(plotManager.delFriend((Player) sender, args[3], Bukkit.getOfflinePlayer(uuid))){
											sender.sendMessage("§6Friend §f" + args[2] + "§6 removed from §f" + args[3] + "§6!");
										}
										else{
											sender.sendMessage("§6Friend §f" + args[2] + "§6 does not exist on §f" + args[3] + "§6!");
										}
									}
									
								}
							}
							else{
								sender.sendMessage("§f" + args[2] + "§6 is not Online!");
							}
						}
						else{
							printUsage(sender, "friends del");
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
							printUsage(sender, "friends list");
						}
					}
					else{
						printUsage(sender, "friends");
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
	
	private void printUsage(CommandSender sender, String cmd){
		if(cmd.equalsIgnoreCase("add"))
			sender.sendMessage("§cUsage: /plot add <player> <plotname>");
		else if(cmd.equalsIgnoreCase("deli"))
			sender.sendMessage("§cUsage: /plot del <id>");
		else if(cmd.equalsIgnoreCase("del"))
			sender.sendMessage("§cUsage: /plot del <player> <plotname>");
		else if(cmd.equalsIgnoreCase("list"))
			sender.sendMessage("§cUsage: /plot list [player]");
		else if(cmd.equalsIgnoreCase("tp"))
			sender.sendMessage("§cUsage: /plot tp [plotname] [player]");
		else if(cmd.equalsIgnoreCase("fav"))
			sender.sendMessage("§cUsage: /plot fav <plotname>");
		else if(cmd.equalsIgnoreCase("friends") || cmd.equalsIgnoreCase("friend"))
			sender.sendMessage("§cUsage: /plot friends <add/del/list>");
		else if(cmd.equalsIgnoreCase("friends add"))
			sender.sendMessage("§cUsage: /plot friends add <player> [plot]");
		else if(cmd.equalsIgnoreCase("friends del"))
			sender.sendMessage("§cUsage: /plot friends del <player> [plot]");
		else if (cmd.equalsIgnoreCase("friends list"))	
			sender.sendMessage("§cUsage: /plot friends list [plot]");		
		else if(cmd.equalsIgnoreCase("rename"))
			sender.sendMessage("§cUsage: /plot rename <oldName> <newName>");
		else if(cmd.equalsIgnoreCase("help"))
			sender.sendMessage("§cUsage: /plot help <topic>");			
		else
			sender.sendMessage("§cThis command doesn't exist!");
	}
}