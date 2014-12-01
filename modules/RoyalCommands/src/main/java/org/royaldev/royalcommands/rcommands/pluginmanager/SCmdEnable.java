package org.royaldev.royalcommands.rcommands.pluginmanager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.CmdPluginManager;
import org.royaldev.royalcommands.rcommands.SubCommand;

import java.util.ArrayList;
import java.util.List;

public class SCmdEnable extends SubCommand<CmdPluginManager> {

    public SCmdEnable(final RoyalCommands instance, final CmdPluginManager parent) {
        super(instance, parent, "enable", true, "Enables a disabled plugin", "<command> [plugin]", new String[0], new Short[]{CompletionType.PLUGIN.getShort()});
    }

    @Override
    public List<String> filterCompletions(List<String> completions, CommandSender cs, Command cmd, String label, String[] args, String arg) {
        final List<String> filtered = new ArrayList<>();
        final PluginManager pm = this.plugin.getServer().getPluginManager();
        for (final String pluginName : completions) {
            final Plugin p = pm.getPlugin(pluginName);
            if (p == null || !p.isEnabled()) continue;
            filtered.add(pluginName);
        }
        return filtered;
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] eargs, final CommandArguments ca) {
        if (eargs.length < 1) {
            cs.sendMessage(MessageColor.NEGATIVE + "Please provide the name of the plugin to enable!");
            return true;
        }
        final PluginManager pm = this.plugin.getServer().getPluginManager();
        final Plugin p = pm.getPlugin(eargs[0]);
        if (p == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "No such plugin!");
            return true;
        }
        if (p.isEnabled()) {
            cs.sendMessage(MessageColor.NEGATIVE + "Plugin is already enabled!");
            return true;
        }
        pm.enablePlugin(p);
        if (p.isEnabled()) {
            cs.sendMessage(MessageColor.POSITIVE + "Successfully enabled " + MessageColor.NEUTRAL + p.getName() + MessageColor.POSITIVE + "!");
        } else {
            cs.sendMessage(MessageColor.NEGATIVE + "Could not enable " + MessageColor.NEUTRAL + p.getName() + MessageColor.NEGATIVE + ".");
        }
        return true;
    }
}
