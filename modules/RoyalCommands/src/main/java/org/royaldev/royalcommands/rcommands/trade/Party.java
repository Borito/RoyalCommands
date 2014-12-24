package org.royaldev.royalcommands.rcommands.trade;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.royaldev.royalcommands.gui.inventory.GUIHolder;

public enum Party {
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

    public abstract boolean canAccessSlot(final int slot);

    public abstract Party getOther();

    public void closeTrade(final Trade trade) {
        if (!this.hasTradeOpen(trade)) return;
        final Player player = trade.getPlayer(this);
        if (player == null) return;
        player.closeInventory();
    }

    public int getNextFreeSlot(final Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            if (!this.canAccessSlot(i) || inventory.getItem(i) != null) continue;
            return i;
        }
        return -1;
    }

    public boolean hasTradeOpen(final Trade trade) {
        final Player player = trade.getPlayer(this);
        if (player == null) return false;
        final InventoryHolder ih = player.getOpenInventory().getTopInventory().getHolder();
        return ih instanceof GUIHolder && ((GUIHolder) ih).getInventoryGUI().equals(trade.getInventoryGUI());
    }
}
