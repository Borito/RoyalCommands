package org.royaldev.royalcommands.gui.inventory.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.gui.inventory.InventoryGUI;
import org.royaldev.royalcommands.gui.inventory.events.InventoryGUIClickEvent;
import org.royaldev.royalcommands.gui.inventory.events.InventoryGUIDragEvent;

public class InventoryGUIEventListener extends InventoryGUIListener {

    @EventHandler
    public void dragInventoryGUI(final InventoryDragEvent e) {
        final InventoryGUI ig = this.getInventoryGUI(e.getInventory()); // TODO: Extract some sort of logic
        final ItemStack item = e.getOldCursor();
        final Player p = this.getPlayer(e.getWhoClicked());
        if (p == null) return;
        final InventoryGUIDragEvent igde = new InventoryGUIDragEvent(ig, p, item, e.getInventorySlots(), e.getRawSlots());
        p.getServer().getPluginManager().callEvent(igde);
        if (igde.isCancelled()) e.setCancelled(true);
    }

    @EventHandler
    public void fireClickEvents(final InventoryClickEvent e) {
        final InventoryGUI ig = this.getInventoryGUI(e.getInventory());
        final ItemStack clicked = e.getCurrentItem();
        final Player p = this.getPlayer(e.getWhoClicked());
        if (p == null) return;
        final InventoryGUIClickEvent igce = new InventoryGUIClickEvent(ig, p, e.getClick(), clicked, e.getAction(), e.getSlot(), e.getRawSlot());
        p.getServer().getPluginManager().callEvent(igce);
        if (igce.isCancelled()) e.setCancelled(true);
    }

}
