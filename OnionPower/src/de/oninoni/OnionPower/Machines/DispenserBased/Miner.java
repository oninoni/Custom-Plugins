package de.oninoni.OnionPower.Machines.DispenserBased;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import de.oninoni.OnionPower.Items.Batrod;
import de.oninoni.OnionPower.Items.CustomsItems;
import de.oninoni.OnionPower.Machines.MachineManager;
import de.oninoni.OnionPower.Machines.Upgrades.RangeUpgrade;

public class Miner extends MachineDispenser{

	private int idleTimer = 0;
	
	private static final int MAXRANGE = 64;
	
	public Miner(Location position, MachineManager machineManager, int power) {
		super(position, machineManager, power);
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				dispenser.getInventory().setItem(0, CustomsItems.getGlassPane((byte) 8, "§4§kSecret§r §4§kMessage"));
				//dispenser.getInventory().setItem(1, getPowerCore());
				dispenser.getInventory().setItem(2, CustomsItems.getGlassPane((byte) 8, "§4§kLol§r §4§kROFL"));
				dispenser.getInventory().setItem(4, CustomsItems.getLaserPrism());
				dispenser.getInventory().setItem(3, CustomsItems.getGlassPane((byte) 8, "§4§kZweiundvierzig§r"));
				dispenser.getInventory().setItem(5, CustomsItems.getGlassPane((byte) 8, "§4§kIch§r §4§kmag§r §4§kZüge"));
				dispenser.getInventory().setItem(6, CustomsItems.getGlassPane((byte) 8, "§4§k42§r §4§k256"));
				dispenser.getInventory().setItem(7, CustomsItems.getMinerPickAxe());
				dispenser.getInventory().setItem(8, CustomsItems.getGlassPane((byte) 8, "§4§kPossseidon§r §4§kstinkt!"));
				
				reOpenInventories();
			}
		}, 1L);
		SetupPowerIO();
	}
	
	public Miner(Location position, MachineManager machineManager) {
		super(position, machineManager);
		SetupPowerIO();
	}
	
	private void SetupPowerIO(){
		for(int i = 0; i < 6; i++){
			allowedOutputs[i] = false;
		}
	}
	
	@Override
	protected void setCoreSlot() {
		coreSlot = 1;
	}

	@Override
	public String getDisplayName() {
		return "§6§lElectrical Stripminer";
	}

	@Override
	public int getMaxPowerOutput() {
		return 0;
	}

	@Override
	public int getMaxPowerInput() {
		return 500;
	}
	
	private int getRange(){
		RangeUpgrade upgrade = ((RangeUpgrade) upgradeManager.getUpgrade(UpgradeType.RangeUpgrade));
		if(upgrade != null){
			return upgrade.getRange();
		}
		return MAXRANGE;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void updateBlock() {
		if(dispenser.getInventory().getItem(7) != null){
			if(dispenser.getInventory().getItem(7).getDurability() >= 250){
				dispenser.getInventory().clear(7);
			}else{
				if(idleTimer <= 0){
					Vector direction = MachineManager.directions[directionAdapter[dispenser.getRawData() % 8]];
					int i;
					ArrayList<InventoryHolder> targetInventories = plugin.getMachineManager().getAdjacentInventoryHolders(this, dispenser.getRawData() % 8);
					for(i = 1; i <= getRange(); i++){
						Location newPos = position.clone();
						newPos.add(direction.clone().multiply(i));
						Block b = newPos.getBlock();
						if(b!= null && b.getType() != Material.AIR && targetInventories.size() > 0){
							if(b.getType() == Material.BEDROCK || b.getState() instanceof InventoryHolder || b.getState() instanceof Jukebox){
								idleTimer = 20;
								break;
							}
							if(b.getType() == Material.WATER || b.getType() == Material.STATIONARY_WATER)continue;
							if(b.getType() == Material.LAVA || b.getType() == Material.STATIONARY_LAVA)continue;
							double distance = position.distance(newPos);
							if(getPower() > 5 * distance){
								
								MinerShot shot = new MinerShot(this, b);
								Runnable r = new Runnable() {
									@Override
									public void run() {
										power -= 5;
										if(shot.update()){
											Collection<ItemStack> drops = b.getDrops(new ItemStack(Material.IRON_PICKAXE));
											ArrayList<ItemStack> totalLeftOver = new ArrayList<>();
											for (ItemStack i : drops) {
												ItemStack leftOver = i;
												for (InventoryHolder inventoryHolder : targetInventories) {
													HashMap<Integer, ItemStack> overflow = inventoryHolder.getInventory().addItem(leftOver);
													if(overflow.size() == 0){
														leftOver = null;
														break;
													}else{
														leftOver = overflow.get(overflow.keySet().toArray()[0]);
													}
												}
												if(leftOver != null){
													totalLeftOver.add(leftOver);
												}
											}
											if(totalLeftOver.size() > 0){
												idleTimer = 20;
												for (ItemStack itemStack : totalLeftOver) {
													position.getWorld().dropItem(position, itemStack);
												}
											}
											b.setType(Material.AIR);
											dispenser.getInventory().getItem(7).setDurability((short) (dispenser.getInventory().getItem(7).getDurability() + 1));
											
											return;
										}
										Bukkit.getScheduler().runTaskLater(plugin, this, 1L);
									}
								};
								idleTimer = i / 2 + 1;
								Bukkit.getScheduler().runTaskLater(plugin, r, 1L);
							}else{
								idleTimer = 20;
							}
							break;
						}
					}
					if(i > MAXRANGE){
						idleTimer = 20;
					}
				}else{
					idleTimer--;
				}
			}
		}
	}
	
	@Override
	public void onMoveFrom(InventoryMoveItemEvent e) {
		e.setCancelled(true);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onClick(InventoryClickEvent e) {
		if(!super.onClick(e)){
			if(dispenser.getInventory().getItem(7) == null && e.getSlot() == 7 && e.getCursor() != null && e.getCursor().getType() == Material.IRON_PICKAXE){
				ItemStack i = CustomsItems.getMinerPickAxe(e.getCursor().getDurability());
				e.setCursor(i);
			}else{
				e.setCancelled(true);
			}
			return false;
		}
		return true;
	}
	
	@Override
	protected void resetItemAt(int id) {
		if(id == 1){
			ItemStack batrod = Batrod.create();
			Batrod.setPower(batrod, getPower());
			dispenser.getInventory().setItem(id, batrod);
		}else if(id == 4){
			dispenser.getInventory().setItem(id, new ItemStack(Material.REDSTONE_BLOCK));
		}else if(id == 7){
			ItemStack i = new ItemStack(Material.IRON_PICKAXE);
			i.setDurability(dispenser.getInventory().getItem(7).getDurability());
			dispenser.getInventory().setItem(id, i);
		}else{
			dispenser.getInventory().setItem(id, new ItemStack(Material.AIR));
		}
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
	public void spawnDesignEntity(int id) {
		
	}

	@Override
	protected void setAvailableUpgrades() {
		availableUpgrades.add(UpgradeType.RedstoneUpgrade);
		availableUpgrades.add(UpgradeType.RangeUpgrade);
	}
}
