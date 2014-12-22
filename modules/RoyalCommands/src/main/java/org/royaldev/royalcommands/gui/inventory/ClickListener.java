package org.royaldev.royalcommands.gui.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ClickListener extends InventoryGUIListener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onClick(final InventoryClickEvent e) {
        System.out.println("ClickListener.onClick");
        System.out.println("e = [" + e + "]");
        final InventoryGUI ig = this.getInventoryGUI(e.getInventory());
        final ItemStack clicked = e.getCurrentItem();
        final ClickHandler ch = this.getClickHandler(ig, clicked);
        final Player p = this.getPlayer(e.getWhoClicked());
        if (ch == null || p == null) return;
        if (!ch.onClick(new ClickEvent(clicked, p, e.getSlot()))) {
            e.setCancelled(true);
        }
    }

}
