package de.oninoni.OnionPower.Machines;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.NMSAdapter;
import de.oninoni.OnionPower.Items.Batrod;
import de.oninoni.OnionPower.Items.PowerCore;
import de.oninoni.OnionPower.Machines.Upgrades.Upgrade;

public class Sorter extends MachineDispenser{

	public Sorter(Location position, MachineManager machineManager, int power, HashMap<Integer, Upgrade> upgrades) {
		super(position, machineManager, power, upgrades);
		coreSlot = 0;
		ItemStack powerCore = PowerCore.create(this);
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < 9; i++){
					dispenser.getInventory().setItem(i, new ItemStack(Material.AIR));
				}
				dispenser.getInventory().setItem(coreSlot, powerCore);
				dispenser.getInventory().setItem(coreSlot + 2, new ItemStack(Material.STAINED_GLASS_PANE));
				dispenser.getInventory().setItem(coreSlot + 6, new ItemStack(Material.STAINED_GLASS_PANE));
				dispenser.getInventory().setItem(coreSlot + 8, new ItemStack(Material.STAINED_GLASS_PANE));
				NMSAdapter.setInvNameDispenser(dispenser, getDisplayName());
				for(HumanEntity viewer : dispenser.getInventory().getViewers()){
					viewer.closeInventory();
					viewer.openInventory(dispenser.getInventory());
				}
			}
		}, 1L);
		@SuppressWarnings("deprecation")
		int direction = directionAdapter[dispenser.getRawData()];
		for(int i = 0; i < 6; i++){
			allowedOutputs[i] = false;
			if(i == direction){
				allowedInputs[i] = false;
			}
		}
	}
	
	@Override
	public void updateBlock() {
		// TODO Auto-generated method stub
	}
	
	public static boolean canCreate(InventoryClickEvent e){
		if(!(e.getInventory().getType() == InventoryType.DISPENSER))return false;
		ItemStack item = e.getCursor();
		if(!(Batrod.check(item) || item.getType() == Material.CHEST))return false;
		if(!(e.getRawSlot() == e.getView().convertSlot(e.getRawSlot())))return false;
		int slot = e.getRawSlot();
		return true;
	}
	
	@Override
	public String getDisplayName() {
		return "§6§lElectrical Sorter";
	}

	@Override
	public int getMaxPowerOutput() {
		return 0;
	}

	@Override
	public int getMaxPowerInput() {
		return 100;
	}
	
	@Override
	public void onClick(InventoryClickEvent e) {
		super.onClick(e);
		int convertSlot = e.getView().convertSlot(e.getRawSlot());
		if(e.getRawSlot() == convertSlot && (convertSlot == coreSlot + 2 || convertSlot == coreSlot + 6 || convertSlot == coreSlot + 8)){
			e.setCancelled(true);
		}
	}
	
	@Override
	public void onMoveInto(InventoryMoveItemEvent e) {
		// TODO Sorting here
	}

	@Override
	public void onMoveFrom(InventoryMoveItemEvent e) {
		e.setCancelled(true);
	}

	@Override
	public void onBreak(BlockBreakEvent e) {
		dispenser.getInventory().setItem(0, Batrod.create());
		for(int i = 1; i < 5; i++){
			dispenser.getInventory().setItem(i, new ItemStack(Material.CHEST));
		}
	}
	
}
