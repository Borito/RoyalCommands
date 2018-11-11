/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdKillAll extends TabCommand {

    public CmdKillAll(final RoyalCommands instance, final String name) {
        super(instance, name, true, new Short[]{});
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args, CommandArguments ca) {
        for (final Player p : this.plugin.getServer().getOnlinePlayers()) {
            if (this.plugin.isVanished(p, cs) || this.ah.isAuthorized(p, cmd, PermType.EXEMPT)) continue;
            if (cs instanceof Player) {
                if (p == cs) continue;
            }
            p.setHealth(0);
        }
        cs.sendMessage(MessageColor.POSITIVE + "You have killed all the players.");
        return true;
    }
}
