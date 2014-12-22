package org.royaldev.royalcommands.rcommands.trade;

import org.bukkit.inventory.Inventory;

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

    public int getNextFreeSlot(final Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            if (!this.canAccessSlot(i) || inventory.getItem(i) != null) continue;
            return i;
        }
        return -1;
    }
}
