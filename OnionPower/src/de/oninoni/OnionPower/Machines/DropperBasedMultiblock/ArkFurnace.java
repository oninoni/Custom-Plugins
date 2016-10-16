package de.oninoni.OnionPower.Machines.DropperBasedMultiblock;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Hopper;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Stairs;
import org.bukkit.util.Vector;

import de.oninoni.OnionPower.Items.PowerItems.Batrod;
import de.oninoni.OnionPower.Machines.MachineManager;

public class ArkFurnace extends MachineDropperMultiblock {

	public ArkFurnace(Location position, MachineManager machineManager) {
		super(position, machineManager);
	}
	
	public ArkFurnace(Location position, MachineManager machineManager, int power) {
		super(position, machineManager, power);
		//TODO Set Items
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void setMutltiblockTemplates() {
		HashMap<Vector, MaterialData> tiA = new HashMap<>();

		tiA.put(new Vector(-1,-1, 0), new MaterialData(Material.SMOOTH_BRICK));
		tiA.put(new Vector( 0,-1, 0), new MaterialData(Material.SMOOTH_BRICK));
		tiA.put(new Vector( 1,-1, 0), new MaterialData(Material.SMOOTH_BRICK));
		tiA.put(new Vector(-1,-1, 1), new MaterialData(Material.SMOOTH_BRICK));
		tiA.put(new Vector( 0,-1, 1), new MaterialData(Material.SMOOTH_BRICK));
		tiA.put(new Vector( 1,-1, 1), new MaterialData(Material.SMOOTH_BRICK));
		tiA.put(new Vector(-1,-1, 2), new MaterialData(Material.SMOOTH_BRICK));
		tiA.put(new Vector( 0,-1, 2), new MaterialData(Material.HOPPER));
		tiA.put(new Vector( 1,-1, 2), new MaterialData(Material.SMOOTH_BRICK));
		                          
		tiA.put(new Vector(-1, 0, 0), new MaterialData(Material.BRICK));
		
		tiA.put(new Vector( 1, 0, 0), new MaterialData(Material.BRICK));
		tiA.put(new Vector(-1, 0, 1), new MaterialData(Material.BRICK));
		//tiA.put(new Vector( 0, 0, 1), new Hopper(BlockFace.SOUTH)); //TODO Make Abstract to Create Iron and Gold Variants
		tiA.put(new Vector( 1, 0, 1), new MaterialData(Material.BRICK));
		tiA.put(new Vector(-1, 0, 2), new MaterialData(Material.BRICK));
		tiA.put(new Vector( 0, 0, 2), new MaterialData(Material.GLASS));
		tiA.put(new Vector( 1, 0, 2), new MaterialData(Material.BRICK));
                                  
		tiA.put(new Vector(-1, 1, 0), new MaterialData(Material.BRICK));
		tiA.put(new Vector( 0, 1, 0), new MaterialData(Material.BRICK));
		tiA.put(new Vector( 1, 1, 0), new MaterialData(Material.BRICK));
		tiA.put(new Vector(-1, 1, 1), new MaterialData(Material.BRICK));
		tiA.put(new Vector( 0, 1, 1), new MaterialData(Material.HOPPER));
		tiA.put(new Vector( 1, 1, 1), new MaterialData(Material.BRICK));
		tiA.put(new Vector(-1, 1, 2), new MaterialData(Material.BRICK));
		tiA.put(new Vector( 0, 1, 2), new MaterialData(Material.BRICK));
		tiA.put(new Vector( 1, 1, 2), new MaterialData(Material.BRICK));
		
		HashMap<Vector, MaterialData> tA = new HashMap<>();
		tA.putAll(tiA);
		
		Stairs stairs = new Stairs(Material.BRICK_STAIRS);
		stairs.setFacingDirection(BlockFace.EAST);
		
		tA.put(new Vector(-1, 1, 0), stairs.clone());
		tA.put(new Vector(-1, 1, 1), stairs.clone());
		tA.put(new Vector(-1, 1, 2), stairs.clone());

		stairs.setFacingDirection(BlockFace.WEST);
		
		tA.put(new Vector( 1, 1, 0), stairs.clone());
		tA.put(new Vector( 1, 1, 1), stairs.clone());
		tA.put(new Vector( 1, 1, 2), stairs.clone());
		
		tA.put(new Vector( 0, 1, 1), new Hopper(BlockFace.DOWN));
		tA.put(new Vector( 0,-1, 2), new Hopper(BlockFace.SOUTH));
		
		tA.put(new Vector( 0, 0, 1), new MaterialData(Material.STATIONARY_LAVA));
		tA.put(new Vector( 0, 0, 2), new MaterialData(Material.STAINED_GLASS, (byte) 7));
		
		setMultiblockTemplates(tiA, tA);
	}

	@Override
	protected boolean doesExplode() {
		return false;
	}

	@Override
	public int getDesignEntityCount() {
		return 0;
	}

	@Override
	public String getDisplayName() {
		return "§6§lArk - Furnace";
	}

	@Override
	public int getMaxPowerInput() {
		return 1000;
	}

	@Override
	public int getMaxPowerOutput() {
		return 0;
	}

	@Override
	public boolean onClickFixed(Inventory inv, int slot, ItemStack cursor, Player p) {
		return true;
	}

	@Override
	protected void resetItemAt(int id) {
		if(id == 4)dropper.getInventory().setItem(id, new Batrod(getPower()));
	}

	@Override
	protected void setAvailableUpgrades() {
		// TODO Rework Redstone Upgrade Using into every Machine itself
	}

	@Override
	protected void setCoreSlot() {
		coreSlot = 4;
	}

	@Override
	protected ArmorStand spawnDesignEntityInternal(int id) {
		return null;
	}

	@Override
	public void updateBlock() {
		// TODO Auto-generated method stub
		
	}
	
}
