package de.oninoni.OnionPower.Machines.DispenserBased;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import de.oninoni.OnionPower.Items.Batrod;
import de.oninoni.OnionPower.Items.CustomsItems;
import de.oninoni.OnionPower.Machines.MachineManager;
import de.oninoni.OnionPower.Machines.Upgrades.UpgradeManager.UpgradeType;

//TODO Check inventory Type you move into

public class Sorter extends MachineDispenser {

	private static final Vector[][] filterDirections = {
			{ new Vector(0.0, 1.0, 0.0), new Vector(0.0, 0.0, 1.0), new Vector(0.0, 0.0, -1.0),
					new Vector(0.0, -1.0, 0.0) },
			{ new Vector(1.0, 0.0, 0.0), new Vector(0.0, 0.0, -1.0), new Vector(0.0, 0.0, 1.0),
					new Vector(-1.0, 0.0, 0.0) },
			{ new Vector(0.0, 1.0, 0.0), new Vector(-1.0, 0.0, 0.0), new Vector(1.0, 0.0, 0.0),
					new Vector(0.0, -1.0, 0.0) },
			{ new Vector(0.0, 1.0, 0.0), new Vector(0.0, 0.0, -1.0), new Vector(0.0, 0.0, 1.0),
					new Vector(0.0, -1.0, 0.0) },
			{ new Vector(1.0, 0.0, 0.0), new Vector(0.0, 0.0, 1.0), new Vector(0.0, 0.0, -1.0),
					new Vector(-1.0, 0.0, 0.0) },
			{ new Vector(0.0, 1.0, 0.0), new Vector(1.0, 0.0, 0.0), new Vector(-1.0, 0.0, 0.0),
					new Vector(0.0, -1.0, 0.0) } };

	private ArrayList<Material[]> filters = new ArrayList<>();

	private int particlesTimeout = 0;

	public Sorter(Location position, MachineManager machineManager) {
		super(position, machineManager);
		for (int i = 0; i < 4; i++) {
			filters.add(new Material[] { Material.AIR, Material.AIR, Material.AIR, Material.AIR, Material.AIR,
					Material.AIR, Material.AIR, Material.AIR, Material.AIR });
		}
		loadFilters();
		setupPowerIO();
	}

	public Sorter(Location position, MachineManager machineManager, int power) {
		super(position, machineManager, power);
		for (int i = 0; i < 4; i++) {
			filters.add(new Material[] { Material.AIR, Material.AIR, Material.AIR, Material.AIR, Material.AIR,
					Material.AIR, Material.AIR, Material.AIR, Material.AIR });
		}
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				// dispenser.getInventory().setItem(coreSlot, getPowerCore());
				dispenser.getInventory().setItem(2, CustomsItems.getGlassPane((byte) 0, "§4Click to Enable Particles"));
				dispenser.getInventory().setItem(6, CustomsItems.getGlassPane((byte) 0, "§4§kSecret§r §4§kMessage"));
				dispenser.getInventory().setItem(8,
						CustomsItems.getGlassPane((byte) 0, "§4§kAnother§r §4§kSecret§r §4§kMessage"));

				List<String> lore = new ArrayList<>();

				lore.add("§6Click here to add items to the filter.");
				lore.add("§6If an items enters the Sorter wich is not filtered");
				lore.add("§6it will leave the sorter towards the front.");

				for (int i = 0; i < 9; i++) {
					lore.add("§h");
				}

				dispenser.getInventory().setItem(1,
						CustomsItems.getGlassPane((byte) 14, "§4Red Sorting Channel", lore));
				dispenser.getInventory().setItem(3,
						CustomsItems.getGlassPane((byte) 13, "§2Green Sorting Channel", lore));
				dispenser.getInventory().setItem(5,
						CustomsItems.getGlassPane((byte) 11, "§9Blue Sorting Channel", lore));
				dispenser.getInventory().setItem(7,
						CustomsItems.getGlassPane((byte) 4, "§eYellow Sorting Channel", lore));

				saveFilters();
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
		return "§6§lElectrical Sorter";
	}

	@Override
	public int getMaxPowerInput() {
		return 100;
	}

	@Override
	public int getMaxPowerOutput() {
		return 0;
	}

	private boolean isInFilter(Material m, int id) {
		Material[] filter = filters.get(id);
		for (int i = 0; i < filter.length; i++) {
			if (m == filter[i]) {
				return true;
			}
		}
		return false;
	}

	private void loadFilter(int id) {
		Material[] filter = filters.get(id);
		int slotID = id * 2 + 1;
		List<String> lore = dispenser.getInventory().getItem(slotID).getItemMeta().getLore();
		for (int i = 0; i < 9; i++) {
			String name = lore.get(i + 3).substring(2);
			filter[i] = Material.getMaterial(name);
		}
		filters.set(id, filter);
	}

	private void loadFilters() {
		for (int i = 0; i < 4; i++) {
			loadFilter(i);
		}
	}

	@Override
	public boolean onClickFixed(Inventory inv, int slot, ItemStack cursor, Player p) {
		boolean shouldCancel = false;
		if (inv.getType() == InventoryType.DISPENSER) {
			if (slot < 9 && slot >= 0) {
				if (slot % 2 == 0) {
					if (slot != 4) {
						shouldCancel = true;
						if (slot == 2) {
							if (particlesTimeout == 0) {
								particlesTimeout = 100;
							}
						}
					}
				} else {
					shouldCancel = true;
					String filterInvName = "";
					if (slot == 1)
						filterInvName = "§4Red Filter";
					if (slot == 3)
						filterInvName = "§2Green Filter";
					if (slot == 5)
						filterInvName = "§9Blue Filter";
					if (slot == 7)
						filterInvName = "§eYellow Filter";
					Inventory filterInv = Bukkit.createInventory(dispenser, 9, filterInvName);
					for (int i = 0; i < 9; i++) {
						filterInv.setItem(i, new ItemStack(filters.get(slot / 2)[i]));
					}
					p.openInventory(filterInv);
				}
			}
		} else {
			if (slot < 9 && slot >= 0) {
				if (cursor.getType() != Material.AIR || inv.getItem(slot).getType() != Material.AIR) {
					shouldCancel = true;
					if (inv.getItem(slot).getType() == Material.AIR || cursor.getType() != Material.AIR) {
						inv.setItem(slot, cursor);
					} else {
						inv.setItem(slot, new ItemStack(Material.AIR));
					}
				}
			}
		}
		return shouldCancel;
	}
	
	@Override
	public void onClose(InventoryCloseEvent e) {
		if (e.getInventory().getType() == InventoryType.CHEST && e.getInventory().getSize() == 9) {
			int filterID = -1;
			if (e.getInventory().getName() == "§4Red Filter") {
				filterID = 0;
			} else if (e.getInventory().getName() == "§2Green Filter") {
				filterID = 1;
			} else if (e.getInventory().getName() == "§9Blue Filter") {
				filterID = 2;
			} else if (e.getInventory().getName() == "§eYellow Filter") {
				filterID = 3;
			}
			if (filterID > -1) {
				Material[] filter = filters.get(filterID);
				for (int i = 0; i < 9; i++) {
					if (e.getInventory().getItem(i) != null) {
						filter[i] = e.getInventory().getItem(i).getType();
					} else {
						filter[i] = Material.AIR;
					}
				}
				filters.set(filterID, filter);
				saveFilter(filterID);
			}
		}
	}

	@Override
	public void onMoveFrom(InventoryMoveItemEvent e) {
		e.setCancelled(true);
	}

	@Override
	public void onMoveInto(InventoryMoveItemEvent e) {
		return;
	}

	@Override
	protected void resetItemAt(int id) {
		if (id != 4) {
			if (id % 2 == 0) {
				ItemStack batrod = Batrod.create();
				Batrod.setPower(batrod, getPower());
				dispenser.getInventory().setItem(id, batrod);
			} else {
				dispenser.getInventory().setItem(id, new ItemStack(Material.CHEST));
			}
		}
	}

	private void saveFilter(int id) {
		Material[] filter = filters.get(id);
		int slotID = id * 2 + 1;
		ItemStack item = dispenser.getInventory().getItem(slotID);
		ItemMeta itemMeta = item.getItemMeta();
		List<String> lore = itemMeta.getLore();
		for (int i = 0; i < 9; i++) {
			lore.set(i + 3, "§h" + filter[i].name());
		}
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);
	}

	private void saveFilters() {
		for (int i = 0; i < 4; i++) {
			saveFilter(i);
		}
	}

	@Override
	protected void setAvailableUpgrades() {
		upgradesAvailable.add(UpgradeType.RedstoneUpgrade);
	}

	@Override
	protected void setCoreSlot() {
		coreSlot = 0;
	}

	private void setupPowerIO() {
		@SuppressWarnings("deprecation")
		int direction = directionAdapter[dispenser.getRawData()];
		for (int i = 0; i < 6; i++) {
			allowedOutputs[i] = false;
			if (i == direction) {
				allowedInputs[i] = false;
			}
		}
	}

	@Override
	public ArmorStand spawnDesignEntityInternal(int id) {
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void updateBlock() {
		ItemStack item = dispenser.getInventory().getItem(4);
		if (item != null && power >= 20 * item.getAmount()) {
			Material material = item.getType();
			Location target;
			if (isInFilter(material, 0)) { // Red Filter
				target = dispenser.getLocation().add(filterDirections[directionAdapter[dispenser.getRawData() % 8]][0]);
			} else if (isInFilter(material, 1)) { // Green Filter
				target = dispenser.getLocation().add(filterDirections[directionAdapter[dispenser.getRawData() % 8]][1]);
			} else if (isInFilter(material, 2)) { // Blue Filter
				target = dispenser.getLocation().add(filterDirections[directionAdapter[dispenser.getRawData() % 8]][2]);
			} else if (isInFilter(material, 3)) { // Yellow Filter
				target = dispenser.getLocation().add(filterDirections[directionAdapter[dispenser.getRawData() % 8]][3]);
			} else { // No Filter
				target = dispenser.getLocation()
						.add(MachineManager.directions[directionAdapter[dispenser.getRawData() % 8]]);
			}
			int itemCountMoved = pushOneItemInto(4, dispenser.getInventory(), target);
			if (itemCountMoved > -1) {
				power -= 20 * itemCountMoved;
			}
		}
		if (particlesTimeout > 0) {
			particlesTimeout--;
			renderParticleSideColored(filterDirections[directionAdapter[dispenser.getRawData() % 8]][0], Color.RED);
			renderParticleSideColored(filterDirections[directionAdapter[dispenser.getRawData() % 8]][1], Color.GREEN);
			renderParticleSideColored(filterDirections[directionAdapter[dispenser.getRawData() % 8]][2], Color.BLUE);
			renderParticleSideColored(filterDirections[directionAdapter[dispenser.getRawData() % 8]][3], Color.YELLOW);
		}
	}

}
