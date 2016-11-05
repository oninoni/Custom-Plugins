package de.oninoni.OnionPower.Machines.DropperBasedMultiblock;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Directional;
import org.bukkit.material.Hopper;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Stairs;
import org.bukkit.util.Vector;

import de.oninoni.OnionPower.NMSAdapter;
import de.oninoni.OnionPower.Items.PowerItems.Batrod;
import de.oninoni.OnionPower.Items.Statics.ArkFurnaceCore;
import de.oninoni.OnionPower.Items.Statics.ArkHeater;
import de.oninoni.OnionPower.Items.Statics.CustomsItems;
import de.oninoni.OnionPower.Machines.MachineManager;
import de.oninoni.OnionPower.Machines.Upgrades.UpgradeManager.UpgradeType;

public abstract class ArkFurnace extends MachineDropperMultiblock {
	
	org.bukkit.block.Hopper inputHopper, outputHopper;
	
	private int chimneyHeight;
	private Location smokePosition;

	public ArkFurnace(Location position, MachineManager machineManager) {
		super(position, machineManager);
		effectOffset = new Vector(0.0f, 1.0f, 0.0f);
		
		getHoppers();
	}
	
	public ArkFurnace(Location position, MachineManager machineManager, int power) {
		super(position, machineManager, power);
		effectOffset = new Vector(0.0f, 1.0f, 0.0f);
		plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				dropper.getInventory().setItem(0, CustomsItems.getArkFurnaceFurnace());
				dropper.getInventory().setItem(2, CustomsItems.getArkFurnaceFurnace());
				dropper.getInventory().setItem(3, CustomsItems.getArkFurnaceFurnace());
				dropper.getInventory().setItem(5, CustomsItems.getArkFurnaceFurnace());
				dropper.getInventory().setItem(6, CustomsItems.getArkFurnaceFurnace());
				dropper.getInventory().setItem(8, CustomsItems.getArkFurnaceFurnace());
				
				dropper.getInventory().setItem(7, ArkHeater.create(0));
				dropper.getInventory().setItem(1, ArkFurnaceCore.create(getSmeltingMaterial()));
				
				reOpenInventories();
			}
		}, 1L);
		
		if(!shouldNotGenerate){
			getHoppers();
			
			NMSAdapter.setInvName(inputHopper, getDisplayName() + "§4§l Input");
			NMSAdapter.setInvName(outputHopper, getDisplayName() + "§4§l Output");
		}
	}
	
	private void getHoppers(){
		Vector vecOutput = new Vector( 0,-1, 2);
		Vector vecInput = new Vector( 0, 1, 1);
		
		vecInput = rotateVector(vecInput, ((Directional)dropper.getData()).getFacing());
		vecOutput = rotateVector(vecOutput, ((Directional)dropper.getData()).getFacing());
		
		inputHopper = (org.bukkit.block.Hopper) position.clone().add(vecInput).getBlock().getState();
		outputHopper = (org.bukkit.block.Hopper) position.clone().add(vecOutput).getBlock().getState();
	}
	
	protected abstract Material getSmeltingMaterial();
	protected abstract Material getSmeltingMaterialBlock();
	protected abstract Material getSmeltingMaterialOre();

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
		tiA.put(new Vector( 0, 0, 1), new MaterialData(getSmeltingMaterialBlock()));
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
		
		stairs.setFacingDirection(BlockFace.WEST); // Flipped for some reason
		
		tA.put(new Vector(-1, 1, 0), stairs.clone());
		tA.put(new Vector(-1, 1, 1), stairs.clone());
		tA.put(new Vector(-1, 1, 2), stairs.clone());

		stairs.setFacingDirection(BlockFace.EAST); // Flipped for some reason
		
		tA.put(new Vector( 1, 1, 0), stairs.clone());
		tA.put(new Vector( 1, 1, 1), stairs.clone());
		tA.put(new Vector( 1, 1, 2), stairs.clone());
		
		tA.put(new Vector( 0, 1, 1), new Hopper(BlockFace.DOWN));
		tA.put(new Vector( 0,-1, 2), new Hopper(BlockFace.SOUTH));
		
		tA.put(new Vector( 0, 0, 1), new MaterialData(Material.STATIONARY_LAVA));
		tA.put(new Vector( 0, 0, 2), new MaterialData(Material.STAINED_GLASS, (byte) 7));
		
		putMultiblockTemplates(tiA, tA);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected boolean checkTemplate(HashMap<Vector, MaterialData> template) {
		int i;
		Location checkChimney = position;
		for(i = 0; i < 32; i++){
			checkChimney = position.clone().add(0.0f, 2.0f + i, 0.0f);
			if(checkChimney.getBlock() == null || checkChimney.getBlock().getType() != Material.BRICK)break;
		}
		if(i <= 2)return false;
		//plugin.getLogger().info(checkChimney.getBlock().getType() + " / " + checkChimney.getBlock().getData());
		if(checkChimney.getBlock() == null || checkChimney.getBlock().getType() != Material.STEP || checkChimney.getBlock().getData() != 0)return false;
		chimneyHeight = i;
		smokePosition = position.clone().add(0.5f, 2.0f + i, 0.5f);
		
		return super.checkTemplate(template);
	}

	@Override
	public List<Location> getProtectedBlocks() {
		List<Location> protectedBlocks = super.getProtectedBlocks();
		
		for(int i = 0; i <= chimneyHeight; i++){
			protectedBlocks.add(position.clone().add(0.0f, 2.0f + i, 0.0f));
		}
		
		return protectedBlocks;
	}
	
	@Override
	protected boolean doesExplode() {
		return true;
	}

	@Override
	public int getDesignEntityCount() {
		return 0;
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
		if(id == 4) dropper.getInventory().setItem(id, new Batrod(getPower()));
		
		if(id == 0 || id == 2 || id == 3 || id == 5 || id == 6 || id == 8){
			dropper.getInventory().setItem(id, new ItemStack(Material.FURNACE));
		}
		
		if(id == 7) dropper.getInventory().setItem(7, new ItemStack(Material.MAGMA));
		if(id == 1) dropper.getInventory().setItem(1, new ItemStack(getSmeltingMaterial()));
	}

	@Override
	protected void setAvailableUpgrades() {
		upgradesAvailable.add(UpgradeType.RedstoneUpgrade);
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
		position.getWorld().spawnParticle(Particle.SMOKE_LARGE, smokePosition, 1, 0.1, 0.0, 0.1, 0);
		if(isInactive())return;
		for (int i = 0; i < inputHopper.getInventory().getSize(); i++) {
			ItemStack itemStack = inputHopper.getInventory().getItem(i);
			if(itemStack == null || itemStack.getType() == Material.AIR)continue;
			if(itemStack.getType() == getSmeltingMaterialOre()){
				if(itemStack.getAmount() > 1){
					itemStack.setAmount(itemStack.getAmount() - 1);
				}else{
					itemStack.setType(Material.AIR);
				}
				inputHopper.getInventory().setItem(i, itemStack);
				
				ArkFurnaceCore.addTime(dropper.getInventory().getItem(1));
				
				break;
			}
		}
		List<Long> times = ArkFurnaceCore.getEnterTimes(dropper.getInventory().getItem(1));
		int heat = ArkHeater.readHeat(dropper.getInventory().getItem(7));
		if(heat >= 600){
			for (Long time : times) {
				if(time <= (System.currentTimeMillis() / 50) - (20 * 60 * (1000.0 / heat))){// 20 * 60 == 1 min Backzeit
					ArkFurnaceCore.removeTime(dropper.getInventory().getItem(1), time);
					ItemStack result = new ItemStack(getSmeltingMaterial(), 2);
					outputHopper.getInventory().addItem(result);
				}
			}
		}
		if(times.size() > 0){
			if(heat == 1000){
				if(power >= 100){
					power -= 400;
					powerOutputTotal += 400;
					position.getWorld().spawnParticle(Particle.SMOKE_LARGE, smokePosition, 3, 0.1, 0.0, 0.1, 0);
					position.getWorld().spawnParticle(Particle.FLAME, smokePosition, 4, 0.1, 0.5, 0.1, 0);
				}else{
					ArkHeater.setHeat(dropper.getInventory().getItem(7), Math.max(heat - 1, 0));
				}
			}else{
				if(power >= 200){
					ArkHeater.setHeat(dropper.getInventory().getItem(7), heat + 1);
					power -= 1000;
					powerOutputTotal += 1000;
					position.getWorld().spawnParticle(Particle.SMOKE_LARGE, smokePosition, 7, 0.1, 0.0, 0.1, 0);
					position.getWorld().spawnParticle(Particle.FLAME, smokePosition, 4, 0.1, 0.5, 0.1, 0);
				}else{
					ArkHeater.setHeat(dropper.getInventory().getItem(7), Math.max(heat - 1, 0));
				}
			}
		}else{
			ArkHeater.setHeat(dropper.getInventory().getItem(7), Math.max(heat - 1, 0));
		}
		needsUpdate = true;
	}
	
	@Override
	public boolean onBoom(Block e) {
		NMSAdapter.resetInvName(inputHopper);
		NMSAdapter.resetInvName(outputHopper);
		return super.onBoom(e);
	}
	
	@Override
	public void onBreak(BlockEvent e) {
		NMSAdapter.resetInvName(inputHopper);
		NMSAdapter.resetInvName(outputHopper);
		super.onBreak(e);
	}
}
