package de.oninoni.OnionPower;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import de.oninoni.OnionPower.Items.PowerItems.PowerItem;

public class ProtocolLibManager {

	private static OnionPower plugin = OnionPower.get();

	private ProtocolManager protocolManager;

	public ProtocolLibManager(ProtocolManager protocolManager) {
		this.protocolManager = protocolManager;
	}
	
	private void handleItem(Player p, ItemStack item, boolean showElytra){
		if (item != null) {
			//plugin.getLogger().info("Server: " + item.toString());
			PowerItem powerItem = new PowerItem(item);
			if(powerItem.check()){
				for (Class<? extends PowerItem> c : PowerItem.classes)
				{
					try {
						PowerItem pitem = (PowerItem) c.getConstructor(ItemStack.class).newInstance(item);
						if (pitem.check()){
							//plugin.getServer().broadcastMessage("" + c.getName());
							if(showElytra && pitem.getType() == Material.ELYTRA){
								if(p.getEquipment().getChestplate() != null && item.getItemMeta() != null && p.getEquipment().getChestplate().getItemMeta() != null && p.getEquipment().getChestplate().getItemMeta().getDisplayName() == item.getItemMeta().getDisplayName()){
									item.setType(pitem.getType());
									continue;
								}
							}
							item.setType(pitem.getVisibleType());
							break;
						}
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | NoSuchMethodException | SecurityException e1) {
						e1.printStackTrace();
					}
				}
			}
			ItemMeta itemMeta = item.getItemMeta();
			if (itemMeta.hasLore() && p.getGameMode() != GameMode.CREATIVE) {
				List<String> lore = itemMeta.getLore();
				for (int k = 0; k < lore.size(); k++) {
					String s = lore.get(k);
					if (s.startsWith("§h")) {
						lore.remove(s);
						k--;
					}
				}
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
			}
		}
	}

	public void addLoreListener() {
		protocolManager.addPacketListener(new PacketAdapter(plugin,
				PacketType.Play.Server.SET_SLOT, 
				PacketType.Play.Server.WINDOW_ITEMS, 
				PacketType.Play.Server.SPAWN_ENTITY,
				PacketType.Play.Server.ENTITY_EQUIPMENT,
				PacketType.Play.Client.SET_CREATIVE_SLOT
				){
					@Override
					public void onPacketSending(PacketEvent event) {
						PacketContainer packet = event.getPacket().deepClone();
						if (event.getPacketType() == PacketType.Play.Server.SET_SLOT) {
							List<ItemStack> sm = packet.getItemModifier().getValues();
							for (int i = 0; i < sm.size(); i++) {
								ItemStack item = sm.get(i);
								handleItem(event.getPlayer(), item, true);
							}
						}
						if (event.getPacketType() == PacketType.Play.Server.WINDOW_ITEMS) {
							List<ItemStack[]> sm = packet.getItemArrayModifier().getValues();
							for (int i = 0; i < sm.size(); i++) {
								ItemStack[] smStack =  sm.get(i);
								for (int j = 0; j < smStack.length; j++) {
									ItemStack item = smStack[j];
									handleItem(event.getPlayer(), item, true);
								}
							}
						}
						if(event.getPacketType() == PacketType.Play.Server.SPAWN_ENTITY){
							List<Entity> sm = packet.getEntityModifier(event).getValues();
							for (Entity entity : sm) {
								if(entity != null && entity.getType() == EntityType.DROPPED_ITEM)
									handleItem(event.getPlayer(), ((Item) entity).getItemStack(), false);
							}
						}
						if(event.getPacketType() == PacketType.Play.Server.ENTITY_EQUIPMENT){
							List<ItemStack> sm = packet.getItemModifier().getValues();
							for (int i = 0; i < sm.size(); i++) {
								ItemStack item = sm.get(i);
								handleItem(event.getPlayer(), item, true);
							}
						}
						
						event.setPacket(packet);
					}
			
					@Override
					public void onPacketReceiving(PacketEvent event) {
						PacketContainer packet = event.getPacket().deepClone();
						if(packet.getType() == PacketType.Play.Client.SET_CREATIVE_SLOT){
							//plugin.getLogger().info("SET_CREATIVE_SLOT");
							List<ItemStack> sm = packet.getItemModifier().getValues();
							for (int i = 0; i < sm.size(); i++) {
								ItemStack item = sm.get(i);
								for (Class<? extends PowerItem> c : PowerItem.classes){
									try {
										PowerItem pitem = (PowerItem) c.getConstructor(ItemStack.class).newInstance(item);
										if (pitem.checkName()){
											//plugin.getLogger().info("Fixing Stuff in: " + item);
											pitem.fixItemFromCreativeSet(item);
											//plugin.getLogger().info("Fixed to: " + item);
											break;
										}
									} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
											| InvocationTargetException | NoSuchMethodException | SecurityException e1) {
										e1.printStackTrace();
									}
								}
							}
						}
						event.setPacket(packet);
					}
		});
	}
}
