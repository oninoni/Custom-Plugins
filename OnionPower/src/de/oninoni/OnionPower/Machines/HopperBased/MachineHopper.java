package de.oninoni.OnionPower.Machines.HopperBased;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Hopper;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;

import de.oninoni.OnionPower.NMSAdapter;
import de.oninoni.OnionPower.Machines.Machine;
import de.oninoni.OnionPower.Machines.MachineManager;

public abstract class MachineHopper extends Machine {

	Hopper hopper;

	public MachineHopper(Location position, MachineManager machineManager, int power) {
		super(position, machineManager, power);
		hopper = (Hopper) invHolder;
		NMSAdapter.setInvNameHopper(hopper, getDisplayName());
	}

	public MachineHopper(Location position, MachineManager machineManager) {
		super(position, machineManager);
		hopper = (Hopper) invHolder;
		NMSAdapter.setInvNameHopper(hopper, getDisplayName());
	}

	public abstract void onPickup(InventoryPickupItemEvent e);

	@Override
	protected boolean isMaterial(Material material) {
		return material == Material.DISPENSER;
	}

	@Override
	public int getMaxPower() {
		return 64000;
	}

	public boolean onClick(InventoryClickEvent e) {
		return super.onClick(e);
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
	public void onClose(InventoryCloseEvent e) {
		return;
	}

	@Override
	public void load() {
		super.load();
		hopper = (Hopper) invHolder;
	}
}