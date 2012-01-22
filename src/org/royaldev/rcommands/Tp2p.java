package org.royaldev.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class Tp2p implements CommandExecutor {

    RoyalCommands plugin;

    public Tp2p(RoyalCommands instance) {
        this.plugin = instance;
    }


    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tp2p")) {
            if (!plugin.isAuthorized(cs, "rcmds.tp2p")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 2) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player t1 = plugin.getServer().getPlayer(args[0].trim());
            Player t2 = plugin.getServer().getPlayer(args[1].trim());
            if (t1 == null || t2 == null || plugin.isVanished(t1) || plugin.isVanished(t2)) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            if (plugin.isAuthorized(t1, "rcmds.exempt.tp2p") || plugin.isAuthorized(t1, "rcmds.exempt.teleport") || plugin.isAuthorized(t2, "rcmds.exempt.tp2p") || plugin.isAuthorized(t2, "rcmds.exempt.teleport")) {
                cs.sendMessage(ChatColor.RED + "You cannot teleport that player!");
                return true;
            }
            Back.backdb.put(t1, t1.getLocation());
            t1.teleport(t2);
            cs.sendMessage(ChatColor.BLUE + "You have teleported " + ChatColor.GRAY + t1.getName() + ChatColor.BLUE + " to " + ChatColor.GRAY + t2.getName() + ChatColor.BLUE + ".");
            return true;
        }
        return false;
    }
}
