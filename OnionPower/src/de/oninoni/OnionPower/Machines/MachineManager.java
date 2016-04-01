package de.oninoni.OnionPower.Machines;

import java.util.HashMap;

import org.bukkit.util.Vector;

public class MachineManager {
	
	private HashMap<Vector, Machine> machines;
	
	public MachineManager() {
		machines = new HashMap<>();
	}
	
	public void Update() {
		for (Vector vec : machines.keySet())
			machines.get(vec).Update();
	}
	
}
