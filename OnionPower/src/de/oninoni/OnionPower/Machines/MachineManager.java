package de.oninoni.OnionPower.Machines;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.util.Vector;

public class MachineManager {
	
	private HashMap<Location, Machine> machines;
	
	public MachineManager() {
		machines = new HashMap<>();
	}
	
	public void update() {
		for (Location pos : machines.keySet())
			machines.get(pos).update();
	}
	
	public void onClick(InventoryClickEvent e) {
		Machine machine = machines.get(e.getInventory().getLocation());
		if (machine == null)
		{
			Location location = e.getView().getTopInventory().getLocation();
			if (Generator.canCreate(e))
				machines.put(location, new Generator(location, this));
			//if (!ElectricFurnace.tryCreation(e))
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
			machines.get(source).onMoveFrom(e);
		}
		if(machines.get(destination) != null){
			machines.get(destination).onMoveInto(e);
		}
	}
		
	public Machine getMachine(Location pos) {
		return machines.get(pos);
	}
	
}
