package org.royaldev.royalcommands.gui.inventory.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.royaldev.royalcommands.gui.inventory.InventoryGUI;

public abstract class InventoryGUIEvent extends Event implements Cancellable {

    private static final HandlerList handlerList = new HandlerList();
    private final InventoryGUI inventoryGUI;
    private boolean cancelled = false;

    public InventoryGUIEvent(final InventoryGUI inventoryGUI) {
        this.inventoryGUI = inventoryGUI;
    }

    public static HandlerList getHandlerList() {
        return InventoryGUIEvent.handlerList;
    }

    @Override
    public HandlerList getHandlers() {
        return InventoryGUIEvent.handlerList;
    }

    public InventoryGUI getInventoryGUI() {
        return this.inventoryGUI;
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
