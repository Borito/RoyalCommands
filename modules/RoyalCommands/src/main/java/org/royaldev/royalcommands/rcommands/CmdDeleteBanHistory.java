/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PlayerConfiguration;
import org.royaldev.royalcommands.configuration.PlayerConfigurationManager;

import java.util.ArrayList;
import java.util.List;

@ReflectCommand
public class CmdDeleteBanHistory extends BaseCommand {

    public CmdDeleteBanHistory(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (args.length < 2) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        final OfflinePlayer op = RUtils.getOfflinePlayer(args[0]);
        final PlayerConfiguration pcm = PlayerConfigurationManager.getConfiguration(op);
        if (!pcm.exists()) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player has never played before!");
            return true;
        }
        final int banToRemove;
        try {
            banToRemove = Integer.parseInt(args[1]) - 1;
        } catch (NumberFormatException e) {
            cs.sendMessage(MessageColor.NEGATIVE + "The ban number given was invalid!");
            return true;
        }
        List<String> prevBans = pcm.getStringList("prevbans");
        if (prevBans == null) prevBans = new ArrayList<>();
        if (prevBans.size() < 1) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player has no previous bans.");
            return true;
        }
        if (banToRemove > prevBans.size() - 1 || banToRemove < 0) {
            cs.sendMessage(MessageColor.NEGATIVE + "No such ban!");
            return true;
        }
        prevBans.remove(banToRemove);
        pcm.set("prevbans", prevBans);
        cs.sendMessage(MessageColor.POSITIVE + "Removed ban " + MessageColor.NEUTRAL + (banToRemove + 1) + MessageColor.POSITIVE + " from " + MessageColor.NEUTRAL + op.getName() + MessageColor.POSITIVE + ".");
        return true;
    }
}
