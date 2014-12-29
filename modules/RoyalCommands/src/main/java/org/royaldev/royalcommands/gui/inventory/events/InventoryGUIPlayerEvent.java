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
