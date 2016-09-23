package de.oninoni.OnionPower.Machines;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

import de.oninoni.OnionPower.NMSAdapter;
import de.oninoni.OnionPower.Machines.Upgrades.Upgrade;

public abstract class MachineFurnace extends Machine{
	
	protected Furnace furnace;
	
	public MachineFurnace(Location position, MachineManager machineManager, int power, HashMap<Integer, Upgrade> upgrades) {
		super(position, machineManager, power, upgrades);
		furnace = ((Furnace) position.getBlock().getState());
		NMSAdapter.setInvNameFurnace(furnace, getDisplayName());
	}
	
	public MachineFurnace(Location position, MachineManager machineManager, HashMap<Integer, Upgrade> upgrades) {
		super(position, machineManager, upgrades);
		furnace = ((Furnace) position.getBlock().getState());
		NMSAdapter.setInvNameFurnace(furnace, getDisplayName());
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
		if(convertSlot == coreSlot){
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
	public void onClose(InventoryCloseEvent e){
		return;
	}
	
	@Override
	public void load() {
		super.load();
		furnace = (Furnace) getPosition().getBlock().getState();
	}
}
