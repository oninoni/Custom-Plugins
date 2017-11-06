package de.oninoni.OnionPower.Machines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.util.Vector;

import de.oninoni.OnionPower.OnionPower;
import de.oninoni.OnionPower.Machines.DispenserBased.BatrodBox;
import de.oninoni.OnionPower.Machines.DispenserBased.MachineDispenser;
import de.oninoni.OnionPower.Machines.DispenserBased.Miner;
import de.oninoni.OnionPower.Machines.DispenserBased.Sorter;
import de.oninoni.OnionPower.Machines.DispenserBased.UpgradeStation;
import de.oninoni.OnionPower.Machines.DropperBased.Multiblock.ArcFurnace;
import de.oninoni.OnionPower.Machines.DropperBased.Multiblock.Enricher;
import de.oninoni.OnionPower.Machines.DropperBased.Multiblock.GoldArcFurnace;
import de.oninoni.OnionPower.Machines.DropperBased.Multiblock.IronArcFurnace;
import de.oninoni.OnionPower.Machines.DropperBased.Multiblock.MachineDropperMultiblock;
import de.oninoni.OnionPower.Machines.FurnaceBased.ElectricFurnace;
import de.oninoni.OnionPower.Machines.FurnaceBased.Generator;
import de.oninoni.OnionPower.Machines.HopperBased.FluidHandler;
import de.oninoni.OnionPower.Machines.HopperBased.MachineHopper;
import de.oninoni.OnionPower.Machines.HopperBased.SolarHopper;

public class MachineManager {

	private static OnionPower plugin = OnionPower.get();

	public static final Vector[] directions = { new Vector(1, 0, 0), new Vector(0, 1, 0), new Vector(0, 0, 1),
			new Vector(-1, 0, 0), new Vector(0, -1, 0), new Vector(0, 0, -1) };

	private HashMap<Location, Machine> machines;
	private HashMap<Location, Integer> displayTimeout;
	
	private HashMap<Location, Machine> protectedBlocks;

	private HashMap<Location, ArmorStand> displayEntites;

	public MachineManager() {
		machines = new HashMap<>();
		
		protectedBlocks = new HashMap<>();

		displayTimeout = new HashMap<>();
		displayEntites = new HashMap<>();
	}

	public ArrayList<InventoryHolder> getAdjacentInventoryHolders(Machine m, int forbiddenSide) {
		int[] forbiddenSides = { forbiddenSide };
		return getAdjacentInventoryHolders(m, forbiddenSides);
	}

	public ArrayList<InventoryHolder> getAdjacentInventoryHolders(Machine m, int[] forbiddenSides) {
		ArrayList<InventoryHolder> adjacents = new ArrayList<>();
		for (int i = 0; i < directions.length; i++) {
			Location l = m.getPosition().clone().add(directions[i]);
			if (l.getBlock().getState() instanceof InventoryHolder && !machines.containsKey(l)) {
				adjacents.add((InventoryHolder) l.getBlock().getState());
			}
		}
		return adjacents;
	}

	public ArrayList<Machine> getAdjacentMachines(Machine m, int forbiddenSide) {
		int[] forbiddenSides = { forbiddenSide };
		return getAdjacentMachines(m, forbiddenSides);
	}

	public ArrayList<Machine> getAdjacentMachines(Machine m, int[] forbiddenSides) {
		ArrayList<Machine> adjacents = new ArrayList<>();
		for (int i = 0; i < directions.length; i++) {
			Location l = m.getPosition().clone().add(directions[i]);
			if (machines.containsKey(l)) {
				adjacents.add(machines.get(l));
			}
		}
		return adjacents;
	}

	public Machine getMachine(Location pos) {
		return machines.get(pos);
	}

	public void killAllNames() {
		Set<Location> displayTimeoutKeySet = displayTimeout.keySet();

		for (Location location : displayTimeoutKeySet) {
			ArmorStand displayEntity = displayEntites.get(location);
			displayEntity.remove();
		}
	}
	
	private void addProtectedBlock(MachineDropperMultiblock m){
		List<Location> blocks = m.getProtectedBlocks();
		for (Location location : blocks) {
			protectedBlocks.put(location, m);
		}
	}

	public void loadData() {
		machines = new HashMap<>();
		protectedBlocks = new HashMap<>();
		plugin.reloadConfig();
		ConfigurationSection config = plugin.getConfig();
		if (config.isConfigurationSection("Machines")) {
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
				if (MachineClass == Generator.class) {
					machines.put(l, new Generator(l, this));
				} else if (MachineClass == ElectricFurnace.class) {
					machines.put(l, new ElectricFurnace(l, this));
				} else if (MachineClass == BatrodBox.class) {
					machines.put(l, new BatrodBox(l, this));
				} else if (MachineClass == Sorter.class) {
					machines.put(l, new Sorter(l, this));
				} else if (MachineClass == Miner.class) {
					machines.put(l, new Miner(l, this));
				} else if (MachineClass == FluidHandler.class) {
					machines.put(l, new FluidHandler(l, this));
				} else if (MachineClass == UpgradeStation.class) {
					machines.put(l, new UpgradeStation(l, this));
				} else if (MachineClass == SolarHopper.class){
					machines.put(l, new SolarHopper(l, this));
				} else if (MachineClass == GoldArcFurnace.class){
					ArcFurnace aF = new GoldArcFurnace(l, this);
					machines.put(l, aF);
					addProtectedBlock(aF);
				} else if (MachineClass == IronArcFurnace.class){
					ArcFurnace aF = new IronArcFurnace(l, this);
					machines.put(l, aF);
					addProtectedBlock(aF);
				} else if (MachineClass == Enricher.class){
					Enricher enricher = new Enricher(l, this);
					machines.put(l, enricher);
					addProtectedBlock(enricher);
				}
			}
			plugin.getLogger().info(machines.size() + " Machine/s loaded!");
		}
	}
	
	private void destroyProtectedBlock(Location l){
		BlockState blockState = l.getBlock().getState();
		if(blockState instanceof InventoryHolder){
			for (HumanEntity humanEntity : ((InventoryHolder) blockState).getInventory().getViewers()) {
				humanEntity.closeInventory();
			}
		}
		protectedBlocks.get(l).destroyMachine();
		List<Location> toBeRemoved = new ArrayList<>();
		for (Location loc : protectedBlocks.keySet()) {
			Machine m = protectedBlocks.get(loc);
			if(m == protectedBlocks.get(l)){
				toBeRemoved.add(loc);
			}
		}
		
		for (Location loc : toBeRemoved) {
			protectedBlocks.remove(loc);
		}
	}

	public void onBoom(EntityExplodeEvent e) {
		List<Block> blocks = e.blockList();
		List<Block> notExloding = new ArrayList<>();
		for (Block block : blocks) {
			Location location = block.getLocation();
			if (machines.containsKey(location)) {
				machines.get(location).closeInventories();
				if (machines.get(location).onBoom(block)) {
					machines.remove(location);
				} else {
					notExloding.add(block);
				}
			}else if(protectedBlocks.containsKey(location)){
				if(protectedBlocks.get(location).doesExplode()){
					destroyProtectedBlock(location);
				}else{
					notExloding.add(block);
				}
			}
		}
		for (Block block : notExloding) {
			e.blockList().remove(block);
		}
	}

	public void onBreak(BlockBreakEvent e) {
		Location location = e.getBlock().getLocation();
		if (machines.containsKey(location)) {
			machines.get(location).closeInventories();
			machines.get(location).onBreak(e);
			machines.remove(location);
		}else if(protectedBlocks.containsKey(location)){
			destroyProtectedBlock(location);
		}
	}

	public void onClick(InventoryClickEvent e) {
		Player owner = (Player) e.getWhoClicked();
		if (e.getInventory().getHolder() == null)
			return;
		Inventory inv = e.getView().getTopInventory().getHolder().getInventory();
		Machine machine = machines.get(inv.getLocation());
		if (machine == null) {
			if (e.getRawSlot() != e.getView().convertSlot(e.getRawSlot()) || e.getSlot() >= e.getInventory().getSize())
				return;
			Location location = e.getView().getTopInventory().getLocation();
						
			if (Machine.canCreate(e, Generator.class, InventoryType.FURNACE))
				machine = new Generator(owner, location, this, 
						Machine.getAllBatrodsPower(e.getView().getTopInventory()));
			else if (Machine.canCreate(e, ElectricFurnace.class, InventoryType.FURNACE))
				machine = new ElectricFurnace(owner, location, this,
						Machine.getAllBatrodsPower(e.getView().getTopInventory()));
			else if (Machine.canCreate(e, BatrodBox.class, InventoryType.DISPENSER))
				machine = new BatrodBox(owner, location, this,
						Machine.getAllBatrodsPower(e.getView().getTopInventory()));
			else if (Machine.canCreate(e, Sorter.class, InventoryType.DISPENSER))
				machine = new Sorter(owner, location, this,
						Machine.getAllBatrodsPower(e.getView().getTopInventory()));
			else if (Machine.canCreate(e, Miner.class, InventoryType.DISPENSER))
				machine = new Miner(owner, location, this,
						Machine.getAllBatrodsPower(e.getView().getTopInventory()));
			else if (Machine.canCreate(e, FluidHandler.class, InventoryType.HOPPER))
				machine = new FluidHandler(owner, location, this,
						Machine.getAllBatrodsPower(e.getView().getTopInventory()));
			else if (Machine.canCreate(e, UpgradeStation.class, InventoryType.DISPENSER))
				machine = new UpgradeStation(owner, location, this, 
						Machine.getAllBatrodsPower(e.getView().getTopInventory()));
			else if (Machine.canCreate(e, SolarHopper.class, InventoryType.HOPPER))
				machine = new SolarHopper(owner, location, this, 
						Machine.getAllBatrodsPower(e.getView().getTopInventory()));
			else if (Machine.canCreate(e, GoldArcFurnace.class, InventoryType.DROPPER))
				machine = new GoldArcFurnace(owner, location, this, 
						Machine.getAllBatrodsPower(e.getView().getTopInventory()));
			else if (Machine.canCreate(e, IronArcFurnace.class, InventoryType.DROPPER))
				machine = new IronArcFurnace(owner, location, this, 
						Machine.getAllBatrodsPower(e.getView().getTopInventory()));
			else if (Machine.canCreate(e, Enricher.class, InventoryType.DROPPER))
				machine = new Enricher(owner, location, this, 
						Machine.getAllBatrodsPower(e.getView().getTopInventory()));
			
			boolean addToList = machine != null;
			
			if (machine instanceof MachineDropperMultiblock)
			{
				MachineDropperMultiblock multiblock = (MachineDropperMultiblock)machine;
				if (multiblock.valid())
				{
					addProtectedBlock(multiblock);												
				}
				else
				{
					addToList = false;
				}
			}
			
			if (addToList)
				machines.put(location, machine);			
			
			if (machines.get(e.getInventory().getLocation()) != null)
				saveData();
		} else {
			if (e.getSlot() == -999)
				return;
			machine.onClick(e);
		}
	}

	public void onClose(InventoryCloseEvent e) {
		if (e.getInventory().getHolder() == null)
			return;
		Machine machine = machines.get(e.getInventory().getHolder().getInventory().getLocation());
		if (machine != null) {
			machine.onClose(e);
		}
	}

	public void onDispense(BlockDispenseEvent e) {
		Location location = e.getBlock().getLocation();
		if (machines.containsKey(location)) {
			Machine m = machines.get(location);
			if (m instanceof MachineDispenser) {
				((MachineDispenser) m).onDispense(e);
			}
		}
	}

	public void onDrag(InventoryDragEvent e) {
		if(e.getView().getTopInventory().getHolder() == null)return;
		Machine machine = machines.get(e.getView().getTopInventory().getHolder().getInventory().getLocation());
		if (machine == null)
			return;

		Set<Integer> slots = e.getRawSlots();

		for (Integer slot : slots) {
			if (slot < e.getView().getTopInventory().getSize()) {
				e.setCancelled(true);
				return;
			}
		}
	}

	public void onEntityDeath(EntityDamageEvent e) {
		
	}

	public void onLoad(ChunkLoadEvent e) {
		for (Location pos : machines.keySet())
			if (pos.getChunk().getX() == e.getChunk().getX() && pos.getChunk().getZ() == e.getChunk().getZ()
					&& pos.getWorld().getName().equalsIgnoreCase(e.getWorld().getName())) {
				machines.get(pos).load();
			}
	}

	public void onMove(InventoryMoveItemEvent e) {
		Location source = e.getSource().getLocation();
		Location destination = e.getDestination().getLocation();
		if (machines.containsKey(source))
			machines.get(source).onMoveFrom(e);
		if (machines.containsKey(destination))
			machines.get(destination).onMoveInto(e);
	}

	public void onMove(PlayerMoveEvent e) {
		Block target = e.getPlayer().getTargetBlock((Set<Material>) null, 4);
		if (target == null)
			return;
		if (machines.containsKey(target.getLocation()) && !displayTimeout.containsKey(target.getLocation())) {
			Machine m = machines.get(target.getLocation());
			if(!(target.getLocation().add(0, 1, 0)).getBlock().getType().isSolid()){
				ArmorStand armorstand = (ArmorStand) target.getLocation().getWorld()
						.spawnEntity(target.getLocation().add(0.5, 1, 0.5).add(m.getEffectOffset()), EntityType.ARMOR_STAND);
				armorstand.setCustomName(m.getDisplayName());
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
			String infoText = "{\"text\":\"" + m.getDisplayName() + "§r§0 | §4Charge: " + m.getPower() + "/" + m.getMaxPower() + "\"}";
			plugin.getNMSAdapter().sendTitle(e.getPlayer(), infoText, 0, 0, 0);
		}
	}

	public void onPickup(InventoryPickupItemEvent e) {
		Location pos = e.getInventory().getLocation();
		if (machines.containsKey(pos)) {
			Machine m = machines.get(pos);
			if (m instanceof MachineHopper)
				((MachineHopper) m).onPickup(e);
		}
	}

	public void onUnload(ChunkUnloadEvent e) {
		for (Location pos : machines.keySet()) {
			if (pos.getChunk().getX() == e.getChunk().getX() && pos.getChunk().getZ() == e.getChunk().getZ()
					&& pos.getWorld().getName().equalsIgnoreCase(e.getWorld().getName())) {
				machines.get(pos).unload();
			}
		}
	}

	public void saveData() {
		plugin.reloadConfig();
		ConfigurationSection config = plugin.getConfig();
		config.createSection("Machines");
		ConfigurationSection machinesList = config.getConfigurationSection("Machines");
		for (Location pos : machines.keySet()) {
			Machine machine = machines.get(pos);
			String sectionName = pos.getBlockX() + "" + pos.getBlockY() + "" + pos.getBlockZ() + ""
					+ pos.getWorld().getName();
			machinesList.createSection(sectionName);
			ConfigurationSection machineSection = machinesList.getConfigurationSection(sectionName);
			machineSection.set("X", pos.getBlockX());
			machineSection.set("Y", pos.getBlockY());
			machineSection.set("Z", pos.getBlockZ());
			machineSection.set("world", pos.getWorld().getName());
			machineSection.set("type", machine.getClass().getName());
		}
		plugin.saveConfig();
		// Bukkit.getLogger().info("Saved Data!");
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
			if (duration <= 0) {
				ArmorStand displayEntity = displayEntites.get(location);
				displayEntity.remove();
				displayEntites.remove(location);
				removeThis.add(location);
				continue;
			} else {
				displayTimeout.put(location, duration);
			}
		}

		for (Location location : removeThis) {
			displayTimeout.remove(location);
		}
	}
	
	public void onArmorStandManipulate(PlayerArmorStandManipulateEvent e){
		String name = e.getRightClicked().getCustomName();
		if(name == null)return;
		String[] split = name.split(":");
		if(split.length == 4){
			int x = Integer.parseInt(split[0]);
			int y = Integer.parseInt(split[1]);
			int z = Integer.parseInt(split[2]);
			//int id= Integer.parseInt(split[3]);
			
			Location l = new Location(e.getRightClicked().getWorld(), x, y, z);
			if(machines.containsKey(l)){
				//Machine m = machines.get(l);
				e.setCancelled(true);
			}
		}		
	}
	
	public void onTeleport(EntityTeleportEvent e){
		for (Location loc : machines.keySet()) {
			Machine m = machines.get(loc);
			for (ArmorStand armorStand : m.designEntities) {
				if(armorStand.getUniqueId() == e.getEntity().getUniqueId()){
					e.setCancelled(true);
				}
			}
		}
	}
}