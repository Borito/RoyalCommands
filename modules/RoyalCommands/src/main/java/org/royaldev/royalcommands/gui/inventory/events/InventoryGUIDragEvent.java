package org.royaldev.royalcommands.gui.inventory.events;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.gui.inventory.InventoryGUI;

import java.util.Set;

public class InventoryGUIDragEvent extends InventoryGUIPlayerEvent {

    private final ItemStack dragged;
    private final Set<Integer> slots;
    private final Set<Integer> rawSlots;

    public InventoryGUIDragEvent(final InventoryGUI inventoryGUI, final Player player, final ItemStack dragged, final Set<Integer> slots, final Set<Integer> rawSlots) {
        super(inventoryGUI, player);
        this.dragged = dragged;
        this.slots = slots;
        this.rawSlots = rawSlots;
    }

    public ItemStack getDragged() {
        return this.dragged;
    }

    public Set<Integer> getRawSlots() {
        return this.rawSlots;
    }

    public Set<Integer> getSlots() {
        return this.slots;
    }
}
