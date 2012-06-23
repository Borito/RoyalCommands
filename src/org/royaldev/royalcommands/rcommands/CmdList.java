package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdList implements CommandExecutor {

    static RoyalCommands plugin;

    public CmdList(RoyalCommands plugin) {
        CmdList.plugin = plugin;
    }

    public static String formatPrepend(Player p) {
        String format = plugin.whoFormat;
        try {
            format = format.replaceAll("(?i)\\{prefix\\}", RoyalCommands.chat.getPlayerPrefix(p));
        } catch (Exception e) {
            format = format.replaceAll("(?i)\\{prefix\\}", "");
        }
        try {
            format = format.replaceAll("(?i)\\{group\\}", RoyalCommands.permission.getPrimaryGroup(p));
        } catch (Exception e) {
            format = format.replaceAll("(?i)\\{group\\}", "");
        }
        try {
            format = format.replaceAll("(?i)\\{suffix\\}", RoyalCommands.chat.getPlayerSuffix(p));
        } catch (Exception e) {
            format = format.replaceAll("(?i)\\{suffix\\}", "");
        }
        format = format.replaceAll("(?i)\\{name\\}", p.getName());
        format = format.replaceAll("(?i)\\{dispname\\}", p.getDisplayName());
        format = RUtils.colorize(format);
        return format;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("list")) {
            if (!plugin.isAuthorized(cs, "rcmds.list")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            Player p[] = plugin.getServer().getOnlinePlayers();
            String ps = "";
            int hid = 0;
            for (Player aP : p) {
                String name = formatPrepend(aP) + ChatColor.WHITE;
                if (!plugin.isVanished(aP)) {
                    if (CmdAfk.afkdb.containsKey(aP)) name = ChatColor.GRAY + "[AFK]" + ChatColor.WHITE + name;
                    ps = (ps.equals("")) ? ps.concat(name) : ps.concat(", " + name);
                } else hid++;
            }
            if (plugin.isAuthorized(cs, "rcmds.seehidden")) {
                if (hid > 0)
                    cs.sendMessage(ChatColor.BLUE + "There are currently " + ChatColor.GRAY + (p.length - hid) + "/" + hid + ChatColor.BLUE + " out of " + ChatColor.GRAY + plugin.getServer().getMaxPlayers() + ChatColor.BLUE + " players online.");
                else
                    cs.sendMessage(ChatColor.BLUE + "There are currently " + ChatColor.GRAY + p.length + ChatColor.BLUE + " out of " + ChatColor.GRAY + plugin.getServer().getMaxPlayers() + ChatColor.BLUE + " players online.");
                for (Player aP : p) {
                    String name = formatPrepend(aP) + ChatColor.WHITE;
                    if (!plugin.isVanished(aP)) continue;
                    name = ChatColor.GRAY + "[HIDDEN]" + ChatColor.WHITE + name;
                    ps = (ps.equals("")) ? ps.concat(name) : ps.concat(", " + name);
                }
            } else
                cs.sendMessage(ChatColor.BLUE + "There are currently " + ChatColor.GRAY + (p.length - hid) + ChatColor.BLUE + " out of " + ChatColor.GRAY + plugin.getServer().getMaxPlayers() + ChatColor.BLUE + " players online.");
            cs.sendMessage("Online Players: " + ps);
            return true;
        }
        return false;
    }
}
