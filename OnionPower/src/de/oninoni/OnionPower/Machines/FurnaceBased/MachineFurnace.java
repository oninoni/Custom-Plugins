package de.oninoni.OnionPower.Machines.FurnaceBased;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

import de.oninoni.OnionPower.NMSAdapter;
import de.oninoni.OnionPower.Machines.Machine;
import de.oninoni.OnionPower.Machines.MachineManager;

public abstract class MachineFurnace extends Machine {

	protected Furnace furnace;

	public MachineFurnace(Location position, MachineManager machineManager) {
		super(position, machineManager);
		furnace = ((Furnace) position.getBlock().getState());
		NMSAdapter.setInvNameFurnace(furnace, getDisplayName());
	}

	public MachineFurnace(Location position, MachineManager machineManager, int power) {
		super(position, machineManager, power);
		furnace = ((Furnace) position.getBlock().getState());
		NMSAdapter.setInvNameFurnace(furnace, getDisplayName());
	}

	@Override
	public int getMaxPower() {
		return 64000;
	}

	@Override
	protected boolean isMaterial(Material material) {
		return material == Material.FURNACE || material == Material.BURNING_FURNACE;
	}

	@Override
	public void load() {
		super.load();
		furnace = (Furnace) getPosition().getBlock().getState();
	}

	@Override
	public boolean onClick(InventoryClickEvent e) {
		return super.onClick(e);
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
}
