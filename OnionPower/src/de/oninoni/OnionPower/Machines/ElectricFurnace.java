package de.oninoni.OnionPower.Machines;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.NMSAdapter;
import de.oninoni.OnionPower.Items.Batrod;
import de.oninoni.OnionPower.Items.ItemData;
import de.oninoni.OnionPower.Items.PowerCore;
import de.oninoni.OnionPower.Machines.Upgrades.Upgrade;

public class ElectricFurnace extends MachineFurnace {
	
	private Material cookingInto;
	private Material cookingFrom;
	
	public ElectricFurnace(Location position, MachineManager machineManager, int power, HashMap<Integer, Upgrade> upgrades) {
		super(position, machineManager, power, upgrades);
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
	public void updateBlock() {
		requestFromConnected();
		if(furnace.getInventory().getSmelting() != null){
			ItemStack smelting = furnace.getInventory().getSmelting();
			if(furnace.getBurnTime() <= 0 && ItemData.smeltable.containsKey(smelting.getType())){
				Material result = ItemData.smeltable.get(smelting.getType());
				if((furnace.getInventory().getResult() == null || furnace.getInventory().getResult().getType() == result) && power >= 2000){
					furnace.setBurnTime((short) 200);
					cookingInto = result;
					cookingFrom = smelting.getType();
				}
			}
		}
		if(furnace.getBurnTime() > 0 ){
			if(furnace.getInventory().getSmelting()!= null && furnace.getInventory().getSmelting().getType() != cookingFrom){
				
			}
			if(furnace.getInventory().getSmelting() == null || furnace.getInventory().getSmelting().getType() != cookingFrom){
				furnace.setBurnTime((short) 0);
				furnace.setCookTime((short) 0);
				cookingInto = null;
				cookingFrom = null;
			}else{
				power -= 20;
				powerOutputTotal = 20;
				furnace.setBurnTime((short) 200);
			}
		}
		if(furnace.getInventory().getSmelting() == null && (furnace.getBurnTime() != 0 || furnace.getCookTime() != 0) && cookingInto != null){
			furnace.setBurnTime((short) 0);
			furnace.setCookTime((short) 0);
		}
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
	public int getMaxPowerInput() {
		return 100;
	}
	
	@Override
	public int getMaxPowerOutput() {
		return 0;
	}
}
