package de.oninoni.OnionPower.Machines;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class Generator extends Machine {

	public Generator(Vector position, World world) {
		super(position, world);
	}

	@Override
	protected boolean isMaterial(Material material) {
		return material == Material.FURNACE 
			|| material == Material.BURNING_FURNACE;
	}

	@Override
	protected int getMaxEnergy() {
		return 6400;
	}

	@Override
	public void Update() {
		
	}
}
