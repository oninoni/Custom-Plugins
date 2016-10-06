package de.oninoni.OnionPower.Machines.DispenserBased;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.oninoni.OnionPower.Items.Batrod;
import de.oninoni.OnionPower.Items.CustomsItems;
import de.oninoni.OnionPower.Machines.MachineManager;
import de.oninoni.OnionPower.Machines.Upgrades.Upgrade;
import de.oninoni.OnionPower.Machines.Upgrades.UpgradeManager.UpgradeType;

public class UpgradeStation extends MachineDispenser{
	
	private final static HashMap<UpgradeType, Material[]> recipes;
	
	static{
		recipes = new HashMap<>();
		recipes.put(UpgradeType.RedstoneUpgrade, new Material[]{
			Material.REDSTONE_TORCH_ON, Material.REDSTONE,			 Material.REDSTONE_TORCH_ON,
			Material.REDSTONE,			Material.REDSTONE_TORCH_ON,	 Material.REDSTONE
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
	
	public UpgradeStation(Location position, MachineManager machineManager, int power) {
		//TODO Always Up
		super(position, machineManager, power);
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				dispenser.getInventory().setItem(1, CustomsItems.getCraftingCore());
				setCraftingCoreLore();
				dispenser.getInventory().clear(2);
			}
		}, 1L);
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
		//TODO Showing what is crafting
		//TODO Removing Recipe when ready -> Idle
		if(slot == 1){
			if(state != State.Ready)return true;
			for(int i = 0; i < 6; i++){
				dispenser.getInventory().clear(i + 3);
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
						int ammount = cursor.getAmount() - 1;
						ItemStack overflow = cursor.clone();
						overflow.setAmount(ammount);
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
		}
		return false;
	}

	@Override
	protected void resetItemAt(int id) {
		switch (id) {
		case 0:
			ItemStack batrod = Batrod.create();
			Batrod.setPower(batrod, getPower());
			dispenser.getInventory().setItem(id, batrod);
			break;
		case 1:
			dispenser.getInventory().setItem(0, new ItemStack(Material.WORKBENCH));
			break;
		case 2:
			dispenser.getInventory().setItem(0, new ItemStack(Material.DIAMOND));
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
	public void spawnDesignEntity(int id) {
		return;
	}
	
	public boolean checkRecipe(Material[] recipe){
		for (int i = 0; i < recipe.length; i++) {
			ItemStack item = dispenser.getInventory().getItem(i + 3);
			if(item == null || item.getType() == Material.AIR || item.getType() != recipe[i])return false;
		}
		return true;
	}
	
	private void setCraftingCoreLore(){
		ItemStack craftingCore = dispenser.getInventory().getItem(1);
		ItemMeta itemMeta = craftingCore.getItemMeta();
		ArrayList<String> lore = new ArrayList<>();
		switch (state) {
		case Idle:
			lore.add("§9Idle.");
			break;
		case Charging:
			lore.add("§9Charging...");
			break;
		case Ready:
			lore.add("§9Press to start!");
			break;
		case Working:
			lore.add("§9Processing... " + working + "%");
			break;
		}
		itemMeta.setLore(lore);
		craftingCore.setItemMeta(itemMeta);
		needsUpdate = true;
	}

	@Override
	public void updateBlock() {
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
				dispenser.getInventory().setItem(2, Upgrade.getItem(target));
				state = State.Idle;
				setCraftingCoreLore();
			}
			return;
		}
	}
}
