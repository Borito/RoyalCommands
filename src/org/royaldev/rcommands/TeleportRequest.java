package org.royaldev.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.HashMap;

public class TeleportRequest implements CommandExecutor {

    RoyalCommands plugin;

    public TeleportRequest(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    public static HashMap<Player, CommandSender> tprdb = new HashMap<Player, CommandSender>();

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("teleportrequest")) {
            if (!plugin.isAuthorized(cs, "rcmds.teleportrequest")) {
                cs.sendMessage(ChatColor.RED
                        + "You don't have permission for that!");
                plugin.log.warning("[RoyalCommands] " + cs.getName()
                        + " was denied access to the command!");
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player t = plugin.getServer().getPlayer(args[0].trim());
            if (t == null) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            if (plugin.isVanished(t)) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            if (plugin.isAuthorized(t, "rcmds.exempt.teleport")) {
                cs.sendMessage(ChatColor.RED
                        + "You may not teleport with that player.");
                return true;
            }
            tprdb.put(t, cs);
            cs.sendMessage(ChatColor.BLUE + "Sent request to " + ChatColor.GRAY
                    + t.getName() + ChatColor.BLUE + ".");
            t.sendMessage(ChatColor.GRAY + cs.getName() + ChatColor.BLUE
                    + " has requested to teleport to you.");
            t.sendMessage(ChatColor.BLUE + "Type " + ChatColor.GRAY
                    + "/tpaccept" + ChatColor.BLUE + " or " + ChatColor.GRAY
                    + "/tpdeny" + ChatColor.BLUE + ".");
            return true;
        }
        return false;
    }

}
