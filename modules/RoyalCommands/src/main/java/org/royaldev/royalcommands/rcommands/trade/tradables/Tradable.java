/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands.trade.tradables;

import org.royaldev.royalcommands.rcommands.trade.Party;

public interface Tradable {

    /**
     * Destroys the thing being traded after it has been traded.
     *
     * @return If the thing was destroyed
     */
    public boolean destroy();

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
