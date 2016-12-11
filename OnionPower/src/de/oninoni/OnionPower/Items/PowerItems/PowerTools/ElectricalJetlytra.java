package de.oninoni.OnionPower.Items.PowerItems.PowerTools;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class ElectricalJetlytra extends PowerTool{
	private static final String NAME = "§4Electrical Jetlytra";
	private static final float thickness = 8;
	
	public ElectricalJetlytra(int power, short damage){
		super(1, damage, NAME, power);
	}
	
	public ElectricalJetlytra(ItemStack item){
		super(item, NAME);
	}
	
	@Override
	protected Material getOriginalType() {
		return Material.ELYTRA;
	}
	
	@Override
	public void onCraft(PrepareItemCraftEvent e) {
		super.onCraft(e);
		if(initialBatrodPower != -1 && initialDurability != -1)
			e.getInventory().setResult(new ElectricalJetlytra(initialBatrodPower, initialDurability));
	}
	
	public static void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(!p.isGliding())return;
		if(!p.isSneaking())return;
		ElectricalJetlytra electricalJetlytra = new ElectricalJetlytra(p.getInventory().getChestplate());
		if(electricalJetlytra.check()){
			int power = electricalJetlytra.getPower();
			if(power==0) return;
			power-=1;
			electricalJetlytra.setPower(power);
			e.getPlayer().getEquipment().setChestplate(electricalJetlytra);
			
			p.setVelocity(p.getVelocity().add(p.getLocation().getDirection().multiply(0.2)).multiply(0.95));Location location = p.getLocation();
			
			Vector direction = p.getLocation().getDirection();
			location = location.add(new Vector(0,0.5,0));
			
			for(int i = 0; i < thickness; i++){
				p.getWorld().spawnParticle(Particle.FLAME, location, (int) (thickness/2), 1/thickness, 1/thickness, 1/thickness, 0);
				p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, location, (int) (thickness/2), 1/thickness, 1/thickness, 1/thickness, 0.1);
				location = location.add(direction.normalize().multiply(1/thickness));
			}
			
			if(power % 1 ==0){
				p.getWorld().playSound(location, Sound.ENTITY_FIREWORK_LAUNCH, 2.0f, 0.6f);
			}
		}

		
		
	}
}
