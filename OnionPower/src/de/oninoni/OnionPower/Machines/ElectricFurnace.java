package de.oninoni.OnionPower.Machines;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Furnace;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.NMSAdapter;
import de.oninoni.OnionPower.Items.Batrod;
import de.oninoni.OnionPower.Items.PowerCore;

public class ElectricFurnace extends MachineFurnace {
	public ElectricFurnace(Location position, MachineManager machineManager) {
		super(position, machineManager);
		rodSlot = 1;
		coreSlot = 1;
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
		if(!(e.getInventory().getItem(0) == null && e.getInventory().getItem(1) == null && e.getInventory().getItem(0) == null))return false;
		return 1 == e.getView().convertSlot(e.getRawSlot()) && Batrod.check(item);
	}
	
	@Override
	public String getDisplayName() {
		return "§6§lElectrical Furnace";
	}

	@Override
	public int getMaxPowerInput() {
		return 40;
	}

	@Override
	public void update() {
		requestFromConnected();
	}

	@Override
	public void onClick(InventoryClickEvent e) {		
		int convertSlot = e.getView().convertSlot(e.getRawSlot());
		if(e.getRawSlot() == convertSlot && convertSlot == 1){
			e.setCancelled(true);
		}
	}	
	
	@Override
	public void onBreak(BlockBreakEvent e) {
		furnace.getInventory().setItem(1, Batrod.create());
	}
	
	@Override
	public void updateDisplay() {
		ItemStack powerCore = furnace.getInventory().getFuel();
		PowerCore.setPowerLevel(powerCore, this);
	}

	@Override
	public int getMaxPowerOutput() {
		// TODO Auto-generated method stub
		return 0;
	}
}
