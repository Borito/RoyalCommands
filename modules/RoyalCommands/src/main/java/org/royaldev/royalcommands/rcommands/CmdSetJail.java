/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;

import java.io.File;
import java.io.IOException;

@ReflectCommand
public class CmdSetJail extends BaseCommand {

    public CmdSetJail(final RoyalCommands instance, final String name) {
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
        float locYaw = p.getLocation().getYaw();
        float locPitch = p.getLocation().getPitch();
        String locW = p.getWorld().getName();

        File pconfl = new File(this.plugin.getDataFolder() + File.separator + "jails.yml");
        if (!pconfl.exists()) {
            try {
                if (!pconfl.createNewFile()) {
                    cs.sendMessage(MessageColor.NEGATIVE + "Could not create a new file!");
                    return true;
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        FileConfiguration pconf = YamlConfiguration.loadConfiguration(pconfl);
        pconf.set("jails." + args[0] + ".set", true);
        pconf.set("jails." + args[0] + ".x", locX);
        pconf.set("jails." + args[0] + ".y", locY);
        pconf.set("jails." + args[0] + ".z", locZ);
        pconf.set("jails." + args[0] + ".pitch", locPitch);
        pconf.set("jails." + args[0] + ".yaw", locYaw);
        pconf.set("jails." + args[0] + ".w", locW);
        try {
            pconf.save(pconfl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        p.sendMessage(MessageColor.POSITIVE + "Jail \"" + MessageColor.NEUTRAL + args[0] + MessageColor.POSITIVE + "\" set.");
        return true;
    }
}
