package de.oninoni.OnionPower.Machines.FurnaceBased;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.NMSAdapter;
import de.oninoni.OnionPower.Items.Batrod;
import de.oninoni.OnionPower.Items.ItemData;
import de.oninoni.OnionPower.Items.PowerCore;
import de.oninoni.OnionPower.Machines.MachineFurnace;
import de.oninoni.OnionPower.Machines.MachineManager;
import de.oninoni.OnionPower.Machines.Upgrades.Upgrade;

public class ElectricFurnace extends MachineFurnace {
	
	private Material cookingInto;
	private Material cookingFrom;
	
	public ElectricFurnace(Location position, MachineManager machineManager, int power, HashMap<Integer, Upgrade> upgrades) {
		super(position, machineManager, power, upgrades);
		rodSlot = 1;
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
	
	public ElectricFurnace(Location position, MachineManager machineManager, HashMap<Integer, Upgrade> upgrades) {
		super(position, machineManager, upgrades);
		rodSlot = 1;
	}
	
	@Override
	protected void setCoreSlot() {
		coreSlot = 1;
	}
	
	public static boolean canCreate(InventoryClickEvent e){
		if(!(e.getInventory().getType() == InventoryType.FURNACE))return false;
		ItemStack item = e.getCursor();
		if(!(e.getView().getTopInventory().getItem(0) == null && e.getView().getTopInventory().getItem(1) == null && e.getView().getTopInventory().getItem(0) == null))return false;
		return e.getRawSlot() == e.getView().convertSlot(e.getRawSlot()) && e.getRawSlot() == 1 && Batrod.check(item);
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
			if(furnace.getCookTime() <= 0 && ItemData.smeltable.containsKey(smelting.getType())){
				Material result = ItemData.smeltable.get(smelting.getType());
				if((furnace.getInventory().getResult() == null || furnace.getInventory().getResult().getType() == result) && power >= 2000){
					furnace.setBurnTime((short) 200);
					cookingInto = result;
					cookingFrom = smelting.getType();
				}else{
					furnace.setBurnTime((short) 0);
					cookingInto = null;
					cookingFrom = null;
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
		super.onClick(e);
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

	@Override
	public boolean onBoom(Block e) {
		furnace.getInventory().clear();
		furnace.getInventory().addItem(Batrod.create());
		return true;
	}
}
