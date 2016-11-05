package de.oninoni.OnionPower.Machines.DropperBasedMultiblock;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Hopper;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Directional;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Stairs;
import org.bukkit.util.Vector;

import de.oninoni.OnionPower.NMSAdapter;
import de.oninoni.OnionPower.Items.PowerItems.Batrod;
import de.oninoni.OnionPower.Machines.MachineManager;

public class Enricher extends MachineDropperMultiblock{
	
	int rotation = 0;
	int ticks = 0;
	
	Hopper inputHopper, outputHopper, outputHopperSecondary;
	
	public Enricher(Location position, MachineManager machineManager) {
		super(position, machineManager);
		getHoppers();
	}
	
	public Enricher(Location position, MachineManager machineManager, int power) {
		super(position, machineManager, power);
		plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				
				
				reOpenInventories();
			}
		}, 1L);
		getHoppers();
		if(!shouldNotGenerate){
			NMSAdapter.setInvName(inputHopper, "§4§lInput");
			NMSAdapter.setInvName(outputHopper, "§4§lOutput - Primary");
			NMSAdapter.setInvName(outputHopperSecondary, "§4§lOutput - Secondary");
		}
	}

	@Override
	protected void setMutltiblockTemplates() {
		HashMap<Vector, MaterialData> tiA = new HashMap<>();

		tiA.put(new Vector(-1.0f, 0.0f, 0.0f), new MaterialData(Material.IRON_BLOCK));
		tiA.put(new Vector( 1.0f, 0.0f, 0.0f), new MaterialData(Material.IRON_BLOCK));
		tiA.put(new Vector(-1.0f, 0.0f, 1.0f), new MaterialData(Material.IRON_BLOCK));
		tiA.put(new Vector( 0.0f, 0.0f, 1.0f), new MaterialData(Material.IRON_FENCE));
		tiA.put(new Vector( 1.0f, 0.0f, 1.0f), new MaterialData(Material.IRON_BLOCK));
		tiA.put(new Vector(-1.0f, 0.0f, 2.0f), new MaterialData(Material.HOPPER));
		tiA.put(new Vector( 0.0f, 0.0f, 2.0f), new MaterialData(Material.IRON_BLOCK));
		tiA.put(new Vector( 1.0f, 0.0f, 2.0f), new MaterialData(Material.HOPPER));
		
		tiA.put(new Vector(-1.0f, 1.0f, 0.0f), new MaterialData(Material.IRON_BLOCK));
		tiA.put(new Vector( 1.0f, 1.0f, 0.0f), new MaterialData(Material.IRON_BLOCK));
		tiA.put(new Vector( 0.0f, 1.0f, 1.0f), new MaterialData(Material.IRON_FENCE));

		tiA.put(new Vector(-1.0f, 2.0f, 0.0f), new MaterialData(Material.IRON_BLOCK));
		tiA.put(new Vector( 0.0f, 2.0f, 0.0f), new MaterialData(Material.HOPPER));
		tiA.put(new Vector( 1.0f, 2.0f, 0.0f), new MaterialData(Material.IRON_BLOCK));
		tiA.put(new Vector( 0.0f, 2.0f, 1.0f), new MaterialData(Material.IRON_BLOCK));
		
		HashMap<Vector, MaterialData> tA = new HashMap<>();
		tA.putAll(tiA);
		
		org.bukkit.material.Hopper hopper = new org.bukkit.material.Hopper(BlockFace.SOUTH);

		tA.put(new Vector(-1.0f, 0.0f, 2.0f), hopper.clone());
		tA.put(new Vector( 1.0f, 0.0f, 2.0f), hopper.clone());
		tA.put(new Vector( 0.0f, 2.0f, 0.0f), hopper.clone());
		
		tA.put(new Vector(-1.0f, 0.0f, 0.0f), new MaterialData(Material.QUARTZ_BLOCK));
		tA.put(new Vector( 1.0f, 0.0f, 0.0f), new MaterialData(Material.QUARTZ_BLOCK));
		tA.put(new Vector(-1.0f, 0.0f, 1.0f), new MaterialData(Material.QUARTZ_BLOCK));
		tA.put(new Vector( 1.0f, 0.0f, 1.0f), new MaterialData(Material.QUARTZ_BLOCK));
		tA.put(new Vector( 0.0f, 0.0f, 2.0f), new MaterialData(Material.QUARTZ_BLOCK));
		
		tA.put(new Vector(-1.0f, 1.0f, 0.0f), new MaterialData(Material.QUARTZ_BLOCK));
		tA.put(new Vector( 1.0f, 1.0f, 0.0f), new MaterialData(Material.QUARTZ_BLOCK));

		Stairs stairs = new Stairs(Material.QUARTZ_STAIRS);
		
		stairs.setFacingDirection(BlockFace.WEST);
		tA.put(new Vector(-1.0f, 2.0f, 0.0f), stairs.clone());

		stairs.setFacingDirection(BlockFace.EAST);
		tA.put(new Vector( 1.0f, 2.0f, 0.0f), stairs.clone());

		stairs.setFacingDirection(BlockFace.SOUTH);
		tA.put(new Vector( 0.0f, 2.0f, 1.0f), stairs.clone());
		
		putMultiblockTemplates(tiA, tA);
	}
	
	private void getHoppers(){
		Vector vecInput = new Vector(0, 2, 0);
		Vector vecOutput = new Vector(1, 0, 2);
		Vector vecOutputSecondary = new Vector(-1, 0,2);
		
		vecInput = rotateVector(vecInput, ((Directional)dropper.getData()).getFacing());
		vecOutput = rotateVector(vecOutput, ((Directional)dropper.getData()).getFacing());
		vecOutputSecondary = rotateVector(vecOutputSecondary, ((Directional)dropper.getData()).getFacing());
		
		inputHopper = (org.bukkit.block.Hopper) position.clone().add(vecInput).getBlock().getState();
		outputHopper = (org.bukkit.block.Hopper) position.clone().add(vecOutput).getBlock().getState();
		outputHopperSecondary = (org.bukkit.block.Hopper) position.clone().add(vecOutputSecondary).getBlock().getState();
	}

	@Override
	protected boolean doesExplode() {
		return true;
	}

	@Override
	public int getDesignEntityCount() {
		return 1;
	}

	@Override
	public String getDisplayName() {
		return "§6§lNuclear Enricher";
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
		if(id == coreSlot)dropper.getInventory().setItem(id, new Batrod(getPower()));
		//TODO Auto-generated method stub
	}

	@Override
	protected void setAvailableUpgrades() {
		
	}

	@Override
	protected void setCoreSlot() {
		coreSlot = 7;
	}
	
	@Override
	protected ArmorStand spawnDesignEntityInternal(int id) {
		BlockFace forward = ((Directional)((BlockState) invHolder).getData()).getFacing();
		ArmorStand armorStand = (ArmorStand) position.getWorld().spawnEntity(position.clone().add(0.5, -0.2, 0.5).add(rotateVector(new Vector(0, 0, 1), forward)), EntityType.ARMOR_STAND);
		
		armorStand.setHelmet(new ItemStack(Material.STAINED_GLASS, 1, (short) 0));
		
		return armorStand;
	}

	@Override
	public void updateBlock() {
		Location l0 = designEntities.get(0).getLocation();
		float yaw = l0.getYaw() + 60;
		l0.setYaw(yaw);
		designEntities.get(0).teleport(l0);
	}
	
	@Override
	public boolean onBoom(Block e) {
		NMSAdapter.resetInvName(inputHopper);
		NMSAdapter.resetInvName(outputHopper);
		NMSAdapter.resetInvName(outputHopperSecondary);
		return super.onBoom(e);
	}
	
	@Override
	public void onBreak(BlockEvent e) {
		NMSAdapter.resetInvName(inputHopper);
		NMSAdapter.resetInvName(outputHopper);
		NMSAdapter.resetInvName(outputHopperSecondary);
		super.onBreak(e);
	}
}
