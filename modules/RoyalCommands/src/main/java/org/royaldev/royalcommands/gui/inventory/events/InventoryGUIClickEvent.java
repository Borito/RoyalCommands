package org.royaldev.royalcommands.gui.inventory.events;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.gui.inventory.ClickHandler;
import org.royaldev.royalcommands.gui.inventory.InventoryGUI;

public class InventoryGUIClickEvent extends InventoryGUIEvent {

    private final InventoryGUI inventoryGUI;
    private final Player clicker;
    private final ClickType clickType;
    private final ItemStack clicked;
    private final ClickHandler clickHandler;
    private final InventoryAction action;
    private final int slot;
    private final int rawSlot;

    public InventoryGUIClickEvent(final InventoryGUI gui, final Player clicker, final ClickType type, final ItemStack clicked, final InventoryAction action, final int slot, final int rawSlot) {
        this.inventoryGUI = gui;
        this.clicker = clicker;
        this.clickType = type;
        this.clicked = clicked;
        this.action = action;
        this.rawSlot = rawSlot;
        this.clickHandler = this.inventoryGUI.getClickHandler(this.clicked);
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

    public Player getClicker() {
        return this.clicker;
    }

    public InventoryGUI getInventoryGUI() {
        return this.inventoryGUI;
    }

    public int getRawSlot() {
        return this.rawSlot;
    }

    public int getSlot() {
        return this.slot;
    }
}
