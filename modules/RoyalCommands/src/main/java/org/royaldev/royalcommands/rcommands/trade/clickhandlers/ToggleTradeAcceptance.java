package org.royaldev.royalcommands.rcommands.trade.clickhandlers;

import org.royaldev.royalcommands.gui.inventory.ClickEvent;
import org.royaldev.royalcommands.gui.inventory.ClickHandler;
import org.royaldev.royalcommands.rcommands.trade.Trade;

public class ToggleTradeAcceptance implements ClickHandler {

    private final Trade trade;

    public ToggleTradeAcceptance(final Trade trade) {
        this.trade = trade;
    }

    @Override
    public boolean onClick(final ClickEvent clickEvent) {
        this.trade.toggleAcceptance(clickEvent.getClicker().getUniqueId());
        this.trade.updateAcceptButton();
        return false;
    }
}
