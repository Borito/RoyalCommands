package org.royaldev.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RoyalCommands;

public class Slap implements CommandExecutor {

    RoyalCommands plugin;

    public Slap(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("slap")) {
            if (!plugin.isAuthorized(cs, "rcmds.slap")) {
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
            Player victim;
            victim = plugin.getServer().getPlayer(args[0]);
            if (plugin.isVanished(victim)) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            if (victim == null) {
                cs.sendMessage(ChatColor.RED + "That person is not online!");
                return true;
            }
            if (plugin.isAuthorized(victim, "rcmds.exempt.slap")) {
                cs.sendMessage(ChatColor.RED + "You may not slap that player.");
                return true;
            }
            plugin.getServer().broadcastMessage(
                    ChatColor.GOLD + cs.getName() + ChatColor.WHITE + " slaps "
                            + ChatColor.RED + victim.getName()
                            + ChatColor.WHITE + "!");
            return true;
        }
        return false;
    }

}
