package org.royaldev.royalcommands.rcommands.pluginmanager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.CmdPluginManager;
import org.royaldev.royalcommands.rcommands.SubCommand;

import java.util.List;

public class SCmdInfo extends SubCommand<CmdPluginManager> {

    public SCmdInfo(final RoyalCommands instance, final CmdPluginManager parent) {
        super(instance, parent, "info", true, "Displays information about a plugin", "<command> [plugin]", new String[0], new Short[]{CompletionType.PLUGIN.getShort()});
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] eargs, CommandArguments ca) {
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
        final PluginDescriptionFile pdf = p.getDescription();
        if (pdf == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "Can't get information from " + MessageColor.NEUTRAL + p.getName() + MessageColor.NEGATIVE + ".");
            return true;
        }
        final String version = pdf.getVersion();
        final List<String> authors = pdf.getAuthors();
        final String site = pdf.getWebsite();
        final List<String> softDep = pdf.getSoftDepend();
        final List<String> dep = pdf.getDepend();
        final String name = pdf.getName();
        final String desc = pdf.getDescription();
        if (name != null && !name.isEmpty()) {
            cs.sendMessage(MessageColor.POSITIVE + "Name");
            cs.sendMessage("  " + MessageColor.NEUTRAL + name);
        }
        if (version != null && !version.isEmpty()) {
            cs.sendMessage(MessageColor.POSITIVE + "Version");
            cs.sendMessage("  " + MessageColor.NEUTRAL + version);
        }
        if (site != null && !site.isEmpty()) {
            cs.sendMessage(MessageColor.POSITIVE + "Site");
            cs.sendMessage("  " + MessageColor.NEUTRAL + site);
        }
        if (desc != null && !desc.isEmpty()) {
            cs.sendMessage(MessageColor.POSITIVE + "Description");
            cs.sendMessage("  " + MessageColor.NEUTRAL + desc.replaceAll("\r?\n", ""));
        }
        if (authors != null && !authors.isEmpty()) {
            cs.sendMessage(MessageColor.POSITIVE + "Author" + ((authors.size() > 1) ? "s" : "") + "");
            cs.sendMessage("  " + MessageColor.NEUTRAL + RUtils.join(authors, MessageColor.RESET + ", " + MessageColor.NEUTRAL));
        }
        if (softDep != null && !softDep.isEmpty()) {
            cs.sendMessage(MessageColor.POSITIVE + "Soft Dependencies");
            cs.sendMessage("  " + MessageColor.NEUTRAL + RUtils.join(softDep, MessageColor.RESET + ", " + MessageColor.NEUTRAL));
        }
        if (dep != null && !dep.isEmpty()) {
            cs.sendMessage(MessageColor.POSITIVE + "Dependencies");
            cs.sendMessage("  " + MessageColor.NEUTRAL + RUtils.join(dep, MessageColor.RESET + ", " + MessageColor.NEUTRAL));
        }
        cs.sendMessage(MessageColor.POSITIVE + "Enabled");
        cs.sendMessage("  " + MessageColor.NEUTRAL + ((p.isEnabled()) ? "Yes" : "No"));
        return true;
    }
}
