/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
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
