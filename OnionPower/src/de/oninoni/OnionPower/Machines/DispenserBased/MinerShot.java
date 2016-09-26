package de.oninoni.OnionPower.Machines.DispenserBased;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import de.oninoni.OnionPower.OnionPower;

public class MinerShot {

	protected static OnionPower plugin = OnionPower.get();
	
	private Block target;
	
	private Location position;
	private Vector direction;
	
	public MinerShot(Miner o, Block t){
		target = t;
		
		position = o.getPosition().clone().add(0.5, 0.375, 0.5);
		direction = target.getLocation().clone().add(0.5, 0.375, 0.5).toVector();
		direction = direction.subtract(position.toVector());
		direction.normalize();
		
		position.getWorld().playSound(position, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 0.2f, 2f);
	}
	
	public boolean update(){
		position.add(direction);
		
		Collection<Entity> intheWay = position.getWorld().getNearbyEntities(position.clone(), direction.normalize().getX() / 2.0, direction.normalize().getY() / 2.0, direction.normalize().getZ() / 2.0);
		
		for (Iterator<Entity> iterator = intheWay.iterator(); iterator.hasNext();) {
			Entity entity = (Entity) iterator.next();
			if(entity instanceof LivingEntity){
				((LivingEntity) entity).damage(((LivingEntity) entity).getHealth());
			}
		}
		
		
		for(int i = -4; i < 4; i++){
			//position.getWorld().spawnParticle(Particle.REDSTONE, , 1, 0, 0, 0, 0);
			position.getWorld().spawnParticle(Particle.REDSTONE, position.clone().add(direction.clone().multiply(-i / 8.0f)), 0, 1, 0, 1, 1);
		}
		
		if(position.clone().subtract(target.getLocation().toVector()).length() < 2){
			Random r = new Random();
			for(int j = 0; j < 100; j++){
				position.getWorld().spawnParticle(Particle.REDSTONE, target.getLocation().clone().add(r.nextFloat(), r.nextFloat(), r.nextFloat()), 0, 1, 0, 1, 1);
			}
		}
		if(position.clone().subtract(target.getLocation().toVector()).length() < 1){
			position.getWorld().playSound(target.getLocation().clone().add(0.5, 0.5, 0.5), Sound.ENTITY_ENDERMEN_TELEPORT, 0.2f, 0.8f);
			return true;
		}
		return false;
	}
	
}
