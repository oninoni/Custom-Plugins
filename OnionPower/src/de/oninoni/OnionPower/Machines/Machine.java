package de.oninoni.OnionPower.Machines;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
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
	
	private boolean displayChanged;
	
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
	
	protected abstract void updateDisplay();
	
	public abstract void onBreak(BlockBreakEvent e);
	
	public void requestPower(Machine requester){
		// max input/output
		int transPower = Math.min(getMaxPowerOutput(), requester.getMaxPowerInput());	
		// max total per update
		transPower = Math.min(transPower, getMaxPowerOutput() - powerOutputTotal);
		transPower = Math.min(transPower, requester.getMaxPowerInput() - requester.powerIntputTotal);	
		// not enough to send / not enough space in requester
		transPower = Math.min(transPower, power);
		transPower = Math.min(transPower, requester.getFreeSpace());
		
		if (transPower <= 0)
			return;
		
		// send power
		power -= transPower;
		requester.power += transPower;
		
		// change power totals
		powerOutputTotal += transPower;
		requester.powerIntputTotal += transPower;	
		
		Bukkit.broadcastMessage("Transfered Power: " + transPower);
	}
	
	private int getFreeSpace() {
		return getMaxPower() - power;
	}

	private List<Machine> getNeighbours() {
		List<Machine> result = new ArrayList<>();
		for (Vector dir : directions){
			Location l = position.clone();
			l.add(dir);
			Machine m = machineManager.getMachine(l);
			if (m != null)
				result.add(m);
		}
		return result;
	}
	
	protected void requestFromNeighbours(){
		List<Machine> machines = getNeighbours();
		for (Machine machine : machines)
			machine.requestPower(this);
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

	public void resetIO() {
		powerIntputTotal = 0;
		powerOutputTotal = 0;
	}
	
	public void updateUI() {
		if (displayChanged)
			updateDisplay();
	}
		
}
