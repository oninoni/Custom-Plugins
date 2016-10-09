package de.oninoni.OnionPower.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import de.oninoni.OnionPower.OnionPower;
import de.oninoni.OnionPower.Items.PowerItems.Batrod;
import de.oninoni.OnionPower.Items.PowerItems.PowerItem;

public class PlayerListener implements Listener {

	private static OnionPower plugin = OnionPower.get();

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		plugin.getMachineManager().onMove(e);
	}
	
	@EventHandler
	public void onPrepareCrafting(PrepareItemCraftEvent e){
		if(e.getInventory().getResult() instanceof Batrod){
			e.getInventory().setResult(new Batrod(0));
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onItemDamage(PlayerItemDamageEvent e){
		PowerItem powerItem = new PowerItem(e.getItem());
		if(powerItem.check()){
			int power = powerItem.readPower();
			if(power > 0){
				powerItem.setPower(Math.max(power - e.getDamage(), 0));
				e.getPlayer().setItemInHand(powerItem);
				e.setCancelled(true);
			}
		}
	}
	

	@EventHandler
	public void onArmorStandManipulate(PlayerArmorStandManipulateEvent e){
		plugin.getMachineManager().onArmorStandManipulate(e);
	}

}
