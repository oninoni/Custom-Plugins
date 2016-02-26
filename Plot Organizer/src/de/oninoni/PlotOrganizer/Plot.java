package de.oninoni.PlotOrganizer;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;

public class Plot {
	
	private static int PLOT_SIZE = 128;
	
	private GridPosition gridPosition;
	private ProtectedCuboidRegion protectedCuboidRegion;
	
	private String name;
	
	PlotOrganizer plugin;
	
	public Plot(GridPosition gp, PlotOrganizer pl, OfflinePlayer owner){
		gridPosition = gp;		
		name = owner.getPlayer().getName() + "'s Plot";
		plugin = pl;
		protectedCuboidRegion = new ProtectedCuboidRegion(
				owner.getName(), 
				new BlockVector(gp.getX() * PLOT_SIZE, 0, gp.getY() * PLOT_SIZE),
				new BlockVector((gp.getX() + 1) * PLOT_SIZE - 1, 255, (gp.getY() + 1) * PLOT_SIZE - 1)
				);
	}
	
	public GridPosition getGridPosition() {
		return gridPosition;
	}
	
	public ProtectedCuboidRegion getProtectedCuboidRegion() {
		return protectedCuboidRegion;
	}
	
	public void teleportTo(Player p){
		p.teleport(new Location(plugin.getPlotWorld(), 
								gridPosition.getX() * PLOT_SIZE, 
								4, 
								gridPosition.getY() * PLOT_SIZE));
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
