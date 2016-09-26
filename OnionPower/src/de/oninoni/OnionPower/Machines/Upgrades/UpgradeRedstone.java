package de.oninoni.OnionPower.Machines.Upgrades;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.Items.CustomsItems;
import de.oninoni.OnionPower.Machines.Machine;
import de.oninoni.OnionPower.Machines.Machine.UpgradeType;

public class UpgradeRedstone extends Upgrade{
	
	public enum ExpectedPower{
		On,
		Off,
		Ignore
	}
	
	public ExpectedPower powerSetting = ExpectedPower.Ignore;

	public UpgradeRedstone() {
		super();
	}

	@Override
	public UpgradeType getType() {
		return UpgradeType.RedstoneUpgrade;
	}
	
	public boolean isMachineOnline(Machine m){
		boolean power = m.getPosition().getBlock().getBlockPower() > 0;
		return (
			powerSetting == ExpectedPower.Ignore ||
			(powerSetting == ExpectedPower.On && power) ||
			(powerSetting == ExpectedPower.Off && !power)
		);
	}

	@Override
	public ItemStack getSettingsItem() {
		ArrayList<String> lore = new ArrayList<>();
		switch (powerSetting) {
		case Ignore:
			lore.add("§rRedstone is ignored!");
			break;
		case Off:
			lore.add("§rRedstone should be off!");
			break;
		case On:
			lore.add("§rRedstone should be on!");
			break;
		}
		ItemStack settingsItem = CustomsItems.getGlassPane((byte) 6, "§5Redstone State Expected:", lore);
		return settingsItem;
	}

	@Override
	public ItemStack onClickSetting() {
		powerSetting = ExpectedPower.values()[(powerSetting.ordinal() + 1) % ExpectedPower.values().length];
		return getSettingsItem();
	}
}
