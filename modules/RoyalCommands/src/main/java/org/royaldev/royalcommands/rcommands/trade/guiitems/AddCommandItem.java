package org.royaldev.royalcommands.rcommands.trade.guiitems;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.gui.inventory.GUIItem;
import org.royaldev.royalcommands.rcommands.trade.Party;
import org.royaldev.royalcommands.rcommands.trade.Trade;

import java.util.Arrays;

public class AddCommandItem extends GUIItem {

    public AddCommandItem(final Trade trade, final Party party) {
        super(
            Material.COMMAND,
            MessageColor.RESET + "Add " + StringUtils.capitalize(party.name().toLowerCase()) + " Command",
            Arrays.asList(
                MessageColor.NEUTRAL + "Adds a command for the " + party.name().toLowerCase(),
                MessageColor.NEUTRAL + "to perform when the trade processes.",
                MessageColor.NEUTRAL + "The " + party.name().toLowerCase() + " is " + trade.getName(party) + "."
            )
        );
    }
}
