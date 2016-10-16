package de.oninoni.OnionPower;

import java.lang.reflect.Field;

import org.bukkit.block.Dispenser;
import org.bukkit.block.Dropper;
import org.bukkit.block.Furnace;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

public class NMSAdapter {

	public static boolean isOnePointNine = false;
	
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

	private static void setInvNameDispenser(Dispenser dispenser, String name) {
		try {
			Field inventoryField = dispenser.getClass().getDeclaredField("dispenser");
			inventoryField.setAccessible(true);
			if (isOnePointNine) {
				net.minecraft.server.v1_9_R2.TileEntityDispenser tileEntitydispenser = ((net.minecraft.server.v1_9_R2.TileEntityDispenser) inventoryField
						.get(dispenser));
				tileEntitydispenser.a(name);
			} else {
				net.minecraft.server.v1_10_R1.TileEntityDispenser tileEntitydispenser = ((net.minecraft.server.v1_10_R1.TileEntityDispenser) inventoryField
						.get(dispenser));
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
			if (isOnePointNine) {
				net.minecraft.server.v1_9_R2.TileEntityDropper tileEntitydropper = ((net.minecraft.server.v1_9_R2.TileEntityDropper) inventoryField
						.get(dropper));
				tileEntitydropper.a(name);
			} else {
				net.minecraft.server.v1_10_R1.TileEntityDropper tileEntitydropper = ((net.minecraft.server.v1_10_R1.TileEntityDropper) inventoryField
						.get(dropper));
				tileEntitydropper.a(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void setInvNameFurnace(Furnace furnace, String name) {
		try {
			Field inventoryField = furnace.getClass().getDeclaredField("furnace");
			inventoryField.setAccessible(true);
			if (isOnePointNine) {
				net.minecraft.server.v1_9_R2.TileEntityFurnace tileEntityFurnace = ((net.minecraft.server.v1_9_R2.TileEntityFurnace) inventoryField
						.get(furnace));
				tileEntityFurnace.a(name);
			} else {
				net.minecraft.server.v1_10_R1.TileEntityFurnace tileEntityFurnace = ((net.minecraft.server.v1_10_R1.TileEntityFurnace) inventoryField
						.get(furnace));
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
			if (isOnePointNine) {
				net.minecraft.server.v1_9_R2.TileEntityHopper tileEntityhopper = ((net.minecraft.server.v1_9_R2.TileEntityHopper) inventoryField
						.get(hopper));
				tileEntityhopper.a(name);
			} else {
				net.minecraft.server.v1_10_R1.TileEntityHopper tileEntityhopper = ((net.minecraft.server.v1_10_R1.TileEntityHopper) inventoryField
						.get(hopper));
				tileEntityhopper.a(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sendTitle(Player p, String titleText, int fadeIn, int stayOnScreen, int fadeOut){
		titleText = "{\"text\": \"" + titleText + "\"}";
		if(isOnePointNine){
			/*net.minecraft.server.v1_9_R2.IChatBaseComponent chatTitle = net.minecraft.server.v1_9_R2.IChatBaseComponent.ChatSerializer.a(titleText);
			net.minecraft.server.v1_9_R2.PacketPlayOutChat title = new net.minecraft.server.v1_9_R2.PacketPlayOutChat(chatTitle, (byte) 2);
			((org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer) p).getHandle().playerConnection.sendPacket(title);*/
		}else{
			net.minecraft.server.v1_10_R1.IChatBaseComponent chatTitle = net.minecraft.server.v1_10_R1.IChatBaseComponent.ChatSerializer.a(titleText);
			net.minecraft.server.v1_10_R1.PacketPlayOutChat title = new net.minecraft.server.v1_10_R1.PacketPlayOutChat(chatTitle, (byte) 2);
			((org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer) p).getHandle().playerConnection.sendPacket(title);
		}
	}
}
