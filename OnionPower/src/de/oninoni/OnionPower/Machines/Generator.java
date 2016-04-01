package de.oninoni.OnionPower.Machines;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.NMSAdapter;
import de.oninoni.OnionPower.Items.Batterod;
import de.oninoni.OnionPower.Items.PowerCore;

public class Generator extends Machine {
	
	private Furnace furnace;
	
	public Generator(Location position) {
		super(position);
		furnace = ((Furnace) position.getBlock().getState());
		furnace.getInventory().setItem(0, PowerCore.create(this));
		NMSAdapter.setInvNameFurnace(furnace, getDisplayName());
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
		if (0 == e.getView().convertSlot(e.getRawSlot())){
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
