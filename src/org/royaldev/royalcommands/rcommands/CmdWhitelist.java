package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdWhitelist implements CommandExecutor {

    RoyalCommands plugin;

    public CmdWhitelist(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("whitelist")) {
            if (!plugin.isAuthorized(cs, "rcmds.whitelist")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (plugin.whl == null) {
                cs.sendMessage(ChatColor.RED + "The whitelist.yml file was invalid! Cannot use whitelist.");
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            String command = args[0];
            if (args.length < 2) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            String player = args[1];
            if (command.equalsIgnoreCase("add")) {
                if (plugin.whitelist.contains(player)) {
                    cs.sendMessage(ChatColor.RED + "That player is already whitelisted!");
                    return true;
                }
                plugin.whitelist.add(player);
                plugin.whl.setStringList("whitelist", plugin.whitelist);
                plugin.whl.save();
                cs.sendMessage(ChatColor.BLUE + "Added " + ChatColor.GRAY + player + ChatColor.BLUE + " to whitelist.");
                return true;
            } else if (command.equalsIgnoreCase("remove")) {
                if (!plugin.whitelist.contains(player)) {
                    cs.sendMessage(ChatColor.RED + "That player is not whitelisted!");
                    return true;
                }
                plugin.whitelist.remove(player);
                plugin.whl.setStringList("whitelist", plugin.whitelist);
                plugin.whl.save();
                cs.sendMessage(ChatColor.BLUE + "Removed " + ChatColor.GRAY + player + ChatColor.BLUE + " from whitelist.");
                return true;
            } else if (command.equalsIgnoreCase("check")) {
                String message = (plugin.whitelist.contains(player)) ? ChatColor.GRAY + player + ChatColor.BLUE + " is in the whitelist." : ChatColor.GRAY + player + ChatColor.RED + " is not in the whitelist.";
                cs.sendMessage(message);
                return true;
            } else {
                cs.sendMessage(ChatColor.RED + "Unknown subcommand!");
                return true;
            }
        }
        return false;
    }

}

