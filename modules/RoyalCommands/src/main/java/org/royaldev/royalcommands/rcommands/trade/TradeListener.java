package org.royaldev.royalcommands.rcommands.trade;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.gui.inventory.InventoryGUI;
import org.royaldev.royalcommands.gui.inventory.events.InventoryGUIClickEvent;

import java.util.List;

public class TradeListener implements Listener {

    private Trade getTrade(final InventoryGUIClickEvent e) {
        return this.getTradeFor(Trade.getTradesFor(e.getClicker().getUniqueId()), e.getInventoryGUI());
    }

    private Trade getTradeFor(final List<Trade> trades, final InventoryGUI ig) {
        for (final Trade trade : trades) {
            if (trade.getInventoryGUI().equals(ig)) return trade;
        }
        return null;
    }

    private boolean isInInventoryGUI(final InventoryGUIClickEvent e, final InventoryGUI ig) {
        return e.getRawSlot() <= ig.getBase().getSize();
    }

    @EventHandler
    public void freeze(final InventoryGUIClickEvent e) {
        final InventoryGUI ig = e.getInventoryGUI();
        if (!this.isInInventoryGUI(e, ig)) return;
        final Trade t = this.getTrade(e);
        if (t == null) return;
        final Party p = t.get(e.getClicker().getUniqueId());
        if (!t.hasAccepted(p)) return;
        e.setCancelled(true);
        e.getClicker().sendMessage(MessageColor.NEGATIVE + "You cannot modify a trade once you have accepted it.");
    }

    @EventHandler(ignoreCancelled = true)
    public void onClick(final InventoryGUIClickEvent e) {
        final InventoryGUI ig = e.getInventoryGUI();
        final Player p = e.getClicker();
        final Trade t = this.getTrade(e);
        if (t == null) return;
        if (!this.isInInventoryGUI(e, ig)) return;
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
