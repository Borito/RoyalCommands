package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AFKUtils;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            Map<String, List<Player>> groups = new HashMap<String, List<Player>>();
            if (plugin.simpleList) {
                String ps = "";
                int hid = 0;
                for (Player pl : p) {
                    String name = formatPrepend(pl) + ChatColor.WHITE;
                    if (plugin.isVanished(pl) && plugin.isAuthorized(cs, "rcmds.seehidden")) {
                        name = ChatColor.GRAY + "[HIDDEN]" + ChatColor.WHITE + name;
                        ps = (ps.equals("")) ? ps.concat(name) : ps.concat(", " + name);
                        hid++;
                    } else {
                        if (AFKUtils.isAfk(pl)) name = ChatColor.GRAY + "[AFK]" + ChatColor.WHITE + name;
                        ps = (ps.equals("")) ? ps.concat(name) : ps.concat(", " + name);
                    }
                }
                String numPlayers;
                if (plugin.isAuthorized(cs, "rcmds.seehidden")) {
                    if (hid < 1) numPlayers = String.valueOf(p.length);
                    else numPlayers = (p.length - hid) + "/" + hid;
                } else numPlayers = String.valueOf(p.length - hid);
                cs.sendMessage(ChatColor.BLUE + "There are currently " + ChatColor.GRAY + numPlayers + ChatColor.BLUE + " out of " + ChatColor.GRAY + plugin.getServer().getMaxPlayers() + ChatColor.BLUE + " players online.");
                cs.sendMessage("Online Players: " + ps);
                return true;
            }
            int hid = 0;
            for (Player pl : p) {
                if (plugin.isVanished(pl)) hid++;
                String group = RoyalCommands.permission.getPrimaryGroup(pl);
                List<Player> inGroup = (groups.containsKey(group)) ? groups.get(group) : new ArrayList<Player>();
                inGroup.add(pl);
                groups.put(group, inGroup);
            }
            String numPlayers;
            if (plugin.isAuthorized(cs, "rcmds.seehidden")) {
                if (hid < 1) numPlayers = String.valueOf(p.length);
                else numPlayers = (p.length - hid) + "/" + hid;
            } else numPlayers = String.valueOf(p.length - hid);
            cs.sendMessage(ChatColor.BLUE + "There are currently " + ChatColor.GRAY + numPlayers + ChatColor.BLUE + " out of " + ChatColor.GRAY + plugin.getServer().getMaxPlayers() + ChatColor.BLUE + " players online.");
            for (String group : groups.keySet()) {
                String online = "";
                for (Player pl : groups.get(group)) {
                    String name = formatPrepend(pl) + ChatColor.WHITE;
                    if (plugin.isVanished(pl) && plugin.isAuthorized(cs, "rcmds.seehidden")) {
                        name = ChatColor.GRAY + "[HIDDEN]" + ChatColor.WHITE + name;
                        online = (online.equals("")) ? online.concat(name) : online.concat(", " + name);
                    } else {
                        if (AFKUtils.isAfk(pl)) name = ChatColor.GRAY + "[AFK]" + ChatColor.WHITE + name;
                        online = (online.equals("")) ? online.concat(name) : online.concat(", " + name);
                    }
                }
                online = RUtils.colorize(group) + ": " + online;
                cs.sendMessage(online);
            }
            return true;
        }
        return false;
    }
}