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
     * Trades a command. In this case, it's less of a trade and more of an agreed-upon action.
     *
     * @param from Party requesting the command
     * @param to   Party performing the command
     * @return If the trade was successful
     */
    @Override
    public boolean trade(final Party from, final Party to) {
        final Player p = this.trade.getPlayer(to);
        return p != null && p.performCommand(this.command);
    }
}
