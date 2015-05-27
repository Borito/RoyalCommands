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

public class SCmdReload extends SubCommand<CmdPluginManager> {

    public SCmdReload(final RoyalCommands instance, final CmdPluginManager parent) {
        super(instance, parent, "reload", true, "Disables then enables a plugin", "<command> [plugin]", new String[0], new Short[]{CompletionType.PLUGIN.getShort()});
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] eargs, final CommandArguments ca) {
        if (eargs.length < 1) {
            cs.sendMessage(MessageColor.NEGATIVE + "Please provide the name of the plugin to reload!");
            return true;
        }
        final PluginManager pm = this.plugin.getServer().getPluginManager();
        final Plugin p = pm.getPlugin(eargs[0]);
        if (p == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "No such plugin!");
            return true;
        }
        pm.disablePlugin(p);
        pm.enablePlugin(p);
        cs.sendMessage(MessageColor.POSITIVE + "Reloaded " + MessageColor.NEUTRAL + p.getName() + MessageColor.POSITIVE + ".");
        return true;
    }
}
