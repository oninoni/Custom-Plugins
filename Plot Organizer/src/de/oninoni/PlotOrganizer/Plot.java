package de.oninoni.PlotOrganizer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;

public class Plot {
	
	private GridPosition gridPosition;
	private ProtectedCuboidRegion protectedCuboidRegion;
	
	private String name;
	
	PlotOrganizer plugin;
	
	public Plot(GridPosition gp, PlotOrganizer plugin){
		this.gridPosition = gp;		
		name = "";
		this.plugin = plugin;
		//TODO Implement Region creation
	}
	
	public GridPosition getGridPosition() {
		return gridPosition;
	}
	
	public ProtectedCuboidRegion getProtectedCuboidRegion() {
		return protectedCuboidRegion;
	}
	
	public void teleportTo(Player p){
		p.teleport(new Location(plugin.getPlotWorld(), gridPosition.getX()*128, 4, gridPosition.getX()*128));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public OfflinePlayer getOwner(){
		return Bukkit.getOfflinePlayer(protectedCuboidRegion.getOwners().getUniqueIds().);		
	}
}
