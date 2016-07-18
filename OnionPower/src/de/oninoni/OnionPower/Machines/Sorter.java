package de.oninoni.OnionPower.Machines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import de.oninoni.OnionPower.NMSAdapter;
import de.oninoni.OnionPower.Items.Batrod;
import de.oninoni.OnionPower.Items.CustomsItems;
import de.oninoni.OnionPower.Items.PowerCore;
import de.oninoni.OnionPower.Machines.Upgrades.Upgrade;

public class Sorter extends MachineDispenser{
	
	private static final Vector[][] filterDirections = {
			{
				new Vector(0.0, 1.0, 0.0),
				new Vector(0.0, 0.0, 1.0),
				new Vector(0.0, 0.0, -1.0),
				new Vector(0.0, -1.0, 0.0)
			},
			{
				new Vector(1.0, 0.0, 0.0),
				new Vector(0.0, 0.0, -1.0),
				new Vector(0.0, 0.0, 1.0),
				new Vector(-1.0, 0.0, 0.0)
			},
			{
				new Vector(0.0, 1.0, 0.0),
				new Vector(-1.0, 0.0, 0.0),
				new Vector(1.0, 0.0, 0.0),
				new Vector(0.0, -1.0, 0.0)
			},
			{
				new Vector(0.0, 1.0, 0.0),
				new Vector(0.0, 0.0, -1.0),
				new Vector(0.0, 0.0, 1.0),
				new Vector(0.0, -1.0, 0.0)
			},
			{
				new Vector(1.0, 0.0, 0.0),
				new Vector(0.0, 0.0, 1.0),
				new Vector(0.0, 0.0, -1.0),
				new Vector(-1.0, 0.0, 0.0)
			},
			{
				new Vector(0.0, 1.0, 0.0),
				new Vector(1.0, 0.0, 0.0),
				new Vector(-1.0, 0.0, 0.0),
				new Vector(0.0, -1.0, 0.0)
			}
	};
	
	private ArrayList<Material[]> filter = new ArrayList<>();
	
	public Sorter(Location position, MachineManager machineManager, int power, HashMap<Integer, Upgrade> upgrades) {
		super(position, machineManager, power, upgrades);
		for(int i = 0; i < 4; i++){
			filter.add(new Material[]{Material.AIR, Material.AIR, Material.AIR, Material.AIR, Material.AIR, Material.AIR, Material.AIR, Material.AIR, Material.AIR});
		}
		saveFilters();
		ItemStack powerCore = PowerCore.create(this);
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				dispenser.getInventory().setItem(coreSlot, powerCore);
				dispenser.getInventory().setItem(2, CustomsItems.getGlassPane((byte) 0, "§4Click to Enable Particles"));
				dispenser.getInventory().setItem(6, CustomsItems.getGlassPane((byte) 0, "§4§kSecret§r §4§kMessage"));
				dispenser.getInventory().setItem(8, CustomsItems.getGlassPane((byte) 0, "§4§kAnother§r §4§kSecret§r §4§kMessage"));
				
				List<String> lore = new ArrayList<>();
				
				lore.add("§6Click here to add items to the filter.");
				lore.add("§6If an items enters the Sorter wich is not filtered");
				lore.add("§6it will leave the sorter towards the front.");
				
				dispenser.getInventory().setItem(1, CustomsItems.getGlassPane((byte) 14, "§4Red Sorting Channel", lore));
				dispenser.getInventory().setItem(3, CustomsItems.getGlassPane((byte) 13, "§2Green Sorting Channel", lore));
				dispenser.getInventory().setItem(5, CustomsItems.getGlassPane((byte) 11, "§9Blue Sorting Channel", lore));
				dispenser.getInventory().setItem(7, CustomsItems.getGlassPane((byte) 4 , "§eYellow Sorting Channel", lore));
				
				NMSAdapter.setInvNameDispenser(dispenser, getDisplayName());
				for(HumanEntity viewer : dispenser.getInventory().getViewers()){
					viewer.closeInventory();
					viewer.openInventory(dispenser.getInventory());
				}
			}
		}, 1L);
		setupPowerIO();
	}
	
	public Sorter(Location position, MachineManager machineManager, HashMap<Integer, Upgrade> upgrades){
		super(position, machineManager, upgrades);
		loadFilters();
		setupPowerIO();
	}
	
	private void setupPowerIO(){
		@SuppressWarnings("deprecation")
		int direction = directionAdapter[dispenser.getRawData()];
		for(int i = 0; i < 6; i++){
			allowedOutputs[i] = false;
			if(i == direction){
				allowedInputs[i] = false;
			}
		}
	}
	
	private void loadFilter(int id){
		
	}
	
	private void loadFilters(){
		for(int i = 0; i < 4; i++){
			loadFilter(i);
		}
	}
	
	private void saveFilter(int id){
		
	}
	
	private void saveFilters(){
		for(int i = 0; i < 4; i++){
			saveFilter(i);
		}
	}
	
	private boolean isInFilter(Material m, int id){
		Material[] subFilter = filter.get(id);
		for(int i = 0; i < subFilter.length; i++){
			if(m == subFilter[i]){
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected void setCoreSlot() {
		coreSlot = 0;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void updateBlock() {
		requestFromConnected();
		ItemStack item = dispenser.getInventory().getItem(4);
		if(item != null && power >= 20){
			Material material = item.getType();
			Location target;
			if(isInFilter(material, 0)){		// Red Filter
				target = dispenser.getLocation().add(filterDirections[directionAdapter[dispenser.getRawData()]][0]);
			}else if(isInFilter(material, 1)){	// Green Filter
				target = dispenser.getLocation().add(filterDirections[directionAdapter[dispenser.getRawData()]][1]);
			}else if(isInFilter(material, 2)){	// Blue Filter
				target = dispenser.getLocation().add(filterDirections[directionAdapter[dispenser.getRawData()]][2]);
			}else if(isInFilter(material, 3)){	// Yellow Filter
				target = dispenser.getLocation().add(filterDirections[directionAdapter[dispenser.getRawData()]][3]);
			}else{								// No Filter
				target = dispenser.getLocation().add(directions[directionAdapter[dispenser.getRawData()]]);
			}
			int itemCountMoved = pushOneItemInto(4, dispenser.getInventory(), target);
			if(itemCountMoved > -1){
				power -= 20 * itemCountMoved;
			}
		}
	}
	
	public static boolean canCreate(InventoryClickEvent e){
		if(!(e.getInventory().getType() == InventoryType.DISPENSER))return false;
		ItemStack item = e.getCursor();
		if(!(Batrod.check(item) || item.getType() == Material.CHEST))return false;
		if(!(e.getRawSlot() == e.getView().convertSlot(e.getRawSlot())))return false;
		int slot = e.getRawSlot();
		for(int i = 0; i < 9; i++){
			if(i == slot)continue;
			if(i % 2 == 1){
				if(!(e.getView().getTopInventory().getItem(i) != null && e.getView().getTopInventory().getItem(i).getType() == Material.CHEST)) return false;
			}else if(i != 4){
				if(!Batrod.check(e.getView().getTopInventory().getItem(i))) return false;
			}
		}
		
		return true;
	}
	
	@Override
	public String getDisplayName() {
		return "§6§lElectrical Sorter";
	}

	@Override
	public int getMaxPowerOutput() {
		return 0;
	}

	@Override
	public int getMaxPowerInput() {
		return 100;
	}
	
	@Override
	public void onClick(InventoryClickEvent e) {
		super.onClick(e);
		int convertSlot = e.getView().convertSlot(e.getRawSlot());
		if(e.getRawSlot() == convertSlot && e.getRawSlot() < 9){
			if(convertSlot % 2 == 0){
				if(convertSlot != 4){
					e.setCancelled(true);
				}
			}else{
				e.setCancelled(true);
				String filterInvName = "";
				if(convertSlot == 1) filterInvName = "§4Red Filter";
				if(convertSlot == 3) filterInvName = "§2Green Filter";
				if(convertSlot == 5) filterInvName = "§9Blue Filter";
				if(convertSlot == 7) filterInvName = "§eYellow Filter";
				Inventory filterInv = Bukkit.createInventory(dispenser, 9, filterInvName);
				for(int i = 0; i < 9; i++){
					filterInv.setItem(i, new ItemStack(filter.get(convertSlot / 2)[i]));
				}
				e.getWhoClicked().openInventory(filterInv);
			}
		}
	}
	
	@Override
	public void onMoveInto(InventoryMoveItemEvent e) {
		return;
	}

	@Override
	public void onMoveFrom(InventoryMoveItemEvent e) {
		e.setCancelled(true);
	}
	
	@Override
	public void onClose(InventoryCloseEvent e) {
		if(e.getInventory().getType() == InventoryType.CHEST && e.getInventory().getSize() == 9){
			int filterID = -1;
			if(e.getInventory().getName() == "§4Red Filter"){
				filterID = 0;
			}else if(e.getInventory().getName() == "§2Green Filter"){
				filterID = 1;
			}else if(e.getInventory().getName() == "§9Blue Filter"){
				filterID = 2;
			}else if(e.getInventory().getName() == "§eYellow Filter"){
				filterID = 3;
			}
			if(filterID > -1){
				Material[] subFilter = filter.get(filterID);
				for(int i = 0; i < 9; i++){
					if(e.getInventory().getItem(i) != null){
						subFilter[i] = e.getInventory().getItem(i).getType();
					}else{
						subFilter[i] = Material.AIR;
					}
					plugin.getLogger().info("" + subFilter[i]);
				}
				filter.set(filterID, subFilter);
				saveFilter(filterID);
			}
		}
	}

	@Override
	public void onBreak(BlockBreakEvent e) {
		for(int i = 0; i < 9; i++){
			dispenser.getInventory().setItem(i, new ItemStack(Material.AIR));
		}
		dispenser.getInventory().setItem(0, Batrod.create());
		for(int i = 1; i < 5; i++){
			dispenser.getInventory().setItem(i, new ItemStack(Material.CHEST));
		}
	}
	
}
