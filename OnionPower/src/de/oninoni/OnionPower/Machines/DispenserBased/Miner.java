package de.oninoni.OnionPower.Machines.DispenserBased;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.Machines.MachineDispenser;
import de.oninoni.OnionPower.Machines.MachineManager;
import de.oninoni.OnionPower.Machines.Upgrades.Upgrade;

public class Miner extends MachineDispenser{

	public Miner(Location position, MachineManager machineManager, int power, HashMap<Integer, Upgrade> upgrades) {
		super(position, machineManager, power, upgrades);
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				dispenser.getInventory().setItem(coreSlot, powerCore);
				dispenser.getInventory().setItem(0, new ItemStack(Material.STAINED_GLASS_PANE));
				dispenser.getInventory().setItem(2, new ItemStack(Material.STAINED_GLASS_PANE));
				dispenser.getInventory().setItem(3, new ItemStack(Material.STAINED_GLASS_PANE));
				dispenser.getInventory().setItem(5, new ItemStack(Material.STAINED_GLASS_PANE));
				dispenser.getInventory().setItem(6, new ItemStack(Material.STAINED_GLASS_PANE));
				dispenser.getInventory().setItem(8, new ItemStack(Material.STAINED_GLASS_PANE));
				for(HumanEntity viewer : dispenser.getInventory().getViewers()){
					viewer.closeInventory();
					viewer.openInventory(dispenser.getInventory());
				}
			}
		}, 1L);
		SetupPowerIO();
	}
	
	public Miner(Location position, MachineManager machineManager, HashMap<Integer, Upgrade> upgrades) {
		super(position, machineManager, upgrades);
		SetupPowerIO();
	}
	
	private void SetupPowerIO(){
		for(int i = 0; i < 6; i++){
			allowedOutputs[i] = false;
		}
	}
	
	@Override
	protected void setCoreSlot() {
		coreSlot = 1;
	}

	@Override
	public String getDisplayName() {
		return "§6§lVertical Miner";
	}

	@Override
	public int getMaxPowerOutput() {
		return 0;
	}

	@Override
	public int getMaxPowerInput() {
		return 500;
	}

	@Override
	public void updateBlock() {
		requestFromConnected();
		//TODO Actual Mining
	}

	@Override
	public void onBreak(BlockEvent e) {
		//TODO onBreak
	}

	@Override
	public boolean onBoom(Block e) {
		// TODO onBoom
		return false;
	}
	
	@Override
	public void onMoveFrom(InventoryMoveItemEvent e) {
		e.setCancelled(true);
	}
}
