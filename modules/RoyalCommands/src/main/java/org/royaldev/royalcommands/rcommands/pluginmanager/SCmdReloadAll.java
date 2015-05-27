/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands.pluginmanager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.CmdPluginManager;
import org.royaldev.royalcommands.rcommands.SubCommand;

public class SCmdReloadAll extends SubCommand<CmdPluginManager> {

    public SCmdReloadAll(final RoyalCommands instance, final CmdPluginManager parent) {
        super(instance, parent, "reloadall", true, "Reloads every plugin", "<command>", new String[0], new Short[0]);
    }

    @Override
    public boolean runCommand(final CommandSender cs, Command cmd, String label, String[] eargs, CommandArguments ca) {
        final PluginManager pm = this.plugin.getServer().getPluginManager();
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                for (Plugin p : pm.getPlugins()) {
                    pm.disablePlugin(p);
                    pm.enablePlugin(p);
                }
                cs.sendMessage(MessageColor.POSITIVE + "Reloaded all plugins!");
            }
        };
        this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, r);
        return true;
    }
}
