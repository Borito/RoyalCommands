package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdCompass implements CommandExecutor {

    RoyalCommands plugin;

    public CmdCompass(RoyalCommands instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equals("compass")) {
            if (!plugin.isAuthorized(cs, "rcmds.compass")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
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
            if (args.length < 2) {
                if (command.equalsIgnoreCase("here")) {
                    p.setCompassTarget(p.getLocation());
                    cs.sendMessage(ChatColor.BLUE + "Your compass now points to your current location.");
                    return true;
                } else if (command.equalsIgnoreCase("reset")) {
                    p.setCompassTarget((p.getBedSpawnLocation() != null) ? p.getBedSpawnLocation() : p.getWorld().getSpawnLocation());
                    p.sendMessage(ChatColor.BLUE + "Reset your compass.");
                    return true;
                } else {
                    cs.sendMessage(cmd.getDescription());
                    return false;
                }
            }
            if (command.equalsIgnoreCase("player")) {
                Player t = plugin.getServer().getPlayer(args[1]);
                if (t == null || plugin.isVanished(t)) {
                    cs.sendMessage(ChatColor.RED + "That player does not exist!");
                    return true;
                }
                p.setCompassTarget(t.getLocation());
                cs.sendMessage(ChatColor.BLUE + "Your compass now points towards " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
                return true;
            }
            if (command.equalsIgnoreCase("location")) {
                if (args.length < 4) {
                    cs.sendMessage(cmd.getDescription());
                    return false;
                }
                double x;
                double y;
                double z;
                try {
                    x = Double.valueOf(args[1]);
                    y = Double.valueOf(args[2]);
                    z = Double.valueOf(args[3]);
                } catch (Exception e) {
                    cs.sendMessage(ChatColor.RED + "Those coordinates are invalid!");
                    return true;
                }
                Location cLocation = new Location(p.getWorld(), x, y, z);
                p.setCompassTarget(cLocation);
                cs.sendMessage(ChatColor.BLUE + "Your compass now points towards " + ChatColor.GRAY + x + ChatColor.BLUE + ", " + ChatColor.GRAY + y + ChatColor.BLUE + ", " + ChatColor.GRAY + z + ChatColor.BLUE + ".");
                return true;
            }
        }
        return false;
    }

}
