package de.oninoni.OnionPower.Machines;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class MachineManager {
	
	private HashMap<Location, Machine> machines;
	
	public MachineManager() {
		machines = new HashMap<>();
	}
	
	public void Update() {
		for (Location pos : machines.keySet())
			machines.get(pos).Update();
	}
	
	public void OnClick(InventoryClickEvent e) {
		Machine machine = machines.get(e.getInventory().getLocation());
		if (machine == null) 
			return;
		
		Bukkit.broadcastMessage(e.getWhoClicked().getName() + " just clicked in some machinery!");
	}
	
	public void OnOpen(InventoryOpenEvent e) {
		Machine machine = machines.get(e.getInventory().getLocation());
		if (machine == null) 
			return;
		
		Bukkit.broadcastMessage(e.getPlayer().getName() + " just opened some machinery!");
	}
	
}
