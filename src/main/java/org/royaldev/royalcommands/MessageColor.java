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
            this.c = ChatColor.valueOf(custom.toUpperCase());
        } catch (IllegalArgumentException e) {
            this.c = def;
        }
    }

    @Override
    public String toString() {
        return this.c.toString();
    }

    public ChatColor getChatColor() {
        return this.c;
    }

}
