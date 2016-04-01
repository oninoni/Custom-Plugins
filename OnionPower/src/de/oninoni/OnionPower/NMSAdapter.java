package de.oninoni.OnionPower;

import java.lang.reflect.Field;

import org.bukkit.block.Furnace;

import net.minecraft.server.v1_9_R1.TileEntityFurnace;

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
	
}
