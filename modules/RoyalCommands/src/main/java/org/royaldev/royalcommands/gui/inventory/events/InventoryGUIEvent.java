package org.royaldev.royalcommands.gui.inventory.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class InventoryGUIEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    public static HandlerList getHandlerList() {
        return InventoryGUIEvent.handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return InventoryGUIEvent.handlers;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean b) {
        this.cancelled = b;
    }
}
