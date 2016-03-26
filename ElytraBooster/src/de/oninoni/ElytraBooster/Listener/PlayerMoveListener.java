package de.oninoni.ElytraBooster.Listener;


import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class PlayerMoveListener implements Listener{
	
	final float thickness = 8;
	
	@EventHandler
	public void onMove(PlayerMoveEvent event){
		Player p = event.getPlayer();
		if(!p.isGliding())return;
		if(!p.isSneaking())return;
		ItemStack elytra = p.getInventory().getChestplate();
		ItemMeta itemMeta = elytra.getItemMeta();
		List<String> lore = itemMeta.getLore();
		if(!(lore.size()==2))return;
		if(!((lore.get(0).equalsIgnoreCase("Fuel Level:"))))return;
		int level = 0;
		String levelLine = lore.get(1);
		for(int i = 0; i < levelLine.length(); i++){
			if(levelLine.substring(i,i + 1).equals("%")){
				level = (int) (Float.parseFloat(levelLine.substring(0,i)) * 100);
				break;
			}
		}
		
		if(level==0.0) return;
		level-=1;
		lore.set(1, level / 100.0f + "%");
		itemMeta.setLore(lore);
		elytra.setItemMeta(itemMeta);

		p.setVelocity(p.getVelocity().add(p.getLocation().getDirection().multiply(0.2)).multiply(0.95));
		
		Location location = p.getLocation();
		Vector direction = p.getLocation().getDirection();
		
		location = location.add(new Vector(0,0.5,0));
		
		
		
		for(int i = 0; i < thickness; i++){
			p.getWorld().spawnParticle(Particle.FLAME, location, (int) (thickness/2), 1/thickness, 1/thickness, 1/thickness, 0);
			p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, location, (int) (thickness/2), 1/thickness, 1/thickness, 1/thickness, 0.1);
			location = location.add(direction.normalize().multiply(1/thickness));
		}
		
		if(level%1==0){
			p.getWorld().playSound(location, Sound.ENTITY_PLAYER_BURP, 2.0f, 0.6f);
		}
		
	}
}