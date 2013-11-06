package org.royaldev.royalcommands.rcommands;

import org.apache.commons.lang.text.StrBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AFKUtils;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ReflectCommand
public class CmdList implements CommandExecutor {

    private static RoyalCommands plugin;

    public CmdList(RoyalCommands plugin) {
        CmdList.plugin = plugin;
    }

    public String getNumOnline(CommandSender cs) {
        int hid = plugin.getNumberVanished();
        int all = plugin.getServer().getOnlinePlayers().length;
        boolean canSeeVanished = plugin.ah.isAuthorized(cs, "rcmds.seehidden");
        String numPlayers;
        if (canSeeVanished && hid > 0) numPlayers = (all - hid) + "/" + hid;
        else numPlayers = String.valueOf(all - hid);
        return MessageColor.POSITIVE + "There are currently " + MessageColor.NEUTRAL + numPlayers + MessageColor.POSITIVE + " out of " + MessageColor.NEUTRAL + plugin.getServer().getMaxPlayers() + MessageColor.POSITIVE + " players online.";
    }

    public static String getSimpleList(CommandSender cs) {
        Player[] pl = plugin.getServer().getOnlinePlayers();
        StringBuilder sb = new StringBuilder();
        for (Player p : pl) {
            if (plugin.isVanished(p) && plugin.ah.isAuthorized(cs, "rcmds.seehidden")) {
                sb.append(MessageColor.NEUTRAL);
                sb.append("[HIDDEN]");
                sb.append(MessageColor.RESET);
                sb.append(formatPrepend(p));
                sb.append(MessageColor.RESET);
                sb.append(", ");
            } else if (!plugin.isVanished(p)) {
                if (AFKUtils.isAfk(p)) sb.append(MessageColor.NEUTRAL).append("[AFK]");
                sb.append(formatPrepend(p));
                sb.append(MessageColor.RESET).append(", ");
            }
        }
        if (sb.length() < 2) return "";
        return "Online Players: " + sb.toString().substring(0, sb.length() - 2);
    }

    public static String[] getGroupList(CommandSender cs) {
        Player[] pl = plugin.getServer().getOnlinePlayers();
        Map<String, List<String>> groups = new HashMap<String, List<String>>();
        StrBuilder sb = new StrBuilder();
        for (Player p : pl) {
            String group;
            try {
                if (!plugin.vh.usingVault()) throw new Exception();
                group = plugin.vh.getPermission().getPrimaryGroup(p);
            } catch (Exception e) {
                group = "No Group";
            }
            List<String> inGroup = (groups.containsKey(group)) ? groups.get(group) : new ArrayList<String>();
            if (plugin.isVanished(p) && plugin.ah.isAuthorized(cs, "rcmds.seehidden"))
                inGroup.add(MessageColor.NEUTRAL + "[HIDDEN]" + MessageColor.RESET + formatPrepend(p));
            else if (!plugin.isVanished(p)) {
                if (AFKUtils.isAfk(p))
                    inGroup.add(MessageColor.NEUTRAL + "[AFK]" + MessageColor.RESET + formatPrepend(p));
                else inGroup.add(formatPrepend(p));
            }
            groups.put(group, inGroup);
        }
        List<String> toRet = new ArrayList<String>();
        for (Map.Entry<String, List<String>> entry : groups.entrySet()) {
            List<String> inGroup = entry.getValue();
            if (inGroup.size() < 1) continue;
            sb.append(groupPrepend(entry.getKey()));
            sb.append(MessageColor.RESET);
            sb.append(": ");
            for (String name : inGroup) {
                sb.append(name);
                sb.append(MessageColor.RESET);
                sb.append(", ");
            }
            if (sb.length() < 2) {
                sb.clear();
                continue;
            }
            toRet.add(sb.toString().substring(0, sb.length() - 2));
            sb.clear();
        }
        for (String s : toRet) if (s == null) toRet.remove(s);
        return toRet.toArray(new String[toRet.size()]);
    }

    public static String groupPrepend(String group) {
        String format = Config.whoGroupFormat;
        try {
            if (!plugin.vh.usingVault()) throw new Exception();
            format = format.replaceAll("(?i)\\{prefix}", plugin.vh.getChat().getGroupPrefix(plugin.getServer().getWorlds().get(0), group));
        } catch (Exception e) {
            String prefix = RUtils.getRChatGroupPrefix(group);
            if (prefix != null) format = format.replace("{prefix}", prefix);
            else format = format.replace("{prefix}", "");
        }
        try {
            if (!plugin.vh.usingVault()) throw new Exception();
            format = format.replaceAll("(?i)\\{suffix}", plugin.vh.getChat().getGroupSuffix(plugin.getServer().getWorlds().get(0), group));
        } catch (Exception e) {
            String suffix = RUtils.getRChatGroupSuffix(group);
            if (suffix != null) format = format.replace("{suffix}", suffix);
            else format = format.replace("{suffix}", "");
        }
        format = format.replace("{group}", group);
        format = RUtils.colorize(format);
        return format;
    }

    public static String formatPrepend(Player p) {
        String format = Config.whoFormat;
        try {
            if (!plugin.vh.usingVault()) throw new Exception();
            format = format.replaceAll("(?i)\\{prefix}", plugin.vh.getChat().getPlayerPrefix(p));
        } catch (Exception e) {
            String prefix = RUtils.getRChatPrefix(p);
            if (prefix != null) format = format.replace("{prefix}", prefix);
            else format = format.replace("{prefix}", "");
        }
        try {
            if (!plugin.vh.usingVault()) throw new Exception();
            format = format.replaceAll("(?i)\\{group}", plugin.vh.getPermission().getPrimaryGroup(p));
        } catch (Exception e) {
            format = format.replaceAll("(?i)\\{group}", "");
        }
        try {
            if (plugin.vh.usingVault())
                format = format.replaceAll("(?i)\\{suffix}", plugin.vh.getChat().getPlayerSuffix(p));
        } catch (Exception e) {
            String suffix = RUtils.getRChatSuffix(p);
            if (suffix != null) format = format.replace("{suffix}", suffix);
            else format = format.replace("{suffix}", "");
        }
        format = format.replaceAll("(?i)\\{name}", p.getName());
        format = format.replaceAll("(?i)\\{dispname}", p.getDisplayName());
        format = RUtils.colorize(format);
        return format;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("list")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.list")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            cs.sendMessage(getNumOnline(cs));
            if (Config.simpleList) {
                String pList = getSimpleList(cs);
                if (pList.equals("")) return true;
                cs.sendMessage(pList);
            } else cs.sendMessage(getGroupList(cs));
            return true;
        }
        return false;
    }
}
