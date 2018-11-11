/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdKickAll extends TabCommand {

    public CmdKickAll(final RoyalCommands instance, final String name) {
        super(instance, name, true, new Short[]{});
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args, CommandArguments ca) {
        String kickreason = Config.kickMessage;
        if (args.length > 0) kickreason = RoyalCommands.getFinalArg(args, 0);
        kickreason = RUtils.colorize(kickreason);
        Player p = null;
        if (cs instanceof Player) p = (Player) cs;
        for (Player t : this.plugin.getServer().getOnlinePlayers()) {
            if (!t.equals(p)) RUtils.kickPlayer(t, cs, kickreason);
        }
        return true;
    }
}
