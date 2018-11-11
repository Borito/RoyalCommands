/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PlayerConfiguration;
import org.royaldev.royalcommands.configuration.PlayerConfigurationManager;

@ReflectCommand
public class CmdSeen extends TabCommand {

    public CmdSeen(final RoyalCommands instance, final String name) {
        super(instance, name, true, new Short[]{});
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args, CommandArguments ca) {
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        final OfflinePlayer t = RUtils.getOfflinePlayer(args[0]);
        if (t.isOnline() && !this.plugin.isVanished((Player) t, cs)) {
            cs.sendMessage(MessageColor.POSITIVE + "The player " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + " was last seen " + MessageColor.NEUTRAL + "now" + MessageColor.POSITIVE + ".");
            return true;
        }
        final PlayerConfiguration pcm = PlayerConfigurationManager.getConfiguration(t);
        if (!pcm.exists()) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player doesn't exist!");
            return true;
        }
        if (pcm.get("seen") == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "I don't know when that player was last seen!");
            return true;
        }
        final long seen = pcm.getLong("seen");
        if (seen < 1L) {
            cs.sendMessage(MessageColor.NEGATIVE + "I don't know when that player was last seen!");
            return true;
        }
        final String lastseen = RUtils.formatDateDiff(seen);
        cs.sendMessage(MessageColor.POSITIVE + "The player " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + " was last seen" + MessageColor.NEUTRAL + lastseen + MessageColor.POSITIVE + " ago.");
        return true;
    }
}
