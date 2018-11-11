/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdDocumentation extends TabCommand {

    public CmdDocumentation(final RoyalCommands instance, final String name) {
        super(instance, name, true, new Short[]{CompletionType.CUSTOM.getShort(), CompletionType.ROYALCOMMANDS_COMMAND.getShort()});
    }
	
	@Override
	protected List<String> getCustomCompletions(final CommandSender cs, final Command cmd, final String label, final String[] args, final String arg) {
		String[] allEndpoints = {"command", "permission"};
		ArrayList<String> endpoints = new ArrayList<>();
				
        for (final String param : allEndpoints) {
			if (!param.startsWith(arg.toLowerCase())) continue;
			endpoints.add(param);
		}
		return endpoints;
	}

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args, CommandArguments ca) {
		String baseUrl = "https://rcmds.wma.im";
        if (args.length < 2) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        switch (args[0].toLowerCase()) {
            case "command":
            case "cmd":
            case "c":
                String commandName = args[1];
                if (commandName.startsWith("/")) commandName = commandName.substring(1);
                final PluginCommand pc = this.plugin.getServer().getPluginCommand(commandName);
                if (!this.plugin.getClass().isInstance(pc.getPlugin())) {
                    cs.sendMessage(MessageColor.NEGATIVE + "Not a " + this.plugin.getName() + " plugin. Try " + MessageColor.NEUTRAL + "/usage" + MessageColor.NEGATIVE + ".");
                    return true;
                }
                cs.sendMessage(MessageColor.POSITIVE + "Link: " + MessageColor.NEUTRAL + baseUrl + "/commands/" + pc.getName().toLowerCase());
                break;
            case "permission":
            case "perm":
            case "p":
                final YamlConfiguration pluginYml = this.plugin.getPluginYml();
                final String perm = args[1].toLowerCase();
                if (!pluginYml.isSet("permissions." + perm)) {
                    cs.sendMessage(MessageColor.NEGATIVE + "No such permission registered!");
                    return true;
                }
                try {
                    cs.sendMessage(MessageColor.POSITIVE + "Link: " + MessageColor.NEUTRAL + baseUrl + "/permissions?search=" + URLEncoder.encode(perm, "UTf-8"));
                } catch (UnsupportedEncodingException e) {
                    cs.sendMessage(MessageColor.NEGATIVE + "Error (" + MessageColor.NEUTRAL + e.getClass().getSimpleName() + MessageColor.NEGATIVE + "): " + MessageColor.NEUTRAL + e.getMessage());
                }
                break;
            default:
                cs.sendMessage(MessageColor.NEGATIVE + "Invalid type. Either " + MessageColor.NEUTRAL + "command" + MessageColor.NEGATIVE + " or " + MessageColor.NEUTRAL + "permission" + MessageColor.NEGATIVE + " must be specified.");
        }
        return true;
    }
}
