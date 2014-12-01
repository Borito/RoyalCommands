package org.royaldev.royalcommands.rcommands.pluginmanager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.CmdPluginManager;
import org.royaldev.royalcommands.rcommands.SubCommand;

public class SCmdList extends SubCommand<CmdPluginManager> {

    public SCmdList(final RoyalCommands instance, final CmdPluginManager parent) {
        super(instance, parent, "list", true, "Lists all the plugins", "<command>", new String[0], new Short[0]);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] eargs, final CommandArguments ca) {
        final PluginManager pm = this.plugin.getServer().getPluginManager();
        final Plugin[] ps = pm.getPlugins();
        final StringBuilder list = new StringBuilder();
        int enabled = 0;
        int disabled = 0;
        for (Plugin p : ps) {
            String name = p.getName();
            if (!p.isEnabled()) {
                name += MessageColor.NEGATIVE + " (disabled)";
                disabled += 1;
            } else enabled += 1;
            list.append(MessageColor.NEUTRAL);
            list.append(name);
            list.append(MessageColor.RESET);
            list.append(", ");
        }
        cs.sendMessage(MessageColor.POSITIVE + "Plugins (" + MessageColor.NEUTRAL + enabled + ((disabled > 0) ? MessageColor.POSITIVE + "/" + MessageColor.NEUTRAL + disabled + " disabled" : "") + MessageColor.POSITIVE + "): " + list.substring(0, list.length() - 4));
        return true;
    }
}
