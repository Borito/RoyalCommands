package org.royaldev.royalcommands.rcommands.trade;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.royaldev.royalcommands.gui.inventory.InventoryGUI;
import org.royaldev.royalcommands.gui.inventory.InventoryGUIListener;

import java.util.List;

public class TradeListener extends InventoryGUIListener {

    private Trade getTradeFor(final List<Trade> trades, final InventoryGUI ig) {
        for (final Trade trade : trades) {
            if (trade.getInventoryGUI().equals(ig)) return trade;
        }
        return null;
    }

    @EventHandler(ignoreCancelled = true)
    public void onClick(final InventoryClickEvent e) {
        final InventoryGUI ig = this.getInventoryGUI(e.getInventory());
        final Player p = this.getPlayer(e.getWhoClicked());
        final Trade t = this.getTradeFor(Trade.getTradesFor(p.getUniqueId()), ig);
        if (t == null) return;
        if (e.getRawSlot() > ig.getBase().getSize()) return;
        final Party party = t.get(p.getUniqueId());
        if (party == null) return;
        if (e.getSlot() % 9 == 4 && e.getAction() == InventoryAction.PICKUP_ALL) {
            return; // if they're using an item in the middle, allow it
        }
        if (!party.canAccessSlot(e.getSlot())) {
            e.setCancelled(true);
            return; // don't change acceptance if nothing happens
        }
        if (t.hasAccepted(party.getOther())) { // decline if changes are made
            t.toggleAcceptance(party.getOther());
        }
    }

}
