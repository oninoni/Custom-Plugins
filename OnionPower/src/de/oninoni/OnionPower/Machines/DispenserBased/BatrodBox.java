package de.oninoni.OnionPower.Machines.DispenserBased;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.Items.Batrod;
import de.oninoni.OnionPower.Machines.MachineDispenser;
import de.oninoni.OnionPower.Machines.MachineManager;
import de.oninoni.OnionPower.Machines.Upgrades.Upgrade;

public class BatrodBox extends MachineDispenser{

	public BatrodBox(Location position, MachineManager machineManager, int power, HashMap<Integer, Upgrade> upgrades) {
		super(position, machineManager, power, upgrades);
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < 9; i++){
					dispenser.getInventory().setItem(i, new ItemStack(Material.AIR));
				}
				dispenser.getInventory().setItem(coreSlot, powerCore);
				dispenser.getInventory().setItem(coreSlot - 1, new ItemStack(Material.STAINED_GLASS_PANE));
				dispenser.getInventory().setItem(coreSlot + 1, new ItemStack(Material.STAINED_GLASS_PANE));

				reOpenInventories();
			}
		}, 1L);
		setupPowerIO();
	}
	
	public BatrodBox(Location position, MachineManager machineManager, HashMap<Integer, Upgrade> upgrades) {
		super(position, machineManager, upgrades);
		setupPowerIO();
	}
	
	private void setupPowerIO(){
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
		super.onClick(e);
		if(e.getRawSlot() >= coreSlot - 1 && e.getRawSlot() <= coreSlot + 1){
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
	protected boolean doesExplode() {
		return true;
	}
	
	@Override
	protected void resetItemAt(int id) {
		ItemStack batrod = Batrod.create();
		Batrod.setPower(batrod, getPower() / 10);
		dispenser.getInventory().setItem(id, batrod);
	}

	@Override
	protected void spawnDesignEntities() {
		// TODO Auto-generated method stub
		
	}
}
