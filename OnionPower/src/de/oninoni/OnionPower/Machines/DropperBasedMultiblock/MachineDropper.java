package de.oninoni.OnionPower.Machines.DropperBasedMultiblock;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Dropper;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

import de.oninoni.OnionPower.Machines.Machine;
import de.oninoni.OnionPower.Machines.MachineManager;

public abstract class MachineDropper extends Machine {

	protected final static int[] directionAdapter = { 4, 1, 5, 2, 3, 0 };

	protected Dropper dropper;

	public MachineDropper(Location position, MachineManager machineManager) {
		super(position, machineManager);
		dropper = ((Dropper) position.getBlock().getState());
	}

	public MachineDropper(Location position, MachineManager machineManager, int power) {
		super(position, machineManager, power);
		dropper = (Dropper) invHolder;
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
		dropper = (Dropper) invHolder;
	}

	@Override
	public void onClose(InventoryCloseEvent e) {
		return;
	}

	public void onDispense(BlockDispenseEvent e) {
		e.setCancelled(true);
	}

	@Override
	public void onMoveFrom(InventoryMoveItemEvent e) {
		return;
	}

	@Override
	public void onMoveInto(InventoryMoveItemEvent e) {
		return;
	}
}
