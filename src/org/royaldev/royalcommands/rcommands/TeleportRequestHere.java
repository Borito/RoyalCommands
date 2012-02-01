package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.HashMap;

public class TeleportRequestHere implements CommandExecutor {

    RoyalCommands plugin;

    public TeleportRequestHere(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    public static HashMap<Player, CommandSender> tprhdb = new HashMap<Player, CommandSender>();

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("teleportrequesthere")) {
            if (!plugin.isAuthorized(cs, "rcmds.teleportrequesthere")) {
                RUtils.dispNoPerms(cs);
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
                cs.sendMessage(ChatColor.RED + "You may not teleport with that player.");
                return true;
            }
            tprhdb.put(t, cs);
            cs.sendMessage(ChatColor.BLUE + "Sent request to " + ChatColor.GRAY
                    + t.getName() + ChatColor.BLUE + ".");
            t.sendMessage(ChatColor.GRAY + cs.getName() + ChatColor.BLUE
                    + " has requested you to teleport to them.");
            t.sendMessage(ChatColor.BLUE + "Type " + ChatColor.GRAY
                    + "/tpaccept" + ChatColor.BLUE + " or " + ChatColor.GRAY
                    + "/tpdeny" + ChatColor.BLUE + ".");
            return true;
        }
        return false;
    }

}
