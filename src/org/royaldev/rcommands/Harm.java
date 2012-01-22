package org.royaldev.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class Harm implements CommandExecutor {

    RoyalCommands plugin;

    public Harm(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("harm")) {
            if (!plugin.isAuthorized(cs, "rcmds.harm")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 2) {
                return false;
            }
            Player victim = plugin.getServer().getPlayer(args[0].trim());
            if (victim == null) {
                cs.sendMessage(ChatColor.RED + "That person is not online!");
                return true;
            }
            if (plugin.isVanished(victim)) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            int toDamage = 0;
            try {
                toDamage = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                cs.sendMessage(ChatColor.RED
                        + "The damage must be a number between 1 and 20!");
                return false;
            }
            if (toDamage > 20 || toDamage <= 0) {
                cs.sendMessage(ChatColor.RED
                        + "The damage you entered is not within 1 and 20!");
                return true;
            }

            if (plugin.isAuthorized(victim, "rcmds.exempt.harm")) {
                cs.sendMessage(ChatColor.RED + "You may not harm that player.");
                return true;
            }
            victim.damage(toDamage);
            victim.sendMessage(ChatColor.RED + "You have just been damaged by "
                    + ChatColor.BLUE + cs.getName() + ChatColor.RED + "!");
            cs.sendMessage(ChatColor.BLUE + "You just damaged " + ChatColor.RED
                    + victim.getName() + ChatColor.BLUE + "!");
            return true;
        }
        return false;
    }

}
