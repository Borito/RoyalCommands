package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdForce implements CommandExecutor {

    RoyalCommands plugin;

    public CmdForce(RoyalCommands instance) {
        this.plugin = instance;
    }


    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("force")) {
            if (!plugin.isAuthorized(cs, "rcmds.force")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 2) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player t = plugin.getServer().getPlayer(args[0].trim());
            if (t == null || plugin.isVanished(t) && !(cs instanceof ConsoleCommandSender)) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            if (plugin.isAuthorized(t, "rcmds.exempt.force")) {
                cs.sendMessage(ChatColor.RED + "You cannot make that player run commands!");
                return true;
            }
            String command = plugin.getFinalArg(args, 1).trim();
            cs.sendMessage(ChatColor.BLUE + "Executing command " + ChatColor.GRAY + "/" + command + ChatColor.BLUE + " from user " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
            t.performCommand(command);
            return true;
        }
        return false;
    }
}
