package de.oninoni.OnionPower.Machines.Upgrades;

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
}
