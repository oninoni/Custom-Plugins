package de.oninoni.OnionPower.Machines;

import org.bukkit.Bukkit;
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
		ItemStack powerCore = PowerCore.create(this);
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				furnace = ((Furnace) position.getBlock().getState());
				furnace.getInventory().setItem(0, new ItemStack(Material.AIR));
				furnace.getInventory().setItem(2, powerCore);
				NMSAdapter.setInvNameFurnace(furnace, getDisplayName());
			}
		}, 0L);
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
