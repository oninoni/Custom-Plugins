package de.oninoni.OnionPower.Machines;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.darkblade12.particleeffect.ParticleEffect;
import com.darkblade12.particleeffect.ParticleEffect.OrdinaryColor;

import de.oninoni.OnionPower.OnionPower;
import de.oninoni.OnionPower.Items.Batrod;
import de.oninoni.OnionPower.Items.PowerCore;
import de.oninoni.OnionPower.Machines.Upgrades.Upgrade;
import de.oninoni.OnionPower.Machines.Upgrades.UpgradeManager;

public abstract class Machine {

	protected static OnionPower plugin = OnionPower.get();
	
	protected int coreSlot;
	protected ItemStack powerCore;
	
	protected UpgradeManager upgradeManager;
	
	private static final int MAX_CABLE_LENGTH = 16;
	
	protected static final Vector[] directions = {
			new Vector( 1,  0,  0),
			new Vector( 0,  1,  0),
			new Vector( 0,  0,  1),
			new Vector(-1,  0,  0),
			new Vector( 0, -1,  0),
			new Vector( 0,  0, -1)
	};
	
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
	
	private Location position;
	
	private Vector vec;
	private String world;
	
	protected int power;
	protected int powerIntputTotal, powerOutputTotal;
	
	private int oldPower;
	private int oldPowerInputTotal, oldPowerOutputTotal;
	
	private List<Machine> sender = new ArrayList<>();
	
	public Machine(Location position, MachineManager machineManager, int power, HashMap<Integer, Upgrade> upgrades){
		initValues(position, machineManager, upgrades);
		this.power = power;
	}
	
	public Machine(Location position, MachineManager machineManager, HashMap<Integer, Upgrade> upgrades){
		initValues(position, machineManager, upgrades);
		
		BlockState state = position.getBlock().getState();
		if(state instanceof InventoryHolder){
			Inventory inv = ((InventoryHolder) state).getInventory();
			this.power = PowerCore.getPowerLevel(inv.getItem(coreSlot));
		}else{
			plugin.getLogger().warning("Machine at " + position + "is not an Inventory anymore!");
			this.power = 0;
		}
	}
	
	private void initValues(Location position, MachineManager machineManager, HashMap<Integer, Upgrade> upgrades){
		setCoreSlot();
		
		powerCore = PowerCore.create(this);
		
		this.position = position;
		this.machineManager = machineManager;
		
		isLoaded = true;
		
		vec = position.toVector();
		world = position.getWorld().getName();
		
		upgradeManager = new UpgradeManager(this, upgrades);
	}
	
	protected abstract boolean isMaterial(Material material);
	
	protected abstract void setCoreSlot();
	
	public abstract int getMaxPower();
	public abstract String getDisplayName();
	public abstract int getMaxPowerOutput();
	public abstract int getMaxPowerInput();
	public abstract void updateBlock();
	
	public abstract void onClick(InventoryClickEvent e);
	public abstract void onClose(InventoryCloseEvent e);
	public abstract void onMoveInto(InventoryMoveItemEvent e);
	public abstract void onMoveFrom(InventoryMoveItemEvent e);
	
	protected abstract void updateDisplay();
	
	public abstract void onBreak(BlockEvent e);
	public abstract boolean onBoom(Block e);
	
	private boolean isLoaded;
	
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
				for (int j = 0; j < directions.length; j++){
					if(i == 0 && !allowedInputs[j])continue;
					Vector dir = directions[j];
					Location offsetPosition = p.clone();
					offsetPosition.add(dir);					
					if (!blockDistance.containsKey(offsetPosition)) {
						Machine machine = machineManager.getMachine(offsetPosition);
						if (machine != null && machine.getMaxPowerOutput() > 0 && machine.allowedOutputs[(j+3)%6]) {
							PathToMachine machinepath = new PathToMachine();
							machinepath.machine = machine;
							Location backtrack = offsetPosition.clone();
							for (int k = i; k >= 1; k--) {								
								for (Vector back : directions) {
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
		
		if (oldPower != power
		 || oldPowerInputTotal != powerIntputTotal
		 || oldPowerOutputTotal != powerOutputTotal) {
			updateDisplay();
			oldPower = power;
			oldPowerInputTotal = powerIntputTotal;
			oldPowerOutputTotal = powerOutputTotal;
		}			
	}
	
	public void update() {
		if (!isLoaded)
			return;
		
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
		if(Batrod.check(item) && power != getMaxPower()){
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
		ParticleEffect.REDSTONE.display(new OrdinaryColor(c), position.clone().add(direction), 16);
	}
}
