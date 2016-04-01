package de.oninoni.OnionPower.Machines;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public abstract class Machine {
	
	private Location position;
	private World world;
	
	protected int power;
	
	public Machine(Location position, World world){
		this.position = position;
		this.world = world;
	}
	
	protected abstract boolean isMaterial(Material material);
	
	public abstract int getMaxEnergy();
	public abstract String getDisplayName();
	public abstract void update();

	public Location getPosition() {
		return position;
	}
	
	public World getWorld() {
		return world;
	}
	
	public int getPower() {
		return power;
	}
}
