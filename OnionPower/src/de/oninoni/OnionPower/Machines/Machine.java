package de.oninoni.OnionPower.Machines;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import de.oninoni.OnionPower.OnionPower;
import de.oninoni.OnionPower.Items.Batrod;
import de.oninoni.OnionPower.Items.PowerCore;
import de.oninoni.OnionPower.Machines.Upgrades.RedstoneUpgrade;
import de.oninoni.OnionPower.Machines.Upgrades.UpgradeManager;

public abstract class Machine {
	
	public enum UpgradeType{
		Upgrade,
		RangeUpgrade,
		RedstoneUpgrade,
		EfficiencyUpgrade
	}
	
	protected EnumSet<UpgradeType> availableUpgrades;
	protected abstract void setAvailableUpgrades();
	
	public boolean upgradeAvailable(UpgradeType type){
		return availableUpgrades.contains(type);
	}
	
	protected static OnionPower plugin = OnionPower.get();
	
	protected ArrayList<ArmorStand> designEntities;
	
	public ArrayList<ArmorStand> getDesignEntities() {
		return designEntities;
	}

	protected int coreSlot;
	
	public ItemStack getPowerCore(){
		ItemStack powerCore = invHolder.getInventory().getItem(coreSlot);
		return powerCore;
	}
	
	public void setPowerCore(ItemStack powerCore){
		invHolder.getInventory().setItem(coreSlot, powerCore);
	}
	
	protected UpgradeManager upgradeManager;
	
	private static final int MAX_CABLE_LENGTH = 16;
	
	protected boolean[] allowedInputs = {
		true,
		true,
		true,
		true,
		true,
		true
	};
	
	protected boolean[] allowedOutputs = {
		true,
		true,
		true,
		true,
		true,
		true
	};
	
	private MachineManager machineManager;
	
	protected Location position;
	protected InventoryHolder invHolder;
	
	private Vector vec;
	private String world;
	
	protected int power;
	protected int powerIntputTotal, powerOutputTotal;
	
	private int oldPower;
	private int oldPowerInputTotal, oldPowerOutputTotal;
	
	private List<Machine> sender = new ArrayList<>();
	
	public Machine(Location position, MachineManager machineManager, int power){
		setCoreSlot();
		invHolder = (InventoryHolder) position.getBlock().getState();
		this.power = power;
		setPowerCore(PowerCore.create(this));
		initValues(position, machineManager);
	}
	
	public Machine(Location position, MachineManager machineManager){
		setCoreSlot();
		invHolder = (InventoryHolder) position.getBlock().getState();
		
		BlockState state = position.getBlock().getState();
		if(state instanceof InventoryHolder){
			this.power = PowerCore.getPowerLevel(((InventoryHolder) state).getInventory().getItem(coreSlot));
		}else{
			plugin.getLogger().warning("Machine at " + position + "is not an Inventory anymore!");
			this.power = 0;
		}
		
		initValues(position, machineManager);
	}
	
	public void setUpgradeLore(){
		ItemStack powerCore = getPowerCore();
		ItemMeta itemMeta = powerCore.getItemMeta();
		List<String> lore = itemMeta.getLore();
		lore = lore.subList(0, 5);
		lore.addAll(upgradeManager.getPowerCoreLore());
		itemMeta.setLore(lore);
		powerCore.setItemMeta(itemMeta);
		setPowerCore(powerCore);
	}
	
	private void initValues(Location position, MachineManager machineManager){
		
		this.position = position;
		this.machineManager = machineManager;
		
		
		isLoaded = true;
		
		vec = position.toVector();
		world = position.getWorld().getName();
		
		designEntities = new ArrayList<>();
		designEntities.ensureCapacity(getDesignEntityCount());
		spawnDesignEntities();

		availableUpgrades = EnumSet.noneOf(UpgradeType.class);
		
		setAvailableUpgrades();
		upgradeManager = new UpgradeManager(this);
	}
	
	protected abstract boolean isMaterial(Material material);
	
	protected abstract void setCoreSlot();
	
	public abstract int getMaxPower();
	public abstract String getDisplayName();
	public abstract int getMaxPowerOutput();
	public abstract int getMaxPowerInput();
	public abstract void updateBlock();
	
	public abstract int getDesignEntityCount();
	
	public abstract void spawnDesignEntity(int id);
	
	protected void setEntity(int id, ArmorStand a) {
		if(designEntities.size() <= id){
			designEntities.add(a);
		}else{
			designEntities.set(id, a);
		}
	}
	
	protected void spawnDesignEntities() {
		for(int i = 0; i < getDesignEntityCount(); i++){
			spawnDesignEntity(i);
		}
	}
	
	public boolean onClick(InventoryClickEvent e){
		if(e.getInventory().getName() == upgradeManager.getName()){
			upgradeManager.onClick(e);
			return true;
		}
		int convertSlot = e.getView().convertSlot(e.getRawSlot());
		if(convertSlot == coreSlot){
			e.setCancelled(true);
			upgradeManager.openInterface(e.getWhoClicked());
		}
		return false;
	}
	
	public abstract void onMoveInto(InventoryMoveItemEvent e);
	public abstract void onMoveFrom(InventoryMoveItemEvent e);

	public abstract void onClose(InventoryCloseEvent e);
	
	protected abstract void resetItemAt(int id);
	protected abstract boolean doesExplode();

	public void updateDisplay() {
		//plugin.getLogger().info("Update!");
		ItemStack powerCore = getPowerCore();
		PowerCore.setPowerLevel(powerCore, this);
		setPowerCore(powerCore);
		updateInventories();
	}
	
	public void onBreak(BlockEvent e){
		for(int i = 0; i < invHolder.getInventory().getSize(); i++){
			resetItemAt(i);
		}
		//TODO Upgrades need to drop
	}
	
	public boolean onBoom(Block e){
		Random r = new Random();
		for(int i = 0; i < invHolder.getInventory().getSize(); i++){
			if(r.nextInt(2) == 0){
				resetItemAt(i);
			}else{
				invHolder.getInventory().setItem(i, new ItemStack(Material.AIR));
			}
		}
		//TODO here too
		return doesExplode();
	}
	
	private boolean isLoaded;
	
	public void closeInventories(){
		Object[] viewers = (invHolder).getInventory().getViewers().toArray();
		for (Object humanEntity : viewers) {
			((HumanEntity) humanEntity).closeInventory();
		}
	}
	
	public void updateInventories(){
		Object[] viewers = (invHolder).getInventory().getViewers().toArray();
		for (Object humanEntity : viewers) {
			((Player) humanEntity).updateInventory();
		}
	}
	
	public void reOpenInventories(){
		Object[] viewers = (invHolder).getInventory().getViewers().toArray();
		for (Object humanEntity : viewers) {
			((HumanEntity) humanEntity).closeInventory();
			((HumanEntity) humanEntity).openInventory(invHolder.getInventory());
		}
	}
	
	public static int getBatrodPower(InventoryClickEvent e, String key, InventoryType type){
		int result = 0;
		
		Material[] template = MachineTemplates.buildTemplates.get(key);
		
		for (int i = 0; i < template.length; i++) {
			Material material = template[i];
			//plugin.getLogger().info("I = " + i + " Mat = " + material + " Item = " + e.getView().getTopInventory().getContents());
			if(material == Material.BARRIER){
				ItemStack batrod;
				batrod = e.getInventory().getItem(i);
				result += Batrod.readPower(batrod);
			}	
		}
		
		return result;
	}
	
	@SuppressWarnings("deprecation")
	public static boolean canCreate(InventoryClickEvent e, String key, InventoryType type){
		//plugin.getLogger().info("Type: " + key);
		Material[] template = MachineTemplates.buildTemplates.get(key);
		
		if(!(e.getView().getTopInventory().getType() == type))return false;
		
		if(e.getView().convertSlot(e.getRawSlot()) != e.getSlot())return false;
		
		int cursorPos = e.getSlot();
		for(int i = 0; i < template.length; i++){
			
			ItemStack check;
			if(i == cursorPos){
				check = e.getCursor();
			}else{
				check = e.getInventory().getItem(i);
			}
			//plugin.getLogger().info("Slot: " + i + " is a " + check);
			//Check for irrelevant Slots
			if(template[i] == Material.COMMAND)continue;
			//Check for empty Slots
			if(check == null){
				if(template[i] == Material.AIR){
					continue;
				}else{
					return false;
				}
			}
			//Check for Batrod Slots
			if(template[i] == Material.BARRIER){
				if(Batrod.check(check)){
					continue;
				}else{
					return false;
				}
			}
			//Check normal Slots
			if(template[i] != check.getType())return false;
		}
		
		e.setCurrentItem(e.getCursor());
		e.setCursor(new ItemStack(Material.AIR));
		e.setCancelled(true);
		return true;
	}
	
	public void requestPower(Machine requester) {
		sender.add(requester);
	}
	
	private int getFreeSpace() {
		return getMaxPower() - power;
	}

	private List<PathToMachine> getConnectedMachines() {
		List<PathToMachine> result = new ArrayList<>();
		List<Location> current;
		List<Location> next = new ArrayList<>();
		HashMap<Location, Integer> blockDistance = new HashMap<>();		
		
		current = new ArrayList<>();
		current.add(position);
		
		for (int i = 0; i <= MAX_CABLE_LENGTH; i++) {
			for (Location p : current){
				blockDistance.put(p, i);			
				plugin.getMachineManager();
				for (int j = 0; j < MachineManager.directions.length; j++){
					if(i == 0 && !allowedInputs[j])continue;
					Vector dir = MachineManager.directions[j];
					Location offsetPosition = p.clone();
					offsetPosition.add(dir);					
					if (!blockDistance.containsKey(offsetPosition)) {
						Machine machine = machineManager.getMachine(offsetPosition);
						if (machine != null && machine.getMaxPowerOutput() > 0 && machine.allowedOutputs[(j+3)%6]) {
							PathToMachine machinepath = new PathToMachine();
							machinepath.machine = machine;
							Location backtrack = offsetPosition.clone();
							for (int k = i; k >= 1; k--) {								
								for (Vector back : MachineManager.directions) {
									Location backPosition = backtrack.clone();
									backPosition.add(back);
									Integer dis = blockDistance.get(backPosition);									
									if (dis != null && dis == k) {
										backtrack.add(back);
										machinepath.path.add(backtrack.clone());
										break;
									}
								}
							}
							result.add(machinepath);
						} else if (offsetPosition.getBlock().getType() == Material.IRON_FENCE) {
							next.add(offsetPosition);
						}
					}
				}
			}
			current = next;
			next = new ArrayList<>();
		}
		
		return result;
	}
	
	protected void requestFromConnected() {
		List<PathToMachine> machines = getConnectedMachines();
		
		for (PathToMachine ptm : machines) {
			ptm.machine.requestPower(this);
		}
		if (machines.size() == 0) {
			position.getWorld().spawnParticle(Particle.BARRIER, position.clone().add(0.5, 1.5, 0.5), 1, 0, 0, 0, 0.1);
		}
	}

	public Location getPosition() {
		return position;
	}
	
	public int getPower() {
		return power;
	}
	
	public int getPowerIntputTotal() {
		return powerIntputTotal;
	}
	
	public int getPowerOutputTotal() {
		return powerOutputTotal;
	}

	public void resetIO() {
		if (!isLoaded)
			return;
		
		powerIntputTotal = 0;
		powerOutputTotal = 0;
	}
	
	public void processPowerTransfer() {
		if (!isLoaded)
			return;
		
		sender.sort(new Comparator<Machine>() {
			@Override
            public int compare(Machine lhs, Machine rhs) {
                return lhs.power > rhs.power ? +1 : -1;
            }
		});
		
		for (Machine requester : sender) {
			transferPowerTo(requester);
			if (powerOutputTotal == getMaxPowerOutput())
				break;
		}
		
		sender.clear();
	}
	
	private void transferPowerTo(Machine requester) {
		//plugin.getLogger().info("Power Transferring...");
		//plugin.getLogger().info(getMaxPowerOutput() + " / " + requester.getMaxPowerInput());
		//plugin.getLogger().info(power + " / " + requester.power);
		
		RedstoneUpgrade redstoneUpgrade = ((RedstoneUpgrade)upgradeManager.getUpgrade(UpgradeType.RedstoneUpgrade));
		
		if(redstoneUpgrade!= null && !redstoneUpgrade.isMachineOnline(this))
			return;
		
		// max total per update
		int transPower = Math.min(
			getMaxPowerOutput() - powerOutputTotal, 
			requester.getMaxPowerInput() - requester.powerIntputTotal
		);	
		// not enough to send / not enough space in requester
		transPower = Math.min(transPower, power);
		transPower = Math.min(transPower, requester.getFreeSpace());
		
		if (transPower <= 0)
			return;
		
		// send power
		power -= transPower;
		requester.power += transPower;
		
		// change power totals
		powerOutputTotal += transPower;
		requester.powerIntputTotal += transPower;	
	}

	public void updateUI() {
		if (!isLoaded)
			return;
		
		//plugin.getLogger().info("Power: " + power + " / OldPower: " + oldPower);
		
		if (oldPower != power
		 || oldPowerInputTotal != powerIntputTotal
		 || oldPowerOutputTotal != powerOutputTotal) {
			updateDisplay();
			oldPower = power;
			oldPowerInputTotal = powerIntputTotal;
			oldPowerOutputTotal = powerOutputTotal;
		}			
	}
	
	protected boolean isActive(){
		RedstoneUpgrade redstoneUpgrade = (RedstoneUpgrade) upgradeManager.getUpgrade(UpgradeType.RedstoneUpgrade);
		return redstoneUpgrade!= null && !redstoneUpgrade.isMachineOnline(this);
	}
	
	public void update() {
		if (!isLoaded)
			return;
		
		if(getDisplayName() != "§6§lGenerator"){
			requestFromConnected();
			
			if(isActive())return;
		}
		
		updateBlock();
	}
	
	public void load() {
		position = new Location(Bukkit.getWorld(world), vec.getX(), vec.getY(), vec.getZ());
		isLoaded = true;
	}
	
	public void unload() {
		isLoaded = false;
	}

	public boolean getIsLoaded() {
		return isLoaded;
	}
	
	protected void chargeRod(ItemStack item){
		if(Batrod.check(item)){
			int rodPower = Batrod.readPower(item);
			int powerTransfered = Math.min(Batrod.MAX_POWER - rodPower, Math.min(getMaxPowerOutput(), power));
			Batrod.setPower(item, rodPower + powerTransfered);
			power -= powerTransfered;
		}
	}
	
	protected void dechargeRod(ItemStack item){
		if(Batrod.check(item) && power < getMaxPower()){
			int rodPower = Batrod.readPower(item);
			int powerTransfered = Math.min(getMaxPower() - power, Math.min(rodPower, getMaxPowerInput()));
			Batrod.setPower(item, rodPower - powerTransfered);
			power += powerTransfered;
		}
	}
	
	protected int pushOneItemInto(int itemPos, Inventory source, Location target){
		ItemStack items = source.getItem(itemPos);
		BlockState state = target.getBlock().getState();
		if(state instanceof InventoryHolder){
			Inventory targetInventory = ((InventoryHolder) state).getInventory();
			
			HashMap<Integer, ItemStack> itemsNotMoved = targetInventory.addItem(items);
			
			if(itemsNotMoved.size() > 0){
				int itemNotCountMoved = itemsNotMoved.get(0).getAmount();
				ItemStack notMoved = source.getItem(4);
				notMoved.setAmount(itemNotCountMoved);
				source.setItem(4, notMoved);
				return items.getAmount() - itemNotCountMoved;
			}else{
				source.setItem(4, new ItemStack(Material.AIR));
				return items.getAmount();
			}
		}
		return -1;
	}
	
	protected void renderParticleSideColored(Vector d, Color c){
		Vector direction = d.clone().add(new Vector(0.5, 0.5, 0.5)).subtract(d.clone().multiply(0.4));
		position.getWorld().spawnParticle(Particle.REDSTONE, position.clone().add(direction), 0, c.getRed() / 255.0f + 0.01f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, 1);
	}
}
