package de.oninoni.OnionPower.Machines;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.NMSAdapter;
import de.oninoni.OnionPower.Items.Batrod;
import de.oninoni.OnionPower.Items.PowerCore;

public class BatrodBox extends Machine{

	Dispenser dispenser;
	
	private final static int[] directionAdapter = {4,1,5,2,3,0};
	
	public BatrodBox(Location position, MachineManager machineManager) {
		super(position, machineManager);
		dispenser = (Dispenser) position.getBlock().getState();
		ItemStack powerCore = PowerCore.create(this);
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < 9; i++){
					dispenser.getInventory().setItem(i, new ItemStack(Material.AIR));
				}
				dispenser.getInventory().setItem(4, powerCore);
				dispenser.getInventory().setItem(3, new ItemStack(Material.STAINED_GLASS_PANE));
				dispenser.getInventory().setItem(5, new ItemStack(Material.STAINED_GLASS_PANE));
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
	public void updateBlock() {
		requestFromConnected();
	}
	
	public static boolean canCreate(InventoryClickEvent e){
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
	protected boolean isMaterial(Material material) {
		return material == Material.DISPENSER;
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
		return 100;
	}

	@Override
	public int getMaxPowerInput() {
		return 100;
	}

	@Override
	public void onClick(InventoryClickEvent e) {
		if((e.getRawSlot() == e.getView().convertSlot(e.getRawSlot())) && e.getRawSlot() >= 3 && e.getRawSlot() <= 5){
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
	protected void updateDisplay() {
		ItemStack powerCore = dispenser.getInventory().getItem(4);
		PowerCore.setPowerLevel(powerCore, this);
	}

	@Override
	public void onBreak(BlockBreakEvent e) {
		for(int i = 0; i < 9; i++){
			dispenser.getInventory().setItem(i, Batrod.create());
		}
	}
	
	@Override
	public void load() {
		super.load();
		dispenser = (Dispenser) getPosition().getBlock().getState();
	}
	
}
