package org.royaldev.royalcommands.gui.inventory.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.gui.inventory.InventoryGUI;
import org.royaldev.royalcommands.gui.inventory.events.InventoryGUIClickEvent;

public class InventoryGUIEventListener extends InventoryGUIListener {

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
