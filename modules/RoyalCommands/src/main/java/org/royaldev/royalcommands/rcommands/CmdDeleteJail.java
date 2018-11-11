/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.Configuration;

@ReflectCommand
public class CmdDeleteJail extends TabCommand {

    public CmdDeleteJail(final RoyalCommands instance, final String name) {
        super(instance, name, true, new Short[]{CompletionType.LIST.getShort()});
    }
	
    @Override
    protected List<String> customList(final CommandSender cs, final Command cmd, final String label, final String[] args, final String arg) {
        final Configuration cm = Configuration.getConfiguration("jails.yml");
		if (!cm.exists() || cm.get("jails") == null) return new ArrayList<>();
        final Map<String, Object> opts = cm.getConfigurationSection("jails").getValues(false);
		ArrayList<String> jails = new ArrayList<>();
		jails.addAll(opts.keySet());
		return jails;
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args, CommandArguments ca) {
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        final Configuration cm = Configuration.getConfiguration("jails.yml");
        if (!cm.isSet("jails." + args[0])) {
            cs.sendMessage(MessageColor.NEGATIVE + "That jail does not exist!");
            return true;
        }
        cm.set("jails." + args[0], null);
        cs.sendMessage(MessageColor.POSITIVE + "The jail \"" + MessageColor.NEUTRAL + args[0] + MessageColor.POSITIVE + "\" has been deleted.");
        return true;
    }
}
