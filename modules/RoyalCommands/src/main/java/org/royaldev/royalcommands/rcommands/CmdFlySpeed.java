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
public class CmdFlySpeed extends BaseCommand {

    public CmdFlySpeed(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        Player p = (Player) cs;
        float flySpeed;
        try {
            flySpeed = Float.valueOf(args[0]);
        } catch (NumberFormatException e) {
            cs.sendMessage(MessageColor.NEGATIVE + "Please enter a valid number!");
            return true;
        }
        if (flySpeed < -1F || flySpeed > 1F) {
            cs.sendMessage(MessageColor.NEGATIVE + "Speed must be between -1 and 1! Default is 0.102.");
            return true;
        }
        cs.sendMessage(MessageColor.POSITIVE + "Set your fly speed to " + MessageColor.NEUTRAL + flySpeed + MessageColor.POSITIVE + ". Default is 0.102.");
        p.setFlySpeed(flySpeed);
        return true;
    }
}
