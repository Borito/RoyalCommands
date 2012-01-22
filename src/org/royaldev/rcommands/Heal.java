package org.royaldev.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class Heal implements CommandExecutor {

    RoyalCommands plugin;

    public Heal(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("heal")) {
            if (!plugin.isAuthorized(cs, "rcmds.heal")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                if (!(cs instanceof Player)) {
                    cs.sendMessage(ChatColor.RED
                            + "You can't heal the console!");
                    return true;
                }
                Player t = (Player) cs;
                t.sendMessage(ChatColor.BLUE + "You have healed yourself!");
                t.setHealth(20);
                return true;
            }
            Player t = plugin.getServer().getPlayer(args[0].trim());
            if (t == null) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            cs.sendMessage(ChatColor.BLUE + "You have healed " + ChatColor.GRAY
                    + t.getName() + ChatColor.BLUE + ".");
            t.sendMessage(ChatColor.BLUE + "You have been healed by "
                    + ChatColor.GRAY + cs.getName() + ChatColor.BLUE + "!");
            t.setHealth(20);
            return true;
        }
        return false;
    }

}
