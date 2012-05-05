package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdCoords implements CommandExecutor {

    public RoyalCommands plugin;

    public CmdCoords(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("coords")) {
            if (!plugin.isAuthorized(cs, "rcmds.coords")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player) && args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            if (args.length < 1) {
                Player p = (Player) cs;
                Location l = p.getLocation();
                cs.sendMessage(ChatColor.BLUE + "x: " + ChatColor.GRAY + l.getX());
                cs.sendMessage(ChatColor.BLUE + "y: " + ChatColor.GRAY + l.getY());
                cs.sendMessage(ChatColor.BLUE + "z: " + ChatColor.GRAY + l.getZ());
                cs.sendMessage(ChatColor.BLUE + "pitch: " + ChatColor.GRAY + l.getPitch());
                cs.sendMessage(ChatColor.BLUE + "yaw: " + ChatColor.GRAY + l.getYaw());
                cs.sendMessage(ChatColor.BLUE + "world: " + ChatColor.GRAY + l.getWorld().getName());
                return true;
            }
            if (!plugin.isAuthorized(cs, "rcmds.others.coords")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            Player t = plugin.getServer().getPlayer(args[0]);
            if (t == null || plugin.isVanished(t)) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            Location l = t.getLocation();
            cs.sendMessage(ChatColor.BLUE + "x: " + ChatColor.GRAY + l.getX());
            cs.sendMessage(ChatColor.BLUE + "y: " + ChatColor.GRAY + l.getY());
            cs.sendMessage(ChatColor.BLUE + "z: " + ChatColor.GRAY + l.getZ());
            cs.sendMessage(ChatColor.BLUE + "pitch: " + ChatColor.GRAY + l.getPitch());
            cs.sendMessage(ChatColor.BLUE + "yaw: " + ChatColor.GRAY + l.getYaw());
            cs.sendMessage(ChatColor.BLUE + "world: " + ChatColor.GRAY + l.getWorld().getName());
            return true;

        }
        return false;
    }

}
