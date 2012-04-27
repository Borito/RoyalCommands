package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdWeather implements CommandExecutor {

    RoyalCommands plugin;

    public CmdWeather(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    public static boolean changeWeather(Player p, String conds) {
        World world = p.getWorld();
        if (conds.toLowerCase().trim().startsWith("sun")) {
            world.setStorm(false);
            world.setThundering(false);
            p.sendMessage(ChatColor.BLUE + "Set weather to " + ChatColor.GRAY + "sun" + ChatColor.BLUE + " in " + ChatColor.GRAY + world.getName() + ChatColor.BLUE + ".");
            return true;
        } else if (conds.toLowerCase().startsWith("rain")) {
            world.setStorm(true);
            world.setThundering(false);
            p.sendMessage(ChatColor.BLUE + "Set weather to " + ChatColor.GRAY + "rain" + ChatColor.BLUE + " in " + ChatColor.GRAY + world.getName() + ChatColor.BLUE + ".");
            return true;
        } else if (conds.toLowerCase().startsWith("storm")) {
            world.setStorm(true);
            world.setThundering(true);
            p.sendMessage(ChatColor.BLUE + "Set weather to " + ChatColor.GRAY + "storm" + ChatColor.BLUE + " in " + ChatColor.GRAY + world.getName() + ChatColor.BLUE + ".");
            return true;
        }
        return false;
    }

    public static boolean validWeather(String conds) {
        return conds.toLowerCase().startsWith("sun") || conds.toLowerCase().startsWith("rain") || conds.toLowerCase().startsWith("storm");
    }

    public static boolean changeWeather(Player p, String conds, int length) {
        if (length < 1) {
            p.sendMessage(ChatColor.RED + "The time specified was invalid!");
            return false;
        }
        World world = p.getWorld();
        if (conds.toLowerCase().trim().startsWith("sun")) {
            world.setStorm(false);
            world.setThundering(false);
            world.setWeatherDuration(length * 20);
            p.sendMessage(ChatColor.BLUE + "Set weather to " + ChatColor.GRAY + "sun" + ChatColor.BLUE + " in " + ChatColor.GRAY + world.getName() + ChatColor.BLUE + " for " + ChatColor.GRAY + length + ChatColor.BLUE + " seconds.");
            return true;
        } else if (conds.toLowerCase().startsWith("rain")) {
            world.setStorm(true);
            world.setThundering(false);
            world.setWeatherDuration(length * 20);
            p.sendMessage(ChatColor.BLUE + "Set weather to " + ChatColor.GRAY + "rain" + ChatColor.BLUE + " in " + ChatColor.GRAY + world.getName() + ChatColor.BLUE + " for " + ChatColor.GRAY + length + ChatColor.BLUE + " seconds.");
            return true;
        } else if (conds.toLowerCase().startsWith("storm")) {
            world.setStorm(true);
            world.setThundering(true);
            world.setWeatherDuration(length * 20);
            p.sendMessage(ChatColor.BLUE + "Set weather to " + ChatColor.GRAY + "storm" + ChatColor.BLUE + " in " + ChatColor.GRAY + world.getName() + ChatColor.BLUE + " for " + ChatColor.GRAY + length + ChatColor.BLUE + " seconds.");
            return true;
        } else {
            p.sendMessage(ChatColor.RED + "Invalid condition!");
            return false;
        }
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("weather")) {
            if (!plugin.isAuthorized(cs, "rcmds.weather")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }

            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            if (args.length == 1) {
                Player p = (Player) cs;
                changeWeather(p, args[0].trim());
                return true;
            } else if (args.length == 2) {
                Player p = (Player) cs;
                String conds = args[0].trim();
                String slength = args[1].trim();
                int length;
                try {
                    length = Integer.parseInt(slength);
                } catch (Exception e) {
                    p.sendMessage(ChatColor.RED + "The time specified was invalid!");
                    return true;
                }
                changeWeather(p, conds, length);
                return true;
            }
        }
        return false;
    }
}
