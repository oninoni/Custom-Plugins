package de.oninoni.OnionPower.Machines.DispenserBased;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import de.oninoni.OnionPower.OnionPower;

public class MinerShot {

	protected static OnionPower plugin = OnionPower.get();
	
	private Block target;
	
	private Location position;
	private Vector direction;
	
	public MinerShot(Miner o, Block t){
		target = t;
		
		position = o.getPosition().clone();
		direction = target.getLocation().toVector().clone();
		direction = direction.subtract(position.toVector());
		direction.normalize();
		
		position.getWorld().playSound(o.getPosition().clone().add(0.5, 0.5, 0.5), Sound.ITEM_CHORUS_FRUIT_TELEPORT, 0.2f, 2f);
	}
	
	public boolean update(){
		position.add(direction);
		
		for(int i = 0; i < 8; i++){
			//position.getWorld().spawnParticle(Particle.REDSTONE, , 1, 0, 0, 0, 0);
			position.getWorld().spawnParticle(Particle.REDSTONE, position.clone().add(0.5, 0.5, 0.5).add(direction.clone().multiply(-i / 8.0f)), 0, 1, 0, 1, 1);
		}
		
		if(position.clone().subtract(target.getLocation().toVector()).length() < 2){
			Random r = new Random();
			for(int j = 0; j < 100; j++){
				position.getWorld().spawnParticle(Particle.REDSTONE, target.getLocation().clone().add(r.nextFloat(), r.nextFloat(), r.nextFloat()), 0, 1, 0, 1, 1);
			}
		}
		if(position.clone().subtract(target.getLocation().toVector()).length() < 1){
			target.setType(Material.AIR);
			position.getWorld().playSound(target.getLocation().clone().add(0.5, 0.5, 0.5), Sound.ENTITY_ENDERMEN_TELEPORT, 0.2f, 0.8f);
			return true;
		}
		return false;
	}
	
}
