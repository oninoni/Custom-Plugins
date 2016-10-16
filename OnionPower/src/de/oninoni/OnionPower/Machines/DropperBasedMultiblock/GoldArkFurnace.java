package de.oninoni.OnionPower.Machines.DropperBasedMultiblock;

import org.bukkit.Location;
import org.bukkit.Material;

import de.oninoni.OnionPower.Machines.MachineManager;

public class GoldArkFurnace extends ArkFurnace{

	public GoldArkFurnace(Location position, MachineManager machineManager) {
		super(position, machineManager);
	}
	
	public GoldArkFurnace(Location position, MachineManager machineManager, int power) {
		super(position, machineManager, power);
	}

	@Override
	public String getDisplayName() {
		return "�6�lGOLD - Ark Furnace";
	}
	
	@Override
	protected Material getSmeltingMaterial() {
		return Material.GOLD_INGOT;
	}

	@Override
	protected Material getSmeltingMaterialBlock() {
		return Material.GOLD_BLOCK;
	}

	@Override
	protected Material getSmeltingMaterialOre() {
		return Material.GOLD_ORE;
	}
}
