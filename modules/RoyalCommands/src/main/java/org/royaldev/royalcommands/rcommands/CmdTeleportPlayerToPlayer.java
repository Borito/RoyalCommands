/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.wrappers.player.MemoryRPlayer;
import org.royaldev.royalcommands.wrappers.player.RPlayer;

@ReflectCommand
public class CmdTeleportPlayerToPlayer extends TabCommand {

    public CmdTeleportPlayerToPlayer(final RoyalCommands instance, final String name) {
        super(instance, name, true, new Short[]{CompletionType.ONLINE_PLAYER.getShort(), CompletionType.ONLINE_PLAYER.getShort()});
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args, CommandArguments ca) {
        if (args.length < 2) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        final Player teleportee = this.plugin.getServer().getPlayer(args[0]);
        final Player destination = this.plugin.getServer().getPlayer(args[1]);
        if (teleportee == null || destination == null || this.plugin.isVanished(teleportee, cs) || this.plugin.isVanished(destination, cs)) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
            return true;
        }
        if (!RUtils.isTeleportAllowed(teleportee) && !this.ah.isAuthorized(cs, "rcmds.tpoverride")) {
            cs.sendMessage(MessageColor.NEGATIVE + "The player " + MessageColor.NEUTRAL + teleportee.getName() + MessageColor.NEGATIVE + " has teleportation off!");
            return true;
        }
        if (!RUtils.isTeleportAllowed(destination) && !this.ah.isAuthorized(cs, "rcmds.tpoverride")) {
            cs.sendMessage(MessageColor.NEGATIVE + "The player " + MessageColor.NEUTRAL + destination.getName() + MessageColor.NEGATIVE + " has teleportation off!");
            return true;
        }
        final RPlayer rp = MemoryRPlayer.getRPlayer(teleportee);
        final String error = rp.getTeleporter().teleport(destination);
        if (!error.isEmpty()) {
            cs.sendMessage(MessageColor.NEGATIVE + error);
            return true;
        }
        cs.sendMessage(MessageColor.POSITIVE + "You have teleported " + MessageColor.NEUTRAL + teleportee.getName() + MessageColor.POSITIVE + " to " + MessageColor.NEUTRAL + destination.getName() + MessageColor.POSITIVE + ".");
        return true;
    }
}
