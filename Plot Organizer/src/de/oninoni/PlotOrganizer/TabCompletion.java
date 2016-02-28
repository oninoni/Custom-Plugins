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
		if (fullArg.startsWith(partArg))
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
		if (("plot").startsWith(command.getName()))
		{
			switch (args.length)
			{
			case 0:
				plugin.getLogger().info("command competion for : " + command.getName());
				addCommandCompletion("/plot", command.getName(), result);
				break;
			case 1:
				addCommandCompletion("add", args[0], result);
				addCommandCompletion("del", args[0], result);
				addCommandCompletion("list", args[0], result);
				addCommandCompletion("tp", args[0], result);
				addCommandCompletion("fav", args[0], result);
				break;
			case 2:
				
			}
		}		
		return result;		
	}
}
