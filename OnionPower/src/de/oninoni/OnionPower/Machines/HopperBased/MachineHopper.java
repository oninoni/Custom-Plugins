package de.oninoni.OnionPower.Machines.HopperBased;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Hopper;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;

import de.oninoni.OnionPower.Machines.Machine;
import de.oninoni.OnionPower.Machines.MachineManager;

public abstract class MachineHopper extends Machine {

	protected Hopper getHopper(){
		return (Hopper) getInvHolder();
	}
	
	public MachineHopper(Location position, MachineManager machineManager) {
		super(position, machineManager);
	}

	public MachineHopper(OfflinePlayer owner, Location position, MachineManager machineManager, int power) {
		super(owner, position, machineManager, power);
	}

	@Override
	public int getMaxPower() {
		return 64000;
	}

	@Override
	protected boolean isMaterial(Material material) {
		return material == Material.DISPENSER;
	}

	@Override
	public void load() {
		super.load();
	}

	@Override
	public void onClose(InventoryCloseEvent e) {
		return;
	}

	@Override
	public void onMoveFrom(InventoryMoveItemEvent e) {
		return;
	}

	@Override
	public void onMoveInto(InventoryMoveItemEvent e) {
		return;
	}

	public abstract void onPickup(InventoryPickupItemEvent e);
}