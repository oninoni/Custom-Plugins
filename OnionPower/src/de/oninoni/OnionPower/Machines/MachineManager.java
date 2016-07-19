package de.oninoni.OnionPower.Machines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import de.oninoni.OnionPower.OnionPower;

public class MachineManager {
	
	private static OnionPower plugin = OnionPower.get();
	
	private HashMap<Location, Machine> machines;
	private HashMap<Location, Integer> displayTimeout;
	private HashMap<Location, ArmorStand> displayEntites;
	
	public MachineManager() {
		machines = new HashMap<>();
		displayTimeout = new HashMap<>();
		displayEntites = new HashMap<>();
	}
	
	public void update() {
		Set<Location> machinesKeySet = machines.keySet();
		
		for (Location pos : machinesKeySet)
			machines.get(pos).resetIO();
		
		for (Location pos : machinesKeySet)
			machines.get(pos).update();
		
		for (Location pos : machinesKeySet)
			machines.get(pos).processPowerTransfer();
		
		for (Location pos : machinesKeySet)
			machines.get(pos).updateUI();
		
		Set<Location> displayTimeoutKeySet = displayTimeout.keySet();
		
		List<Location> removeThis = new ArrayList<Location>();
		
		for (Location location : displayTimeoutKeySet) {
			int duration = displayTimeout.get(location);
			duration--;
			if(duration <= 0){
				ArmorStand displayEntity = displayEntites.get(location);
				displayEntity.remove();
				displayEntites.remove(location);
				removeThis.add(location);
				continue;
			}else{
				displayTimeout.put(location, duration);
			}
		}
		
		for (Location location : removeThis) {
			displayTimeout.remove(location);
		}
	}
	
	public void killAllNames(){
		Set<Location> displayTimeoutKeySet = displayTimeout.keySet();
		
		for (Location location : displayTimeoutKeySet) {
			ArmorStand displayEntity = displayEntites.get(location);
			displayEntity.remove();
		}
	}
	
	public void onClick(InventoryClickEvent e) {
		Machine machine = machines.get(e.getInventory().getHolder().getInventory().getLocation());
		if (machine == null)
		{
			Location location = e.getView().getTopInventory().getLocation();
			if (Generator.canCreate(e))
				machines.put(location, new Generator(location, this, 0, new HashMap<>()));
			if (ElectricFurnace.canCreate(e))
				machines.put(location, new ElectricFurnace(location, this, 0, new HashMap<>()));
			if (BatrodBox.canCreate(e))
				machines.put(location, new BatrodBox(location, this, 0, new HashMap<>()));
			if (Sorter.canCreate(e))
				machines.put(location, new Sorter(location, this, 0, new HashMap<>()));
			
			
			if(machines.get(e.getInventory().getLocation()) != null	)
				saveData();
		}
		else
		{
			machine.onClick(e);
		}
	}
	
	/*public void onOpen(InventoryOpenEvent e) {
		Machine machine = machines.get(e.getInventory().getLocation());
		if (machine == null) 
			return;
	}*/
	
	public void onDrag(InventoryDragEvent e){
		Machine machine = machines.get(e.getView().getTopInventory().getHolder().getInventory().getLocation());
		if(machine == null){
			e.setCancelled(true);
			return;
		}
		Set<Integer> slots = e.getRawSlots();
		
		for (Integer slot : slots) {
			if(slot < e.getView().getTopInventory().getSize() ){
				e.setCancelled(true);
				return;
			}
		}
	}
	
	public void onClose(InventoryCloseEvent e){
		Machine machine = machines.get(e.getInventory().getHolder().getInventory().getLocation());
		if(machine != null){
			machine.onClose(e);
		}
	}
	
	public void onMove(InventoryMoveItemEvent e){
		Location source = e.getSource().getLocation();
		Location destination = e.getDestination().getLocation();
		if(machines.get(source) != null)
			machines.get(source).onMoveFrom(e);
		if(machines.get(destination) != null)
			machines.get(destination).onMoveInto(e);
	}
	
	public void onBreak(BlockBreakEvent e){
		Location location = e.getBlock().getLocation();
		if(machines.containsKey(location)){
			machines.get(location).onBreak(e);
			machines.remove(location);
		}
	}
	
	public void onBoom(EntityExplodeEvent e){
		List<Block> blocks = e.blockList();
		List<Block> notExloding = new ArrayList<>();
		for (Block block : blocks) {
			Location location = block.getLocation();
			if(machines.containsKey(location)){
				if(machines.get(location).onBoom(block)){
					machines.remove(location);
				}else{
					notExloding.add(block);
				}
			}
		}
		for (Block block : notExloding) {
			e.blockList().remove(block);
		}
	}
	
	public void onLoad(ChunkLoadEvent e){
		for (Location pos : machines.keySet())
			if (pos.getChunk().getX() == e.getChunk().getX() && 
				pos.getChunk().getZ() == e.getChunk().getZ() &&
				pos.getWorld().getName().equalsIgnoreCase(e.getWorld().getName())) {
				Bukkit.broadcastMessage("LOADED CRAZY SHIT");
				machines.get(pos).load();
			}	
	}
	
	public void onMove(PlayerMoveEvent e){
		Block target = e.getPlayer().getTargetBlock((Set<Material>) null, 4);
		if(target == null)return;
		if(machines.containsKey(target.getLocation()) && !displayTimeout.containsKey(target.getLocation())){
			ArmorStand armorstand = (ArmorStand) target.getLocation().getWorld().spawnEntity(target.getLocation().add(0.5, 1, 0.5), EntityType.ARMOR_STAND);
			armorstand.setCustomName(machines.get(target.getLocation()).getDisplayName());
			armorstand.setCustomNameVisible(true);
			armorstand.setMarker(true);
			armorstand.setBasePlate(false);
			armorstand.setVisible(false);
			armorstand.setSmall(true);
			armorstand.setAI(false);
			armorstand.setGravity(false);
			displayEntites.put(target.getLocation(), armorstand);
			displayTimeout.put(target.getLocation(), 20);
		}
	}
	
	public void onUnload(ChunkUnloadEvent e){		
		for (Location pos : machines.keySet()) {
			if (pos.getChunk().getX() == e.getChunk().getX() && 
				pos.getChunk().getZ() == e.getChunk().getZ() &&
				pos.getWorld().getName().equalsIgnoreCase(e.getWorld().getName())) {
				Bukkit.broadcastMessage("UNLOADED HOLY SHIT");
				machines.get(pos).unload();
			}
		}
	}
		
	public Machine getMachine(Location pos) {
		return machines.get(pos);
	}
	
	public void loadData(){
		machines = new HashMap<>();
		plugin.reloadConfig();
		ConfigurationSection config = plugin.getConfig();
		if(config.isConfigurationSection("Machines")){
			ConfigurationSection machinesList = config.getConfigurationSection("Machines");
			Set<String> machineKeys = machinesList.getKeys(false);
			for (String sectionName : machineKeys) {
				ConfigurationSection machineSection = machinesList.getConfigurationSection(sectionName);
				int x = machineSection.getInt("X");
				int y = machineSection.getInt("Y");
				int z = machineSection.getInt("Z");
				World w = Bukkit.getWorld(machineSection.getString("world"));
				Class<?> MachineClass;
				try {
					MachineClass = Class.forName(machineSection.getString("type"));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					break;
				}
				Location l = new Location(w, x, y, z);
				if(MachineClass == Generator.class){
					machines.put(l, new Generator(l, this, new HashMap<>()));
				}
				else if(MachineClass == ElectricFurnace.class){
					machines.put(l, new ElectricFurnace(l, this, new HashMap<>()));
				}
				else if(MachineClass == BatrodBox.class){
					machines.put(l, new BatrodBox(l, this, new HashMap<>()));
				}
				else if(MachineClass == Sorter.class){
					machines.put(l, new Sorter(l, this, new HashMap<>()));
				}
			}
		}
	}
	
	public void saveData(){
		plugin.reloadConfig();
		ConfigurationSection config = plugin.getConfig();
		config.createSection("Machines");
		ConfigurationSection machinesList = config.getConfigurationSection("Machines");
		for (Location pos : machines.keySet()) {
			Machine machine = machines.get(pos);
			String sectionName = pos.getBlockX() + "" + pos.getBlockY() + "" + pos.getBlockZ() + "" + pos.getWorld().getName();
			machinesList.createSection(sectionName);
			ConfigurationSection machineSection = machinesList.getConfigurationSection(sectionName);
			machineSection.set("X", pos.getBlockX());
			machineSection.set("Y", pos.getBlockY());
			machineSection.set("Z", pos.getBlockZ());
			machineSection.set("world", pos.getWorld().getName());
			//machineSection.set("power", machine.getPower());
			machineSection.set("type", machine.getClass().getName());
		}
		plugin.saveConfig();
		Bukkit.getLogger().info("Saved Data!");
	}
}
