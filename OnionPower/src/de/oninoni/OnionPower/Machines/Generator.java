package de.oninoni.OnionPower.Machines;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.NMSAdapter;
import de.oninoni.OnionPower.Items.Batterod;
import de.oninoni.OnionPower.Items.BurnTimes;
import de.oninoni.OnionPower.Items.PowerCore;

public class Generator extends Machine {
	
	private Furnace furnace;
	
	public Generator(Location position, MachineManager machineManager) {
		super(position, machineManager);
		ItemStack powerCore = PowerCore.create(this);

		furnace = ((Furnace) position.getBlock().getState());
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				furnace.getInventory().setItem(0, new ItemStack(Material.AIR));
				furnace.getInventory().setItem(2, powerCore);
				NMSAdapter.setInvNameFurnace(furnace, getDisplayName());
				for(HumanEntity viewer : furnace.getInventory().getViewers()){
					viewer.closeInventory();
					viewer.openInventory(furnace.getInventory());
				}
			}
		}, 1L);
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
	public void update() {
		powerIntputTotal = 0;
		powerOutputTotal = 0;
		if (furnace.getBurnTime() == 0) {
			ItemStack fuel = furnace.getInventory().getFuel();
			
			if (fuel != null){
				Material mat = fuel.getType();
				if (BurnTimes.Item.containsKey(mat) && BurnTimes.Item.get(mat) + power <= getMaxPower()) {
					Bukkit.broadcastMessage("Burning Coal!");
					if (fuel.getAmount() == 1)
						furnace.getInventory().remove(fuel);
					else
						fuel.setAmount(fuel.getAmount() - 1);
					furnace.setBurnTime(BurnTimes.Item.get(mat));
				}
			}
		}
		if (furnace.getBurnTime() > 0) {
			power += 2;
			ItemStack powerCore = furnace.getInventory().getResult();
			PowerCore.setPowerLevel(powerCore, this);
		}
	}
	
	public static boolean canCreate(InventoryClickEvent e) {
		ItemStack item = e.getCursor();
		if (0 == e.getView().convertSlot(e.getRawSlot())){
			if (Batterod.check(item)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String getDisplayName() {
		return "§6§lGenerator";
	}

	@Override
	public void onClick(InventoryClickEvent e) {
		int convertSlot = e.getView().convertSlot(e.getRawSlot());
		if(convertSlot == 2){
			e.setCancelled(true);
		}
	}

	@Override
	public void onMoveInto(InventoryMoveItemEvent e) {
		
	}

	@Override
	public void onMoveFrom(InventoryMoveItemEvent e) {
		e.setCancelled(true);
	}

	@Override
	public int getMaxPowerOutput() {
		return 4;
	}

	@Override
	public int getMaxPowerInput() {
		return 0;
	}
}
