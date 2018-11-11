/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdRealName extends TabCommand {

    public CmdRealName(final RoyalCommands instance, final String name) {
        super(instance, name, true, new Short[]{CompletionType.CUSTOM.getShort()});
    }
	
	
	@Override
	protected List<String> getCustomCompletions(final CommandSender cs, final Command cmd, final String label, final String[] args, final String arg) {
		ArrayList<String> endpoints = new ArrayList<>();
        for (final Player param : cs.getServer().getOnlinePlayers()) {
			if (!param.getDisplayName().toLowerCase().startsWith(arg.toLowerCase())) continue;
			endpoints.add(param.getDisplayName());
		}
		return endpoints;
	}

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args, CommandArguments ca) {
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        Player t = null;
        final String nickname = RUtils.colorize(args[0]);
        for (final Player p : this.plugin.getServer().getOnlinePlayers()) {
            if (!p.getDisplayName().equalsIgnoreCase(nickname)) continue;
            t = p;
            break;
        }
        if (t == null || this.plugin.isVanished(t, cs)) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
            return true;
        }
        cs.sendMessage(MessageColor.NEUTRAL + t.getDisplayName() + MessageColor.POSITIVE + " = " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
        return true;
    }
}
