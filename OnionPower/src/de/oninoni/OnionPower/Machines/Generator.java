package de.oninoni.OnionPower.Machines;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.Items.Batterod;

public class Generator extends Machine {
	
	public Generator(Location position, World world) {
		super(position, world);
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
	
	public static boolean tryCreation(InventoryClickEvent e) {
		ItemStack item = e.getCursor();
		if (1 == e.getView().convertSlot(e.getRawSlot()))
		{
			if (Batterod.check(item)){
				if (item.getAmount() > 1){
					item.setAmount(item.getAmount() - 1);				
				}
				
				Bukkit.broadcastMessage("Creation successfull theoretically");
				
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
