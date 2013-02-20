package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdServerTitle implements CommandExecutor {
    private RoyalCommands plugin;
    
    public CmdServerTitle(RoyalCommands plugin) {
        this.plugin = plugin;
    }
    
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (args.length > 0) {

            if (args[0].equalsIgnoreCase("clear")) {
                if (!plugin.isAuthorized(cs, "rcmds.servertitle.clear")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                FileConfiguration conf = plugin.getConfig();
                plugin.currentServerTitle = plugin.defaultServerTitle;
                conf.set("current-server-title", plugin.currentServerTitle);
                cs.sendMessage(ChatColor.GREEN + "Server title successfully set back to the default: " + ChatColor.RESET + RUtils.colorize(plugin.defaultServerTitle));
                return true;
            }

            if (args[0].equalsIgnoreCase("set")) {
                if (!plugin.isAuthorized(cs, "rcmds.servertitle.set")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                if (args.length < 2) {
                    cs.sendMessage(ChatColor.RED + "You must put a title to set!");
                    return true;
                }
                String title = RoyalCommands.getFinalArg(args, 1);
                FileConfiguration conf = plugin.getConfig();
                plugin.currentServerTitle = title;
                conf.set("current-server-title", plugin.currentServerTitle);
                cs.sendMessage(ChatColor.GREEN + "Server title successfully set to: " + ChatColor.RESET + RUtils.colorize(title));
                return true;
            }

        }

        if (!plugin.isAuthorized(cs, "rcmds.servertitle.view")) {
            RUtils.dispNoPerms(cs);
            return true;
        }

        cs.sendMessage(ChatColor.GREEN + "Current Server Title: " + ChatColor.RESET + RUtils.colorize(plugin.currentServerTitle));
        return true;
    }
}
