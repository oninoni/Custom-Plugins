package de.oninoni.PlotOrganizer;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;

public class Plot {
	
	private GridPosition gridPosition;
	private ProtectedCuboidRegion protectedCuboidRegion;
	
	private String name;
	
	PlotOrganizer plugin;
	
	public Plot(GridPosition gp, PlotOrganizer plugin, OfflinePlayer owner){
		this.gridPosition = gp;		
		name = "";
		this.plugin = plugin;
		protectedCuboidRegion = new ProtectedCuboidRegion(
				owner.getName(), 
				new BlockVector(gp.getX()*128, 0, gp.getY()*128),
				new BlockVector(gp.getX()*128 + 127, 255, gp.getY()*128 + 127)
				);
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
		return Bukkit.getOfflinePlayer((UUID) protectedCuboidRegion.getOwners().getUniqueIds().toArray()[0]);		
	}
}
