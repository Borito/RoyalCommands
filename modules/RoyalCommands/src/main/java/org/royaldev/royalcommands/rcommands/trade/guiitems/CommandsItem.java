package org.royaldev.royalcommands.rcommands.trade.guiitems;

import org.bukkit.Material;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.gui.inventory.GUIItem;

import java.util.List;

public class CommandsItem extends GUIItem {

    public CommandsItem(final List<String> commands) {
        super(
            Material.ENCHANTMENT_TABLE,
            MessageColor.RESET + "Commands",
            commands
        );
    }
}
