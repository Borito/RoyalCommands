package org.royaldev.royalcommands.protocol;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.protocol.packets.Packet67SetSlot;
import org.royaldev.royalcommands.protocol.packets.Packet68SetWindowItems;
import org.royaldev.royalcommands.spawninfo.SpawnInfo;

import java.util.ArrayList;
import java.util.List;

public class ProtocolListener {

    private final RoyalCommands plugin;
    private final ProtocolManager pm = ProtocolLibrary.getProtocolManager();

    public ProtocolListener(RoyalCommands instance) {
        plugin = instance;
    }

    public void initialize() {
        createSetSlotListener();
        createWindowItemsListener();
        //createSetCreativeSlotListener();
    }

    public void createSetSlotListener() {
        pm.addPacketListener(new PacketAdapter(plugin, ConnectionSide.SERVER_SIDE, Packets.Server.SET_SLOT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                final Packet67SetSlot p = new Packet67SetSlot(event.getPacket());
                if (p.getSlotData() == null) return;
                p.setSlotData(SpawnInfo.SpawnInfoManager.removeSpawnInfo(p.getSlotData()));
                event.setPacket(p.getHandle());
            }
        });
    }

    public void createWindowItemsListener() {
        pm.addPacketListener(new PacketAdapter(plugin, ConnectionSide.SERVER_SIDE, Packets.Server.WINDOW_ITEMS) {
            @Override
            public void onPacketSending(PacketEvent event) {
                final Packet68SetWindowItems p = new Packet68SetWindowItems(event.getPacket());
                final List<ItemStack> newItems = new ArrayList<ItemStack>();
                for (ItemStack is : p.getItems()) {
                    if (is == null) { // SpawnInfoManager can't take null ItemStacks
                        newItems.add(is);
                        continue;
                    }
                    newItems.add(SpawnInfo.SpawnInfoManager.removeSpawnInfo(is));
                }
                p.setItems(newItems.toArray(new ItemStack[newItems.size()]));
                event.setPacket(p.getHandle());
            }
        });
    }

    /*public void createSetCreativeSlotListener() {
        pm.addPacketListener(new PacketAdapter(plugin, ConnectionSide.CLIENT_SIDE, Packets.Client.SET_CREATIVE_SLOT) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                final Packet6BCreativeInventoryAction p = new Packet6BCreativeInventoryAction(event.getPacket());
                if (p.getClickedItem() == null) return;
                final ItemStack inInv = null; // figure out how to do this?
                if (inInv == null || inInv.getType() == Material.AIR) return;
                final SpawnInfo si = SpawnInfo.SpawnInfoManager.getSpawnInfo(inInv);
                if (!si.isSpawned() && !si.hasComponents()) return;
                p.setClickedItem(SpawnInfo.SpawnInfoManager.applySpawnInfo(p.getClickedItem(), si));
                System.out.println("Re-applied data");
                event.setPacket(p.getHandle());
            }
        });
    }*/

}
