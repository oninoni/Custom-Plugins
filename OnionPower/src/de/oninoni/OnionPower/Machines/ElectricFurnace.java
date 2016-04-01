package de.oninoni.OnionPower.Machines;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

public class ElectricFurnace extends Machine{

	public ElectricFurnace(Location position, MachineManager machineManager) {
		super(position, machineManager);
	}
	
	public boolean canCreate(InventoryClickEvent e){
		
		return false;
	}

	@Override
	protected boolean isMaterial(Material material) {
		return material == Material.FURNACE 
			|| material == Material.BURNING_FURNACE;
	}

	@Override
	public int getMaxPower() {
		return 6400;
	}

	@Override
	public String getDisplayName() {
		return "§6§lElectric Furnace";
	}

	@Override
	public int getMaxPowerOutput() {
		return 0;
	}

	@Override
	public int getMaxPowerInput() {
		return 4;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(InventoryClickEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMoveInto(InventoryMoveItemEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMoveFrom(InventoryMoveItemEvent e) {
		// TODO Auto-generated method stub
		
	}

}
