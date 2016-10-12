package de.oninoni.OnionPower;

import java.lang.reflect.Field;

import org.bukkit.block.Dispenser;
import org.bukkit.block.Furnace;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;

public class NMSAdapter {

	public static boolean isOnePointNine = false;

	public static void setInvNameDispenser(Dispenser dispenser, String name) {
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

	public static void setInvNameFurnace(Furnace furnace, String name) {
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

	public static void setInvNameHopper(Hopper hopper, String name) {
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
		net.minecraft.server.v1_10_R1.IChatBaseComponent chatTitle = net.minecraft.server.v1_10_R1.IChatBaseComponent.ChatSerializer.a(titleText);
		net.minecraft.server.v1_10_R1.PacketPlayOutChat title = new net.minecraft.server.v1_10_R1.PacketPlayOutChat(chatTitle, (byte) 2);
		((org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer) p).getHandle().playerConnection.sendPacket(title);
	}
}
