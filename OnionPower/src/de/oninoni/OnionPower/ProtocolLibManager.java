package de.oninoni.OnionPower;

import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

public class ProtocolLibManager {

	private static OnionPower plugin = OnionPower.get();

	private ProtocolManager protocolManager;

	public ProtocolLibManager(ProtocolManager protocolManager) {
		this.protocolManager = protocolManager;
	}

	public void addLoreListener() {
		protocolManager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL,
				PacketType.Play.Server.SET_SLOT, PacketType.Play.Server.WINDOW_ITEMS) {
			@Override
			public void onPacketSending(PacketEvent event) {
				if (event.getPlayer().getGameMode() == GameMode.CREATIVE)
					return;
				if (event.getPacketType() == PacketType.Play.Server.SET_SLOT) {
					PacketContainer packet = event.getPacket().deepClone();
					List<ItemStack> sm = packet.getItemModifier().getValues();
					for (int i = 0; i < sm.size(); i++) {
						ItemStack item = sm.get(i);
						if (item != null) {
							ItemMeta itemMeta = item.getItemMeta();
							if (itemMeta.hasLore()) {
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
					event.setPacket(packet);
				}
				if (event.getPacketType() == PacketType.Play.Server.WINDOW_ITEMS) {
					PacketContainer packet = event.getPacket().deepClone();
					List<ItemStack[]> sm = packet.getItemArrayModifier().getValues();
					for (int i = 0; i < sm.size(); i++) {
						for (int j = 0; j < sm.get(i).length; j++) {
							ItemStack item = sm.get(i)[j];
							if (item != null) {
								ItemMeta itemMeta = item.getItemMeta();
								if (itemMeta.hasLore()) {
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
					}
					event.setPacket(packet);
				}
			}
		});
	}
}
