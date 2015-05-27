/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.gui.inventory.events;

import org.bukkit.entity.Player;
import org.royaldev.royalcommands.gui.inventory.InventoryGUI;

public abstract class InventoryGUIPlayerEvent extends InventoryGUIEvent {

    private final Player player;

    public InventoryGUIPlayerEvent(final InventoryGUI inventoryGUI, final Player player) {
        super(inventoryGUI);
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

}
