package de.oninoni.OnionPower.NMS;

import java.lang.reflect.Field;

import org.bukkit.block.Dispenser;
import org.bukkit.block.Dropper;
import org.bukkit.block.Furnace;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

public abstract class NMSAdapter {

	public static String version = "";
	
	public static void setInvName(InventoryHolder block, String name){
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
	
	public static void resetInvName(InventoryHolder block){
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

	private static void setInvNameDispenser(Dispenser dispenser, String name) {
		try {
			Field inventoryField = dispenser.getClass().getDeclaredField("dispenser");
			inventoryField.setAccessible(true);
			if (version.equals("1.9.4-R0.1-SNAPSHOT")) {
				net.minecraft.server.v1_9_R2.TileEntityDispenser tileEntitydispenser = ((net.minecraft.server.v1_9_R2.TileEntityDispenser) inventoryField.get(dispenser));
				tileEntitydispenser.a(name);
			}
			if(version.equals("1.10.2-R0.1-SNAPSHOT")) {
				net.minecraft.server.v1_10_R1.TileEntityDispenser tileEntitydispenser = ((net.minecraft.server.v1_10_R1.TileEntityDispenser) inventoryField.get(dispenser));
				tileEntitydispenser.a(name);
			}
			if (version.equals("1.11-R0.1-SNAPSHOT")){
				net.minecraft.server.v1_11_R1.TileEntityDispenser tileEntitydispenser = ((net.minecraft.server.v1_11_R1.TileEntityDispenser) inventoryField.get(dispenser));
				tileEntitydispenser.a(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void setInvNameDropper(Dropper dropper, String name) {
		try {
			Field inventoryField = dropper.getClass().getDeclaredField("dropper");
			inventoryField.setAccessible(true);
			if (version.equals("1.9.4-R0.1-SNAPSHOT")) {
				net.minecraft.server.v1_9_R2.TileEntityDropper tileEntitydropper = ((net.minecraft.server.v1_9_R2.TileEntityDropper) inventoryField.get(dropper));
				tileEntitydropper.a(name);
			}
			if(version.equals("1.10.2-R0.1-SNAPSHOT")){
				net.minecraft.server.v1_10_R1.TileEntityDropper tileEntitydropper = ((net.minecraft.server.v1_10_R1.TileEntityDropper) inventoryField.get(dropper));
				tileEntitydropper.a(name);
			}
			if (version.equals("1.11-R0.1-SNAPSHOT")){
				net.minecraft.server.v1_11_R1.TileEntityDropper tileEntityDropper = ((net.minecraft.server.v1_11_R1.TileEntityDropper) inventoryField.get(dropper));
				tileEntityDropper.a(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void setInvNameFurnace(Furnace furnace, String name) {
		try {
			Field inventoryField = furnace.getClass().getDeclaredField("furnace");
			inventoryField.setAccessible(true);
			if (version.equals("1.9.4-R0.1-SNAPSHOT")) {
				net.minecraft.server.v1_9_R2.TileEntityFurnace tileEntityFurnace = ((net.minecraft.server.v1_9_R2.TileEntityFurnace) inventoryField.get(furnace));
				tileEntityFurnace.a(name);
			} 
			if (version.equals("1.10.2-R0.1-SNAPSHOT")) {
				net.minecraft.server.v1_10_R1.TileEntityFurnace tileEntityFurnace = ((net.minecraft.server.v1_10_R1.TileEntityFurnace) inventoryField.get(furnace));
				tileEntityFurnace.a(name);
			}
			if (version.equals("1.11-R0.1-SNAPSHOT")){
				net.minecraft.server.v1_11_R1.TileEntityFurnace tileEntityFurnace = ((net.minecraft.server.v1_11_R1.TileEntityFurnace) inventoryField.get(furnace));
				tileEntityFurnace.a(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void setInvNameHopper(Hopper hopper, String name) {
		try {
			Field inventoryField = hopper.getClass().getDeclaredField("hopper");
			inventoryField.setAccessible(true);
			if (version.equals("1.9.4-R0.1-SNAPSHOT")) {
				net.minecraft.server.v1_9_R2.TileEntityHopper tileEntityhopper = ((net.minecraft.server.v1_9_R2.TileEntityHopper) inventoryField.get(hopper));
				tileEntityhopper.a(name);
			} 
			if(version.equals("1.10.2-R0.1-SNAPSHOT")) {
				net.minecraft.server.v1_10_R1.TileEntityHopper tileEntityhopper = ((net.minecraft.server.v1_10_R1.TileEntityHopper) inventoryField.get(hopper));
				tileEntityhopper.a(name);
			}
			if (version.equals("1.11-R0.1-SNAPSHOT")){
				net.minecraft.server.v1_11_R1.TileEntityHopper tileEntityhopper = ((net.minecraft.server.v1_11_R1.TileEntityHopper) inventoryField.get(hopper));
				tileEntityhopper.a(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendTitle(Player p, String titleText, int fadeIn, int stayOnScreen, int fadeOut){
		sendTitleInternal(p, titleText, fadeIn, stayOnScreen, fadeOut);
	}
	
	protected abstract void sendTitleInternal(Player p, String titleText, int fadeIn, int stayOnScreen, int fadeOut);
}
