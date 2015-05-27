/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands.trade;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.gui.inventory.InventoryGUI;
import org.royaldev.royalcommands.gui.inventory.events.InventoryGUIClickEvent;
import org.royaldev.royalcommands.gui.inventory.events.InventoryGUIDragEvent;
import org.royaldev.royalcommands.gui.inventory.events.InventoryGUIPlayerEvent;

import java.util.List;

public class TradeListener implements Listener {

    private Trade getTrade(final InventoryGUIPlayerEvent e) {
        return this.getTradeFor(Trade.getTradesFor(e.getPlayer().getUniqueId()), e.getInventoryGUI());
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

    private boolean shouldReturn(final InventoryGUIPlayerEvent igpe) {
        final InventoryGUI ig = igpe.getInventoryGUI();
        final Player p = igpe.getPlayer();
        final Trade t = this.getTrade(igpe);
        if (t == null || igpe instanceof InventoryGUIClickEvent && !this.isInInventoryGUI((InventoryGUIClickEvent) igpe, ig)) {
            return true;
        }
        final Party party = t.get(p.getUniqueId());
        return party == null;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void freeze(final InventoryGUIClickEvent e) {
        if (this.shouldReturn(e)) return;
        final Trade t = this.getTrade(e);
        final Party p = t.get(e.getPlayer().getUniqueId());
        // If he hasn't accepted or the item has a command
        if (!t.hasAccepted(p) || e.getClickHandler() != null) return;
        e.setCancelled(true);
        e.getPlayer().sendMessage(MessageColor.NEGATIVE + "You cannot modify a trade once you have accepted it.");
    }

    @EventHandler(ignoreCancelled = true)
    public void onClick(final InventoryGUIClickEvent e) {
        if (this.shouldReturn(e)) return;
        final Player p = e.getPlayer();
        final Trade t = this.getTrade(e);
        final Party party = t.get(p.getUniqueId());
        switch (e.getAction()) {
            case NOTHING:
            case DROP_ALL_CURSOR:
            case DROP_ALL_SLOT:
            case DROP_ONE_CURSOR:
            case DROP_ONE_SLOT:
            case PICKUP_ALL:
            case PICKUP_HALF:
            case PICKUP_ONE:
            case PICKUP_SOME:
            case PLACE_ALL:
            case PLACE_ONE:
            case PLACE_SOME:
                break;
            default:
                return;
        }
        if (e.getSlot() % 9 == 4 && e.getAction() == InventoryAction.PICKUP_ALL) {
            return; // if they're using an item in the middle, allow it
        }
        if (!party.canAccessSlot(e.getSlot())) {
            e.setCancelled(true);
            return; // don't change acceptance if nothing happens
        }
        t.setAcceptance(party.getOther(), false); // decline if changes are made
    }

    @EventHandler(ignoreCancelled = true)
    public void onDrag(final InventoryGUIDragEvent e) {
        if (this.shouldReturn(e)) return;
        final Trade t = this.getTrade(e);
        final Party p = t.get(e.getPlayer().getUniqueId());
        for (final int slot : e.getSlots()) {
            if (p.canAccessSlot(slot)) continue;
            e.setCancelled(true);
            break;
        }
    }

}
