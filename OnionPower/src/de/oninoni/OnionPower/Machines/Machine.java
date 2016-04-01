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
	protected abstract int getMaxEnergy();
	
	public abstract void Update();

	public Location getPosition() {
		return position;
	}
	
	public World getWorld() {
		return world;
	}
}
