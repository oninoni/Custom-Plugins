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

public class BatrodBox extends MachineDispenser{

	public BatrodBox(Location position, MachineManager machineManager, int power, HashMap<Integer, Upgrade> upgrades) {
		super(position, machineManager, power, upgrades);
		ItemStack powerCore = PowerCore.create(this);
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < 9; i++){
					dispenser.getInventory().setItem(i, new ItemStack(Material.AIR));
				}
				dispenser.getInventory().setItem(coreSlot, powerCore);
				dispenser.getInventory().setItem(coreSlot - 1, new ItemStack(Material.STAINED_GLASS_PANE));
				dispenser.getInventory().setItem(coreSlot + 1, new ItemStack(Material.STAINED_GLASS_PANE));
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
			if(i == direction){
				allowedOutputs[i] = true;
				allowedInputs[i] = false;
			}else{
				allowedOutputs[i] = false;
			}
		}
	}
	
	@Override
	protected void setCoreSlot() {
		coreSlot = 4;
	}

	@Override
	public void updateBlock() {
		requestFromConnected();
		for(int i = 0; i < 3; i++){
			chargeRod(dispenser.getInventory().getItem(i));
			dechargeRod(dispenser.getInventory().getItem(i + 6));
		}
	}
	
	public static boolean canCreate(InventoryClickEvent e){
		if(!(e.getInventory().getType() == InventoryType.DISPENSER))return false;
		ItemStack item = e.getCursor();
		if(!Batrod.check(item))return false;
		if(!(e.getRawSlot() == e.getView().convertSlot(e.getRawSlot())))return false;
		int slot = e.getRawSlot();
		for(int i = 0; i < 8; i++){
			int slotToCheck = i;
			if(i >= slot)slotToCheck++;
			ItemStack itemToCheck = e.getView().getTopInventory().getItem(slotToCheck);
			if(!Batrod.check(itemToCheck))return false;
		}
		return true;
	}
	
	@Override
	public int getMaxPower() {
		return 640000;
	}

	@Override
	public String getDisplayName() {
		return "§6§lBatrod - Box";
	}

	@Override
	public int getMaxPowerOutput() {
		return 500;
	}

	@Override
	public int getMaxPowerInput() {
		return 500;
	}

	@Override
	public void onClick(InventoryClickEvent e) {
		if((e.getRawSlot() == e.getView().convertSlot(e.getRawSlot())) && e.getRawSlot() >= coreSlot - 1 && e.getRawSlot() <= coreSlot + 1){
			e.setCancelled(true);
		}
	}

	@Override
	public void onMoveInto(InventoryMoveItemEvent e) {
		e.setCancelled(true);
	}

	@Override
	public void onMoveFrom(InventoryMoveItemEvent e) {
		e.setCancelled(true);
	}
	
	@Override
	public void onBreak(BlockBreakEvent e) {
		for(int i = 0; i < 9; i++){
			dispenser.getInventory().setItem(i, Batrod.create());
		}
	}
}
