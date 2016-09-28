package de.oninoni.OnionPower.Machines.HopperBased;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.Items.Batrod;
import de.oninoni.OnionPower.Items.CustomsItems;
import de.oninoni.OnionPower.Items.InternalTank;
import de.oninoni.OnionPower.Items.PowerCore;
import de.oninoni.OnionPower.Machines.MachineManager;

public class FluidHandler extends MachineHopper{
	
	private int fluidLevel = 0;
	
	private static final int MAXFLUIDLEVEL = 64;
	
	private boolean isLava = false;
	
	//TODO Rewrite for 1 Bucket per Slot (cancel + remove 1 item in hand)
	//TODO Move into Slots wich Stack up
	
	public FluidHandler(Location position, MachineManager machineManager, int power) {
		super(position, machineManager, power);
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				hopper.getInventory().setItem(3, InternalTank.create(fluidLevel, MAXFLUIDLEVEL, false));
				hopper.getInventory().setItem(4, CustomsItems.getCraftingCore());
				
				reOpenInventories();
			}
		}, 1L);
	}
	
	public FluidHandler(Location position, MachineManager machineManager) {
		super(position, machineManager);
	}
	
	public int addFluid(int ammount){
		int overflow = 0;
		fluidLevel += ammount;
		if(fluidLevel > MAXFLUIDLEVEL){
			overflow = fluidLevel - MAXFLUIDLEVEL;
			fluidLevel = MAXFLUIDLEVEL;
		}
		return overflow;
	}

	@Override
	protected void setAvailableUpgrades() {
		return;
	}

	@Override
	protected void setCoreSlot() {
		coreSlot = 2;
	}

	@Override
	public String getDisplayName() {
		return "§6§lFluid Handler";
	}

	@Override
	public int getMaxPowerOutput() {
		return 0;
	}

	@Override
	public int getMaxPowerInput() {
		return 100;
	}
	
	private void updateTank(){
		
	}
	
	private void fillSlot(int id){
		ItemStack bucket = hopper.getInventory().getItem(id);
		if(bucket != null && bucket.getType() == Material.BUCKET){
			if(fluidLevel >= bucket.getAmount()){
				fluidLevel -= bucket.getAmount();
				if(isLava){
					bucket.setType(Material.LAVA_BUCKET);
				}else{
					bucket.setType(Material.WATER_BUCKET);
				}
				needsUpdate = true;
			}
		}
	}

	@Override
	public void updateBlock() {
		fillSlot(0);
		fillSlot(1);
	}
	
	@Override
	public void onMoveFrom(InventoryMoveItemEvent e) {
		super.onMoveFrom(e);
		if(!(e.getItem().getType() == Material.LAVA_BUCKET || e.getItem().getType() == Material.WATER_BUCKET)){
			e.setCancelled(true);
		}
	}
	
	@Override
	public void onMoveInto(InventoryMoveItemEvent e) {
		super.onMoveInto(e);
		if(e.getItem().getType() != Material.BUCKET){
			e.setCancelled(true);
		}
	}

	@Override
	public int getDesignEntityCount() {
		return 0;
	}

	@Override
	public void spawnDesignEntity(int id) {
		return;
	}

	@Override
	protected void resetItemAt(int id) {
		switch (id) {
		case 2:
			ItemStack batrod = Batrod.create();
			Batrod.setPower(batrod, PowerCore.getPowerLevel(getPowerCore()));
			hopper.getInventory().setItem(id, batrod);
			break;
		case 3:
			hopper.getInventory().setItem(id, new ItemStack(Material.GLASS));
			break;
		case 4:
			hopper.getInventory().setItem(id, new ItemStack(Material.WORKBENCH));
			break;
		}
	}
	
	@Override
	public boolean onClick(InventoryClickEvent e) {
		if(!super.onClick(e)){
			if(e.getSlot() == 0 || e.getSlot() == 1){
				if(e.getCursor() != null  && !(e.getCursor().getType() == Material.BUCKET || e.getCursor().getType() == Material.AIR)){
					e.setCancelled(true);
				}
			}else{
				e.setCancelled(true);
			}
			return false;
		}
		return true;
	}

	@Override
	protected boolean doesExplode() {
		return true;
	}
}
