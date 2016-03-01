package de.oninoni.PlotOrganizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TabCompletion {
	
	private static String[] plotCmds = {"add", "del", "list", "tp", "fav", "friends", "rename", "help"};
	private static String[] friendsCmds = {"add", "del", "list"};
	
	private PlotOrganizer plugin;
	private PlotManager plotManager;
	
	public TabCompletion(PlotOrganizer pl, PlotManager pm){
		plugin = pl;
		plotManager = pm;
	}
	
	private void addPlotCompletion(OfflinePlayer player, String partArg, List<String> list){
		ArrayList<String> plotNames = plotManager.getPlotNames(player);
		for (int i = 0; i < plotNames.size(); i++){
			String name = plotNames.get(i);
			if (name.toLowerCase().startsWith(partArg.toLowerCase()))
				list.add(name);
		}
	}
	
	private void addCommandCompletion(String fullArg, String partArg, List<String> list){
		if (fullArg.toLowerCase().startsWith(partArg.toLowerCase()))
			list.add(fullArg);
	}
	
	private void addCommandCompletion(String[] fullArgs, String partArg, List<String> list){
		for (int i = 0; i < fullArgs.length; i++)
			addCommandCompletion(fullArgs[i], partArg, list);
	}
	
	private void addPlayerCompletion(String partArg, List<String> list){		
		Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
		for (Iterator<? extends Player> p = players.iterator(); p.hasNext();) {
			Player player = (Player) p.next();
			String name = player.getName();
			addCommandCompletion(name, partArg, list);
		}
	}
	
	@SuppressWarnings("deprecation")
	public List<String> complete(CommandSender sender, Command command, String[] args) {
		List<String> result = new ArrayList<String>();
		if ("plot".toLowerCase().startsWith(command.getName().toLowerCase()))
		{
			if (args.length == 0){
				plugin.getLogger().info("command completion for : " + command.getName());
				addCommandCompletion("/plot", command.getName(), result);
			}else if (args.length == 1){
				addCommandCompletion(plotCmds, args[0], result);
			}else if (args.length >= 2){
				if (args[0].equalsIgnoreCase(plotCmds[0])){ 		// add
					if (args.length == 2){
						addPlayerCompletion(args[1], result);
					}
				}else if (args[0].equalsIgnoreCase(plotCmds[1])){ 	// del
					if (args.length == 2){
						addPlayerCompletion(args[1], result);
					}else if (args.length == 3){
						addPlotCompletion(Bukkit.getOfflinePlayer(args[1]), args[2], result);
					}
				}else if (args[0].equalsIgnoreCase(plotCmds[2])){	// list
					if (args.length == 2){
						addPlayerCompletion(args[1], result);
					}
				}else if (args[0].equalsIgnoreCase(plotCmds[3])){	// tp
					if (args.length == 2 && sender instanceof Player){
						addPlotCompletion((Player) sender, args[1], result);
					}else if (args.length == 3){
						addPlayerCompletion(args[2], result);
					}
				}else if (args[0].equalsIgnoreCase(plotCmds[4])){	// fav
					if (args.length == 2){
						addPlayerCompletion(args[1], result);
					}
				}else if (args[0].equalsIgnoreCase(plotCmds[5])){	// friends
					if (args.length == 2){
						addCommandCompletion(friendsCmds, args[1], result);
					}else if(args.length >= 3){
						if (args[1].equalsIgnoreCase(friendsCmds[0]) || 	// add
							args[1].equalsIgnoreCase(friendsCmds[1])){		// del
							if(args.length == 3)
								addPlayerCompletion(args[2], result);
							else if(args.length == 4)
								addPlotCompletion(Bukkit.getOfflinePlayer(args[2]), args[3], result);
						}else if(args[1].equalsIgnoreCase(friendsCmds[2])){	// list
							if(args.length == 3 && sender instanceof Player)
								addPlotCompletion((Player) sender, args[2], result);
						}
					}
				}else if (args[0].equalsIgnoreCase(plotCmds[6])){	// rename
					if (args.length == 2 && sender instanceof Player){
						addPlotCompletion((Player) sender, args[1], result);
					}
				}else if (args[0].equalsIgnoreCase(plotCmds[7])){	// help
					if (args.length == 2)
						addCommandCompletion(plotCmds, args[1], result);
					else if (args.length == 3 && 
							(args[1].equalsIgnoreCase("friend") || args[1].equalsIgnoreCase("friends")))
						addCommandCompletion(friendsCmds, args[2], result);
				}
			}
		}		
		return result;		
	}
}
