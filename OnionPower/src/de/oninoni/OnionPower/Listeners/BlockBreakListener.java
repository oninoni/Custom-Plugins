package de.oninoni.OnionPower.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import de.oninoni.OnionPower.NMSAdapter;
import de.oninoni.OnionPower.OnionPower;
import de.oninoni.OnionPower.Items.PowerItems.PowerItem;

public class BlockBreakListener implements Listener {

	private static OnionPower plugin = OnionPower.get();

	@EventHandler
	public void onBoom(EntityExplodeEvent e) {
		plugin.getMachineManager().onBoom(e);
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		PowerItem pitem = new PowerItem(e.getPlayer().getInventory().getItemInMainHand());
		if(pitem.check()){
			if(pitem.readPower() == 0){
				e.setCancelled(true);
				NMSAdapter.sendTitle(e.getPlayer(), "§4Please Recharge Your Pickaxe", 2, 20, 20);
				pitem.setDurability((short) 0);
				e.getPlayer().getInventory().setItemInMainHand(pitem);
				return;
			}
		}
		plugin.getMachineManager().onBreak(e);
	}
}
