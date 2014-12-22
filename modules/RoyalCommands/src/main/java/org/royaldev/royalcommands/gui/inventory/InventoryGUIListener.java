package org.royaldev.royalcommands.gui.inventory;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class InventoryGUIListener implements Listener {

    public ClickHandler getClickHandler(final InventoryGUI ig, final ItemStack is) {
        if (ig == null || is == null || is.getType() == Material.AIR) return null;
        return ig.getClickHandler(is);
    }

    public InventoryGUI getInventoryGUI(final Inventory i) {
        final InventoryHolder ih = i.getHolder();
        if (!(ih instanceof GUIHolder)) return null;
        return ((GUIHolder) ih).getInventoryGUI();
    }

    public Player getPlayer(final HumanEntity he) {
        return he instanceof Player ? (Player) he : null;
    }

}
