package de.oninoni.OnionPower.Machines;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class MachineManager {
	
	private HashMap<Location, Machine> machines;
	
	public MachineManager() {
		machines = new HashMap<>();
	}
	
	public void update() {
		Set<Location> keySet = machines.keySet();
		
		for (Location pos : keySet)
			machines.get(pos).resetIO();
		
		for (Location pos : machines.keySet())
			machines.get(pos).update();
		
		for (Location pos: keySet)
			machines.get(pos).updateUI();
			
	}
	
	public void onClick(InventoryClickEvent e) {
		Machine machine = machines.get(e.getInventory().getLocation());
		if (machine == null)
		{
			Location location = e.getView().getTopInventory().getLocation();
			if (Generator.canCreate(e))
				machines.put(location, new Generator(location, this));
			if (ElectricFurnace.canCreate(e))
				machines.put(location, new ElectricFurnace(location, this));
		}
		else
		{
			machine.onClick(e);
		}
	}
	
	public void onOpen(InventoryOpenEvent e) {
		Machine machine = machines.get(e.getInventory().getLocation());
		if (machine == null) 
			return;
		
		Bukkit.broadcastMessage(e.getPlayer().getName() + " just opened some machinery!");
	}
	
	public void onMove(InventoryMoveItemEvent e){
		Location source = e.getSource().getLocation();
		Location destination = e.getDestination().getLocation();
		if(machines.get(source) != null){
			Bukkit.getLogger().info("SOURCE!");
			machines.get(source).onMoveFrom(e);
		}
		if(machines.get(destination) != null){
			Bukkit.getLogger().info("DESTINATION!");
			machines.get(destination).onMoveInto(e);
		}
	}
	
	public void onBreak(BlockBreakEvent e){
		Location location = e.getBlock().getLocation();
		if(machines.containsKey(location)){
			machines.get(location).onBreak(e);
			machines.remove(location);
		}
	}
		
	public Machine getMachine(Location pos) {
		return machines.get(pos);
	}
	
}
