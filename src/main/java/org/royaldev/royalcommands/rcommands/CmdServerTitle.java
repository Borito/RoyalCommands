package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdServerTitle implements CommandExecutor {
    private RoyalCommands plugin;

    public CmdServerTitle(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("servertitle")) {
            if (args.length < 1) {
                if (!plugin.isAuthorized(cs, "rcmds.servertitle.view")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                cs.sendMessage(ChatColor.BLUE + "Current server title: " + ChatColor.GRAY + RUtils.colorize(plugin.currentServerTitle));
                return true;
            }
            if (args[0].equalsIgnoreCase("clear")) {
                if (!plugin.isAuthorized(cs, "rcmds.servertitle.clear")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                plugin.currentServerTitle = plugin.defaultServerTitle;
                cs.sendMessage(ChatColor.BLUE + "Server title successfully set back to the default: " + ChatColor.GRAY + RUtils.colorize(plugin.defaultServerTitle));
                return true;
            }
            if (args[0].equalsIgnoreCase("set")) {
                if (!plugin.isAuthorized(cs, "rcmds.servertitle.set")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                if (args.length < 2) {
                    cs.sendMessage(ChatColor.RED + "You must enter a title to set!");
                    return true;
                }
                plugin.currentServerTitle = RoyalCommands.getFinalArg(args, 1);
                cs.sendMessage(ChatColor.BLUE + "Server title successfully set to: " + ChatColor.GRAY + RUtils.colorize(plugin.currentServerTitle));
                return true;
            }
        }
        return false;
    }
}
