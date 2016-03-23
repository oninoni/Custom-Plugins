package de.oninoni.OninoniUtil;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import de.oninoni.OninoniUtil.Listener.PlayerJoinListener;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class OninoniUtil extends JavaPlugin{
	
	private PermissionsEx permissionsEx;
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String cmd = command.getName();
		Player p = null;
		
		if (sender instanceof Player){
			p = (Player) sender;
		}
		
		if (cmd.equalsIgnoreCase("home")){
			if (p != null){
				if (checkSky(p)){
					if(p.getBedSpawnLocation() != null){
						p.teleport(p.getBedSpawnLocation().add(new Vector(0.5, 0, 0.5)));
						p.sendMessage("§6Teleported to your home");
					}else{
						p.sendMessage("§6You don't have a home!\nSleep in a bed that is not obstructed to set your home!");
					}
				}else{
					p.sendMessage("§6Sky connection obstructed");
				}
				return true;
			}
		}else if (cmd.equalsIgnoreCase("spawn")){
			if (p != null){
				if (checkSky(p)){
					p.teleport(getWorld().getSpawnLocation().add(new Vector(0.5, 0, 0.5)));
					p.sendMessage("§6Teleported to the world spawn");
				}else{
					p.sendMessage("§6Sky connection obstructed");
				}
				return true;
			}
		}		
		return false;
	}
	
	private World getWorld(){
		return getServer().getWorld("world");
	}
	
	private boolean checkSky(Player p){
		return getWorld().getBlockAt(p.getLocation()).getLightFromSky() > 0;
	}

	public void onEnable() {
		permissionsEx = (PermissionsEx) getServer().getPluginManager().getPlugin("PermissionsEx");
		if(permissionsEx != null){
			getLogger().info("PermissionsEx detected!");
		}else{
			getLogger().warning("PermissionsEx missing!");
		}
		
		PlayerJoinListener playerJoinListener = new PlayerJoinListener(this);
		getServer().getPluginManager().registerEvents(playerJoinListener, this);
		
	}
	
	public void onDisable() {
		
	}
	
	public PermissionsEx getPermissionsEx() {
		return permissionsEx;
	}
}
