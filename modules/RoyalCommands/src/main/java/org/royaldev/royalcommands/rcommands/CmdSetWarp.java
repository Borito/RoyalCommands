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
import org.royaldev.royalcommands.configuration.Configuration;

@ReflectCommand
public class CmdSetWarp extends BaseCommand {

    public CmdSetWarp(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {

        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }

        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        Player p = (Player) cs;

        double locX = p.getLocation().getX();
        double locY = p.getLocation().getY();
        double locZ = p.getLocation().getZ();
        Float locYaw = p.getLocation().getYaw();
        Float locPitch = p.getLocation().getPitch();
        String locW = p.getWorld().getName();
        String name = args[0].toLowerCase();

        Configuration warps = Configuration.getConfiguration("warps.yml");
        if (!warps.exists()) warps.createFile();
        warps.set("warps." + name + ".set", true);
        warps.set("warps." + name + ".x", locX);
        warps.set("warps." + name + ".y", locY);
        warps.set("warps." + name + ".z", locZ);
        warps.set("warps." + name + ".pitch", locPitch);
        warps.set("warps." + name + ".yaw", locYaw);
        warps.set("warps." + name + ".w", locW);
        p.sendMessage(MessageColor.POSITIVE + "Warp \"" + MessageColor.NEUTRAL + name + MessageColor.POSITIVE + "\" set.");
        return true;
    }
}
