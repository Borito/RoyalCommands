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

import java.util.Map;
import java.util.Map.Entry;

public class SCmdCommands extends SubCommand<CmdPluginManager> {

    public SCmdCommands(final RoyalCommands instance, final CmdPluginManager parent) {
        super(instance, parent, "commands", true, "Lists all registered commands and their description of a plugin", "<command> [plugin]", new String[]{"command"}, new Short[]{CompletionType.PLUGIN.getShort()});
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] eargs, final CommandArguments ca) {
        if (eargs.length < 1) {
            cs.sendMessage(MessageColor.NEGATIVE + "Please provide the name of the plugin!");
            return true;
        }
        final PluginManager pm = this.plugin.getServer().getPluginManager();
        final Plugin p = pm.getPlugin(eargs[0]);
        if (p == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "No such plugin!");
            return true;
        }
        // TODO: Look at CommandMap instead
        final Map<String, Map<String, Object>> commands = p.getDescription().getCommands();
        if (commands == null) {
            cs.sendMessage(MessageColor.NEUTRAL + p.getName() + MessageColor.NEGATIVE + " has no registered commands.");
            return true;
        }
        for (final Entry<String, Map<String, Object>> entry : commands.entrySet()) {
            final Object odesc = entry.getValue().get("description");
            final String desc = (odesc != null) ? odesc.toString() : "";
            final Object ousage = entry.getValue().get("usage");
            final String usage = (ousage != null) ? ousage.toString() : "/<command>";
            cs.sendMessage(MessageColor.POSITIVE + usage.replace("<command>", entry.getKey()));
            if (!desc.trim().isEmpty()) cs.sendMessage("  " + MessageColor.NEUTRAL + desc);
        }
        return true;
    }
}
