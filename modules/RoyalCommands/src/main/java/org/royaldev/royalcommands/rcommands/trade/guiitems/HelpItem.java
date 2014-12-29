package org.royaldev.royalcommands.rcommands.trade.guiitems;

import org.bukkit.Material;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.gui.inventory.GUIItem;

import java.util.Arrays;

public class HelpItem extends GUIItem {

    public HelpItem() {
        super(
            Material.PAPER,
            MessageColor.RESET + "Help",
            Arrays.asList(
                MessageColor.NEUTRAL + "Gives you a book about trades."
            )
        );
    }
}
