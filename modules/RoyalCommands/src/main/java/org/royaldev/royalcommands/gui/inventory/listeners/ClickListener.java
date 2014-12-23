package org.royaldev.royalcommands.gui.inventory.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.royaldev.royalcommands.gui.inventory.ClickEvent;
import org.royaldev.royalcommands.gui.inventory.ClickHandler;
import org.royaldev.royalcommands.gui.inventory.events.InventoryGUIClickEvent;

public class ClickListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onClick(final InventoryGUIClickEvent e) {
        final ClickHandler ch = e.getClickHandler();
        if (ch == null) return;
        if (!ch.onClick(new ClickEvent(e.getClicked(), e.getPlayer(), e.getSlot()))) {
            e.setCancelled(true);
        }
    }

}
