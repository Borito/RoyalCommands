package org.royaldev.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RoyalCommands;

public class Motd implements CommandExecutor {

    static RoyalCommands plugin;

    public Motd(RoyalCommands instance) {
        plugin = instance;
    }


    public static void showMotd(CommandSender cs) {
        Player p[] = plugin.getServer().getOnlinePlayers();
        String ps = "";
        int hid = 0;
        for (Player aP : p) {
            if (!plugin.isVanished(aP)) {
                if (ps.equals("")) {
                    ps = ps.concat(aP.getName());
                } else {
                    ps = ps.concat(", " + aP.getName());
                }
            } else {
                hid++;
            }
        }
        Integer onnum = plugin.getServer().getOnlinePlayers().length;
        String onlinenum;
        try {
            onlinenum = Integer.toString(onnum - hid);
        } catch (Exception e) {
            onlinenum = null;
        }
        Integer maxon = plugin.getServer().getMaxPlayers();
        String maxonl;
        try {
            maxonl = Integer.toString(maxon);
        } catch (Exception e) {
            maxonl = null;
        }
        for (String s : plugin.motd) {
            if (s == null) {
                continue;
            }
            s = s.replaceAll("(&([a-f0-9]))", "\u00A7$2");
            s = s.replace("{name}", cs.getName());
            if (cs instanceof Player) {
                s = s.replace("{dispname}", ((Player) cs).getDisplayName());
            } else {
                s = s.replace("{dispname}", cs.getName());
            }
            if (onlinenum != null) {
                s = s.replace("{players}", onlinenum);
            }
            s = s.replace("{playerlist}", ps);
            if (cs instanceof Player) {
                s = s.replace("{world}", ((Player) cs).getWorld().getName());
            } else {
                s = s.replace("{world}", "No World");
            }
            if (maxonl != null) {
                s = s.replace("{maxplayers}", maxonl);
            }
            if (plugin.getServer().getServerName() != null || !plugin.getServer().getServerName().equals("")) {
                s = s.replace("{servername}", plugin.getServer().getServerName());
            } else {
                s = s.replace("{servername}", "this server");
            }
            cs.sendMessage(s);
        }
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("motd")) {
            if (!plugin.isAuthorized(cs, "rcmds.motd")) {
                cs.sendMessage(ChatColor.RED
                        + "You don't have permission for that!");
                plugin.log.warning("[RoyalCommands] " + cs.getName()
                        + " was denied access to the command!");
                return true;
            }
            showMotd(cs);
            return true;
        }
        return false;
    }
}
