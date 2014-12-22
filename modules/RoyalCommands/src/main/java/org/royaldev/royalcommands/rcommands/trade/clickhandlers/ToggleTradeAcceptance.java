package org.royaldev.royalcommands.rcommands.trade.clickhandlers;

import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.gui.inventory.ClickEvent;
import org.royaldev.royalcommands.gui.inventory.ClickHandler;
import org.royaldev.royalcommands.rcommands.trade.Trade;
import org.royaldev.royalcommands.rcommands.trade.Party;

public class ToggleTradeAcceptance implements ClickHandler {

    private final Trade trade;

    public ToggleTradeAcceptance(final Trade trade) {
        this.trade = trade;
    }

    @Override
    public boolean onClick(final ClickEvent clickEvent) {
        this.trade.toggleAcceptance(clickEvent.getClicker().getUniqueId());
        final ItemStack clicked = clickEvent.getClickedItem();
        this.trade.getInventoryGUI().updateItemStack(this.trade.getInventoryGUI().setItemMeta(
            clicked,
            null,
            MessageColor.NEUTRAL + "This toggles your acceptance of the trade.",
            MessageColor.NEUTRAL + "Once both sides have accepted, the trade will process.",
            this.trade.getTradeStatusLore(Party.TRADER),
            this.trade.getTradeStatusLore(Party.TRADEE)
        ));
        return false;
    }
}
