package org.royaldev.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RoyalCommands;

public class Teleport implements CommandExecutor {

    RoyalCommands plugin;

    public Teleport(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("teleport")) {
            if (!plugin.isAuthorized(cs, "rcmds.teleport")) {
                cs.sendMessage(ChatColor.RED
                        + "You don't have permission for that!");
                plugin.log.warning("[RoyalCommands] " + cs.getName()
                        + " was denied access to the command!");
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED
                        + "This command is only available to players!");
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player t = plugin.getServer().getPlayer(args[0].trim());
            if (t == null || plugin.isVanished(t)) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            if (plugin.isAuthorized(t, "rcmds.exempt.teleport")) {
                cs.sendMessage(ChatColor.RED
                        + "You may not teleport with that player.");
                return true;
            }
            Player p = (Player) cs;
            Back.backdb.put(p, p.getLocation());
            p.sendMessage(ChatColor.BLUE + "Teleporting you to "
                    + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
            p.teleport(t.getLocation());
            return true;
        }
        return false;
    }

}
