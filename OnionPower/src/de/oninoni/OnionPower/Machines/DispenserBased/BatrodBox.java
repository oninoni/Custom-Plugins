package de.oninoni.OnionPower.Machines.DispenserBased;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.Items.PowerItems.Batrod;
import de.oninoni.OnionPower.Machines.MachineManager;
import de.oninoni.OnionPower.Machines.Upgrades.UpgradeManager.UpgradeType;

public class BatrodBox extends MachineDispenser {

	public BatrodBox(Location position, MachineManager machineManager) {
		super(position, machineManager);
		setupPowerIO();
	}

	public BatrodBox(OfflinePlayer owner, Location position, MachineManager machineManager, int power) {
		super(owner, position, machineManager, power);
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 9; i++) {
					if (i != getCoreSlot())
						getDispenser().getInventory().setItem(i, new ItemStack(Material.AIR));
				}
				getDispenser().getInventory().setItem(getCoreSlot() - 1, new ItemStack(Material.STAINED_GLASS_PANE));
				getDispenser().getInventory().setItem(getCoreSlot() + 1, new ItemStack(Material.STAINED_GLASS_PANE));

				reOpenInventories();
			}
		}, 1L);
		setupPowerIO();
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
	public String getDisplayName() {
		return "§6§lBatrod - Box";
	}

	@Override
	public int getMaxPower() {
		return 640000;
	}

	@Override
	public int getMaxPowerInput() {
		return 500;
	}

	@Override
	public int getMaxPowerOutput() {
		return 500;
	}

	@Override
	public boolean onClickFixed(Inventory inv, int slot, ItemStack cursor, Player p) {
		if (slot >= getCoreSlot() - 1 && slot <= getCoreSlot() + 1) {
			return true;
		}
		return false;
	}

	@Override
	public void onMoveFrom(InventoryMoveItemEvent e) {
		e.setCancelled(true);
	}

	@Override
	public void onMoveInto(InventoryMoveItemEvent e) {
		e.setCancelled(true);
	}

	@Override
	protected void resetItemAt(int slot) {
		getDispenser().getInventory().setItem(slot, new Batrod(getPower() / 10));
	}

	@Override
	protected void setAvailableUpgrades() {
		upgradesAvailable.add(UpgradeType.RedstoneUpgrade);
	}

	@Override
	protected int getCoreSlot() {
		return 4;
	}

	private void setupPowerIO() {
		@SuppressWarnings("deprecation")
		int direction = directionAdapter[getDispenser().getRawData() % 8];
		for (int i = 0; i < 6; i++) {
			if (i == direction) {
				allowedOutputs[i] = true;
				allowedInputs[i] = false;
			} else {
				allowedOutputs[i] = false;
			}
		}
	}

	@Override
	public ArmorStand spawnDesignEntityInternal(int id) {
		return null;
	}

	@Override
	public void updateBlock() {
		if(isInactive())return;
		for (int i = 0; i < 3; i++) {
			chargeItem(getDispenser().getInventory(), i);
			dechargeRod(getDispenser().getInventory(), i + 6);
		}
	}
}
