package de.oninoni.PlotOrganizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TabCompletion {
	
	private PlotOrganizer plugin;
	
	public TabCompletion(PlotOrganizer pl){
		plugin = pl;
	}
	
	private void addCommandCompletion(String fullArg, String partArg, List<String> list){
		if (fullArg.toLowerCase().startsWith(partArg.toLowerCase()))
			list.add(fullArg);
	}
	
	private void addPlayerCompletion(String partArg, List<String> list){		
		Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
		for (Iterator<? extends Player> p = players.iterator(); p.hasNext();) {
			Player player = (Player) p.next();
			String name = player.getName();
			addCommandCompletion(name, partArg, list);
		}
	}

	public List<String> complete(CommandSender sender, Command command, String[] args) {
		List<String> result = new ArrayList<String>();
		if ("plot".toLowerCase().startsWith(command.getName().toLowerCase()))
		{
			if (args.length == 0){
				plugin.getLogger().info("command completion for : " + command.getName());
				addCommandCompletion("/plot", command.getName(), result);
			}else if (args.length == 1){
				addCommandCompletion("add", args[0], result);
				addCommandCompletion("del", args[0], result);
				addCommandCompletion("list", args[0], result);
				addCommandCompletion("tp", args[0], result);
				addCommandCompletion("fav", args[0], result);
			}else if (args.length >= 2){
				if (args[0].equalsIgnoreCase("add")){
					if (args.length == 2){
						addPlayerCompletion(args[1], result);
					}
				}else if (args[0].equalsIgnoreCase("del")){
					
				}else if (args[0].equalsIgnoreCase("list")){
					
				}else if (args[0].equalsIgnoreCase("tp")){
					
				}else if (args[0].equalsIgnoreCase("fav")){
					
				}
			}
		}		
		return result;		
	}
}
