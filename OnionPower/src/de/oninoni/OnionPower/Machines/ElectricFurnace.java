package de.oninoni.OnionPower.Machines;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.NMSAdapter;
import de.oninoni.OnionPower.Items.Batterod;
import de.oninoni.OnionPower.Items.PowerCore;

public class ElectricFurnace extends Machine {

	Furnace furnace;
	
	public ElectricFurnace(Location position, MachineManager machineManager) {
		super(position, machineManager);
		ItemStack powerCore = PowerCore.create(this);
		
		furnace = ((Furnace) position.getBlock().getState());
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				furnace.getInventory().setItem(1, powerCore);
				NMSAdapter.setInvNameFurnace(furnace, getDisplayName());
				for(HumanEntity viewer : furnace.getInventory().getViewers()){
					viewer.closeInventory();
					viewer.openInventory(furnace.getInventory());
				}
			}
		}, 1L);
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
		int convertSlot = e.getView().convertSlot(e.getRawSlot());
		if(e.getRawSlot() == convertSlot && convertSlot == 1){
			e.setCancelled(true);
		}
	}

	@Override
	public void onMoveInto(InventoryMoveItemEvent e) {
		//Auch ok!
	}

	@Override
	public void onMoveFrom(InventoryMoveItemEvent e) {
		//Ist ok!
	}

	@Override
	public void onBreak(BlockBreakEvent e) {
		furnace.getInventory().setItem(1, Batterod.create());
	}

}
