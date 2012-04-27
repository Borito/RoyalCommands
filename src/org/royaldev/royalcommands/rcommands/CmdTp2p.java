package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdTp2p implements CommandExecutor {

    RoyalCommands plugin;

    public CmdTp2p(RoyalCommands instance) {
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
            if (!RUtils.isTeleportAllowed(t1) && !plugin.isAuthorized(cs, "rcmds.tpoverride")) {
                cs.sendMessage(ChatColor.RED + "The player " + ChatColor.GRAY + t1.getName() + ChatColor.RED + " has teleportation off!");
                return true;
            }
            if (!RUtils.isTeleportAllowed(t2) && !plugin.isAuthorized(cs, "rcmds.tpoverride")) {
                cs.sendMessage(ChatColor.RED + "The player " + ChatColor.GRAY + t2.getName() + ChatColor.RED + " has teleportation off!");
                return true;
            }
            t1.teleport(t2);
            cs.sendMessage(ChatColor.BLUE + "You have teleported " + ChatColor.GRAY + t1.getName() + ChatColor.BLUE + " to " + ChatColor.GRAY + t2.getName() + ChatColor.BLUE + ".");
            return true;
        }
        return false;
    }
}
