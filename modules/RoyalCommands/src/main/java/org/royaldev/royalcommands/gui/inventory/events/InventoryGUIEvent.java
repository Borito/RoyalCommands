package org.royaldev.royalcommands.gui.inventory.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class InventoryGUIEvent extends Event implements Cancellable {

    private static final HandlerList handlerList = new HandlerList();
    private boolean cancelled = false;

    public static HandlerList getHandlerList() {
        return InventoryGUIEvent.handlerList;
    }

    @Override
    public HandlerList getHandlers() {
        return InventoryGUIEvent.handlerList;
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
