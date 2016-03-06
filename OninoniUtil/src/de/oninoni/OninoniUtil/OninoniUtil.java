package de.oninoni.OninoniUtil;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class OninoniUtil extends JavaPlugin{
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String cmd = command.toString();
		Player p = null;
		
		if (sender instanceof Player){
			p = (Player) sender;
		}
		
		if (cmd.equalsIgnoreCase("home")){
			if (p != null){
				if (checkSky(p)){
					p.teleport(p.getBedSpawnLocation());
					p.sendMessage("§6Teleported to home");
				}else{
					p.sendMessage("§6Sky connection obstucted");
				}
			}			
		}else if (cmd.equalsIgnoreCase("spawn")){
			if (p != null){
				if (checkSky(p)){
					p.teleport(getWorld().getSpawnLocation());
					p.sendMessage("§6Teleported to spawn");
				}else{
					p.sendMessage("§6Sky connection obstucted");
				}
			}
		}
		
		return false;
	}
	
	private World getWorld(){
		return getServer().getWorld("world");
	}
	
	private boolean checkSky(Player p){
		return getWorld().getBlockAt(p.getLocation()).getLightFromSky() == 15;
	}
	
}
