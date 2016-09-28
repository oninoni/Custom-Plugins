package de.oninoni.OnionPower.Machines.HopperBased;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.inventory.InventoryClickEvent;

import de.oninoni.OnionPower.Items.CustomsItems;
import de.oninoni.OnionPower.Machines.MachineManager;

public class FluidHandler extends MachineHopper{
	
	private int FluidLevel = 0;
	
	private static final int MAXFLUIDLEVEL = 64;
	
	public FluidHandler(Location position, MachineManager machineManager, int power) {
		super(position, machineManager, power);
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				hopper.getInventory().setItem(2, CustomsItems.getTank());
				hopper.getInventory().setItem(3, CustomsItems.getCraftingCore());
				
				reOpenInventories();
			}
		}, 1L);
		SetupPowerIO();
	}
	
	public FluidHandler(Location position, MachineManager machineManager) {
		super(position, machineManager);
		SetupPowerIO();
	}
	
	private void SetupPowerIO(){
		for(int i = 0; i < 6; i++){
			allowedOutputs[i] = false;
		}
	}

	@Override
	protected void setAvailableUpgrades() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setCoreSlot() {
		coreSlot = 1;
	}

	@Override
	public String getDisplayName() {
		return "§6§lFluid Handler";
	}

	@Override
	public int getMaxPowerOutput() {
		return 0;
	}

	@Override
	public int getMaxPowerInput() {
		return 100;
	}

	@Override
	public void updateBlock() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getDesignEntityCount() {
		return 0;
	}

	@Override
	public void spawnDesignEntity(int id) {
		return;
	}

	@Override
	protected void resetItemAt(int id) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onClick(InventoryClickEvent e) {
		if(!super.onClick(e)){
			if(e.getSlot() == 0 || e.getSlot() == 4){
				if(e.getSlot() == 0){
					
				}else{
					
				}
			}else{
				e.setCancelled(true);
			}
			return false;
		}
		return true;
	}

	@Override
	protected boolean doesExplode() {
		return true;
	}
}
