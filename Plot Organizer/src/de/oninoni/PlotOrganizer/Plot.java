package de.oninoni.PlotOrganizer;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldedit.world.DataException;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;

@SuppressWarnings("deprecation")
public class Plot {
	
	private static int PLOT_SIZE = 128;
	
	private GridPosition gridPosition;
	private String name;
	
	/* TODO: Plot Spawnpoint changeable
	 * 
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
				protectedCuboidRegion.getOwners().addPlayer(plugin.getWorldGuard().wrapOfflinePlayer(owner));
			}
		}else{
			protectedCuboidRegion = new ProtectedCuboidRegion(
				getPlotName(id),
				new BlockVector(gp.getX() * PLOT_SIZE + 4, 1, gp.getY() * PLOT_SIZE + 4),
				new BlockVector((gp.getX() + 1) * PLOT_SIZE - 5, 255, (gp.getY() + 1) * PLOT_SIZE - 5)
			);
			protectedCuboidRegion.getOwners().addPlayer(plugin.getWorldGuard().wrapOfflinePlayer(owner));
			
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
	
	/**
	 * Adds a Player to the Region as a Member
	 * @param player The Player that gets added
	 * @return false means player is already Member
	 * 
	 */
	public boolean addMember(OfflinePlayer player){
		Set<UUID> members = getMembers();
		if(members.contains(player.getUniqueId())){
			return false;
		}
		protectedCuboidRegion.getMembers().addPlayer(plugin.getWorldGuard().wrapOfflinePlayer(player));
		return true;
	}
	
	/**
	 * Removes a Player from the Region as a Member
	 * @param player The Player that gets removed
	 * @return false means player is not Member
	 */
	public boolean removeMember(OfflinePlayer player){
		Set<UUID> members = getMembers();
		if(!members.contains(player.getUniqueId())){
			return false;
		}
		protectedCuboidRegion.getMembers().removePlayer(plugin.getWorldGuard().wrapOfflinePlayer(player));
		return true;
	}
	
	/**
	 * Returns all the Members as a Set<UUID>
	 * @return Members as Set<UUID>
	 */
	public Set<UUID> getMembers(){
		return protectedCuboidRegion.getMembers().getUniqueIds();
	}
	
	public void pasteSchematic(){
		try{
			File dir = new File(plugin.getDataFolder(), "plot.schematic");
			
			EditSession editSession = new EditSession(new BukkitWorld(plugin.getPlotWorld()), 128*128*3*2);
			editSession.enableQueue();
			
			SchematicFormat schematic = SchematicFormat.getFormat(dir);
			CuboidClipboard clipboard = schematic.load(dir);
			
			clipboard.paste(editSession, new Vector(gridPosition.getX()*128, 64, gridPosition.getY()*128), false);
			editSession.flushQueue();
		}catch (DataException | IOException ex) {
            ex.printStackTrace();
        } catch (MaxChangedBlocksException ex) {
            ex.printStackTrace();
        }
	}
	
	static String getPlotName(int id) {
		return "plot_" + id;
	}
}
