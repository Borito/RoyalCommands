package org.royaldev.royalcommands.rcommands.trade.conversation.prompts;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.royaldev.royalcommands.MessageColor;

import java.util.ArrayList;
import java.util.List;

public class CollectCommands extends StringPrompt {

    private final List<String> commands = new ArrayList<>();

    private void saveCommands(final ConversationContext context) {
        context.setSessionData("commands", this.commands);
    }

    @Override
    public String getPromptText(final ConversationContext context) {
        if (context.getSessionData("commands") == null) {
            return MessageColor.POSITIVE + "Enter commands, without leading slashes, until you are finished. Once finished, type " + MessageColor.NEUTRAL + "done" + MessageColor.POSITIVE + ".";
        } else {
            return MessageColor.POSITIVE + "Command saved. Continue entering commands or type " + MessageColor.NEUTRAL + "done" + MessageColor.POSITIVE + ".";
        }
    }

    @Override
    public Prompt acceptInput(final ConversationContext context, final String input) {
        this.commands.add(MessageColor.NEUTRAL + "/" + input);
        this.saveCommands(context);
        return this;
    }
}
