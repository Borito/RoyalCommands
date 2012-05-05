package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdFly implements CommandExecutor {

    RoyalCommands plugin;

    public CmdFly(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("fly")) {
            if (!plugin.isAuthorized(cs, "rcmds.fly")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player) & args.length < 1) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            if (args.length < 1) {
                Player p = (Player) cs;
                if (p.getAllowFlight()) p.setAllowFlight(false);
                else p.setAllowFlight(true);
                String status = (p.getAllowFlight()) ? "on" : "off";
                p.sendMessage(ChatColor.BLUE + "Toggled flight to " + ChatColor.GRAY + status + ChatColor.BLUE + ".");
            } else {
                if (!plugin.isAuthorized(cs, "rcmds.others.fly")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                Player t = plugin.getServer().getPlayer(args[0]);
                if (t == null || plugin.isVanished(t)) {
                    cs.sendMessage(ChatColor.RED + "That player does not exist!");
                    return true;
                }
                if (t.getAllowFlight()) t.setAllowFlight(false);
                else t.setAllowFlight(true);
                String status = (t.getAllowFlight()) ? "on" : "off";
                cs.sendMessage(ChatColor.BLUE + "Toggled flight to " + ChatColor.GRAY + status + ChatColor.BLUE + " on " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
                t.sendMessage(ChatColor.BLUE + "You have had flight toggled to " + ChatColor.GRAY + status + ChatColor.BLUE + ".");
            }
            return true;
        }
        return false;
    }

}
