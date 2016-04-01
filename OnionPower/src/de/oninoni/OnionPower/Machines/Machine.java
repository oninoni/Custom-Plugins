package de.oninoni.OnionPower.Machines;

import org.bukkit.Location;
import org.bukkit.Material;

import de.oninoni.OnionPower.OnionPower;

public abstract class Machine {
	
	protected static OnionPower plugin = OnionPower.get();
	
	private Location position;
	
	protected int power;
	
	public Machine(Location position){
		this.position = position;
	}
	
	protected abstract boolean isMaterial(Material material);
	
	public abstract int getMaxPower();
	public abstract String getDisplayName();
	public abstract void update();

	public Location getPosition() {
		return position;
	}
	
	public int getPower() {
		return power;
	}
}
