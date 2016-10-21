package de.oninoni.OnionPower.Machines.DropperBasedMultiblock;

import org.bukkit.Location;
import org.bukkit.Material;

import de.oninoni.OnionPower.Machines.MachineManager;

public class IronArkFurnace extends ArkFurnace{

	public IronArkFurnace(Location position, MachineManager machineManager) {
		super(position, machineManager);
	}
	
	public IronArkFurnace(Location position, MachineManager machineManager, int power) {
		super(position, machineManager, power);
	}

	@Override
	public String getDisplayName() {
		return "�6�lIRON - Ark Furnace";
	}

	@Override
	protected Material getSmeltingMaterial() {
		return Material.IRON_INGOT;
	}

	@Override
	protected Material getSmeltingMaterialBlock() {
		return Material.IRON_BLOCK;
	}

	@Override
	protected Material getSmeltingMaterialOre() {
		return Material.IRON_ORE;
	}
}