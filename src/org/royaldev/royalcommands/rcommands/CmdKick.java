package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdKick implements CommandExecutor {

    RoyalCommands plugin;

    public CmdKick(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("kick")) {
            if (!plugin.isAuthorized(cs, "rcmds.kick")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player t = plugin.getServer().getPlayer(args[0]);
            if (t == null || plugin.isVanished(t)) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            if (plugin.isAuthorized(t, "rcmds.exempt.kick")) {
                cs.sendMessage(ChatColor.RED + "You cannot kick that player!");
                return true;
            }
            if (args.length == 1) {
                plugin.getServer().broadcast(ChatColor.RED + "The player " + ChatColor.GRAY + t.getName() + ChatColor.RED + " has been kicked for " + ChatColor.GRAY + plugin.kickMessage + ChatColor.RED + " by " + ChatColor.GRAY + cs.getName() + ChatColor.RED + ".", "rcmds.see.kick");
                t.kickPlayer(plugin.kickMessage);
                return true;
            } else if (args.length > 1) {
                String kickMessage = RUtils.colorize(plugin.getFinalArg(args, 1));
                plugin.getServer().broadcast(ChatColor.RED + "The player " + ChatColor.GRAY + t.getName() + ChatColor.RED + " has been kicked for " + ChatColor.GRAY + kickMessage + ChatColor.RED + " by " + ChatColor.GRAY + cs.getName() + ChatColor.RED + ".", "rcmds.see.kick");
                t.kickPlayer(kickMessage);
                return true;
            }
        }
        return false;
    }
}
