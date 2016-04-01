package de.oninoni.OnionPower.Machines;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.Items.Batterod;

public class Generator extends Machine {
	
	public Generator(Location position) {
		super(position);
	}
	
	@Override
	protected boolean isMaterial(Material material) {
		return material == Material.FURNACE 
			|| material == Material.BURNING_FURNACE;
	}
	
	@Override
	public int getMaxPower() {
		return 6400;
	}
	
	@Override
	public void update() {
		
	}
	
	public static boolean canCreate(InventoryClickEvent e) {
		ItemStack item = e.getCursor();
		if (1 == e.getView().convertSlot(e.getRawSlot())){
			if (Batterod.check(item)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String getDisplayName() {
		return "§6§lGenerator";
	}
}
