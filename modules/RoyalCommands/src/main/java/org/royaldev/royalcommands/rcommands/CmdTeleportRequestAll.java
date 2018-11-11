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
import org.royaldev.royalcommands.rcommands.teleport.TeleportRequest;
import org.royaldev.royalcommands.rcommands.teleport.TeleportRequest.TeleportType;

@ReflectCommand
public class CmdTeleportRequestAll extends TabCommand {

    public CmdTeleportRequestAll(final RoyalCommands instance, final String name) {
        super(instance, name, true, new Short[]{});
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args, CommandArguments ca) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        Player p = (Player) cs;
        for (Player t : this.plugin.getServer().getOnlinePlayers()) {
            if (!RUtils.isTeleportAllowed(t) && !this.ah.isAuthorized(cs, "rcmds.tpoverride")) continue;
            if (t.equals(p)) continue;
            TeleportRequest.send(p, t, TeleportType.HERE, false);
        }
        p.sendMessage(MessageColor.POSITIVE + "You have sent a teleport request to all players.");
        return true;
    }
}
