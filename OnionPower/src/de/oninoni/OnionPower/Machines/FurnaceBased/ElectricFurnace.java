package de.oninoni.OnionPower.Machines.FurnaceBased;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.Items.Batrod;
import de.oninoni.OnionPower.Items.ItemData;
import de.oninoni.OnionPower.Items.PowerCore;
import de.oninoni.OnionPower.Machines.MachineFurnace;
import de.oninoni.OnionPower.Machines.MachineManager;

public class ElectricFurnace extends MachineFurnace {
	
	private Material cookingInto;
	private Material cookingFrom;
	
	public ElectricFurnace(Location position, MachineManager machineManager, int power) {
		super(position, machineManager, power);
		
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				furnace.getInventory().setItem(0, new ItemStack(Material.AIR));
				furnace.getInventory().setItem(1, powerCore);

				reOpenInventories();
			}
		}, 1L);
	}
	
	public ElectricFurnace(Location position, MachineManager machineManager) {
		super(position, machineManager);
	}
	
	@Override
	protected void setCoreSlot() {
		coreSlot = 1;
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
	public boolean onClick(InventoryClickEvent e) {		
		return super.onClick(e);
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
	protected void resetItemAt(int id) {
		if(id == coreSlot){
			ItemStack batrod = Batrod.create();
			Batrod.setPower(batrod, getPower());
			furnace.getInventory().setItem(id, batrod);
		}
	}
	
	@Override
	protected boolean doesExplode() {
		return true;
	}

	@Override
	public int getDesignEntityCount() {
		return 0;
	}

	@Override
	public void spawnDesignEntity(int id) {
		
	}

	@Override
	protected void setAvailableUpgrades() {
		// TODO Auto-generated method stub
		
	}
}
