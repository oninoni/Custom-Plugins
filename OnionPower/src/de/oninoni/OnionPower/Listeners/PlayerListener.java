package de.oninoni.OnionPower.Listeners;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.OnionPower;
import de.oninoni.OnionPower.Items.PowerItems.PowerItem;
import de.oninoni.OnionPower.Items.PowerItems.PowerTools.ElectricalJetlytra;

public class PlayerListener implements Listener {

	private static OnionPower plugin = OnionPower.get();

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		plugin.getMachineManager().onMove(e);
		
		ElectricalJetlytra.onMove(e);
	}
	
	@EventHandler
	public void onPrepareCrafting(PrepareItemCraftEvent e){
		PowerItem powerItem = new PowerItem(e.getInventory().getResult());
		if(powerItem.check()){
			powerItem.onCraft(e);
		}
	}
	
	@EventHandler
	public void onItemDamage(PlayerItemDamageEvent e){
		//plugin.getServer().broadcastMessage("Damage on Tool detected!");
		PowerItem powerItem = new PowerItem(e.getItem());
		if(powerItem.check()){
			int power = powerItem.getPower();
			if(power > 0){
				//plugin.getLogger().info(""+powerItem);
				int slotFound = -1;
				for (int i = 0; i < e.getPlayer().getInventory().getSize(); i++) {
					ItemStack item = e.getPlayer().getInventory().getItem(i);
					if(item == null)continue;
					//plugin.getLogger().info(""+item);
					if(powerItem.isSimilar(item)){
						//plugin.getServer().broadcastMessage("Found");
						slotFound = i;
						break;
					}
				}
				
				powerItem.setPower(Math.max(power - e.getDamage() * 20, 0));
				e.getPlayer().getInventory().setItem(slotFound, powerItem);
				e.setCancelled(true);
			}
		}
	}
	

	@EventHandler
	public void onArmorStandManipulate(PlayerArmorStandManipulateEvent e){
		plugin.getMachineManager().onArmorStandManipulate(e);
	}
	
	@EventHandler
	public void onPickup(PlayerPickupItemEvent e){
		ItemStack item = e.getItem().getItemStack();
		for (Class<? extends PowerItem> c : PowerItem.classes){
			//plugin.getLogger().info("+++");
			try {
				PowerItem pitem = (PowerItem) c.getConstructor(ItemStack.class).newInstance(item);
				if (pitem.checkName()){
					//plugin.getLogger().info("Pickup: " + pitem);
					pitem.fixItemFromCreativeSet(item);
					break;
				}
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e1) {
				e1.printStackTrace();
			}
		}
	}
}
