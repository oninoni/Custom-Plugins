package de.oninoni.OnionPower.Machines.FurnaceBased;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import de.oninoni.OnionPower.Items.ItemData;
import de.oninoni.OnionPower.Items.PowerItems.Batrod;
import de.oninoni.OnionPower.Machines.MachineManager;
import de.oninoni.OnionPower.Machines.Upgrades.UpgradeManager.UpgradeType;

public class ElectricFurnace extends MachineFurnace {

	private Material cookingInto;
	private Material cookingFrom;

	public ElectricFurnace(Location position, MachineManager machineManager) {
		super(position, machineManager);
	}

	public ElectricFurnace(Location position, MachineManager machineManager, int power) {
		super(position, machineManager, power);

		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				furnace.getInventory().setItem(0, new ItemStack(Material.AIR));
				// furnace.getInventory().setItem(1, getPowerCore());

				reOpenInventories();
			}
		}, 1L);
	}

	@Override
	protected boolean doesExplode() {
		return true;
	}

	@Override
	public int getDesignEntityCount() {
		return 2;
	}

	@Override
	public String getDisplayName() {
		return "§6§lElectrical Furnace";
	}

	@Override
	public int getMaxPowerInput() {
		return 100;
	}

	@Override
	public int getMaxPowerOutput() {
		return 0;
	}

	@Override
	public boolean onClickFixed(Inventory inv, int slot, ItemStack cursor, Player p) {
		return false;
	}
	
	@Override
	protected void resetItemAt(int id) {
		if (id == coreSlot) {
			furnace.getInventory().setItem(id, new Batrod(getPower()));
		}
	}

	@Override
	protected void setAvailableUpgrades() {
		upgradesAvailable.add(UpgradeType.RedstoneUpgrade);
	}

	@Override
	protected void setCoreSlot() {
		coreSlot = 1;
	}

	@Override
	public ArmorStand spawnDesignEntityInternal(int id) {
		@SuppressWarnings("deprecation")
		int data = (position.getBlock().getData() - 2) % 4;
		Location p = position.clone().add(0.5f, 0.0f, 0.5f).add((MachineManager.directions[directionAdapter[data]]).clone().multiply(0.7));
		switch (data) {
		case 1:
			p.setYaw(180.0f);
			break;
		case 2:
			p.setYaw(270.0f);
			break;
		case 3:
			p.setYaw(90.0f);
			break;
		}
		Vector offset = MachineManager.directions[directionAdapter[data]].clone();
		offset.crossProduct(new Vector(0.0f, 1.0f, 0.0f).multiply(0.2f * (id == 0 ? -1 : 1)));
		p.add(offset);
		
		ArmorStand armorStand = (ArmorStand) position.getWorld().spawnEntity(p, EntityType.ARMOR_STAND);
		armorStand.setSmall(true);
		armorStand.setHelmet(new ItemStack(Material.IRON_FENCE));
		armorStand.setHeadPose(new EulerAngle(Math.PI, 0.0f, 0.0f));
		armorStand.setMarker(true);
		return armorStand;
	}

	@Override
	public void updateBlock() {
		if(isInactive())return;
		if (furnace.getInventory().getSmelting() != null) {
			ItemStack smelting = furnace.getInventory().getSmelting();
			if (furnace.getCookTime() <= 0 && ItemData.smeltable.containsKey(smelting.getType())) {
				Material result = ItemData.smeltable.get(smelting.getType());
				if ((furnace.getInventory().getResult() == null
						|| furnace.getInventory().getResult().getType() == result) && power >= 2000) {
					furnace.setBurnTime((short) 200);
					cookingInto = result;
					cookingFrom = smelting.getType();
				} else {
					furnace.setBurnTime((short) 0);
					cookingInto = null;
					cookingFrom = null;
				}
			}
		}
		if (furnace.getBurnTime() > 0) {
			if (furnace.getInventory().getSmelting() != null
					&& furnace.getInventory().getSmelting().getType() != cookingFrom) {

			}
			if (furnace.getInventory().getSmelting() == null
					|| furnace.getInventory().getSmelting().getType() != cookingFrom) {
				furnace.setBurnTime((short) 0);
				furnace.setCookTime((short) 0);
				cookingInto = null;
				cookingFrom = null;
			} else {
				power -= 20;
				powerOutputTotal = 20;
				furnace.setBurnTime((short) 200);
			}
		}
		if (furnace.getInventory().getSmelting() == null && (furnace.getBurnTime() != 0 || furnace.getCookTime() != 0)
				&& cookingInto != null) {
			furnace.setBurnTime((short) 0);
			furnace.setCookTime((short) 0);
		}
	}
}
