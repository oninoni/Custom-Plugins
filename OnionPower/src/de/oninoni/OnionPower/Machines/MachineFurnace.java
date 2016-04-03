package de.oninoni.OnionPower.Machines;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.Items.Batrod;
import de.oninoni.OnionPower.Items.PowerCore;
import de.oninoni.OnionPower.Machines.Upgrades.MachineUpgrade;

public abstract class MachineFurnace extends Machine{

	protected int rodSlot;
	protected int coreSlot;
	
	protected Furnace furnace;
	
	public MachineFurnace(Location position, MachineManager machineManager, int power, HashMap<Integer, MachineUpgrade> upgrades) {
		super(position, machineManager, power, upgrades);
		furnace = ((Furnace) position.getBlock().getState());
	}
	
	@Override
	protected boolean isMaterial(Material material) {
		return material == Material.FURNACE 
			|| material == Material.BURNING_FURNACE;
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
	public void onMoveFrom(InventoryMoveItemEvent e) {
		return;
	}
	
	@Override
	public void onMoveInto(InventoryMoveItemEvent e) {
		return;
	}
	
	@Override
	public void onBreak(BlockBreakEvent e) {
		furnace.getInventory().setItem(coreSlot, Batrod.create());
	}
	
	@Override
	public void updateDisplay() {
		ItemStack powerCore = furnace.getInventory().getItem(coreSlot);
		PowerCore.setPowerLevel(powerCore, this);
	}
	
	@Override
	public void load() {
		super.load();
		furnace = (Furnace) getPosition().getBlock().getState();
	}
}
