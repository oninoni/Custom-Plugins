package de.oninoni.OnionPower.Machines.DispenserBased;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
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
	}
	
	public boolean update(){
		position.add(direction);

		
		for(int i = 0; i < 8; i++){
			position.getWorld().spawnParticle(Particle.REDSTONE, position.clone().add(0.5, 0.5, 0.5).add(direction.clone().multiply(-i / 8.0f)), 1, 0, 0, 0, 0);
		}

		if(position.clone().subtract(target.getLocation().toVector()).length() < 2){
			position.getWorld().spawnParticle(Particle.REDSTONE, target.getLocation().clone().add(0.5, 0.5, 0.5), 100, 0.4, 0.4, 0.4, 0);
		}
		if(position.clone().subtract(target.getLocation().toVector()).length() < 1){
			plugin.getLogger().info("PEW!");
			target.setType(Material.AIR);
			return true;
		}
		return false;
	}
	
}
