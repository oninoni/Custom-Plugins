package de.oninoni.OnionPower.Machines.FurnaceBased;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Furnace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import de.oninoni.OnionPower.Items.ItemData;
import de.oninoni.OnionPower.Items.PowerItems.Batrod;
import de.oninoni.OnionPower.Machines.MachineManager;
import de.oninoni.OnionPower.Machines.Upgrades.UpgradeManager.UpgradeType;

public class Generator extends MachineFurnace {

	private boolean disableSmeltingSlot = false;

	public Generator(Location position, MachineManager machineManager) {
		super(position, machineManager);
		SetupPowerIO();
	}

	public Generator(OfflinePlayer owner, Location position, MachineManager machineManager, int power) {
		super(owner, position, machineManager, power);

		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				getFurnace().getInventory().setItem(1, new ItemStack(Material.AIR));

				reOpenInventories();
			}
		}, 1L);
		SetupPowerIO();
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
		return "§6§lGenerator";
	}

	@Override
	public int getMaxPowerInput() {
		return 0;
	}

	@Override
	public int getMaxPowerOutput() {
		return 100;
	}

	@Override
	public boolean onClickFixed(Inventory inv, int slot, ItemStack cursor, Player p) {
		if (disableSmeltingSlot && slot == 0) {
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
		Material mat = e.getItem().getType();
		int slot = e.getDestination().first(mat);
		if(slot == -1){
			slot = e.getDestination().firstEmpty();
		}
		switch (slot) {
		case 0:
			break;
		case 1:
			if(!ItemData.smeltable.containsKey(mat))
				e.setCancelled(true);
			break;
		case 2:
			e.setCancelled(true);
			break;
		default:
			e.setCancelled(true);
			break;
		}
	}

	@Override
	protected void resetItemAt(int slot) {
		if (slot == getCoreSlot()) {
			getFurnace().getInventory().setItem(slot, new Batrod(getPower()));
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

	private void SetupPowerIO() {
		for (int i = 0; i < 6; i++) {
			allowedOutputs[i] = true;
			allowedInputs[i] = false;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public ArmorStand spawnDesignEntityInternal(int id) {
		int data = (position.getBlock().getData() - 2) % 4;
		Location p = position.clone().add(0.5f, 0.0f, 0.5f).add((MachineManager.directions[directionAdapter[data]]).clone().multiply(0.7));
		p.add(0.0f, 0.5f, 0.0f);
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
		Furnace furnace = getFurnace();
		
		chargeItem(furnace.getInventory(), 0);
		dechargeRod(furnace.getInventory(), 1);
		if(isInactive())return;
		if (furnace.getBurnTime() <= 0) {
			ItemStack fuel = furnace.getInventory().getFuel();
			Batrod batrod = new Batrod(fuel);
			if (batrod.check() || isInactive())
				return;
			if (fuel != null) {
				Material mat = fuel.getType();
				if (ItemData.burnTime.containsKey(mat) && ItemData.burnTime.get(mat) + power <= getMaxPower()) {
					ItemStack top = furnace.getInventory().getSmelting();
					ItemStack powerCore = furnace.getInventory().getResult();
					furnace.getInventory().setSmelting(new ItemStack(Material.PORK));
					furnace.getInventory().setResult(new ItemStack(Material.AIR));
					disableSmeltingSlot = true;
					Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
						@Override
						public void run() {
							furnace.getInventory().setSmelting(top);
							furnace.getInventory().setResult(powerCore);
							disableSmeltingSlot = false;
						}
					}, 1L);
				}
			}
		}
		if (furnace.getBurnTime() > 0) {
			power += 20;
			powerIntputTotal = 20;
		}
	}
}
