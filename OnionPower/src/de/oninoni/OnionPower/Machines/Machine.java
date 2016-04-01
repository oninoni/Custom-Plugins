package de.oninoni.OnionPower.Machines;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

public abstract class Machine {
	
	private Vector position;
	private World world;
	
	protected int power;
	
	public Machine(Vector position, World world){
		this.position = position;
		this.world = world;
	}
	
	protected abstract boolean isMaterial(Material material);
	protected abstract int getMaxEnergy();
	
	public abstract void Update();

	public Vector getPosition() {
		return position;
	}
	
	public World getWorld() {
		return world;
	}
}
