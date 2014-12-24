package org.royaldev.royalcommands.rcommands.trade.clickhandlers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.gui.inventory.ClickEvent;
import org.royaldev.royalcommands.gui.inventory.ClickHandler;
import org.royaldev.royalcommands.tools.BookMaker;

public class GiveHelpBook implements ClickHandler {

    @Override
    public boolean onClick(final ClickEvent clickEvent) {
        final Player p = clickEvent.getClicker();
        final ItemStack book = BookMaker.createBook(Config.tradeHelp);
        if (book == null) {
            p.sendMessage(MessageColor.NEGATIVE + "The help book is not properly configured!");
            return false;
        }
        p.getInventory().addItem(book);
        return false;
    }
}
