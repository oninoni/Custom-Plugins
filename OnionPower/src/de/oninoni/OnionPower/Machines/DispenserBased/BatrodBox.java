package de.oninoni.OnionPower.Machines.DispenserBased;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.Items.Batrod;
import de.oninoni.OnionPower.Machines.MachineManager;
import de.oninoni.OnionPower.Machines.Upgrades.UpgradeManager.UpgradeType;

public class BatrodBox extends MachineDispenser {

	public BatrodBox(Location position, MachineManager machineManager, int power) {
		super(position, machineManager, power);
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 9; i++) {
					if (i != coreSlot)
						dispenser.getInventory().setItem(i, new ItemStack(Material.AIR));
				}
				// dispenser.getInventory().setItem(coreSlot, getPowerCore());
				dispenser.getInventory().setItem(coreSlot - 1, new ItemStack(Material.STAINED_GLASS_PANE));
				dispenser.getInventory().setItem(coreSlot + 1, new ItemStack(Material.STAINED_GLASS_PANE));

				reOpenInventories();
			}
		}, 1L);
		setupPowerIO();
	}

	public BatrodBox(Location position, MachineManager machineManager) {
		super(position, machineManager);
		setupPowerIO();
	}

	private void setupPowerIO() {
		@SuppressWarnings("deprecation")
		int direction = directionAdapter[dispenser.getRawData() % 8];
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
	protected void setCoreSlot() {
		coreSlot = 4;
	}

	@Override
	public void updateBlock() {
		for (int i = 0; i < 3; i++) {
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
	public boolean onClick(InventoryClickEvent e) {
		if (!super.onClick(e)) {
			if (e.getRawSlot() >= coreSlot - 1 && e.getRawSlot() <= coreSlot + 1) {
				e.setCancelled(true);
			}
			return false;
		}
		return true;
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
	public int getDesignEntityCount() {
		return 0;
	}

	@Override
	public void spawnDesignEntity(int id) {

	}

	@Override
	protected void setAvailableUpgrades() {
		availableUpgrades.add(UpgradeType.RedstoneUpgrade);
	}
}
