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

public class CmdTime implements CommandExecutor {

    RoyalCommands plugin;

    public CmdTime(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    public static void smoothTimeChange(long time, World world) {
        /*if (time >= world.getTime()) for (long i = world.getTime(); i < time; i++) world.setTime(i);
        else for (long i = world.getTime(); i > time; i--) world.setTime(i);*/
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
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player p = (Player) cs;
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
