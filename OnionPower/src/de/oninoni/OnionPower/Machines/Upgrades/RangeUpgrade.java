package de.oninoni.OnionPower.Machines.Upgrades;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.Items.CustomsItems;
import de.oninoni.OnionPower.Machines.Machine;
import de.oninoni.OnionPower.Machines.Upgrades.UpgradeManager.UpgradeType;

public class RangeUpgrade extends Upgrade {

	int range;

	public RangeUpgrade(Machine m) {
		this(m, 5);
	}

	public RangeUpgrade(Machine m, int value) {
		super(m);
		range = value;
	}

	public int getRange() {
		return (int) Math.pow(2, range);
	}

	@Override
	public ItemStack getSettingsItem() {
		ArrayList<String> lore = new ArrayList<>();
		if (range == 0) {
			lore.add("§41 Block");
		} else {
			lore.add("§4" + getRange() + " Blocks");
		}

		ItemStack settingsItem = CustomsItems.getGlassPane((byte) 6, "§6Range will be limited to:", lore);
		return settingsItem;
	}

	@Override
	public UpgradeType getType() {
		return UpgradeType.RangeUpgrade;
	}

	@Override
	public ItemStack onClickSetting() {
		range = (range + 1) % 8;
		return getSettingsItem();
	}

}
