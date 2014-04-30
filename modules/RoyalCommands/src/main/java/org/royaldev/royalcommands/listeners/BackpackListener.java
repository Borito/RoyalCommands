package org.royaldev.royalcommands.listeners;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.RUtils;

import java.util.UUID;

public class BackpackListener implements Listener {

    @EventHandler
    public void backpackClose(InventoryCloseEvent e) {
        if (e.getInventory() == null || e.getInventory().getName() == null) return; // modpacks
        final InventoryHolder ih = e.getInventory().getHolder();
        if (!(ih instanceof BackpackHolder)) return;
        final BackpackHolder bh = (BackpackHolder) ih;
        final Inventory backpack = RUtils.getBackpack(bh.getOwnerUUID(), bh.getWorld());
        backpack.setContents(e.getInventory().getContents());
        RUtils.saveBackpack(bh.getOwnerUUID(), bh.getWorld(), backpack);
    }

    @EventHandler
    public void backpackClearOnDeath(PlayerDeathEvent e) {
        if (!Config.backpackReset) return;
        final Player p = e.getEntity();
        final Inventory backpack = RUtils.getBackpack(p);
        backpack.clear();
        RUtils.saveBackpack(p, backpack);
    }

    public static class BackpackHolder implements InventoryHolder {

        private final UUID ownerUUID;
        private final World w;

        public BackpackHolder(UUID ownerUUID, World w) {
            this.ownerUUID = ownerUUID;
            this.w = w;
        }

        public UUID getOwnerUUID() {
            return this.ownerUUID;
        }

        public World getWorld() {
            return w;
        }

        /**
         * No function.
         *
         * @return Returns null
         * @deprecated No use
         */
        @Override
        @Deprecated
        public Inventory getInventory() {
            return null;
        }
    }

}
