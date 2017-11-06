package de.oninoni.OnionPower.Machines.HopperBased;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Hopper;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import de.oninoni.OnionPower.Items.PowerItems.Batrod;
import de.oninoni.OnionPower.Items.Statics.CustomsItems;
import de.oninoni.OnionPower.Machines.MachineManager;
import de.oninoni.OnionPower.Machines.Upgrades.UpgradeManager.UpgradeType;

public class SolarHopper extends MachineHopper{
	
	public SolarHopper(Location position, MachineManager machineManager) {
		super(position, machineManager);
		setUpPowerIO();
	}

	public SolarHopper(OfflinePlayer owner, Location position, MachineManager machineManager, int power) {
		super(owner, position, machineManager, power);
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				Hopper hopper = getHopper();
				
				hopper.getInventory().setItem(0, CustomsItems.getGlassPane((byte) 11, "§9Solar Cell"));
				hopper.getInventory().setItem(1, CustomsItems.getGlassPane((byte) 11, "§9Solar Cell"));
				hopper.getInventory().setItem(3, CustomsItems.getGlassPane((byte) 11, "§9Solar Cell"));
				hopper.getInventory().setItem(4, CustomsItems.getGlassPane((byte) 11, "§9Solar Cell"));
				
				reOpenInventories();
			}
		}, 1L);
		setUpPowerIO();
	}
	
	private void setUpPowerIO(){
		for(int i = 0; i < 6; i++){
			allowedInputs[i] = false;
			allowedOutputs[i] = true;
		}
	}

	@Override
	public void onPickup(InventoryPickupItemEvent e) {
		e.setCancelled(true);
	}

	@Override
	protected boolean doesExplode() {
		return true;
	}

	@Override
	public int getDesignEntityCount() {
		return 4;
	}

	@Override
	public String getDisplayName() {
		return "§6§lSolar Hopper";
	}

	@Override
	public int getMaxPowerInput() {
		return 0;
	}

	@Override
	public int getMaxPowerOutput() {
		return 50;
	}

	@Override
	public boolean onClickFixed(Inventory inv, int slot, ItemStack cursor, Player p) {
		return true;
	}

	@Override
	protected void resetItemAt(int slot) {
		Hopper hopper = getHopper();
		
		switch (slot) {
		case 0:
		case 4:
			hopper.getInventory().setItem(slot, new ItemStack(Material.GLASS));
			break;
		case 1:
		case 3:
			hopper.getInventory().setItem(slot, new ItemStack(Material.LAPIS_BLOCK));
			break;
		case 2:
			hopper.getInventory().setItem(slot, new Batrod(getPower()));
			break;
		}
	}

	@Override
	protected void setAvailableUpgrades() {
		upgradesAvailable.add(UpgradeType.RedstoneUpgrade);
	}

	@Override
	protected int getCoreSlot() {
		return 2;
	}

	@Override
	protected ArmorStand spawnDesignEntityInternal(int id) {
		int xOffset = id >= 2 ? 1 : -1;
		int zOffset = id % 2 == 0 ? 1 : -1;
		Location p = position.clone();
		p.add(0.5f, 0.3f, 0.5f);
		p.add(xOffset * 0.225f, 0.0f, zOffset * 0.225f);
		ArmorStand armorStand = (ArmorStand) position.getWorld().spawnEntity(p, EntityType.ARMOR_STAND);
		armorStand.setHelmet(new ItemStack(Material.LAPIS_BLOCK));
		armorStand.setSmall(true);
		armorStand.setHeadPose(new EulerAngle(Math.PI, 0.0f, 0.0f));
		armorStand.setMarker(true);
		return armorStand;
	}

	@Override
	public void updateBlock() {
		Hopper hopper = getHopper();
		
		if(isInactive())return;
		int lightLevel = hopper.getBlock().getLightFromSky();
		if(lightLevel == 15){
			power++;
			powerIntputTotal++;
		}
	}
}
