package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdVip implements CommandExecutor {

    RoyalCommands plugin;

    public CmdVip(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("vip")) {
            if (!plugin.isAuthorized(cs, "rcmds.vip")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            // add, check, remove, list - list too stressful atm
            String command = args[0];
            if (command.equalsIgnoreCase("add")) {
                if (args.length < 2) {
                    cs.sendMessage(ChatColor.RED + "No player specified!");
                    return true;
                }
                OfflinePlayer t = plugin.getServer().getOfflinePlayer(args[1]);
                if (!PConfManager.getPConfExists(t)) {
                    cs.sendMessage(ChatColor.RED + "That player does not exist!");
                    return true;
                }
                if (PConfManager.getPVal(t, "vip") != null && PConfManager.getPValBoolean(t, "vip")) {
                    cs.sendMessage(ChatColor.RED + "That player is already in the VIP list.");
                    return true;
                }
                PConfManager.setPValBoolean(t, true, "vip");
                cs.sendMessage(ChatColor.BLUE + "Successfully added " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + " to the VIP list.");
                return true;
            } else if (command.equalsIgnoreCase("remove")) {
                if (args.length < 2) {
                    cs.sendMessage(ChatColor.RED + "No player specified!");
                    return true;
                }
                OfflinePlayer t = plugin.getServer().getOfflinePlayer(args[1]);
                if (!PConfManager.getPConfExists(t)) {
                    cs.sendMessage(ChatColor.RED + "That player does not exist!");
                    return true;
                }
                if (PConfManager.getPVal(t, "vip") == null || !PConfManager.getPValBoolean(t, "vip")) {
                    cs.sendMessage(ChatColor.RED + "That player is not in the VIP list.");
                    return true;
                }
                PConfManager.setPValBoolean(t, false, "vip");
                cs.sendMessage(ChatColor.BLUE + "Successfully removed " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + " to the VIP list.");
                return true;
            } else if (command.equalsIgnoreCase("check")) {
                if (args.length < 2) {
                    cs.sendMessage(ChatColor.RED + "No player specified!");
                    return true;
                }
                OfflinePlayer t = plugin.getServer().getOfflinePlayer(args[1]);
                if (!PConfManager.getPConfExists(t)) {
                    cs.sendMessage(ChatColor.RED + "That player does not exist!");
                    return true;
                }
                boolean inList = PConfManager.getPValBoolean(t, "vip");
                if (inList) {
                    cs.sendMessage(ChatColor.BLUE + "The player " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + " is in the VIP list.");
                    return true;
                }
                cs.sendMessage(ChatColor.RED + "The player " + ChatColor.GRAY + t.getName() + ChatColor.RED + " is not in the VIP list.");
                return true;
            } else if (command.equalsIgnoreCase("?")) {
                String cmdName = cmd.getName();
                cs.sendMessage(ChatColor.GRAY + "/" + cmdName + " add [player] " + ChatColor.BLUE + " - Adds a player to the VIP list.");
                cs.sendMessage(ChatColor.GRAY + "/" + cmdName + " remove [player] " + ChatColor.BLUE + " - Removes a player from the VIP list.");
                cs.sendMessage(ChatColor.GRAY + "/" + cmdName + " check [player] " + ChatColor.BLUE + " - Checks if a player is in the VIP list.");
                cs.sendMessage(ChatColor.GRAY + "/" + cmdName + " ? " + ChatColor.BLUE + " - Displays this help.");
                return true;
            } else {
                cs.sendMessage(ChatColor.RED + "Invalid subcommand!");
                cs.sendMessage(ChatColor.RED + "Try " + ChatColor.GRAY + "/" + cmd.getName() + " ?" + ChatColor.RED + ".");
                return true;
            }
        }
        return false;
    }

}
