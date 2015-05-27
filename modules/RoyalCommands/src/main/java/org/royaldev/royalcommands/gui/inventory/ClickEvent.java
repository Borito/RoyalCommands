/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.gui.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ClickEvent {

    private final ItemStack clickedItem;
    private final Player clicker;
    private final int clickedSlot;

    public ClickEvent(final ItemStack item, final Player clicker, final int slot) {
        this.clickedItem = item;
        this.clicker = clicker;
        this.clickedSlot = slot;
    }

    public ItemStack getClickedItem() {
        return this.clickedItem;
    }

    public int getClickedSlot() {
        return this.clickedSlot;
    }

    public Player getClicker() {
        return this.clicker;
    }
}
