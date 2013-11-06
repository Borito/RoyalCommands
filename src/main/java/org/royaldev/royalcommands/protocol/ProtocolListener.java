package org.royaldev.royalcommands.protocol;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.ListenerOptions;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.protocol.packets.Packet67SetSlot;
import org.royaldev.royalcommands.protocol.packets.Packet68SetWindowItems;
import org.royaldev.royalcommands.protocol.packets.Packet6BCreativeInventoryAction;
import org.royaldev.royalcommands.spawninfo.NbtFactory;
import org.royaldev.royalcommands.spawninfo.SpawnInfo;

import java.util.ArrayList;
import java.util.List;

public class ProtocolListener {

    private final RoyalCommands plugin;
    private final ProtocolManager pm = ProtocolLibrary.getProtocolManager();
    protected static final String NBT_INFO_KEY = "rcmds-spawninfo";

    public ProtocolListener(RoyalCommands instance) {
        plugin = instance;
    }

    public void initialize() {
        createSetSlotListener();
        createWindowItemsListener();
        createSetCreativeSlotListener();
    }

    public void uninitialize() {
        pm.removePacketListeners(plugin);
    }

    public void createSetSlotListener() {
        pm.addPacketListener(new PacketAdapter(plugin, ConnectionSide.SERVER_SIDE, Packets.Server.SET_SLOT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (!Config.useProtocolLib) return;
                final Packet67SetSlot p = new Packet67SetSlot(event.getPacket());
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
        pm.addPacketListener(new PacketAdapter(plugin, ConnectionSide.SERVER_SIDE, Packets.Server.WINDOW_ITEMS) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (!Config.useProtocolLib) return;
                final Packet68SetWindowItems p = new Packet68SetWindowItems(event.getPacket());
                final List<ItemStack> newItems = new ArrayList<ItemStack>();
                for (ItemStack is : p.getItems()) {
                    if (is == null) { // SpawnInfoManager can't take null ItemStacks
                        newItems.add(is);
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
                p.setItems(newItems.toArray(new ItemStack[newItems.size()]));
            }
        });
    }

    final SpawnRenameProcessor srp = new SpawnRenameProcessor();

    public void createSetCreativeSlotListener() {
        pm.addPacketListener(new PacketAdapter(plugin, ConnectionSide.CLIENT_SIDE, new ListenerOptions[]{ListenerOptions.INTERCEPT_INPUT_BUFFER}, Packets.Client.SET_CREATIVE_SLOT) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if (!Config.useProtocolLib) return;
                srp.unprocessFieldStack(event);
                final Packet6BCreativeInventoryAction p = new Packet6BCreativeInventoryAction(event.getPacket());
                if (p.getClickedItem() == null) return;
                final ItemStack is = p.getClickedItem();
                final NbtFactory.NbtCompound nbtc = NbtFactory.fromItemTag(is);
                if (!nbtc.containsKey(NBT_INFO_KEY)) return;
                final SpawnInfo si = new SpawnInfo(nbtc.getString(NBT_INFO_KEY, "false/null/false/null"));
                nbtc.remove(NBT_INFO_KEY);
                if (nbtc.isEmpty()) NbtFactory.setItemTag(is, null);
                else NbtFactory.setItemTag(is, nbtc);
                if (si.isSpawned() || si.hasComponents()) SpawnInfo.SpawnInfoManager.applySpawnInfo(is, si);
                p.setClickedItem(is);
            }
        });
    }
}
