package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.List;

public class CmdRemove implements CommandExecutor {

    RoyalCommands plugin;

    public CmdRemove(RoyalCommands instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("remove")) {
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player p = (Player) cs;
            String command = args[0];
            int radius = -1;
            if (args.length > 1) {
                try {
                    radius = Integer.valueOf(args[1]);
                    if (radius < 0) {
                        cs.sendMessage(ChatColor.RED + "Invalid radius!");
                        return true;
                    }
                } catch (Exception e) {
                    cs.sendMessage(ChatColor.RED + "Invalid radius!");
                    return true;
                }
            }
            List<Entity> entlist = (radius < 0) ? p.getWorld().getEntities() : p.getNearbyEntities(radius, radius, radius);
            int count = 0;
            if (command.equalsIgnoreCase("mobs")) {
                for (Entity e : entlist) {
                    if (!(e instanceof LivingEntity)) continue;
                    if (e instanceof Player) continue;
                    e.remove();
                    count++;
                }
                cs.sendMessage(ChatColor.BLUE + "Removed " + ChatColor.GRAY + count + " " + ((count != 1) ? "mobs" : "mob") + ChatColor.BLUE + ".");
            } else if (command.equalsIgnoreCase("arrows")) {
                for (Entity e : entlist) {
                    if (e instanceof Arrow) {
                        e.remove();
                        count++;
                    }
                }
                cs.sendMessage(ChatColor.BLUE + "Removed " + ChatColor.GRAY + count + " " + ((count != 1) ? "arrows" : "arrow") + ChatColor.BLUE + ".");
            } else if (command.equalsIgnoreCase("boats")) {
                for (Entity e : entlist) {
                    if (e instanceof Boat) {
                        e.remove();
                        count++;
                    }
                }
                cs.sendMessage(ChatColor.BLUE + "Removed " + ChatColor.GRAY + count + " " + ((count != 1) ? "boats" : "boat") + ChatColor.BLUE + ".");
            } else if (command.equalsIgnoreCase("littnt")) {
                for (Entity e : entlist) {
                    if (e instanceof TNTPrimed) {
                        e.remove();
                        count++;
                    }
                }
                cs.sendMessage(ChatColor.BLUE + "Removed " + ChatColor.GRAY + count + " tnt"  + ChatColor.BLUE + ".");
            } else if (command.equalsIgnoreCase("all")) {
                for (Entity e : entlist) {
                    if (e instanceof Player) continue;
                    e.remove();
                    count++;
                }
                cs.sendMessage(ChatColor.BLUE + "Removed " + ChatColor.GRAY + count + " " + ((count != 1) ? "entities" : "entity") + ChatColor.BLUE + ".");
            }
            return true;
        }
        return false;
    }

}
