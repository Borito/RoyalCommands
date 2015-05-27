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
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdSetLevel extends BaseCommand {

    public CmdSetLevel(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        if (!(cs instanceof Player) && args.length == 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }

        int lvl;
        try {
            lvl = Integer.valueOf(args[0]);
        } catch (NumberFormatException e) {
            cs.sendMessage(MessageColor.NEGATIVE + "The level must be a number!");
            return true;
        }

        Player t = this.plugin.getServer().getPlayer((args.length > 1) ? args[1] : cs.getName());
        if (t == null || this.plugin.isVanished(t, cs)) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
            return true;
        }
        t.setLevel(lvl);
        if (!cs.equals(t))
            cs.sendMessage(MessageColor.POSITIVE + "Set the level of " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + " to " + MessageColor.NEUTRAL + lvl + MessageColor.POSITIVE + ".");
        t.sendMessage(MessageColor.POSITIVE + "Your level has been set to " + MessageColor.NEUTRAL + lvl + MessageColor.POSITIVE + ".");
        return true;
    }
}
