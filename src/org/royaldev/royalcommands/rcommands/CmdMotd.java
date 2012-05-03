package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdMotd implements CommandExecutor {

    static RoyalCommands plugin;

    public CmdMotd(RoyalCommands instance) {
        plugin = instance;
    }


    public static void showMotd(CommandSender cs) {
        Player p[] = plugin.getServer().getOnlinePlayers();
        String ps = "";
        int hid = 0;
        for (Player aP : p) {
            String name = CmdList.formatPrepend(aP) + ChatColor.WHITE;
            if (!plugin.isVanished(aP)) {
                if (CmdAfk.afkdb.containsKey(aP)) name = ChatColor.GRAY + "[AFK]" + ChatColor.WHITE + name;
                ps = (ps.equals("")) ? ps.concat(name) : ps.concat(", " + name);
            } else hid++;
        }
        if (plugin.isAuthorized(cs, "rcmds.seehidden")) {
            for (Player aP : p) {
                String name = CmdList.formatPrepend(aP) + ChatColor.WHITE;
                if (!plugin.isVanished(aP)) continue;
                name = ChatColor.GRAY + "[HIDDEN]" + ChatColor.WHITE + name;
                ps = (ps.equals("")) ? ps.concat(name) : ps.concat(", " + name);
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
            if (s == null) continue;
            s = RUtils.colorize(s);
            s = s.replace("{name}", cs.getName());
            s = (cs instanceof Player) ? s.replace("{dispname}", ((Player) cs).getDisplayName()) : s.replace("{dispname}", cs.getName());
            if (onlinenum != null) s = s.replace("{players}", onlinenum);
            s = s.replace("{playerlist}", ps);
            s = (cs instanceof Player) ? s.replace("{world}", ((Player) cs).getWorld().getName()) : s.replace("{world}", "No World");
            if (maxonl != null) s = s.replace("{maxplayers}", maxonl);
            s = (plugin.getServer().getServerName() != null || !plugin.getServer().getServerName().equals("")) ? s.replace("{servername}", plugin.getServer().getServerName()) : s.replace("{servername}", "this server");
            cs.sendMessage(s);
        }
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("motd")) {
            if (!plugin.isAuthorized(cs, "rcmds.motd")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            showMotd(cs);
            return true;
        }
        return false;
    }
}
