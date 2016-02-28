package de.oninoni.PlotOrganizer;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;

public class Plot {
	
	private static int PLOT_SIZE = 128;
	
	private GridPosition gridPosition;
	private String name;
	
	/* TODO: Plots
	 * add Friends to plot oder machen wir das einfach über dir protection?
	*/
	
	private ProtectedCuboidRegion protectedCuboidRegion;
	
	private PlotOrganizer plugin;
	
	public Plot(GridPosition gp, PlotOrganizer pl, OfflinePlayer owner, int id){
		gridPosition = gp;		
		name = "";
		plugin = pl;
		
		if(plugin.getWorldGuard().getRegionManager(plugin.getPlotWorld()).hasRegion(getPlotName(id))){
			plugin.getLogger().info(getPlotName(id));
			
			protectedCuboidRegion = (ProtectedCuboidRegion) plugin.getWorldGuard().getRegionManager(plugin.getPlotWorld()).getRegion(getPlotName(id));
			if(protectedCuboidRegion.getOwners().getPlayers().toArray().length == 0){
				DefaultDomain owners = new DefaultDomain();
				owners.addPlayer(owner.getUniqueId());
				
				protectedCuboidRegion.setOwners(owners);
			}
		}else{
			protectedCuboidRegion = new ProtectedCuboidRegion(
				getPlotName(id),
				new BlockVector(gp.getX() * PLOT_SIZE, 1, gp.getY() * PLOT_SIZE),
				new BlockVector((gp.getX() + 1) * PLOT_SIZE - 1, 255, (gp.getY() + 1) * PLOT_SIZE - 1)
			);
			DefaultDomain owners = new DefaultDomain();
			owners.addPlayer(owner.getUniqueId());
			
			protectedCuboidRegion.setOwners(owners);
			
			RegionManager regionManager = plugin.getWorldGuard().getRegionManager(plugin.getPlotWorld());
			regionManager.addRegion(protectedCuboidRegion);
			
			try {
				regionManager.save();
			} catch (Exception e) {
			     plugin.getLogger().warning("Failed to write region: "  + e.getMessage() );
			}
		}
		
		
	}
	
	public GridPosition getGridPosition() {
		return gridPosition;
	}
	
	public ProtectedCuboidRegion getProtectedCuboidRegion() {
		return protectedCuboidRegion;
	}
	
	public void teleportTo(Player p){
		p.teleport(new Location(
			plugin.getPlotWorld(), 
			(gridPosition.getX() + 0.5) * PLOT_SIZE, 
			65,
			(gridPosition.getY() + 0.5) * PLOT_SIZE
		));
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
	
	static String getPlotName(int id) {
		return "plot_" + id;
	}
}
