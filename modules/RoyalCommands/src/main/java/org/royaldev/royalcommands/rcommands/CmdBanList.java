/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands;

import java.util.Set;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdBanList extends TabCommand {

    public CmdBanList(final RoyalCommands instance, final String name) {
        super(instance, name, true, new Short[]{});
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args, CommandArguments ca) {
        final Set<OfflinePlayer> banList = this.plugin.getServer().getBannedPlayers();
        if (banList.isEmpty()) {
            cs.sendMessage(MessageColor.NEGATIVE + "There are no banned players!");
            return true;
        }
        cs.sendMessage(MessageColor.POSITIVE + "There are " + MessageColor.NEUTRAL + banList.size() + MessageColor.POSITIVE + " banned players:");
        final StringBuilder sb = new StringBuilder();
        for (OfflinePlayer op : banList) {
            sb.append(MessageColor.NEUTRAL);
            sb.append(op.getName());
            sb.append(MessageColor.RESET);
            sb.append(", ");
        }
        cs.sendMessage(sb.substring(0, sb.length() - 4));
        return true;
    }
}
