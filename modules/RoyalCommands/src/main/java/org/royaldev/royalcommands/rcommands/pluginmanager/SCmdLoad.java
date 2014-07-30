package org.royaldev.royalcommands.rcommands.pluginmanager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.UnknownDependencyException;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.CmdPluginManager;
import org.royaldev.royalcommands.rcommands.SubCommand;

import java.io.File;
import java.util.List;

public class SCmdLoad extends SubCommand<CmdPluginManager> {

    public SCmdLoad(final RoyalCommands instance, final CmdPluginManager parent) {
        super(instance, parent, "load", true, "Loads and enables a new plugin", "<command> [jar]", new String[0], new Short[]{CompletionType.LIST.getShort()});
    }

    @Override
    public List<String> customList(CommandSender cs, Command cmd, String label, String[] args, String arg) {
        return this.getParent().getMatchingJarNames(arg);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] eargs, CommandArguments ca) {
        if (eargs.length < 1) {
            cs.sendMessage(MessageColor.NEGATIVE + "Please provide the name of the jar to load!");
            return true;
        }
        final PluginManager pm = this.plugin.getServer().getPluginManager();
        final File f = new File(this.plugin.getDataFolder().getParentFile(), eargs[0]);
        if (!f.exists()) {
            cs.sendMessage(MessageColor.NEGATIVE + "That file does not exist!");
            return true;
        }
        if (!f.canRead()) {
            cs.sendMessage(MessageColor.NEGATIVE + "Can't read that file!");
            return true;
        }
        final Plugin p;
        try {
            p = pm.loadPlugin(f);
            if (p == null) {
                cs.sendMessage(MessageColor.NEGATIVE + "Could not load plugin: plugin was invalid.");
                cs.sendMessage(MessageColor.NEGATIVE + "Make sure it ends with .jar!");
                return true;
            }
            pm.enablePlugin(p);
        } catch (UnknownDependencyException e) {
            cs.sendMessage(MessageColor.NEGATIVE + "Missing dependency: " + e.getMessage());
            return true;
        } catch (InvalidDescriptionException e) {
            cs.sendMessage(MessageColor.NEGATIVE + "That plugin contained an invalid description!");
            return true;
        } catch (InvalidPluginException e) {
            cs.sendMessage(MessageColor.NEGATIVE + "That file is not a plugin!");
            return true;
        }
        if (p.isEnabled()) {
            cs.sendMessage(MessageColor.POSITIVE + "Loaded and enabled " + MessageColor.NEUTRAL + p.getName() + MessageColor.POSITIVE + " successfully.");
        } else {
            cs.sendMessage(MessageColor.NEGATIVE + "Could not load and enable " + MessageColor.NEUTRAL + p.getName() + MessageColor.NEGATIVE + ".");
        }
        return true;
    }
}
