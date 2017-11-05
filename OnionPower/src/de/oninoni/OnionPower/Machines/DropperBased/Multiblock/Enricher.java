package de.oninoni.OnionPower.Machines.DropperBased.Multiblock;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dropper;
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

import de.oninoni.OnionPower.Items.Uranium;
import de.oninoni.OnionPower.Items.PowerItems.Batrod;
import de.oninoni.OnionPower.Items.Statics.UraniumBuffer;
import de.oninoni.OnionPower.Machines.MachineManager;

public class Enricher extends MachineDropperMultiblock{
	
	private int speed = 0;
	
	Hopper inputHopper, outputHopper, outputHopperSecondary;
	
	public Enricher(Location position, MachineManager machineManager) {
		super(position, machineManager);
		getHoppers();
	}
	
	public Enricher(OfflinePlayer owner, Location position, MachineManager machineManager, int power) {
		super(owner, position, machineManager, power);
		plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				Dropper dropper = getDropper();
				
				dropper.getInventory().setItem(7, UraniumBuffer.create());
				
				reOpenInventories();
			}
		}, 1L);
		
		if(!shouldNotGenerate){
			getHoppers();
			
			plugin.getNMSAdapter().setInvName(inputHopper, "§4§lInput");
			plugin.getNMSAdapter().setInvName(outputHopper, "§4§lOutput - Primary");
			plugin.getNMSAdapter().setInvName(outputHopperSecondary, "§4§lOutput - Secondary");
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
		
		Dropper dropper = getDropper();
		
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
		if(id == coreSlot)
			getDropper().getInventory().setItem(id, new Batrod(getPower()));
		//TODO Auto-generated method stub
	}

	@Override
	protected void setAvailableUpgrades() {
		
	}

	@Override
	protected void setCoreSlot() {
		coreSlot = 4;
	}
	
	@Override
	protected ArmorStand spawnDesignEntityInternal(int id) {
		BlockFace forward = ((Directional)((BlockState) getDropper()).getData()).getFacing();
		ArmorStand armorStand = (ArmorStand) position.getWorld().spawnEntity(position.clone().add(0.5, -0.2, 0.5).add(rotateVector(new Vector(0, 0, 1), forward)), EntityType.ARMOR_STAND);
		
		armorStand.setHelmet(new ItemStack(Material.STAINED_GLASS, 1, (short) 0));
		
		return armorStand;
	}
	
	@Override
	public boolean onBoom(Block e) {
		plugin.getNMSAdapter().resetInvName(inputHopper);
		plugin.getNMSAdapter().resetInvName(outputHopper);
		plugin.getNMSAdapter().resetInvName(outputHopperSecondary);
		
		return super.onBoom(e);
	}
	
	@Override
	public void onBreak(BlockEvent e) {
		plugin.getNMSAdapter().resetInvName(inputHopper);
		plugin.getNMSAdapter().resetInvName(outputHopper);
		plugin.getNMSAdapter().resetInvName(outputHopperSecondary);
		
		super.onBreak(e);
	}

	@Override
	public void updateBlock() {
		Dropper dropper = getDropper();
		
		if(speed > 0){
			Location l0 = designEntities.get(0).getLocation();
			float yaw = l0.getYaw() + speed;
			l0.setYaw(yaw);
			designEntities.get(0).teleport(l0);
		}
		if(!isInactive()){
			if(inputHopper.getInventory().contains(Material.COBBLESTONE)){
				if(speed == 90){
					Random r = new Random();
					int r1 = r.nextInt(10);
					HashMap<Integer, ItemStack> overflow = new HashMap<>();
					if(r1 != 0)
						overflow = outputHopper.getInventory().addItem(new ItemStack(Material.GRAVEL));
					if(overflow.size() == 0){
						int itemPos = inputHopper.getInventory().first(Material.COBBLESTONE);
						ItemStack item = inputHopper.getInventory().getItem(itemPos);
						int amount = item.getAmount();
						if(amount > 1){
							item.setAmount(amount - 1);
							inputHopper.getInventory().setItem(itemPos, item);
						}else{
							inputHopper.getInventory().setItem(itemPos, new ItemStack(Material.AIR));
						}
						
						ItemStack uraniumBuffer = dropper.getInventory().getItem(7);
						int r2  =r.nextInt(8);
						if(r2 == 0){
							//TODO playSound Geigerzaehler
							if(UraniumBuffer.addLevel(uraniumBuffer)){
								HashMap<Integer, ItemStack> overflowSecondary = outputHopperSecondary.getInventory().addItem(new Uranium());
								if(overflowSecondary.size() == 0){
									UraniumBuffer.setLevel(uraniumBuffer, 0);
								}
							}
						}
						
						needsUpdate = true;
					}else{
						speed = 0;
					}
				}
				if(speed < 90){
					speed++;
				}
			}else{
				if(speed > 0){
					speed--;
				}
			}
		}else{
			if(speed > 0){
				speed--;
			}
		}
	}
}
