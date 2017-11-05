package de.oninoni.OnionPower.Machines.DropperBased.Multiblock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.material.Colorable;
import org.bukkit.material.Directional;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import de.oninoni.OnionPower.Machines.MachineManager;
import de.oninoni.OnionPower.Machines.DropperBased.MachineDropper;

public abstract class MachineDropperMultiblock extends MachineDropper{
	
	private HashMap<Vector, MaterialData> templateInActive;
	private HashMap<Vector, MaterialData> templateActive;
	
	boolean shouldNotGenerate = false;
	
	public MachineDropperMultiblock(Location position, MachineManager machineManager) {
		super(position, machineManager);
		templateInActive = new HashMap<>();
		templateActive = new HashMap<>();
		setMutltiblockTemplates();
		if(!checkActive()){
			destroyMachine(owner);
		}
	}
	
	public MachineDropperMultiblock(OfflinePlayer owner, Location position, MachineManager machineManager, int power){
		super(owner, position, machineManager, power);
		templateInActive = new HashMap<>();
		templateActive = new HashMap<>();
		setMutltiblockTemplates();
		if(checkInActive()){
			setActive();
		}else{
			shouldNotGenerate = true;
			destroyMachine(owner);
		}
	}
	
	protected Vector rotateVector(Vector vec, BlockFace face){
		Vector v = vec.clone();
		switch (face) {
		case EAST:
			v.setX(-vec.getZ());
			v.setZ(vec.getX());
			break;
		case WEST:
			v.setX(vec.getZ());
			v.setZ(-vec.getX());
			break;
		case SOUTH:
			v.setX(-vec.getX());
			v.setZ(-vec.getZ());
			break;
		case NORTH:
			break;
		default:
			return null;
		}
		return v;
	}
	
	protected abstract void setMutltiblockTemplates();
	
	protected void putMultiblockTemplates(HashMap<Vector, MaterialData> tiA, HashMap<Vector, MaterialData> tA){
		templateInActive.putAll(tiA);
		templateActive.putAll(tA);
	}
	
	protected boolean checkInActive(){
		return checkTemplate(templateInActive);
	}
	
	protected boolean checkActive(){
		return checkTemplate(templateActive);
	}
	
	protected boolean checkTemplate(HashMap<Vector, MaterialData> template){
		BlockFace forward = ((Directional)getDropper().getData()).getFacing();
		if(template == null) return false;
		for (Vector v : template.keySet()) {
			MaterialData mat = template.get(v).clone();
			
			Vector vec = rotateVector(v, forward);
			if(vec == null) return false;
			
			Location targetLocation = position.clone().add(vec);
			Block targetBlock = targetLocation.getBlock();
			
			//plugin.getLogger().info(vec + " / " + targetLocation + " : " + targetBlock.getState().getData() + " / " + mat);
			
			if((targetBlock == null || targetBlock.getType() == Material.AIR) && mat.getItemType() != Material.AIR) return false;
			
			if(targetBlock.getType() != mat.getItemType()) return false;
			
			if(template != templateInActive){
				if(mat instanceof Directional){
					//plugin.getLogger().info("Directional!");
					Directional dirMat = (Directional) mat;
					dirMat.setFacingDirection(rotateBlockFace(dirMat.getFacing(), forward));
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
	
	private BlockFace rotateBlockFace(BlockFace face, BlockFace forward){
		//plugin.getServer().broadcastMessage("Gets: " + face);
		if(face == BlockFace.DOWN)return face;
		//plugin.getServer().broadcastMessage("Calculates: " + BlockFace.values()[(face.ordinal() + forward.ordinal()) % 4]);
		return BlockFace.values()[(face.ordinal() + forward.ordinal()) % 4];
	}
	
	protected void setActive(){
		//plugin.getServer().broadcastMessage("Active!");
		setTemplate(templateActive);
	}
	
	protected void setInactive(){
		//plugin.getServer().broadcastMessage("Inactive!");
		setTemplate(templateInActive);
	}
	
	private void setTemplate(HashMap<Vector, MaterialData> template){
		BlockFace forward = ((Directional)getDropper().getData()).getFacing();
		for (Vector v : template.keySet()) {
			Vector vec = rotateVector(v, forward);
			if(vec == null) return;
			
			//plugin.getServer().broadcastMessage("Processing...");
			Location targetLocation = position.clone().add(vec);
			Block targetBlock = targetLocation.getBlock();
			
			MaterialData mat = template.get(v).clone();
			
			targetBlock.setType(mat.getItemType());
			
			if(mat instanceof Directional){
				Directional dirMat = (Directional) mat;
				dirMat.setFacingDirection(rotateBlockFace(dirMat.getFacing(), forward));
				mat = (MaterialData) dirMat;
			}
			
			BlockState targetState = targetBlock.getState();
			targetState.setData(mat);
			targetState.update();
		}
	}
	
	public List<Location> getProtectedBlocks(){
		List<Location> protectedBlocks = new ArrayList<>();
		
		for (Vector v : templateInActive.keySet()) {
			Vector vec = rotateVector(v, ((Directional)getDropper().getData()).getFacing());
			Location protectedBlock = position.clone();
			protectedBlock.add(vec);
			protectedBlocks.add(protectedBlock);
		}
		
		return protectedBlocks;
	}
	
	@Override
	public void onBreak(BlockEvent e) {
		if(!shouldNotGenerate){
			setInactive();
			shouldNotGenerate = false;
		}
		super.onBreak(e);
	}
	
	@Override
	public boolean onBoom(Block e) {
		if(!shouldNotGenerate){
			setInactive();
			shouldNotGenerate = false;
		}
		return super.onBoom(e);
	}
}
