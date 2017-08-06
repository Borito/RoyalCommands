/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands.trade.clickhandlers;

import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.gui.inventory.ClickEvent;
import org.royaldev.royalcommands.gui.inventory.ClickHandler;
import org.royaldev.royalcommands.rcommands.trade.Party;
import org.royaldev.royalcommands.rcommands.trade.Trade;
import org.royaldev.royalcommands.shaded.mkremins.fanciful.FancyMessage;
import org.royaldev.royalcommands.wrappers.player.MemoryRPlayer;
import org.royaldev.royalcommands.wrappers.player.RPlayer;

public class RemindOtherParty implements ClickHandler {

    private final Trade trade;

    public RemindOtherParty(final Trade trade) {
        this.trade = trade;
    }

    @Override
    public boolean onClick(final ClickEvent clickEvent) {
        final Party party = this.trade.get(clickEvent.getClicker().getUniqueId());
        if (party == null) return false;
        final Party remind = party.getOther();
        final RPlayer rp = MemoryRPlayer.getRPlayer(this.trade.get(remind));
        final Player remindPlayer = rp.getPlayer();
        if (remindPlayer == null) return false;
        remindPlayer.sendMessage(MessageColor.NEUTRAL + clickEvent.getClicker().getName() + MessageColor.POSITIVE + " would like you to check your mutual trade.");
        new FancyMessage("To do so, click ")
            .color(MessageColor.POSITIVE.cc())
            .then("here")
            .color(MessageColor.NEUTRAL.cc())
            .command("/trade " + clickEvent.getClicker().getName())
            .then(".")
            .color(MessageColor.POSITIVE.cc())
            .send(remindPlayer);
        return false;
    }
}
