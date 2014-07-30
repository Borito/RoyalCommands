package org.royaldev.royalcommands.rcommands.pluginmanager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.CmdPluginManager;
import org.royaldev.royalcommands.rcommands.SubCommand;

import java.util.List;

public class SCmdDisable extends SubCommand<CmdPluginManager> {

    public SCmdDisable(final RoyalCommands instance, final CmdPluginManager parent) {
        super(instance, parent, "disable", true, "Disables an already loaded plugin", "<command> [plugin]", new String[0], new Short[]{CompletionType.PLUGIN.getShort()});
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] eargs, CommandArguments ca) {
        if (eargs.length < 1) {
            cs.sendMessage(MessageColor.NEGATIVE + "Please provide the name of the plugin to disable!");
            return true;
        }
        final PluginManager pm = this.plugin.getServer().getPluginManager();
        final Plugin p = pm.getPlugin(eargs[0]);
        if (p == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "No such plugin!");
            return true;
        }
        if (!p.isEnabled()) {
            cs.sendMessage(MessageColor.NEUTRAL + p.getName() + MessageColor.NEGATIVE + " is already disabled!");
        }
        final List<String> depOnBy = this.getParent().getDependedOnBy(p);
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
        pm.disablePlugin(p);
        if (!p.isEnabled()) {
            cs.sendMessage(MessageColor.POSITIVE + "Disabled " + MessageColor.NEUTRAL + p.getName() + MessageColor.POSITIVE + " successfully!");
        } else {
            cs.sendMessage(MessageColor.NEGATIVE + "Could not disabled that plugin!");
        }
        return true;
    }
}
