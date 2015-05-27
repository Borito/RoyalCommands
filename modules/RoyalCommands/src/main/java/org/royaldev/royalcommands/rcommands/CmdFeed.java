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
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdFeed extends BaseCommand {

    public CmdFeed(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (args.length < 1) {
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "You can't feed the console!");
                return true;
            }
            Player t = (Player) cs;
            t.sendMessage(MessageColor.POSITIVE + "You have fed yourself!");
            t.setFoodLevel(20);
            t.setSaturation(20F);
            return true;
        }
        Player t = this.plugin.getServer().getPlayer(args[0]);
        if (!this.ah.isAuthorized(cs, cmd, PermType.OTHERS)) {
            RUtils.dispNoPerms(cs);
            return true;
        }
        if (t == null || this.plugin.isVanished(t, cs)) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
            return true;
        }
        cs.sendMessage(MessageColor.POSITIVE + "You have fed " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
        t.sendMessage(MessageColor.POSITIVE + "You have been fed by " + MessageColor.NEUTRAL + cs.getName() + MessageColor.POSITIVE + "!");
        t.setFoodLevel(20);
        t.setSaturation(20F);
        return true;
    }
}
