package de.oninoni.OnionPower.Machines;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.Items.Batterod;

public class ElectricFurnace extends Machine {

	public ElectricFurnace(Location position, MachineManager machineManager) {
		super(position, machineManager);
	}
	
	public static boolean canCreate(InventoryClickEvent e){
		ItemStack item = e.getCursor();
		return 1 == e.getView().convertSlot(e.getRawSlot()) && Batterod.check(item);
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
		return "§6§lElectrical Furnace";
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
		powerIntputTotal = 0;
		powerOutputTotal = 0;
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
