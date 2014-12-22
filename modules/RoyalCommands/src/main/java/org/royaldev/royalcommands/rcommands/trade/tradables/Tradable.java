package org.royaldev.royalcommands.rcommands.trade.tradables;

import org.royaldev.royalcommands.rcommands.trade.Party;

public interface Tradable {

    /**
     * Trades something. If this method is being called, it is safe to assume that the thing being traded has already
     * been provided by <code>from</code>. There is no need to check if <code>from</code> has the thing being traded in
     * most cases.
     *
     * @param from Party the trade is coming from.
     * @param to   Party the trade is going to.
     * @return If the trade was successful
     */
    public boolean trade(final Party from, final Party to);

}
