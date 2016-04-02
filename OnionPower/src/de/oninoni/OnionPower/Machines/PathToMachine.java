package de.oninoni.OnionPower.Machines;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

public class PathToMachine {
	public Machine machine;
	public List<Location> path;
	
	public PathToMachine() {
		path = new ArrayList<>();
	}
}
