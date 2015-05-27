/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands.pluginmanager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
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

public class SCmdUpdate extends SubCommand<CmdPluginManager> {

    public SCmdUpdate(final RoyalCommands instance, final CmdPluginManager parent) {
        super(instance, parent, "update", true, "Disables the plugin and loads the new jar", "<command> [plugin] [jar]", new String[0], new Short[]{CompletionType.PLUGIN.getShort(), CompletionType.LIST.getShort()});
    }

    @Override
    public List<String> customList(CommandSender cs, Command cmd, String label, String[] args, String arg) {
        return this.getParent().getMatchingJarNames(arg);
    }

    @Override
    public boolean runCommand(final CommandSender cs, Command cmd, String label, String[] eargs, CommandArguments ca) {
        if (eargs.length < 2) {
            cs.sendMessage(MessageColor.NEGATIVE + "Please provide the name of the plugin to update and its filename!");
            return true;
        }
        final PluginManager pm = this.plugin.getServer().getPluginManager();
        final Plugin p = pm.getPlugin(eargs[0]);
        if (p == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "No such plugin!");
            return true;
        }
        List<String> depOnBy = this.getParent().getDependedOnBy(p);
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
        final File f = new File(this.plugin.getDataFolder().getParentFile() + File.separator + eargs[1]);
        if (!f.exists()) {
            cs.sendMessage(MessageColor.NEGATIVE + "That file does not exist!");
            return true;
        }
        if (!f.canRead()) {
            cs.sendMessage(MessageColor.NEGATIVE + "Can't read that file!");
            return true;
        }
        cs.sendMessage(MessageColor.POSITIVE + "Starting update process.");
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                SCmdUpdate.this.getParent().unregisterAllPluginCommands(p.getName());
                HandlerList.unregisterAll(p);
                SCmdUpdate.this.plugin.getServer().getScheduler().cancelTasks(p);
                pm.disablePlugin(p);
                try {
                    Plugin loadedPlugin = pm.loadPlugin(f);
                    if (loadedPlugin == null) {
                        cs.sendMessage(MessageColor.NEGATIVE + "Could not load plugin: plugin was invalid.");
                        cs.sendMessage(MessageColor.NEGATIVE + "Make sure it ends with .jar!");
                        return;
                    }
                    pm.enablePlugin(loadedPlugin);
                } catch (UnknownDependencyException e) {
                    cs.sendMessage(MessageColor.NEGATIVE + "Missing dependency: " + e.getMessage());
                    return;
                } catch (InvalidDescriptionException e) {
                    cs.sendMessage(MessageColor.NEGATIVE + "That plugin contained an invalid description!");
                    return;
                } catch (InvalidPluginException e) {
                    cs.sendMessage(MessageColor.NEGATIVE + "That file is not a plugin!");
                    return;
                }
                SCmdUpdate.this.getParent().removePluginFromList(p);
                cs.sendMessage(MessageColor.POSITIVE + "Updated " + MessageColor.NEUTRAL + p.getName() + MessageColor.POSITIVE + " successfully.");
            }
        };
        this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, r);
        return true;
    }
}
