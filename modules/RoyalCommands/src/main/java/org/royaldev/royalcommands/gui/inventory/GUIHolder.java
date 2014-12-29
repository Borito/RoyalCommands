package org.royaldev.royalcommands.gui.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class GUIHolder implements InventoryHolder {

    private final InventoryGUI inventoryGUI;

    public GUIHolder(final InventoryGUI inventoryGUI) {
        this.inventoryGUI = inventoryGUI;
    }

    @Override
    public Inventory getInventory() {
        return this.getInventoryGUI().getBase();
    }

    public InventoryGUI getInventoryGUI() {
        return this.inventoryGUI;
    }
}
