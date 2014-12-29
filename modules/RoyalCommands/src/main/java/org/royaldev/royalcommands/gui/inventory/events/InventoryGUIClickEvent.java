package org.royaldev.royalcommands.gui.inventory.events;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.gui.inventory.ClickHandler;
import org.royaldev.royalcommands.gui.inventory.InventoryGUI;

public class InventoryGUIClickEvent extends InventoryGUIPlayerEvent {

    private final ClickType clickType;
    private final ItemStack clicked;
    private final ClickHandler clickHandler;
    private final InventoryAction action;
    private final int slot;
    private final int rawSlot;

    public InventoryGUIClickEvent(final InventoryGUI inventoryGUI, final Player clicker, final ClickType type, final ItemStack clicked, final InventoryAction action, final int slot, final int rawSlot) {
        super(inventoryGUI, clicker);
        this.clickType = type;
        this.clicked = clicked;
        this.action = action;
        this.rawSlot = rawSlot;
        this.clickHandler = this.getInventoryGUI().getClickHandler(this.clicked);
        this.slot = slot;
    }

    public InventoryAction getAction() {
        return this.action;
    }

    public ClickHandler getClickHandler() {
        return this.clickHandler;
    }

    public ClickType getClickType() {
        return this.clickType;
    }

    public ItemStack getClicked() {
        return this.clicked;
    }

    public int getRawSlot() {
        return this.rawSlot;
    }

    public int getSlot() {
        return this.slot;
    }

    @Override
    public String toString() {
        return String.format(
            "%s@%s[inventoryGUI=%s, player=%s, clickType=%s, clicked=%s, clickHandler=%s, action=%s, slot=%s, rawSlot=%s]",
            this.getClass().getName(),
            this.hashCode(),
            this.getInventoryGUI(),
            this.getPlayer(),
            this.clickType,
            this.clicked,
            this.clickHandler,
            this.action,
            this.slot,
            this.rawSlot
        );
    }
}
