package de.oninoni.OnionPower.Machines;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Dispenser;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.Items.PowerCore;
import de.oninoni.OnionPower.Machines.Upgrades.Upgrade;

public abstract class MachineDispenser extends Machine{
	
	protected final static int[] directionAdapter = {4,1,5,2,3,0};
	
	protected int coreSlot;
	
	protected Dispenser dispenser;
	
	public MachineDispenser(Location position, MachineManager machineManager, int power, HashMap<Integer, Upgrade> upgrades) {
		super(position, machineManager, power, upgrades);
		dispenser = ((Dispenser) position.getBlock().getState());
	}
	
	@Override
	protected boolean isMaterial(Material material) {
		return material == Material.DISPENSER;
	}
	
	@Override
	public int getMaxPower() {
		return 64000;
	}
	
	@Override
	public void onClick(InventoryClickEvent e) {
		int convertSlot = e.getView().convertSlot(e.getRawSlot());
		if(e.getRawSlot() == convertSlot && convertSlot == coreSlot){
			e.setCancelled(true);
		}
	}
	
	@Override
	protected void updateDisplay() {
		ItemStack powerCore = dispenser.getInventory().getItem(coreSlot);
		PowerCore.setPowerLevel(powerCore, this);
	}
	
	@Override
	public void load() {
		super.load();
		dispenser = (Dispenser) getPosition().getBlock().getState();
	}
}
