package org.royaldev.royalcommands.rcommands.trade.guiitems;

import org.bukkit.Material;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.gui.inventory.GUIItem;
import org.royaldev.royalcommands.rcommands.trade.Party;
import org.royaldev.royalcommands.rcommands.trade.Trade;

import java.util.Arrays;

public class ToggleAcceptanceItem extends GUIItem {

    public ToggleAcceptanceItem(final Trade trade) {
        super(
            Material.BOOK_AND_QUILL,
            MessageColor.RESET + "Toggle Acceptance",
            Arrays.asList(
                MessageColor.NEUTRAL + "This toggles your acceptance of the trade.",
                MessageColor.NEUTRAL + "Once both sides have accepted,",
                MessageColor.NEUTRAL + "the trade will process.",
                trade.getTradeStatusLore(Party.TRADER),
                trade.getTradeStatusLore(Party.TRADEE)
            )
        );
    }
}
