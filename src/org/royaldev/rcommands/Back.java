package org.royaldev.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.HashMap;

public class Back implements CommandExecutor {

    RoyalCommands plugin;

    public Back(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    public static HashMap<Player, Location> backdb = new HashMap<Player, Location>();

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("back")) {
            if (!plugin.isAuthorized(cs, "rcmds.back")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;

            if (!backdb.containsKey(p)) {
                cs.sendMessage(ChatColor.RED + "You have no place to go back to!");
                return true;
            }

            Location to = backdb.get(p);
            backdb.put(p, p.getLocation());
            p.teleport(to);
            p.sendMessage(ChatColor.BLUE + "Returning to your previous location.");
            return true;
        }
        return false;
    }

}
