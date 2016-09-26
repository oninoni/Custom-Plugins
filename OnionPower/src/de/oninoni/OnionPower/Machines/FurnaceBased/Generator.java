package de.oninoni.OnionPower.Machines.FurnaceBased;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.Items.Batrod;
import de.oninoni.OnionPower.Items.ItemData;
import de.oninoni.OnionPower.Machines.MachineFurnace;
import de.oninoni.OnionPower.Machines.MachineManager;
import de.oninoni.OnionPower.Machines.Upgrades.Upgrade;

public class Generator extends MachineFurnace {
	
	private boolean disableSmeltingSlot = false;
	
	public Generator(Location position, MachineManager machineManager, int power, HashMap<Integer, Upgrade> upgrades) {
		super(position, machineManager, power, upgrades);
		
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				furnace.getInventory().setItem(1, new ItemStack(Material.AIR));
				furnace.getInventory().setItem(coreSlot, powerCore);

				reOpenInventories();
			}
		}, 1L);
	}
	
	public Generator(Location position, MachineManager machineManager, HashMap<Integer, Upgrade> upgrades){
		super(position, machineManager, upgrades);
	}
	
	@Override
	protected void setCoreSlot() {
		coreSlot = 2;
	}
	
	@Override
	public void updateBlock() {
		if (furnace.getBurnTime() <= 0) {
			ItemStack fuel = furnace.getInventory().getFuel();
			if(Batrod.check(fuel))return;
			if (fuel != null) {
				Material mat = fuel.getType();
				if (ItemData.burnTime.containsKey(mat) && ItemData.burnTime.get(mat) + power <= getMaxPower()) {
					ItemStack top = furnace.getInventory().getSmelting();
					ItemStack powerCore = furnace.getInventory().getResult();
					furnace.getInventory().setSmelting(new ItemStack(Material.PORK));
					furnace.getInventory().setResult(new ItemStack(Material.AIR));
					disableSmeltingSlot = true;
					Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
						@Override
						public void run() {
							furnace.getInventory().setSmelting(top);
							furnace.getInventory().setResult(powerCore);
							disableSmeltingSlot = false;
						}
					}, 1L);
				}
			}
		}
		if (furnace.getBurnTime() > 0) {
			power += 20;
			powerIntputTotal = 20;
		}
		chargeRod(furnace.getInventory().getSmelting());
		dechargeRod(furnace.getInventory().getFuel());
	}
	
	@Override
	public boolean onClick(InventoryClickEvent e) {
		if(super.onClick(e)){
			if(disableSmeltingSlot && e.getRawSlot() == 0){
				e.setCancelled(true);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public String getDisplayName() {
		return "§6§lGenerator";
	}

	@Override
	public void onMoveFrom(InventoryMoveItemEvent e) {
		e.setCancelled(true);
	}

	@Override
	public int getMaxPowerOutput() {
		return 100;
	}

	@Override
	public int getMaxPowerInput() {
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
