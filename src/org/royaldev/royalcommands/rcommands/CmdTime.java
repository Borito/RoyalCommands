package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class CmdTime implements CommandExecutor {

    RoyalCommands plugin;

    public CmdTime(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    public static void smoothTimeChange(long time, World world) {
        for (long i = world.getTime() + 1; i != time; i++) {
            if (i == 24001) {
                i = 0;
                if (time == 0) break;
            }
            world.setTime(i);
        }
    }

    public static Long getValidTime(String time) {
        Long vtime;
        try {
            vtime = Long.valueOf(time);
            if (vtime > 24000L) vtime = vtime % 24000L;
        } catch (Exception e) {
            if (time.equalsIgnoreCase("day")) vtime = 0L;
            else if (time.equalsIgnoreCase("midday")) vtime = 6000L;
            else if (time.equalsIgnoreCase("sunset")) vtime = 12000L;
            else if (time.equalsIgnoreCase("night")) vtime = 14000L;
            else if (time.equalsIgnoreCase("midnight")) vtime = 18000L;
            else if (time.equalsIgnoreCase("sunrise")) vtime = 23000L;
            else return null;
        }
        return vtime;
    }

    public static Map<String, String> getRealTime(long ticks) {
        if (ticks > 24000L) ticks = ticks % 24000L;
        DecimalFormat df = new DecimalFormat("00");
        df.setRoundingMode(RoundingMode.DOWN);
        float thour = 1000F;
        float tminute = 16.6666666666666666666666666666666666666666666666666666666666666666666666F;
        float hour = (ticks / thour) + 6F;
        if (hour >= 24F) hour = hour - 24F;
        float minute = (ticks % thour) / tminute;
        String meridian = (hour >= 12F) ? "PM" : "AM";
        float twelvehour = (hour > 12F) ? hour - 12F : hour;
        if (df.format(twelvehour).equals("00")) twelvehour = 12F;
        Map<String, String> toRet = new HashMap<String, String>();
        toRet.put("24h", df.format(hour) + ":" + df.format(minute));
        toRet.put("12h", df.format(twelvehour) + ":" + df.format(minute) + " " + meridian);
        return toRet;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("time")) {
            if (!plugin.isAuthorized(cs, "rcmds.time")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (cs instanceof ConsoleCommandSender) {
                if (args.length < 1) {
                    cs.sendMessage(cmd.getDescription());
                    for (World w : plugin.getServer().getWorlds()) {
                        long ticks = w.getTime();
                        Map<String, String> times = getRealTime(ticks);
                        cs.sendMessage(ChatColor.BLUE + "Current time in " + ChatColor.GRAY + w.getName() + ChatColor.BLUE + " is " + ChatColor.GRAY + ticks + ChatColor.BLUE + " (" + ChatColor.GRAY + times.get("24h") + ChatColor.BLUE + "/" + ChatColor.GRAY + times.get("12h") + ChatColor.BLUE + ").");
                    }
                    return false;
                }
                if (getValidTime(args[0]) == null) {
                    cs.sendMessage(ChatColor.RED + "Invalid time specified!");
                    return true;
                }
                long time = getValidTime(args[0]);
                if (time < 0) {
                    cs.sendMessage(ChatColor.RED + "The time entered was invalid!");
                    return true;
                }
                if (args.length == 1) {
                    for (World w : plugin.getServer().getWorlds()) {
                        if (plugin.smoothTime) smoothTimeChange(time, w);
                        w.setTime(time);
                    }
                    cs.sendMessage(ChatColor.BLUE + "Time in all worlds set to " + ChatColor.GRAY + time + ChatColor.BLUE + ".");
                }
                if (args.length > 1) {
                    World w = plugin.getServer().getWorld(args[1].trim());
                    if (w == null) {
                        cs.sendMessage(ChatColor.RED + "No such world!");
                        return true;
                    }
                    if (plugin.smoothTime) smoothTimeChange(time, w);
                    w.setTime(time);
                    cs.sendMessage(ChatColor.BLUE + "Time in world " + ChatColor.GRAY + w.getName() + ChatColor.BLUE + " set to " + ChatColor.GRAY + time + ChatColor.BLUE + ".");
                }
                return true;
            }
            Player p = (Player) cs;
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                long ticks = p.getWorld().getTime();
                Map<String, String> times = getRealTime(ticks);
                cs.sendMessage(ChatColor.BLUE + "Current time is " + ChatColor.GRAY + ticks + ChatColor.BLUE + " (" + ChatColor.GRAY + times.get("24h") + ChatColor.BLUE + "/" + ChatColor.GRAY + times.get("12h") + ChatColor.BLUE + ").");
                return false;
            }
            World world = (args.length > 1) ? plugin.getServer().getWorld(args[1]) : p.getWorld();
            if (world == null) world = p.getWorld();
            if (getValidTime(args[0]) == null) {
                cs.sendMessage(ChatColor.RED + "Invalid time specified!");
                return true;
            }
            long time = getValidTime(args[0]);
            if (time < 0) {
                cs.sendMessage(ChatColor.RED + "The time entered was invalid!");
                return true;
            }
            if (plugin.smoothTime) smoothTimeChange(time, world);
            world.setTime(time);
            p.sendMessage(ChatColor.BLUE + "Set time in " + ChatColor.GRAY + world.getName() + ChatColor.BLUE + " to " + ChatColor.GRAY + time + ChatColor.BLUE + " ticks.");
            return true;
        }
        return false;
    }
}
