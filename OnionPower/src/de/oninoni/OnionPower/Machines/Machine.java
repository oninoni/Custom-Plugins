package de.oninoni.OnionPower.Machines;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.util.Vector;

import de.oninoni.OnionPower.OnionPower;

public abstract class Machine {

	protected static OnionPower plugin = OnionPower.get();
	
	private static final int MAX_CABLE_LENGTH = 16;
	
	private static final Vector[] directions = {
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
	
	public Machine(Location position, MachineManager machineManager){
		this.position = position;
		this.machineManager = machineManager;
		
		isLoaded = true;
		
		vec = position.toVector();
		world = position.getWorld().getName();
	}

	
	protected abstract boolean isMaterial(Material material);
	
	public abstract int getMaxPower();
	public abstract String getDisplayName();
	public abstract int getMaxPowerOutput();
	public abstract int getMaxPowerInput();
	public abstract void updateBlock();
	
	public abstract void onClick(InventoryClickEvent e);
	public abstract void onMoveInto(InventoryMoveItemEvent e);
	public abstract void onMoveFrom(InventoryMoveItemEvent e);
	
	protected abstract void updateDisplay();
		
	public abstract void onBreak(BlockBreakEvent e);
	
	private boolean isLoaded;
	
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
}
