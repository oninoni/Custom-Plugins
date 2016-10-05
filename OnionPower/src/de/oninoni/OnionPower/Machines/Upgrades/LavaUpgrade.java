package de.oninoni.OnionPower.Machines.Upgrades;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.Items.CustomsItems;
import de.oninoni.OnionPower.Machines.Machine;
import de.oninoni.OnionPower.Machines.HopperBased.FluidHandler;
import de.oninoni.OnionPower.Machines.Upgrades.UpgradeManager.UpgradeType;

public class LavaUpgrade extends Upgrade {

	enum LavaUpgradeState {
		Water, ShouldLava, Lava, ShouldWater
	}

	public LavaUpgrade(Machine m) {
		this(m, LavaUpgradeState.Water.ordinal());
	}

	public LavaUpgrade(Machine m, int value) {
		super(m);
		state = LavaUpgradeState.values()[value];
	}

	protected LavaUpgradeState state;

	public int getState() {
		return state.ordinal();
	}

	public boolean isLava() {
		return state == LavaUpgradeState.Lava || state == LavaUpgradeState.ShouldWater;
	}

	@Override
	public UpgradeType getType() {
		return UpgradeType.LavaUpgrade;
	}

	@Override
	public ItemStack getSettingsItem() {
		ArrayList<String> lore = new ArrayList<>();
		if (state == LavaUpgradeState.Water) {
			lore.add("§4Water Mode");
		} else if (state == LavaUpgradeState.Lava) {
			lore.add("§4Lava Mode");
		} else {
			lore.add("§4To change the mode please");
			lore.add("§4empty the Container first!");
		}

		ItemStack settingsItem = CustomsItems.getGlassPane((byte) 6, "§6Active Mode:", lore);
		return settingsItem;
	}

	@Override
	public ItemStack onClickSetting() {
		if (machine instanceof FluidHandler) {
			FluidHandler fluidHandler = (FluidHandler) machine;
			if (fluidHandler.getLevel() > 0) {
				if (state == LavaUpgradeState.Water) {
					state = LavaUpgradeState.ShouldLava;
				} else if (state == LavaUpgradeState.Lava) {
					state = LavaUpgradeState.ShouldWater;
				}
			} else {
				if (state == LavaUpgradeState.Water || state == LavaUpgradeState.ShouldLava) {
					state = LavaUpgradeState.Lava;
				} else {
					state = LavaUpgradeState.Water;
				}
			}
		}

		return getSettingsItem();

	}

}
