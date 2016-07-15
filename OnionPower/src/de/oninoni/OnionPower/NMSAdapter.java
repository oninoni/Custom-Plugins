package de.oninoni.OnionPower;

import java.lang.reflect.Field;

import org.bukkit.block.Dispenser;
import org.bukkit.block.Furnace;

import net.minecraft.server.v1_10_R1.TileEntityDispenser;
import net.minecraft.server.v1_10_R1.TileEntityFurnace;

public class NMSAdapter {
	
	public static void setInvNameFurnace(Furnace furnace, String name) {
		try{
			Field inventoryField = furnace.getClass().getDeclaredField("furnace");
			inventoryField.setAccessible(true);
			TileEntityFurnace tileEntityFurnace = ((TileEntityFurnace) inventoryField.get(furnace));
			tileEntityFurnace.a(name);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void setInvNameDispenser(Dispenser dispenser, String name){
		try{
			Field inventoryField = dispenser.getClass().getDeclaredField("dispenser");
			inventoryField.setAccessible(true);
			TileEntityDispenser tileEntityDispenser = ((TileEntityDispenser) inventoryField.get(dispenser));
			tileEntityDispenser.a(name);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
