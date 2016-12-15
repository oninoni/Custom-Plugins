package de.oninoni.OnionPower.Items.TutorialBook;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.Items.PowerItems.Batrod;

public class BookRecipes {
	
	public static String createRecipe(Material[] recipe){
		String json = "";
		
		for(int i = 0; i < 9; i++){
			ItemStack item;
			
			if(recipe[i] == Material.COMMAND){
				if(i != 0)
					json += ",";
				json += "{\"text\":\"[]\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Empty Slot\"}}";
			}else{
				if(recipe[i] == Material.BARRIER){
					item = new Batrod(0);
				}else{
					item = new ItemStack(recipe[i]);
				}
				
				String itemData = "{";
					itemData += "id:\\\"minecraft:" + item.getType().name().toLowerCase() + "\\\"";
					itemData += ",Count:1";
					if(item.getItemMeta() != null && item.getItemMeta().getDisplayName() != null){
						itemData += ",tag:{";
							itemData += "display:{";
								itemData += "Name:\\\"" + item.getItemMeta().getDisplayName() + "\\\"";
						
								List<String> lore = item.getItemMeta().getLore();
								if(lore != null && lore.size() > 0){
									itemData += ",Lore:[";
									for(int j = 0;j < lore.size(); j++){
										if(lore.get(j).startsWith("§h"))continue;
										if(j != 0){
											itemData += ",";
										}
										itemData += "\\\"" + lore.get(j) + "\\\"";
									}
									itemData += "]";
								}
							itemData += "}";
						itemData += "}";
					}
				itemData += "}";
				
				if(i != 0)
					json += ",";
				
				json += "{\"text\":\"[]\"";
				
				json += ",\"hoverEvent\":{";
				
				json += "\"action\":\"show_item\"";
				json += ",\"value\":";
				json += "\"" + itemData + "\"";
				
				json += "}}";
			}
			if(i%3 == 2){
				json += ",{\"text\":\"\n\"}";
			}else{
				json += ",{\"text\":\"  \",\"hoverEvent\":{}}";
			}
		}
		
		return json;
	}
}