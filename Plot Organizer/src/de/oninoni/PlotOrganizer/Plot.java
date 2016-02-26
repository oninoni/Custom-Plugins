package de.oninoni.PlotOrganizer;

import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;

public class Plot {
	
	private GridPosition gridPosition;
	private ProtectedCuboidRegion protectedCuboidRegion;
	
	public Plot(GridPosition gp, PlotOrganizer plugin){
		this.gridPosition = gp;		
	}
	
	public GridPosition getGridPosition() {
		return gridPosition;
	}
	
	public ProtectedCuboidRegion getProtectedCuboidRegion() {
		return protectedCuboidRegion;
	}
}
