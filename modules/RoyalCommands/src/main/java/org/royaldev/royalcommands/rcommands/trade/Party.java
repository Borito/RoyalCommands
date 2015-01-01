package org.royaldev.royalcommands.rcommands.trade;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.royaldev.royalcommands.gui.inventory.GUIHolder;

/**
 * Represents the parties in a {@link Trade}.
 */
public enum Party {
    /**
     * The person that did not initiate the trade (the recipient).
     */
    TRADEE {
        @Override
        public boolean canAccessSlot(final int slot) {
            return slot % 9 >= 5;
        }

        @Override
        public Party getOther() {
            return TRADER;
        }
    },
    /**
     * The person that initiated the trade.
     */
    TRADER {
        @Override
        public boolean canAccessSlot(final int slot) {
            return slot % 9 <= 3;
        }

        @Override
        public Party getOther() {
            return TRADEE;
        }
    };

    /**
     * Checks to see if this Party can access the given slot in an inventory.
     *
     * @param slot Slot to check
     * @return true if it can be accessed, false if otherwise
     */
    public abstract boolean canAccessSlot(final int slot);

    /**
     * Gets the other party (the one that isn't this one).
     *
     * @return Other party
     */
    public abstract Party getOther();

    /**
     * Closes the given trade for this Party, if it is open.
     *
     * @param trade Trade to close
     */
    public void closeTrade(final Trade trade) {
        if (!this.hasTradeOpen(trade)) return;
        final Player player = trade.getPlayer(this);
        if (player == null) return;
        player.closeInventory();
    }

    /**
     * Gets the next open slot for this Party in an inventory.
     *
     * @param inventory Inventory to check
     * @return Slot or -1 if no slot is open
     */
    public int getNextFreeSlot(final Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            if (!this.canAccessSlot(i) || inventory.getItem(i) != null) continue;
            return i;
        }
        return -1;
    }

    /**
     * Checks to see if this Party has the given trade open.
     *
     * @param trade Trade to check
     * @return true if the trade inventory is open, false if otherwise
     */
    public boolean hasTradeOpen(final Trade trade) {
        final Player player = trade.getPlayer(this);
        if (player == null) return false;
        final InventoryHolder ih = player.getOpenInventory().getTopInventory().getHolder();
        return ih instanceof GUIHolder && ((GUIHolder) ih).getInventoryGUI().equals(trade.getInventoryGUI());
    }
}
