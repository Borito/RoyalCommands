/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands;

import java.text.DecimalFormat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdMemory extends BaseCommand {

    public CmdMemory(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {

        double memUsed = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576D;
        double memMax = Runtime.getRuntime().maxMemory() / 1048576D;
        double memFree = memMax - memUsed;
        double percfree = (100D / memMax) * memFree;
        ChatColor color;
        if (percfree >= 60D) color = ChatColor.GREEN;
        else if (percfree >= 35D) color = ChatColor.YELLOW;
        else color = ChatColor.RED;
        DecimalFormat df = new DecimalFormat("00.00");
        cs.sendMessage(color + df.format(percfree) + "% " + MessageColor.POSITIVE + "free (" + color + memUsed + MessageColor.POSITIVE + " MB/" + memMax + " MB)");
        return true;
    }
}
