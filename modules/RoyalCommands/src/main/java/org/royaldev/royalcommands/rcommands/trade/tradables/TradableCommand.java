/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands.trade.tradables;

import org.bukkit.entity.Player;
import org.royaldev.royalcommands.rcommands.trade.Party;
import org.royaldev.royalcommands.rcommands.trade.Trade;

public class TradableCommand implements Tradable {

    private final Trade trade;
    private final String command;

    public TradableCommand(final Trade trade, final String command) {
        this.trade = trade;
        this.command = command;
    }

    /**
     * Always returns false. At the moment, a tradable command cannot be destroyed.
     *
     * @return false
     */
    @Override
    public boolean destroy() {
        return false;
    }

    /**
     * Trades a command. In this case, it's less of a trade and more of an agreed-upon action.
     *
     * @param from Party performing the command
     * @param to   Party requesting the command
     * @return If the trade was successful
     */
    @Override
    public boolean trade(final Party from, final Party to) {
        final Player p = this.trade.getPlayer(from);
        return p != null && p.performCommand(this.command);
    }
}
