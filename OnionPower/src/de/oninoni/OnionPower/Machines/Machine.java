package de.oninoni.OnionPower.Machines;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.util.Vector;

import de.oninoni.OnionPower.OnionPower;

public abstract class Machine {
	
	protected static OnionPower plugin = OnionPower.get();
	
	private final Vector[] directions = {
			new Vector( 1,  0,  0),
			new Vector(-1,  0,  0),
			new Vector( 0,  1,  0),
			new Vector( 0, -1,  0),
			new Vector( 0,  0,  1),
			new Vector( 0,  0, -1)
	};
	
	private MachineManager machineManager;
	
	private Location position;
	
	protected int power;
	protected int powerIntputTotal, powerOutputTotal;
	
	public Machine(Location position, MachineManager machineManager){
		this.position = position;
		this.machineManager = machineManager;
	}
	
	protected abstract boolean isMaterial(Material material);
	
	public abstract int getMaxPower();
	public abstract String getDisplayName();
	public abstract int getMaxPowerOutput();
	public abstract int getMaxPowerInput();
	public abstract void update();
	
	public abstract void onClick(InventoryClickEvent e);
	public abstract void onMoveInto(InventoryMoveItemEvent e);
	public abstract void onMoveFrom(InventoryMoveItemEvent e);
	
	public abstract void onBreak(BlockBreakEvent e);
	
	public int requestPower(int powerRequested){
		int powerOutput = Math.min(powerRequested, Math.min(power, getMaxPowerOutput()));
		power -= powerOutput;
		powerOutputTotal += powerOutput;
		return powerOutput;
	}
	
	private List<Machine> getNeighbours() {
		List<Machine> result = new ArrayList<>();
		for (Vector dir : directions){
			Machine m = machineManager.getMachine(position.add(dir));
			result.add(m);
		}
		return result;
	}
	
	protected void requestFromNeighbours(){	
		int powerInput = 0;
		int powerRequested = Math.min(getMaxPower() - power, getMaxPowerInput());
		List<Machine> machines = getNeighbours();
		for (Machine machine : machines) {
			powerInput += machine.requestPower(powerRequested - powerInput);
		}
		
		powerIntputTotal += powerInput;
		power += powerInput;
	}

	public Location getPosition() {
		return position;
	}
	
	public int getPower() {
		return power;
	}
	
	public int getPowerIntputTotal() {
		return powerIntputTotal;
	}
	
	public int getPowerOutputTotal() {
		return powerOutputTotal;
	}
}
