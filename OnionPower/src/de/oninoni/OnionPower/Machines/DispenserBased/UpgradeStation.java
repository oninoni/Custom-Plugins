package de.oninoni.OnionPower.Machines.DispenserBased;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.oninoni.OnionPower.Items.PowerItems.Batrod;
import de.oninoni.OnionPower.Items.Statics.CustomsItems;
import de.oninoni.OnionPower.Machines.MachineManager;
import de.oninoni.OnionPower.Machines.Upgrades.Upgrade;
import de.oninoni.OnionPower.Machines.Upgrades.UpgradeManager.UpgradeType;

public class UpgradeStation extends MachineDispenser{
	
	private final static HashMap<UpgradeType, Material[]> recipes;
	
	static{
		recipes = new HashMap<>();
		recipes.put(UpgradeType.RedstoneUpgrade, new Material[]{
				Material.REDSTONE_TORCH_ON, Material.REDSTONE,				Material.REDSTONE_TORCH_ON,
				Material.REDSTONE,			Material.REDSTONE_TORCH_ON,		Material.REDSTONE
		});
		recipes.put(UpgradeType.RangeUpgrade, new Material[]{
				Material.REDSTONE_TORCH_ON,	Material.REDSTONE,				Material.REDSTONE_TORCH_ON,
				Material.REDSTONE,			Material.IRON_PICKAXE,			Material.REDSTONE
		});
		recipes.put(UpgradeType.LavaUpgrade, new Material[]{
				Material.BUCKET,			Material.REDSTONE,				Material.BUCKET,
				Material.REDSTONE,			Material.BUCKET,				Material.REDSTONE
		});
	}
	
	private enum State{
		Idle,
		Charging,
		Ready,
		Working,
	}
	
	private State state = State.Idle;
	private UpgradeType target = UpgradeType.Upgrade;
	private int working = 0;

	public UpgradeStation(Location position, MachineManager machineManager) {
		super(position, machineManager);
	}
	
	public UpgradeStation(OfflinePlayer owner, Location position, MachineManager machineManager, int power) {
		super(owner, position, machineManager, power);
		setUpwards();
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				Dispenser dispenser = getDispenser();
				
				dispenser.getInventory().setItem(1, CustomsItems.getCraftingCore());
				setCraftingCoreLore();
				dispenser.getInventory().clear(2);
			}
		}, 1L);
	}
	
	@SuppressWarnings("deprecation")
	private void setUpwards(){
		Dispenser dispenser = getDispenser();
		
		plugin.getLogger().info(dispenser.getRawData() + "");
		dispenser.getBlock().setData((byte) 1);
		plugin.getLogger().info(dispenser.getRawData() + "");
	}

	@Override
	protected boolean doesExplode() {
		return false;
	}

	@Override
	public int getDesignEntityCount() {
		return 1;
	}

	@Override
	public String getDisplayName() {
		return "§6§lUpgrade Station";
	}

	@Override
	public int getMaxPowerInput() {
		return 100;
	}

	@Override
	public int getMaxPowerOutput() {
		return 0;
	}

	@Override
	public boolean onClickFixed(Inventory inv, int slot, ItemStack cursor, Player p) {
		if(state == State.Working)return true;
		if(slot == 1){
			if(state != State.Ready)return true;
			Dispenser dispenser = getDispenser();
			
			for(int i = 0; i < 6; i++){
				ItemStack item = dispenser.getInventory().getItem(i + 3);
				int amount = item.getAmount() - 1;
				if(amount == 0){
					item.setType(Material.AIR);
				}else{
					item.setAmount(amount);
				}
				dispenser.getInventory().setItem(i + 3, item);
			}
			state = State.Working;
			setCraftingCoreLore();
			return true;
		}
		if(slot == 2){
			if(state != State.Idle)return true;
			if(Upgrade.isUpgrade(cursor) || cursor == null || cursor.getType() == Material.AIR){
				ItemStack current = inv.getItem(2);
				if(!cursor.isSimilar(current)){
					if(cursor.getAmount() > 1){
						int amount = cursor.getAmount() - 1;
						ItemStack overflow = cursor.clone();
						overflow.setAmount(amount);
						cursor.setAmount(1);
						HashMap<Integer, ItemStack> overflow2 = p.getInventory().addItem(overflow);
						if(overflow2.size() > 0){
							for (int key : overflow2.keySet()) {
								p.getLocation().getWorld().dropItemNaturally(p.getLocation(), overflow2.get(key));
							}
						}
					}
					inv.setItem(2, cursor);
					p.setItemOnCursor(current);
				}
				return true;
			}
			return true;
		}else{
			if(Upgrade.isUpgrade(cursor)){
				return true;
			}
			state = State.Idle;
			setCraftingCoreLore();
		}
		return false;
	}

	@Override
	protected void resetItemAt(int id) {
		switch (id) {
		case 0:
			getDispenser().getInventory().setItem(id, new Batrod(getPower()));
			break;
		case 1:
			getDispenser().getInventory().setItem(id, new ItemStack(Material.WORKBENCH));
			break;
		case 2:
			getDispenser().getInventory().setItem(id, new ItemStack(Material.DIAMOND));
			break;
		}
	}

	@Override
	protected void setAvailableUpgrades() {
		upgradesAvailable.add(UpgradeType.RedstoneUpgrade);
	}

	@Override
	protected void setCoreSlot() {
		coreSlot = 0;
	}

	@Override
	protected ArmorStand spawnDesignEntityInternal(int id){
		switch (id) {
		case 0:
			ArmorStand armorStand = (ArmorStand) position.getWorld().spawnEntity(position.clone().add(0.5, 0.075f, 0.5), EntityType.ARMOR_STAND);
			armorStand.setHelmet(new ItemStack(Material.WORKBENCH));
			armorStand.setSmall(true);
			return armorStand;
		}
		return null;
	}
	
	public boolean checkRecipe(Material[] recipe){
		Dispenser dispenser = getDispenser();
		
		for (int i = 0; i < recipe.length; i++) {
			ItemStack item = dispenser.getInventory().getItem(i + 3);
			if(item == null || item.getType() == Material.AIR || item.getType() != recipe[i])return false;
		}
		return true;
	}
	
	private void setCraftingCoreLore(){
		ItemStack craftingCore = getDispenser().getInventory().getItem(1);
		ItemMeta itemMeta = craftingCore.getItemMeta();
		ArrayList<String> lore = new ArrayList<>();
		switch (state) {
		case Idle:
			lore.add("§9Idle.");
			break;
		case Charging:
			lore.add("§9Charging...");
			lore.add(Upgrade.getName(target));
			break;
		case Ready:
			lore.add("§9Press to start!");
			lore.add(Upgrade.getName(target));
			break;
		case Working:
			lore.add("§9Processing... §4" + working + "%");
			lore.add(Upgrade.getName(target));
			break;
		}
		itemMeta.setLore(lore);
		craftingCore.setItemMeta(itemMeta);
		needsUpdate = true;
	}

	@Override
	public void updateBlock() {
		Dispenser dispenser = getDispenser();
		
		if(isInactive())return;
		switch (state) {
		case Idle:
			ItemStack upgrade = dispenser.getInventory().getItem(2);
			if(Upgrade.isUpgrade(upgrade) && upgrade.getItemMeta().getDisplayName().length() == 13){
				for(UpgradeType type: recipes.keySet()){
					Material[] recipe = recipes.get(type);
					if(checkRecipe(recipe)){
						target = type;
						state = State.Charging;
						setCraftingCoreLore();
					}
				}
			}
			return;
		case Charging:
			if(power >= 1000){
				state = State.Ready;
				setCraftingCoreLore();
			}
			return;
		case Ready:
			return;
		case Working:
			if(working < 100){
				working++;
				power -=10;
				setCraftingCoreLore();
			}else{
				working = 0;
				dispenser.getInventory().setItem(2, Upgrade.getItem(target));
				state = State.Idle;
				setCraftingCoreLore();
			}
			return;
		}
	}
}
