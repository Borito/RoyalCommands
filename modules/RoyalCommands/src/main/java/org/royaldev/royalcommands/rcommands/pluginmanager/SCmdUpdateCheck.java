package org.royaldev.royalcommands.rcommands.pluginmanager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.CmdPluginManager;
import org.royaldev.royalcommands.rcommands.SubCommand;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SCmdUpdateCheck extends SubCommand<CmdPluginManager> {

    public SCmdUpdateCheck(final RoyalCommands instance, final CmdPluginManager parent) {
        super(instance, parent, "updatecheck", true, "Attempts to check for the newest version of a plugin; may not always work correctly", "<command> [plugin] (tag)", new String[0], new Short[]{CompletionType.PLUGIN.getShort()});
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
        String tag = (eargs.length > 1) ? RoyalCommands.getFinalArg(eargs, 1) : p.getName();
        try {
            tag = URLEncoder.encode(tag, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            cs.sendMessage(MessageColor.NEGATIVE + "Tell the developer enc1.");
            return true;
        }
        if (p.getDescription() == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "Plugin has no description!");
            return true;
        }
        final String version = p.getDescription().getVersion();
        if (version == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "Plugin has not set a version!");
            return true;
        }
        try {
            final String checked = this.getParent().updateCheck(tag, version);
            cs.sendMessage(MessageColor.POSITIVE + "Current version is " + MessageColor.NEUTRAL + version + MessageColor.POSITIVE + "; newest version is " + MessageColor.NEUTRAL + checked + MessageColor.POSITIVE + ".");
        } catch (Exception e) {
            cs.sendMessage(MessageColor.NEGATIVE + "Could not check for update!");
        }
        return true;
    }
}
