package de.oninoni.OnionPower.Machines.HopperBased;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.Items.Batrod;
import de.oninoni.OnionPower.Items.CustomsItems;
import de.oninoni.OnionPower.Items.InternalTank;
import de.oninoni.OnionPower.Items.PowerCore;
import de.oninoni.OnionPower.Machines.MachineManager;
import de.oninoni.OnionPower.Machines.Upgrades.LavaUpgrade;
import de.oninoni.OnionPower.Machines.Upgrades.UpgradeManager.UpgradeType;

public class FluidHandler extends MachineHopper {

	private static final int MAXFLUIDLEVEL = 64;

	private int fluidLevel;

	private boolean isLava = false;

	public FluidHandler(Location position, MachineManager machineManager) {
		super(position, machineManager);
		fluidLevel = InternalTank.readTankLevel(hopper.getInventory().getItem(3));
	}

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
		fluidLevel = 0;
	}

	public int addFluid(int ammount) {
		int overflow = 0;
		fluidLevel += ammount;
		if (fluidLevel > MAXFLUIDLEVEL) {
			overflow = fluidLevel - MAXFLUIDLEVEL;
			fluidLevel = MAXFLUIDLEVEL;
		}
		if (ammount != overflow) {
			InternalTank.setTankLevel(hopper.getInventory().getItem(3), this);
			needsUpdate = true;
		}
		return overflow;
	}

	@Override
	protected boolean doesExplode() {
		return true;
	}

	private void fillSlot(int id) {
		ItemStack bucket = hopper.getInventory().getItem(id);
		if (bucket != null && bucket.getType() == Material.BUCKET) {
			if (fluidLevel >= bucket.getAmount() && power >= bucket.getAmount() * 10) {
				power -= bucket.getAmount() * 10;
				fluidLevel -= bucket.getAmount();
				if (isLava) {
					bucket.setType(Material.LAVA_BUCKET);
				} else {
					bucket.setType(Material.WATER_BUCKET);
				}
				InternalTank.setTankLevel(hopper.getInventory().getItem(3), this);
				needsUpdate = true;
			}
		}
	}

	@Override
	public int getDesignEntityCount() {
		return 0;
	}

	@Override
	public String getDisplayName() {
		return "§6§lFluid Handler";
	}

	public int getLevel() {
		return fluidLevel;
	}

	public int getMaxLevel() {
		return MAXFLUIDLEVEL;
	}

	@Override
	public int getMaxPowerInput() {
		return 100;
	}

	@Override
	public int getMaxPowerOutput() {
		return 0;
	}

	public boolean isLavaMode() {
		return isLava;
	}

	@Override
	public boolean onClickFixed(Inventory inv, int slot, ItemStack cursor, Player p) {
		if (slot == 3 || slot == 4) {
			return true;
		} else {
			if (inv.getItem(slot) == null || inv.getItem(slot).getType() == Material.AIR) {
				if (cursor == null)
					return false;
				if (cursor.getType() == Material.BUCKET) {
					if (cursor.getAmount() > 1) {
						ItemStack bucket = cursor.clone();
						bucket.setAmount(1);
						inv.setItem(slot, bucket);
						ItemStack leftOver = cursor;
						leftOver.setAmount(leftOver.getAmount() - 1);
						return true;
					}
				} else {
					return true;
				}
			} else {
				if (inv.getItem(slot).getType() == Material.BUCKET) {
					if (!(cursor == null || cursor.getType() == Material.AIR))
						return true;
				} else if (inv.getItem(slot).getType() == Material.LAVA_BUCKET
						|| inv.getItem(slot).getType() == Material.WATER_BUCKET) {
					if (cursor == null)
						return false;
					if (cursor.getAmount() > 1) {
						ItemStack filledBucket = inv.getItem(slot);
						ItemStack bucket = cursor.clone();
						bucket.setAmount(1);
						inv.setItem(slot, bucket);
						ItemStack leftOver = cursor;
						leftOver.setAmount(leftOver.getAmount() - 1);
						HashMap<Integer, ItemStack> overflow = p.getInventory()
								.addItem(filledBucket);
						if (overflow.size() > 0) {
							for (int key : overflow.keySet()) {
								ItemStack drop = overflow.get(key);
								position.getWorld().dropItemNaturally(p.getLocation(), drop);
							}
						}
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public void onMoveFrom(InventoryMoveItemEvent e) {
		super.onMoveFrom(e);
		if (!(e.getItem().getType() == Material.LAVA_BUCKET || e.getItem().getType() == Material.WATER_BUCKET)) {
			e.setCancelled(true);
		}
	}

	@Override
	public void onMoveInto(InventoryMoveItemEvent e) {
		super.onMoveInto(e);
		if (e.getItem().getType() != Material.BUCKET) {
			e.setCancelled(true);
		} else {
			if (e.getDestination().contains(Material.BUCKET)) {
				e.setCancelled(true);
			} else if (e.getItem().getAmount() > 1) {
				ItemStack overflow = e.getItem().clone();
				overflow.setAmount(e.getItem().getAmount() - 1);
				e.getItem().setAmount(1);
				e.getInitiator().addItem(overflow);
			}
		}
	}

	@Override
	public void onPickup(InventoryPickupItemEvent e) {
		ItemStack item = e.getItem().getItemStack();
		if (item.getType() != Material.BUCKET) {
			e.setCancelled(true);
		} else {
			if (e.getInventory().contains(Material.BUCKET)) {
				e.setCancelled(true);
			} else if (item.getAmount() > 1) {
				item.setAmount(item.getAmount() - 1);
				e.setCancelled(true);
				ItemStack oneBucket = item.clone();
				oneBucket.setAmount(1);
				e.getInventory().addItem(oneBucket);
			}
		}
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
	protected void setAvailableUpgrades() {
		upgradesAvailable.add(UpgradeType.RedstoneUpgrade);
		upgradesAvailable.add(UpgradeType.LavaUpgrade);
	}

	@Override
	protected void setCoreSlot() {
		coreSlot = 2;
	}

	@Override
	public ArmorStand spawnDesignEntityInternal(int id) {
		return null;
	}

	@Override
	public void updateBlock() {
		LavaUpgrade lavaUpgrade = (LavaUpgrade) upgradeManager.getUpgrade(UpgradeType.LavaUpgrade);
		if (lavaUpgrade != null && isLava != lavaUpgrade.isLava()) {
			isLava = lavaUpgrade.isLava();
			hopper.getInventory().setItem(3, InternalTank.setLavaMode(this));
			needsUpdate = true;
		}
		fillSlot(0);
		fillSlot(1);
	}
}
