package de.oninoni.OnionPower.Machines;

import java.util.ArrayList;
import java.util.Collection;
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
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
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

import de.oninoni.OnionPower.NMSAdapter;
import de.oninoni.OnionPower.OnionPower;
import de.oninoni.OnionPower.Items.PowerItems.Batrod;
import de.oninoni.OnionPower.Items.PowerItems.PowerCore;
import de.oninoni.OnionPower.Items.PowerItems.PowerItem;
import de.oninoni.OnionPower.Machines.Upgrades.RedstoneUpgrade;
import de.oninoni.OnionPower.Machines.Upgrades.UpgradeManager;
import de.oninoni.OnionPower.Machines.Upgrades.UpgradeManager.UpgradeType;

public abstract class Machine {

	protected static OnionPower plugin = OnionPower.get();

	private static final int MAX_CABLE_LENGTH = 16;
	
	protected Vector effectOffset;

	//TODO Rewrite Constructor for init Function so you can check before Generating

	@SuppressWarnings("deprecation")
	public static boolean canCreate(InventoryClickEvent e, String key, InventoryType type) {
		// plugin.getLogger().info("Type: " + key);
		Material[] template = MachineTemplates.buildTemplates.get(key);

		if (!(e.getView().getTopInventory().getType() == type))
			return false;

		if (e.getView().convertSlot(e.getRawSlot()) != e.getSlot())
			return false;

		int cursorPos = e.getSlot();
		for (int i = 0; i < template.length; i++) {
			ItemStack check;
			if (i == cursorPos) {
				check = e.getCursor();
			} else {
				check = e.getInventory().getItem(i);
			}
			// plugin.getLogger().info("Slot: " + i + " is a " + check);
			// Check for irrelevant Slots
			if (template[i] == Material.COMMAND)
				continue;
			// Check for empty Slots
			if (check == null) {
				if (template[i] == Material.AIR) {
					continue;
				} else {
					return false;
				}
			}
			// Check for higher Stacks
			if( check.getAmount() > 1)
				return false;
			// Check for Batrod Slots
			if (template[i] == Material.BARRIER) {
				Batrod batrod = new Batrod(check);
				if (batrod.check()) {
					continue;
				} else {
					return false;
				}
			}
			// Check normal Slots
			if (template[i] != check.getType())
				return false;
		}

		e.setCurrentItem(e.getCursor());
		e.setCursor(new ItemStack(Material.AIR));
		e.setCancelled(true);
		return true;
	}

	public static int getAllBatrodsPower(Inventory inv) {
		int result = 0;

		for (ItemStack item : inv.getContents()) {
			Batrod batrod = new Batrod(item);
			if(batrod.check())
				result += batrod.getPower();
		}

		return result;
	}

	protected InventoryHolder invHolder;

	protected int coreSlot;

	protected EnumSet<UpgradeType> upgradesAvailable;
	protected UpgradeManager upgradeManager;

	protected boolean[] allowedInputs = { true, true, true, true, true, true };
	protected boolean[] allowedOutputs = { false, false, false, false, false, false };

	private MachineManager machineManager;

	protected Location position;
	private Vector vec;
	private String world;

	private boolean isLoaded;

	protected int power;
	private int powerOld;
	protected int powerIntputTotal, powerOutputTotal;
	private int powerInputTotalOld, powerOutputTotalOld;

	private List<Machine> sender = new ArrayList<>();

	protected boolean needsUpdate = false;

	protected ArrayList<ArmorStand> designEntities;

	public Machine(Location position, MachineManager machineManager) {
		setCoreSlot();
		BlockState state = position.getBlock().getState();
		if (state instanceof InventoryHolder) {
			invHolder = (InventoryHolder) position.getBlock().getState();
			PowerCore powerCore = new PowerCore(getPowerCore(), this);
			if(powerCore.check()){
				this.power = powerCore.getPower();
			}
		} else {
			plugin.getLogger().warning("Machine at " + position + "is not an Inventory anymore!");
			this.power = 0;
		}
		initValues(position, machineManager);
	}

	public Machine(Location position, MachineManager machineManager, int power) {
		setCoreSlot();
		invHolder = (InventoryHolder) position.getBlock().getState();
		this.power = power;
		setPowerCore(new PowerCore(this));
		NMSAdapter.setInvName(invHolder, getDisplayName());
		initValues(position, machineManager);
	}

	protected void chargeItem(Inventory inv, int id) {
		PowerItem powerItem = new PowerItem(inv.getItem(id));
		if (powerItem.check()) {
			int rodPower = powerItem.getPower();
			int powerTransfered = Math.min(powerItem.maxPower - rodPower, Math.min(getMaxPowerOutput(), power));
			powerItem.setPower(rodPower + powerTransfered);
			inv.setItem(id, powerItem);
			power -= powerTransfered;
		}
	}

	public void closeInventories() {
		Object[] viewers = (invHolder).getInventory().getViewers().toArray();
		for (Object humanEntity : viewers) {
			((HumanEntity) humanEntity).closeInventory();
		}
	}

	protected void dechargeRod(Inventory inv, int id) {
		PowerItem powerItem = new PowerItem(inv.getItem(id));
		if (powerItem.check() && power < getMaxPower()) {
			int rodPower = powerItem.getPower();
			int powerTransfered = Math.min(getMaxPower() - power, Math.min(rodPower, getMaxPowerInput()));
			powerItem.setPower(rodPower - powerTransfered);
			inv.setItem(id, powerItem);
			power += powerTransfered;
		}
	}

	protected abstract boolean doesExplode();

	private List<PathToMachine> getConnectedMachines() {
		List<PathToMachine> result = new ArrayList<>();
		List<Location> current;
		List<Location> next = new ArrayList<>();
		HashMap<Location, Integer> blockDistance = new HashMap<>();

		current = new ArrayList<>();
		current.add(position);

		for (int i = 0; i <= MAX_CABLE_LENGTH; i++) {
			for (Location p : current) {
				blockDistance.put(p, i);
				plugin.getMachineManager();
				for (int j = 0; j < MachineManager.directions.length; j++) {
					if (i == 0 && !allowedInputs[j])
						continue;
					Vector dir = MachineManager.directions[j];
					Location offsetPosition = p.clone();
					offsetPosition.add(dir);
					if (!blockDistance.containsKey(offsetPosition)) {
						Machine machine = machineManager.getMachine(offsetPosition);
						if (machine != null && machine.getMaxPowerOutput() > 0 && machine.allowedOutputs[(j + 3) % 6]) {
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

	public abstract int getDesignEntityCount();

	public abstract String getDisplayName();

	private int getFreeSpace() {
		return getMaxPower() - power;
	}

	public boolean getIsLoaded() {
		return isLoaded;
	}

	public abstract int getMaxPower();

	public abstract int getMaxPowerInput();

	public abstract int getMaxPowerOutput();

	public Location getPosition() {
		return position;
	}

	public int getPower() {
		return power;
	}

	public ItemStack getPowerCore() {
		ItemStack powerCore = invHolder.getInventory().getItem(coreSlot);
		return powerCore;
	}

	public int getPowerIntputTotal() {
		return powerIntputTotal;
	}

	public int getPowerOutputTotal() {
		return powerOutputTotal;
	}

	private void initValues(Location position, MachineManager machineManager) {
		this.position = position;
		this.machineManager = machineManager;

		isLoaded = true;

		vec = position.toVector();
		world = position.getWorld().getName();

		designEntities = new ArrayList<>();
		designEntities.ensureCapacity(getDesignEntityCount());
		spawnDesignEntities();

		upgradesAvailable = EnumSet.noneOf(UpgradeType.class);

		setAvailableUpgrades();
		upgradeManager = new UpgradeManager(this);
		
		effectOffset = new Vector();
	}

	protected boolean isActive() {
		RedstoneUpgrade redstoneUpgrade = (RedstoneUpgrade) upgradeManager.getUpgrade(UpgradeType.RedstoneUpgrade);
		return redstoneUpgrade != null && !redstoneUpgrade.isMachineOnline(this);
	}

	protected abstract boolean isMaterial(Material material);

	public void load() {
		position = new Location(Bukkit.getWorld(world), vec.getX(), vec.getY(), vec.getZ());
		isLoaded = true;
		invHolder = (InventoryHolder) position.getBlock().getState();
	}

	public boolean onBoom(Block e) {
		killDesignEnities();
		if (doesExplode()) {
			Random r = new Random();
			for (int i = 0; i < invHolder.getInventory().getSize(); i++) {
				if (r.nextInt(2) == 0) {
					resetItemAt(i);
				} else {
					invHolder.getInventory().setItem(i, new ItemStack(Material.AIR));
				}
			}
			upgradeManager.onBoom();
			return true;
		}
		return false;
	}
	
	private void killDesignEnities(){
		for (ArmorStand armorStand : designEntities) {
			armorStand.remove();
		}
	}

	public void onBreak(BlockEvent e) {
		killDesignEnities();
		for (int i = 0; i < invHolder.getInventory().getSize(); i++) {
			resetItemAt(i);
		}
		upgradeManager.onBreak();
	}
	
	public void onClick(InventoryClickEvent e) {
		Inventory inv = e.getView().getTopInventory();
		if (coreSlot == e.getRawSlot() && inv.getName() != upgradeManager.getName()) {
			e.setCancelled(true);
			upgradeManager.openInterface(e.getWhoClicked());
			return;
		}
		int slot;
		ItemStack cursor;
		//plugin.getLogger().info("Click at " + e.getSlot() + " / " + e.getRawSlot());
		//plugin.getLogger().info("Cursor: " + e.getCursor() + " / Current: " + e.getCurrentItem());
		if(e.isShiftClick()){
			cursor = e.getCurrentItem();
			if(e.getRawSlot() > inv.getSize()){
				//plugin.getLogger().info("Shift from outside!");
				slot = -1;
				if(e.getCursor() != null && e.getCursor().getType() != null){
					slot = inv.first(e.getCursor().getType());
					if(slot == -1){
						slot = inv.firstEmpty();
						if(slot == -1){
							return;
						}
					}
				}
			}else{
				slot = e.getRawSlot();
			}
		}else{
			slot = e.getRawSlot();
			cursor = e.getCursor();
		}
		if(slot >= inv.getSize())return;
		//plugin.getLogger().info("Clicking in machine at: " + slot + " with: " + cursor);
		if (inv.getName() == upgradeManager.getName()) {
			e.setCancelled(upgradeManager.onClickFixed(inv, slot, cursor, (Player) e.getWhoClicked()));
		}else{
			e.setCancelled(onClickFixed(inv, slot, cursor, (Player) e.getWhoClicked()));
		}
	}
	
	public abstract boolean onClickFixed(Inventory inv, int slot, ItemStack cursor, Player p);

	public abstract void onClose(InventoryCloseEvent e);

	public abstract void onMoveFrom(InventoryMoveItemEvent e);

	public abstract void onMoveInto(InventoryMoveItemEvent e);

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

	protected int pushOneItemInto(int itemPos, Inventory source, Location target) {
		ItemStack items = source.getItem(itemPos);
		BlockState state = target.getBlock().getState();
		if (state instanceof InventoryHolder) {
			Inventory targetInventory = ((InventoryHolder) state).getInventory();

			HashMap<Integer, ItemStack> itemsNotMoved = targetInventory.addItem(items);

			if (itemsNotMoved.size() > 0) {
				int itemNotCountMoved = itemsNotMoved.get(0).getAmount();
				ItemStack notMoved = source.getItem(4);
				notMoved.setAmount(itemNotCountMoved);
				source.setItem(4, notMoved);
				return items.getAmount() - itemNotCountMoved;
			} else {
				source.setItem(4, new ItemStack(Material.AIR));
				return items.getAmount();
			}
		}
		return -1;
	}

	protected void renderParticleSideColored(Vector d, Color c) {
		Vector direction = d.clone().add(new Vector(0.5, 0.5, 0.5)).subtract(d.clone().multiply(0.4));
		position.getWorld().spawnParticle(Particle.REDSTONE, position.clone().add(direction), 0,
				c.getRed() / 255.0f + 0.01f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, 1);
	}

	public void reOpenInventories() {
		Object[] viewers = (invHolder).getInventory().getViewers().toArray();
		for (Object humanEntity : viewers) {
			((HumanEntity) humanEntity).closeInventory();
			((HumanEntity) humanEntity).openInventory(invHolder.getInventory());
		}
	}

	protected void requestFromConnected() {
		List<PathToMachine> machines = getConnectedMachines();

		for (PathToMachine ptm : machines) {
			ptm.machine.requestPower(this);
		}
		if (machines.size() == 0) {
			position.getWorld().spawnParticle(Particle.BARRIER, position.clone().add(0.5, 1.5, 0.5).add(effectOffset), 1, 0, 0, 0, 0.1);
		}
	}

	public void requestPower(Machine requester) {
		sender.add(requester);
	}

	public void resetIO() {
		if (!isLoaded)
			return;

		powerIntputTotal = 0;
		powerOutputTotal = 0;
	}

	protected abstract void resetItemAt(int id);

	protected abstract void setAvailableUpgrades();

	protected abstract void setCoreSlot();

	protected void setEntity(int id, ArmorStand a) {
		if (designEntities.size() <= id) {
			designEntities.add(a);
		} else {
			designEntities.set(id, a);
		}
	}

	public void setPowerCore(ItemStack powerCore) {
		invHolder.getInventory().setItem(coreSlot, powerCore);
	}

	public void saveUpgradeAsLore() {
		ItemStack powerCore = getPowerCore();
		ItemMeta itemMeta = powerCore.getItemMeta();
		List<String> lore = itemMeta.getLore();
		lore = lore.subList(0, 5);
		lore.addAll(upgradeManager.getPowerCoreLore());
		itemMeta.setLore(lore);
		powerCore.setItemMeta(itemMeta);
		setPowerCore(powerCore);
	}

	protected void spawnDesignEntities() {
		ArrayList<Integer> foundIDS = new ArrayList<>();
		Collection<Entity> entitiesNearby = position.getWorld().getNearbyEntities(position, 2.0f, 2.0f, 2.0f);
		//plugin.getLogger().info("Found " + entitiesNearby.size() + " Entities nearby!");
		for(Entity entity : entitiesNearby){
			if(entity instanceof ArmorStand){
				ArmorStand armorStand = (ArmorStand) entity;
				String[] parts = armorStand.getCustomName().split(":");
				if(parts.length == 4 && 
						parts[0].equalsIgnoreCase(""+position.getBlockX()) && 
						parts[1].equalsIgnoreCase(""+position.getBlockY()) && 
						parts[2].equalsIgnoreCase(""+position.getBlockZ())){
					//plugin.getLogger().info("Detected ");
					foundIDS.add(Integer.parseInt(parts[3]));
					designEntities.add(armorStand);
				}
			}
		}
		for (int i = 0; i < getDesignEntityCount(); i++) {
			if(!foundIDS.contains(i)){
				spawnDesignEntity(i);
			}
		}
	}

	public void spawnDesignEntity(int id) {
		ArmorStand armorStand = spawnDesignEntityInternal(id);
		armorStand.setVisible(false);
		armorStand.setGravity(false);
		armorStand.setInvulnerable(true);
		armorStand.setCollidable(false);
		armorStand.setCustomName(position.getBlockX() + ":" + position.getBlockY() + ":" + position.getBlockZ() + ":" + id);
		designEntities.add(id, armorStand);
	}
	
	protected abstract ArmorStand spawnDesignEntityInternal(int id);

	private void transferPowerTo(Machine requester) {
		// plugin.getLogger().info("Power Transferring...");
		// plugin.getLogger().info(getMaxPowerOutput() + " / " +
		// requester.getMaxPowerInput());
		// plugin.getLogger().info(power + " / " + requester.power);

		RedstoneUpgrade redstoneUpgrade = ((RedstoneUpgrade) upgradeManager.getUpgrade(UpgradeType.RedstoneUpgrade));

		if (redstoneUpgrade != null && !redstoneUpgrade.isMachineOnline(this))
			return;

		// max total per update
		int transPower = Math.min(getMaxPowerOutput() - powerOutputTotal,
				requester.getMaxPowerInput() - requester.powerIntputTotal);
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

	public void unload() {
		isLoaded = false;
	}

	public void update() {
		if (!isLoaded)
			return;

		if (getDisplayName() != "§6§lGenerator" && getDisplayName() != "§6§lSolar Hopper") {
			requestFromConnected();

			if (isActive())
				return;
		}

		updateBlock();
	}

	public abstract void updateBlock();

	public void updateDisplay() {
		// plugin.getLogger().info("Update!");
		PowerCore powerCore = new PowerCore(getPowerCore(), this);
		powerCore.setPower(power);
		setPowerCore(powerCore);
		updateInventories();
	}

	public void updateInventories() {
		Object[] viewers = (invHolder).getInventory().getViewers().toArray();
		for (Object humanEntity : viewers) {
			((Player) humanEntity).updateInventory();
		}
	}

	public void updateUI() {
		if (!isLoaded)
			return;

		// plugin.getLogger().info("Power: " + power + " / OldPower: " +
		// oldPower);

		if (powerOld != power || powerInputTotalOld != powerIntputTotal || powerOutputTotalOld != powerOutputTotal
				|| needsUpdate) {
			updateDisplay();
			powerOld = power;
			powerInputTotalOld = powerIntputTotal;
			powerOutputTotalOld = powerOutputTotal;
			needsUpdate = false;
		}
	}

	public boolean upgradeAvailable(UpgradeType type) {
		return upgradesAvailable.contains(type);
	}
	
	public void destroyMachine(){
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				plugin.getServer().getPluginManager().callEvent(new BlockBreakEvent(position.getBlock(), null));
				NMSAdapter.resetInvName(invHolder);
			}
		}, 0L);
	}
	
	public Vector getEffectOffset() {
		return effectOffset;
	}
}
