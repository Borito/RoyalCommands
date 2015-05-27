/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands.pluginmanager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.CmdPluginManager;
import org.royaldev.royalcommands.rcommands.SubCommand;

import java.io.File;
import java.util.List;

public class SCmdDelete extends SubCommand<CmdPluginManager> {

    public SCmdDelete(final RoyalCommands instance, final CmdPluginManager parent) {
        super(instance, parent, "delete", true, "Tries to delete the specified jar", "<command> [jar]", new String[0], new Short[]{CompletionType.LIST.getShort()});
    }

    @Override
    protected List<String> customList(CommandSender cs, Command cmd, String label, String[] args, String arg) {
        return this.getParent().getMatchingJarNames(arg);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] eargs, final CommandArguments ca) {
        if (eargs.length < 1) {
            cs.sendMessage(MessageColor.NEGATIVE + "Please specify the filename to delete!");
            return true;
        }
        final String toDelete = eargs[0];
        if (!toDelete.endsWith(".jar")) {
            cs.sendMessage(MessageColor.NEGATIVE + "Please only specify jar files!");
            return true;
        }
        if (toDelete.contains(File.separator)) {
            cs.sendMessage(MessageColor.NEGATIVE + "Please don't try to leave the plugins directory!");
            return true;
        }
        final File f = new File(this.plugin.getDataFolder().getParentFile() + File.separator + toDelete);
        if (!f.exists()) {
            cs.sendMessage(MessageColor.NEGATIVE + "No such file!");
            return true;
        }
        if (!f.delete()) {
            cs.sendMessage(MessageColor.NEGATIVE + "Could not delete " + MessageColor.NEUTRAL + f.getName() + MessageColor.NEGATIVE + ".");
        } else {
            cs.sendMessage(MessageColor.POSITIVE + "Deleted " + MessageColor.NEUTRAL + f.getName() + MessageColor.POSITIVE + ".");
        }
        return true;
    }
}
