package de.oninoni.OnionPower;

import java.lang.reflect.Field;

import org.bukkit.block.Dispenser;
import org.bukkit.block.Furnace;
import org.bukkit.block.Hopper;

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
}
