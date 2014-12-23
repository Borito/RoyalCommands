package org.royaldev.royalcommands.gui.inventory.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public abstract class InventoryGUIEvent extends Event implements Cancellable {

    private boolean cancelled = false;

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean b) {
        this.cancelled = b;
    }
}
