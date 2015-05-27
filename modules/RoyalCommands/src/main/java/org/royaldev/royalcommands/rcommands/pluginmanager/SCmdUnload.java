/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands.pluginmanager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.CmdPluginManager;
import org.royaldev.royalcommands.rcommands.SubCommand;

import java.util.List;

public class SCmdUnload extends SubCommand<CmdPluginManager> {

    public SCmdUnload(final RoyalCommands instance, final CmdPluginManager parent) {
        super(instance, parent, "unload", true, "Unloads a plugin and removes it from the plugin list", "<command> [plugin]", new String[0], new Short[]{CompletionType.PLUGIN.getShort()});
    }

    @Override
    public boolean runCommand(final CommandSender cs, Command cmd, String label, String[] eargs, CommandArguments ca) {
        if (eargs.length < 1) {
            cs.sendMessage(MessageColor.NEGATIVE + "Please provide the name of the plugin to unload!");
            return true;
        }
        final PluginManager pm = this.plugin.getServer().getPluginManager();
        final Plugin p = pm.getPlugin(eargs[0]);
        if (p == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "No such plugin!");
            return true;
        }
        List<String> depOnBy = this.getParent().getDependedOnBy(p);
        if (!depOnBy.isEmpty()) {
            cs.sendMessage(MessageColor.NEGATIVE + "Could not unload " + MessageColor.NEUTRAL + p.getName() + MessageColor.NEGATIVE + " because it is depended on by the following:");
            StringBuilder sb = new StringBuilder();
            for (String dep : depOnBy) {
                sb.append(MessageColor.NEUTRAL);
                sb.append(dep);
                sb.append(MessageColor.RESET);
                sb.append(", ");
            }
            cs.sendMessage(sb.substring(0, sb.length() - 4)); // "&r, " = 4
            return true;
        }
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                SCmdUnload.this.getParent().unregisterAllPluginCommands(p.getName());
                HandlerList.unregisterAll(p);
                SCmdUnload.this.plugin.getServer().getScheduler().cancelTasks(p);
                pm.disablePlugin(p);
                SCmdUnload.this.getParent().removePluginFromList(p);
                cs.sendMessage(MessageColor.POSITIVE + "Unloaded " + MessageColor.NEUTRAL + p.getName() + MessageColor.POSITIVE + ".");
            }
        };
        cs.sendMessage(MessageColor.POSITIVE + "Unloading...");
        this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, r);
        return true;
    }
}
