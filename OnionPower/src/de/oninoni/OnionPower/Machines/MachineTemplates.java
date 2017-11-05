package de.oninoni.OnionPower.Machines;

import java.util.HashMap;

import org.bukkit.Material;

import de.oninoni.OnionPower.Machines.DispenserBased.BatrodBox;
import de.oninoni.OnionPower.Machines.DispenserBased.Miner;
import de.oninoni.OnionPower.Machines.DispenserBased.Sorter;
import de.oninoni.OnionPower.Machines.DispenserBased.UpgradeStation;
import de.oninoni.OnionPower.Machines.DropperBased.Multiblock.Enricher;
import de.oninoni.OnionPower.Machines.DropperBased.Multiblock.GoldArcFurnace;
import de.oninoni.OnionPower.Machines.DropperBased.Multiblock.IronArcFurnace;
import de.oninoni.OnionPower.Machines.FurnaceBased.ElectricFurnace;
import de.oninoni.OnionPower.Machines.FurnaceBased.Generator;
import de.oninoni.OnionPower.Machines.HopperBased.FluidHandler;
import de.oninoni.OnionPower.Machines.HopperBased.SolarHopper;

public class MachineTemplates {

	public static HashMap<String, Material[]> buildTemplates;

	// BARRIER => BATROD
	// IRRELEVANT (STAYS the same) => COMMAND

	static {
		buildTemplates = new HashMap<>();
		buildTemplates.put(Miner.class.getName(),
				new Material[] { Material.AIR, Material.BARRIER, Material.AIR, Material.AIR, Material.REDSTONE_BLOCK,
						Material.AIR, Material.AIR, Material.IRON_PICKAXE, Material.AIR });
		buildTemplates.put(Sorter.class.getName(), new Material[] { Material.BARRIER, Material.CHEST, Material.BARRIER,
				Material.CHEST, Material.COMMAND, Material.CHEST, Material.BARRIER, Material.CHEST, Material.BARRIER });
		buildTemplates.put(BatrodBox.class.getName(),
				new Material[] { Material.BARRIER, Material.BARRIER, Material.BARRIER, Material.BARRIER,
						Material.BARRIER, Material.BARRIER, Material.BARRIER, Material.BARRIER, Material.BARRIER });
		buildTemplates.put(UpgradeStation.class.getName(), 
				new Material[] {
						Material.BARRIER, Material.WORKBENCH, Material.DIAMOND,
				});

		buildTemplates.put(Generator.class.getName(), new Material[] { Material.COMMAND, Material.BARRIER });
		buildTemplates.put(ElectricFurnace.class.getName(), new Material[] { Material.BARRIER });

		buildTemplates.put(FluidHandler.class.getName(),
				new Material[] { Material.AIR, Material.AIR, Material.BARRIER, Material.GLASS, Material.WORKBENCH });
		buildTemplates.put(SolarHopper.class.getName(),
				new Material[]{	Material.GLASS, Material.LAPIS_BLOCK, Material.BARRIER, Material.LAPIS_BLOCK, Material.GLASS});
		
		buildTemplates.put(GoldArcFurnace.class.getName(), 
				new Material[]{ Material.FURNACE, Material.GOLD_INGOT, Material.FURNACE, Material.FURNACE, Material.BARRIER, Material.FURNACE, Material.FURNACE, Material.MAGMA, Material.FURNACE});
		buildTemplates.put(IronArcFurnace.class.getName(), 
				new Material[]{ Material.FURNACE, Material.IRON_INGOT, Material.FURNACE, Material.FURNACE, Material.BARRIER, Material.FURNACE, Material.FURNACE, Material.MAGMA, Material.FURNACE});
		buildTemplates.put(Enricher.class.getName(), 
				new Material[]{ Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK, Material.THIN_GLASS, Material.BARRIER, Material.THIN_GLASS, Material.IRON_BLOCK, Material.HOPPER, Material.IRON_BLOCK,});
	}
}
