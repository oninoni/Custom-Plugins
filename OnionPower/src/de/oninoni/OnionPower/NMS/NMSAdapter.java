package de.oninoni.OnionPower.NMS;

import java.util.List;

import org.bukkit.block.Dispenser;
import org.bukkit.block.Dropper;
import org.bukkit.block.Furnace;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.meta.BookMeta;

public abstract class NMSAdapter {
	
	public void setInvName(InventoryHolder block, String name){
		if(block instanceof Dispenser){
			setInvNameDispenser((Dispenser) block, name);
		}else if(block instanceof Dropper){
			setInvNameDropper((Dropper) block, name);
		}else if(block instanceof Hopper){
			setInvNameHopper((Hopper) block, name);
		}else if(block instanceof Furnace){
			setInvNameFurnace((Furnace) block, name);
		}
	}
	
	public void resetInvName(InventoryHolder block){
		if(block instanceof Dispenser){
			setInvNameDispenser((Dispenser) block, "Dispenser");
		}else if(block instanceof Dropper){
			setInvNameDropper((Dropper) block, "Dropper");
		}else if(block instanceof Hopper){
			setInvNameHopper((Hopper) block, "Hopper");
		}else if(block instanceof Furnace){
			setInvNameFurnace((Furnace) block, "Furnace");
		}
	}
	
	protected abstract void setInvNameDispenser(Dispenser dispenser, String name);
	protected abstract void setInvNameDropper(Dropper dropper, String name);
	protected abstract void setInvNameFurnace(Furnace furnace, String name);
	protected abstract void setInvNameHopper(Hopper hopper, String name);
	
	public abstract void sendTitle(Player p, String titleText, int fadeIn, int stayOnScreen, int fadeOut);
	
	public void addPages(BookMeta meta, List<String> json){
		for(int i = 0; i < json.size(); i++){
			addPage(meta, json.get(i));
		}
	}
	
	protected abstract void addPage(BookMeta meta, String json);
}
