package org.royaldev.royalcommands.gui.inventory;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredListener;

public class ClickListener implements Listener {

    private ClickHandler getClickHandler(final InventoryGUI ig, final ItemStack is) {
        if (ig == null || is == null || is.getType() == Material.AIR) return null;
        return ig.getClickHandler(is);
    }

    private InventoryGUI getInventoryGUI(final Inventory i) {
        final InventoryHolder ih = i.getHolder();
        if (!(ih instanceof GUIHolder)) return null;
        return ((GUIHolder) ih).getInventoryGUI();
    }

    private Player getPlayer(final HumanEntity he) {
        return he instanceof Player ? (Player) he : null;
    }

    @EventHandler
    public void onClick(final InventoryClickEvent e) { // FIXME: This is being called twice :(
        System.out.println("ClickListener.onClick");
        System.out.println("e = [" + e + "]");
        System.out.println(e.getAction());
        for (final RegisteredListener rl : e.getHandlers().getRegisteredListeners()) {
            System.out.println("Registered listener:");
            System.out.println(rl.getPlugin().getName());
            System.out.println(rl.getListener().getClass().getName());
            System.out.println();
        }
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
