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

public class SCmdDownloadLink extends SubCommand<CmdPluginManager> {

    public SCmdDownloadLink(final RoyalCommands instance, final CmdPluginManager parent) {
        super(instance, parent, "downloadlink", true, "Attempts to download a plugin from the URL", "<command> [url] (savename) (recursive)", new String[]{"downloadurl"}, new Short[0]);
    }

    @Override
    public boolean runCommand(final CommandSender cs, Command cmd, String label, String[] eargs, CommandArguments ca) {
        if (eargs.length < 1) {
            cs.sendMessage(MessageColor.NEGATIVE + "Please provide a link to download from!");
            return true;
        }
        final boolean recursive = eargs.length > 2 && eargs[2].equalsIgnoreCase("true");
        final String url = eargs[0];
        final String commandUsed = label;
        final String saveAs = eargs.length > 1 ? eargs[1].replaceAll("(\\\\|/)", "") : "";
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                if (SCmdDownloadLink.this.getParent().downloadAndMovePlugin(url, saveAs, recursive, cs)) {
                    cs.sendMessage(MessageColor.POSITIVE + "Downloaded plugin. Use " + MessageColor.NEUTRAL + "/" + commandUsed + " load" + MessageColor.POSITIVE + " to enable it.");
                } else cs.sendMessage(MessageColor.NEGATIVE + "Could not download that plugin. Please try again.");
            }
        };
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, r);
        return true;
    }
}
