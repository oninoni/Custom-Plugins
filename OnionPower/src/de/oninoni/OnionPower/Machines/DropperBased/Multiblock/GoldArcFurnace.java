package de.oninoni.OnionPower.Machines.DropperBased.Multiblock;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

import de.oninoni.OnionPower.Machines.MachineManager;

public class GoldArcFurnace extends ArcFurnace{

	public GoldArcFurnace(Location position, MachineManager machineManager) {
		super(position, machineManager);
	}
	
	public GoldArcFurnace(OfflinePlayer owner, Location position, MachineManager machineManager, int power) {
		super(owner, position, machineManager, power);
	}

	@Override
	public String getDisplayName() {
		return "�6�lGOLD - Arc Furnace";
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
