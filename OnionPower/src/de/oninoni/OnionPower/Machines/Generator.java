package de.oninoni.OnionPower.Machines;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.NMSAdapter;
import de.oninoni.OnionPower.Items.Batrod;
import de.oninoni.OnionPower.Items.ItemData;
import de.oninoni.OnionPower.Items.PowerCore;

public class Generator extends MachineFurnace {
	public Generator(Location position, MachineManager machineManager) {
		super(position, machineManager);
		rodSlot = 0;
		coreSlot = 2;
		ItemStack powerCore = PowerCore.create(this);
		
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				furnace.getInventory().setItem(rodSlot, new ItemStack(Material.AIR));
				furnace.getInventory().setItem(coreSlot, powerCore);
				NMSAdapter.setInvNameFurnace(furnace, getDisplayName());
				for(HumanEntity viewer : furnace.getInventory().getViewers()){
					viewer.closeInventory();
					viewer.openInventory(furnace.getInventory());
				}
			}
		}, 1L);
	}
	
	@Override
	public void update() {
		if (furnace.getBurnTime() == 0) {
			ItemStack fuel = furnace.getInventory().getFuel();
			
			if (fuel != null){
				Material mat = fuel.getType();
				if (ItemData.burnTime.containsKey(mat) && ItemData.burnTime.get(mat) + power <= getMaxPower()) {
					/*
					if (fuel.getAmount() == 1)
						furnace.getInventory().remove(fuel);
					else
						fuel.setAmount(fuel.getAmount() - 1);
					furnace.setBurnTime(ItemData.burnTime.get(mat));
					*/
					ItemStack top = furnace.getInventory().getSmelting();
					ItemStack powerCore = furnace.getInventory().getResult();
					furnace.getInventory().setSmelting(new ItemStack(Material.PORK));
					furnace.getInventory().setResult(new ItemStack(Material.AIR));
					Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
						@Override
						public void run() {
							furnace.getInventory().setSmelting(top);
							furnace.getInventory().setResult(powerCore);
						}
					}, 1L);
				}
			}
		}
		if (furnace.getBurnTime() > 0) {
			power += 20;
			powerIntputTotal = 20;
		}
	}
	
	public static boolean canCreate(InventoryClickEvent e) {
		ItemStack item = e.getCursor();
		if(!(e.getInventory().getItem(0) == null && e.getInventory().getItem(1) == null && e.getInventory().getItem(0) == null))return false;
		return 0 == e.getView().convertSlot(e.getRawSlot()) && Batrod.check(item);
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
		return 40;
	}

	@Override
	public int getMaxPowerInput() {
		return 0;
	}
}
