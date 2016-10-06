package de.oninoni.OnionPower.Machines.Upgrades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.OnionPower;
import de.oninoni.OnionPower.Items.CustomsItems;
import de.oninoni.OnionPower.Machines.Machine;

public class UpgradeManager {

	public enum UpgradeType {
		Upgrade, RangeUpgrade, RedstoneUpgrade, EfficiencyUpgrade, LavaUpgrade,
	}

	protected static OnionPower plugin = OnionPower.get();

	@SuppressWarnings("incomplete-switch")
	private static HashMap<Integer, Upgrade> load(Machine m) {
		// plugin.getLogger().info("Loading... Upgrades...");
		HashMap<Integer, Upgrade> loaded = new HashMap<>();

		List<String> lore = m.getPowerCore().getItemMeta().getLore();
		lore = lore.subList(5, lore.size());

		for (String line : lore) {
			line = line.substring(2);
			String[] splitted = line.split(":");
			if (splitted.length == 3) {
				int id = Integer.parseInt(splitted[0]);
				UpgradeType type = UpgradeType.values()[Integer.parseInt(splitted[1])];
				int value = Integer.parseInt(splitted[2]);
				// plugin.getLogger().info(type + " / " +
				// UpgradeType.RedstoneUpgrade.toString());
				// plugin.getLogger().info(id + "|" + type + "|" + value);
				switch (type) {
				case RedstoneUpgrade:
					loaded.put(id, new RedstoneUpgrade(m, value));
					break;
				case EfficiencyUpgrade:
					break;
				case RangeUpgrade:
					loaded.put(id, new RangeUpgrade(m, value));
					break;
				case LavaUpgrade:
					loaded.put(id, new LavaUpgrade(m, value));
					break;
				}
			}
		}

		// plugin.getLogger().info("Loaded " + loaded.size() + " Upgrades");

		return loaded;
	}

	private Machine machine;

	private Inventory inv;

	HashMap<Integer, Upgrade> upgrades;

	public UpgradeManager(Machine m) {
		this(m, load(m));
	}

	private UpgradeManager(Machine m, HashMap<Integer, Upgrade> upgrades) {
		this.upgrades = upgrades;
		machine = m;

		inv = plugin.getServer().createInventory((InventoryHolder) machine.getPosition().getBlock().getState(), 18,
				getName());
		for (int i = 0; i < 9; i++)
			inv.setItem(i + 9, CustomsItems.getGlassPane((byte) 15, "§4NO Upgrade in this Slot"));

		Set<Integer> keySet = upgrades.keySet();
		for (Integer key : keySet) {
			Upgrade u = upgrades.get(key);
			// plugin.getLogger().info("Loaded: " + u.getName());
			ItemStack uItem = Upgrade.getItem(u.getType());
			inv.setItem(key, uItem);
			inv.setItem(key + 9, u.getSettingsItem());
		}
	}

	public String getName() {
		return "§4Upgrades:";
	}

	public List<String> getPowerCoreLore() {
		List<String> lore = new ArrayList<>();
		Set<Integer> keySet = upgrades.keySet();
		for (Integer key : keySet) {
			Upgrade u = upgrades.get(key);
			if (u instanceof RedstoneUpgrade) {
				lore.add("§h" + key + ":" + u.getType().ordinal() + ":"
						+ ((RedstoneUpgrade) u).getPowerSetting().ordinal());
			} else if (u instanceof RangeUpgrade) {
				lore.add("§h" + key + ":" + u.getType().ordinal() + ":" + ((RangeUpgrade) u).getRange());
			} else if (u instanceof LavaUpgrade) {
				lore.add("§h" + key + ":" + u.getType().ordinal() + ":" + ((LavaUpgrade) u).getState());
			}
		}
		return lore;
	}

	public Upgrade getUpgrade(UpgradeType type) {
		Set<Integer> keySet = upgrades.keySet();
		for (Integer key : keySet) {
			Upgrade u = upgrades.get(key);
			if (u.getType() == type) {
				return u;
			}
		}
		return null;
	}

	public void onBoom() {
		Random r = new Random();
		Set<Integer> keySet = upgrades.keySet();
		for (Integer key : keySet) {
			if (r.nextInt(2) == 0) {
				Upgrade u = upgrades.get(key);
				machine.getPosition().getWorld().dropItemNaturally(machine.getPosition(), u.getItem());
			}
		}
	}

	public void onBreak() {
		Set<Integer> keySet = upgrades.keySet();
		for (Integer key : keySet) {
			Upgrade u = upgrades.get(key);
			machine.getPosition().getWorld().dropItemNaturally(machine.getPosition(), u.getItem());
		}
	}
	
	public boolean onClickFixed(Inventory inv, int slot, ItemStack cursor, Player p){
		if (slot >= 0 && slot <= 8) {
			// Removing all Upgrades
			if (inv.getItem(slot) != null && inv.getItem(slot).getType() != Material.AIR) {
				if (Upgrade.isUpgrade(inv.getItem(slot), UpgradeType.Upgrade)) {
					upgrades.remove(slot);
					inv.setItem(slot + 9,
							CustomsItems.getGlassPane((byte) 15, "§4NO Upgrade in this Slot"));
				}
			}
			// Adding all Upgrades
			if (cursor != null && cursor.getType() != Material.AIR) {
				if (Upgrade.isUpgrade(cursor, UpgradeType.RedstoneUpgrade)
						&& machine.upgradeAvailable(UpgradeType.RedstoneUpgrade)) {
					// plugin.getLogger().info("Adding " +
					// Upgrade.getName(UpgradeType.RedstoneUpgrade) + " to " +
					// slot);
					RedstoneUpgrade upgrade = new RedstoneUpgrade(machine);
					upgrades.put(slot, upgrade);
					inv.setItem(slot + 9, upgrade.getSettingsItem());
				} else if (Upgrade.isUpgrade(cursor, UpgradeType.RangeUpgrade)
						&& machine.upgradeAvailable(UpgradeType.RangeUpgrade)) {
					RangeUpgrade upgrade = new RangeUpgrade(machine);
					upgrades.put(slot, upgrade);
					inv.setItem(slot + 9, upgrade.getSettingsItem());
				} else if (Upgrade.isUpgrade(cursor, UpgradeType.LavaUpgrade)
						&& machine.upgradeAvailable(UpgradeType.LavaUpgrade)) {
					LavaUpgrade upgrade = new LavaUpgrade(machine);
					upgrades.put(slot, upgrade);
					inv.setItem(slot + 9, upgrade.getSettingsItem());
				} else {
					// plugin.getLogger().info("ABORT!" + e.getCursor());
					return true;
				}
			}
			updateIventories();
		} else if (slot >= 9 && slot <= 17) {
			// plugin.getLogger().info("Clicked in Slot: " + slot);
			if (inv.getItem(slot) != null && inv.getItem(slot).getType() != Material.AIR
					&& upgrades.containsKey(slot - 9)) {
				Upgrade u = upgrades.get(slot - 9);
				inv.setItem(slot, u.onClickSetting());
				updateIventories();
				return true;
			} else {
				if (slot == 10)
					p.getInventory().addItem(Upgrade.getItem(UpgradeType.RedstoneUpgrade));
				else
					p.getInventory().addItem(Upgrade.getItem(UpgradeType.LavaUpgrade));
				updateIventories();
				return true;
			}
		} else {
			plugin.getLogger().warning("Upgrade Slot: " + slot + " selected!");
		}
		return false;
	}

	public void openInterface(HumanEntity p) {
		p.openInventory(inv);
	}

	public void updateIventories() {
		machine.saveUpgradeAsLore();

		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				for (HumanEntity viewer : inv.getViewers()) {
					((Player) viewer).updateInventory();
				}
			}
		}, 1L);
	}
}
