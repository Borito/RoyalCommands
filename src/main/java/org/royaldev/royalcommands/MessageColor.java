package org.royaldev.royalcommands;

import org.bukkit.ChatColor;

public enum MessageColor {

    NEGATIVE(Config.negativeChatColor, ChatColor.RED),
    NEUTRAL(Config.neutralChatColor, ChatColor.GRAY),
    POSITIVE(Config.positiveChatColor, ChatColor.BLUE),
    RESET(Config.resetChatColor, ChatColor.RESET);

    private ChatColor c;

    MessageColor(String custom, ChatColor def) {
        try {
            c = ChatColor.valueOf(custom.toUpperCase());
        } catch (IllegalArgumentException e) {
            c = def;
        }
    }

    @Override
    public String toString() {
        return c.toString();
    }

    public ChatColor getChatColor() {
        return c;
    }

}
