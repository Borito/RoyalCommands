package org.royaldev.royalcommands.rcommands.trade.clickhandlers;

import org.bukkit.Material;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.gui.inventory.ClickEvent;
import org.royaldev.royalcommands.gui.inventory.ClickHandler;
import org.royaldev.royalcommands.rcommands.trade.Party;
import org.royaldev.royalcommands.rcommands.trade.Trade;
import org.royaldev.royalcommands.rcommands.trade.conversation.prompts.CollectCommands;

import java.util.List;

public class AddPartyCommand implements ClickHandler {

    private final Trade trade;
    private final ConversationFactory cf;

    public AddPartyCommand(final Trade trade, final Party party) {
        this.trade = trade;
        this.cf = new ConversationFactory(RoyalCommands.getInstance())
            .withEscapeSequence("done")
            .withFirstPrompt(new CollectCommands())
            .withLocalEcho(false)
            .addConversationAbandonedListener(new ConversationAbandonedListener() {
                @Override
                public void conversationAbandoned(final ConversationAbandonedEvent event) {
                    //noinspection unchecked
                    final List<String> commands = (List<String>) event.getContext().getSessionData("commands");
                    if (commands == null || commands.isEmpty()) return;
                    AddPartyCommand.this.trade.addItem(
                        new RemoveItem(trade),
                        party,
                        Material.ENCHANTMENT_TABLE,
                        MessageColor.RESET + "Commands",
                        commands.toArray(new String[commands.size()])
                    );
                }
            });
    }

    @Override
    public boolean onClick(final ClickEvent clickEvent) {
        final Player p = clickEvent.getClicker();
        p.closeInventory();
        this.cf.buildConversation(p).begin();
        return false;
    }
}
