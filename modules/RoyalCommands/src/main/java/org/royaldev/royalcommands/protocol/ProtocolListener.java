/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.protocol;

import com.comphenix.attribute.NbtFactory;
import com.comphenix.protocol.PacketType.Play.Client;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.protocol.packets.WrapperPlayClientSetCreativeSlot;
import org.royaldev.royalcommands.protocol.packets.WrapperPlayServerSetSlot;
import org.royaldev.royalcommands.protocol.packets.WrapperPlayServerWindowItems;
import org.royaldev.royalcommands.spawninfo.SpawnInfo;

public class ProtocolListener {

    protected static final String NBT_INFO_KEY = "rcmds-spawninfo";
    final SpawnRenameProcessor srp = new SpawnRenameProcessor();
    private final RoyalCommands plugin;
    private final ProtocolManager pm = ProtocolLibrary.getProtocolManager();

    public ProtocolListener(RoyalCommands instance) {
        this.plugin = instance;
    }

    public void createSetCreativeSlotListener() {
        this.pm.addPacketListener(new PacketAdapter(PacketAdapter.params(this.plugin, Client.SET_CREATIVE_SLOT).optionIntercept()) {
            @Override
			/* TODO Fix this in 1.12 
			[18:22:25] [Netty Epoll Server IO #2/ERROR]: [RoyalCommands] Unhandled exception occured in onPacketReceiving(PacketEvent) for RoyalCommands
			java.lang.RuntimeException: An internal error occured.
				at com.comphenix.protocol.reflect.accessors.DefaultMethodAccessor.invoke(DefaultMethodAccessor.java:20) ~[ProtocolLib_(2).jar:4.3.0]
				at com.comphenix.protocol.utility.StreamSerializer.deserializeCompound(StreamSerializer.java:162) ~[ProtocolLib_(2).jar:4.3.0]
				at org.royaldev.royalcommands.protocol.ProtocolListener$1.onPacketReceiving(ProtocolListener.java:2043) ~[RoyalCommands-4.1.2-SNAPSHOT.jar:?]
			Caused by: java.lang.NullPointerException
		        at net.minecraft.server.v1_12_R1.NBTCompressedStreamTools.a(NBTCompressedStreamTools.java:84) ~[spigot.jar:git-Spigot-596221b-9a1fc1e]
		        at net.minecraft.server.v1_12_R1.NBTCompressedStreamTools.a(NBTCompressedStreamTools.java:53) ~[spigot.jar:git-Spigot-596221b-9a1fc1e]
		        at net.minecraft.server.v1_12_R1.PacketDataSerializer.j(PacketDataSerializer.java:231) ~[spigot.jar:git-Spigot-596221b-9a1fc1e]
		        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[?:1.8.0_131]
		        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[?:1.8.0_131]
		        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[?:1.8.0_131]
		        at java.lang.reflect.Method.invoke(Method.java:498) ~[?:1.8.0_131]
		        at com.comphenix.protocol.reflect.accessors.DefaultMethodAccessor.invoke(DefaultMethodAccessor.java:16) ~[?:?]
		        ... 46 more
			[18:22:25] [Netty Epoll Server IO #2/ERROR]: Parameters:
				net.minecraft.server.v1_12_R1.PacketPlayInSetCreativeSlot@dd45e78[
					slot=9
					b=1xtile.woolCarpet@9
				]
			*/
            public void onPacketReceiving(PacketEvent event) {
				if (!Config.useProtocolLib) {
					return;
				}
				this.plugin.getLogger().warning("RC | 1");
				// TODO The below line causes an error.. or something within it does.
				try {
					ProtocolListener.this.srp.unprocessFieldStack(event);
				} catch (Exception exc) {
					this.plugin.getLogger().warning("RC | 2");
					this.plugin.getLogger().warning("Ran into issues with unprocessFieldStack: " + exc.getMessage() + ". Inventory items may go invisible.");
				}
				this.plugin.getLogger().warning("RC | 3");
				
				final WrapperPlayClientSetCreativeSlot p = new WrapperPlayClientSetCreativeSlot(event.getPacket());
				if (p.getClickedItem() == null) {
					return;
				}
				final ItemStack is = p.getClickedItem();
				final NbtFactory.NbtCompound nbtc = NbtFactory.fromItemTag(is);
				if (!nbtc.containsKey(NBT_INFO_KEY)) {
					return;
				}
				final SpawnInfo si = new SpawnInfo(nbtc.getString(NBT_INFO_KEY, "false/null/false/null"));
				nbtc.remove(NBT_INFO_KEY);
				if (nbtc.isEmpty()) {
					NbtFactory.setItemTag(is, null);
				} else {
					NbtFactory.setItemTag(is, nbtc);
				}
				if (si.isSpawned() || si.hasComponents()) {
					SpawnInfo.SpawnInfoManager.applySpawnInfo(is, si);
				}
				p.setClickedItem(is);
            }
        });
    }

    public void createSetSlotListener() {
        this.pm.addPacketListener(new PacketAdapter(PacketAdapter.params(this.plugin, Server.SET_SLOT)) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (!Config.useProtocolLib) return;
                final WrapperPlayServerSetSlot p = new WrapperPlayServerSetSlot(event.getPacket());
                ItemStack is = p.getSlotData();
                if (is == null) return;
                final SpawnInfo si = SpawnInfo.SpawnInfoManager.getSpawnInfo(is);
                if (!si.isSpawned() && !si.hasComponents()) return;
                is = SpawnInfo.SpawnInfoManager.removeSpawnInfo(is);
                final NbtFactory.NbtCompound nbtc = NbtFactory.fromItemTag(is);
                nbtc.put(NBT_INFO_KEY, si.toString());
                NbtFactory.setItemTag(is, nbtc);
                p.setSlotData(is);
            }
        });
    }

    public void createWindowItemsListener() {
        this.pm.addPacketListener(new PacketAdapter(PacketAdapter.params(this.plugin, Server.WINDOW_ITEMS)) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (!Config.useProtocolLib) return;
                final WrapperPlayServerWindowItems p = new WrapperPlayServerWindowItems(event.getPacket());
                final List<ItemStack> newItems = new ArrayList<>();
                for (ItemStack is : p.getSlotData()) {
                    if (is == null) { // SpawnInfoManager can't take null ItemStacks
                        newItems.add(null);
                        continue;
                    }
                    final SpawnInfo si = SpawnInfo.SpawnInfoManager.getSpawnInfo(is);
                    if (!si.isSpawned() && !si.hasComponents()) continue;
                    is = SpawnInfo.SpawnInfoManager.removeSpawnInfo(is);
                    final NbtFactory.NbtCompound nbtc = NbtFactory.fromItemTag(is);
                    nbtc.put(NBT_INFO_KEY, si.toString());
                    NbtFactory.setItemTag(is, nbtc);
                    newItems.add(is);
                }
                p.setSlotData(newItems);
            }
        });
    }

    public void initialize() {
        this.createSetSlotListener();
        this.createWindowItemsListener();
        this.createSetCreativeSlotListener();
    }

    public void uninitialize() {
        this.pm.removePacketListeners(this.plugin);
    }
}
