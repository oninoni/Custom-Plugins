package de.oninoni.OnionPower.Machines.Upgrades;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.Items.Statics.CustomsItems;
import de.oninoni.OnionPower.Machines.Machine;
import de.oninoni.OnionPower.Machines.Upgrades.UpgradeManager.UpgradeType;

public class RedstoneUpgrade extends Upgrade {

	public enum ExpectedPower {
		On, Off, Ignore
	}

	public ExpectedPower powerSetting;

	public RedstoneUpgrade(Machine m) {
		this(m, ExpectedPower.Ignore.ordinal());
	}

	public RedstoneUpgrade(Machine m, int value) {
		super(m);
		powerSetting = ExpectedPower.values()[value];
	}

	public ExpectedPower getPowerSetting() {
		return powerSetting;
	}

	@Override
	public ItemStack getSettingsItem() {
		ArrayList<String> lore = new ArrayList<>();
		switch (powerSetting) {
		case Ignore:
			lore.add("§4Redstone is ignored");
			break;
		case Off:
			lore.add("§4Redstone should be off");
			break;
		case On:
			lore.add("§4Redstone should be on");
			break;
		}
		ItemStack settingsItem = CustomsItems.getGlassPane((byte) 6, "§6Redstone State Expected:", lore);
		return settingsItem;
	}

	@Override
	public UpgradeType getType() {
		return UpgradeType.RedstoneUpgrade;
	}

	public boolean isMachineOnline(Machine m) {
		boolean power = m.getPosition().getBlock().getBlockPower() > 0;
		return (powerSetting == ExpectedPower.Ignore || (powerSetting == ExpectedPower.On && power)
				|| (powerSetting == ExpectedPower.Off && !power));
	}

	@Override
	public ItemStack onClickSetting() {
		powerSetting = ExpectedPower.values()[(powerSetting.ordinal() + 1) % ExpectedPower.values().length];
		return getSettingsItem();
	}
}
