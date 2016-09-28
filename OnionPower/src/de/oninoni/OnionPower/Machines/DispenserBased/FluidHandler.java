package de.oninoni.OnionPower.Machines.DispenserBased;

import org.bukkit.Location;

import de.oninoni.OnionPower.Machines.MachineManager;

public class FluidHandler extends MachineDispenser{

	public FluidHandler(Location position, MachineManager machineManager) {
		super(position, machineManager);
		// TODO Auto-generated constructor stub
	}
	
	public FluidHandler(Location position, MachineManager machineManager, int power) {
		super(position, machineManager, power);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void setAvailableUpgrades() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setCoreSlot() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMaxPowerOutput() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxPowerInput() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateBlock() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getDesignEntityCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void spawnDesignEntity(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void resetItemAt(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean doesExplode() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
