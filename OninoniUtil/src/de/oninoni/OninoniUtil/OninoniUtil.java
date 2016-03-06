package de.oninoni.OninoniUtil;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

public class OninoniUtil extends JavaPlugin{
	
	private HashMap<OfflinePlayer, Location> homes;
	
	private void loadHomes(){
		homes = new HashMap<OfflinePlayer, Location>();
		
		
	}
	
	private void saveHomes(){
		
	}
	
	public void onEnable() {
		loadHomes();
	}
	
	public void onDisable() {
		saveHomes();
	}
}