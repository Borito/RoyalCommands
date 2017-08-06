/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands;

import org.bukkit.ChatColor;

public enum MessageColor {

    NEGATIVE(ChatColor.RED),
    NEUTRAL(ChatColor.GRAY),
    POSITIVE(ChatColor.BLUE),
    RESET(ChatColor.RESET);

    private final ChatColor def;

    MessageColor(final ChatColor def) {
        this.def = def;
    }

    public ChatColor cc() {
        return this.getChatColor();
    }

    protected ChatColor byStringOrDefault(final String s) {
        try {
            return ChatColor.valueOf(s.toUpperCase());
        } catch (final IllegalArgumentException ex) {
            return this.def;
        }
    }

    public ChatColor getChatColor() {
        final String s;
        switch (this) {
            case NEGATIVE:
                s = Config.negativeChatColor;
                break;
            case NEUTRAL:
                s = Config.neutralChatColor;
                break;
            case POSITIVE:
                s = Config.positiveChatColor;
                break;
            case RESET:
                s = Config.resetChatColor;
                break;
            default:
                s = null;
        }
        return this.byStringOrDefault(s);
    }

    @Override
    public String toString() {
        return this.getChatColor().toString();
    }

}
