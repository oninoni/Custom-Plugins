package de.oninoni.OnionPower.Machines.DropperBasedMultiblock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.material.Colorable;
import org.bukkit.material.Directional;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import de.oninoni.OnionPower.Machines.MachineManager;

public abstract class MachineDropperMultiblock extends MachineDropper{
	
	private HashMap<Vector, MaterialData> templateInActive;
	private HashMap<Vector, MaterialData> templateActive;
	
	public MachineDropperMultiblock(Location position, MachineManager machineManager) {
		super(position, machineManager);
		setMutltiblockTemplates();
		if(!checkActive()){
			destroyMachine();
		}
	}
	
	public MachineDropperMultiblock(Location position, MachineManager machineManager, int power){
		super(position, machineManager, power);
		setMutltiblockTemplates();
		if(checkInActive()){
			setActive();
		}else{
			destroyMachine();
		}
	}
	
	protected Vector rotateVector(Vector vec, BlockFace face){
		switch (face) {
		case EAST:
			vec.setX(-vec.getZ());
			vec.setZ(vec.getX());
			break;
		case WEST:
			vec.setX(vec.getZ());
			vec.setZ(-vec.getX());
			break;
		case SOUTH:
			vec.setX(-vec.getX());
			vec.setZ(-vec.getZ());
			break;
		default:
			break;
		}
		return vec;
	}
	
	protected abstract void setMutltiblockTemplates();
	
	protected void setMultiblockTemplates(HashMap<Vector, MaterialData> tiA, HashMap<Vector, MaterialData> tA){
		templateInActive= tiA;
		templateActive	= tA;
	}
	
	protected boolean checkInActive(){
		return checkTemplate(templateInActive);
	}
	
	protected boolean checkActive(){
		return checkTemplate(templateActive);
	}
	
	private boolean checkTemplate(HashMap<Vector, MaterialData> template){
		BlockFace forward = ((Directional)dropper.getData()).getFacing();
		if(template == null)return false;
		for (Vector vec : template.keySet()) {
			MaterialData mat = template.get(vec).clone();
			
			vec = rotateVector(vec, forward);
			
			Location targetLocation = position.clone().add(vec);
			Block targetBlock = targetLocation.getBlock();
			
			//plugin.getLogger().info(vec + " / " + targetLocation + " : " + targetBlock.getState().getData() + " / " + mat);
			
			if((targetBlock == null || targetBlock.getType() == Material.AIR) && mat.getItemType() != Material.AIR) return false;
			
			if(targetBlock.getType() != mat.getItemType()) return false;
			
			//TODO Rotate
			if(template != templateInActive){
				if(mat instanceof Directional){
					//plugin.getLogger().info("Directional!");
					Directional dirMat = (Directional) mat;
					if(dirMat.getFacing() != ((Directional) targetBlock.getState().getData()).getFacing()) return false;
				}
			}
			
			if(mat instanceof Colorable){
				//plugin.getLogger().info("Colorable!");
				Colorable colMat = (Colorable) mat;
				if(colMat.getColor() != ((Colorable) targetBlock.getState().getData()).getColor()) return false;
			}
		}
		return true;
	}
	
	protected void setActive(){
		setTemplate(templateActive);
	}
	
	protected void setInactive(){
		setTemplate(templateInActive);
	}
	
	private void setTemplate(HashMap<Vector, MaterialData> template){
		BlockFace forward = ((Directional)dropper.getData()).getFacing();
		for (Vector vec : template.keySet()) {
			rotateVector(vec, forward);
			//TODO Rotation Fails here for some reason
			MaterialData mat = template.get(vec);
			Location targetLocation = position.clone().add(vec);
			Block targetBlock = targetLocation.getBlock();
			targetBlock.setType(mat.getItemType());
			BlockState targetState = targetBlock.getState();
			targetState.setData(mat);
			targetState.update();
		}
	}
	
	public List<Location> getProtectedBlocks(){
		List<Location> protectedBlocks = new ArrayList<>();
		
		for (Vector vec : templateInActive.keySet()) {
			vec = rotateVector(vec, ((Directional)dropper.getData()).getFacing());
			Location protectedBlock = position.clone();
			protectedBlock.add(vec);
			protectedBlocks.add(protectedBlock);
		}
		
		return protectedBlocks;
	}
	
	@Override
	public void onBreak(BlockEvent e) {
		setInactive();
		super.onBreak(e);
	}
}
