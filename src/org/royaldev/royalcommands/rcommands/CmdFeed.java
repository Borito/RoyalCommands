package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdFeed implements CommandExecutor {

    RoyalCommands plugin;

    public CmdFeed(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("feed")) {
            if (!plugin.isAuthorized(cs, "rcmds.feed")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                if (!(cs instanceof Player)) {
                    cs.sendMessage(ChatColor.RED + "You can't feed the console!");
                    return true;
                }
                Player t = (Player) cs;
                t.sendMessage(ChatColor.BLUE + "You have fed yourself!");
                t.setFoodLevel(20);
                t.setSaturation(20);
                return true;
            }
            Player t = plugin.getServer().getPlayer(args[0]);
            if (!plugin.isAuthorized(cs, "rcmds.others.feed")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (t == null || plugin.isVanished(t)) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            cs.sendMessage(ChatColor.BLUE + "You have fed " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
            t.sendMessage(ChatColor.BLUE + "You have been fed by " + ChatColor.GRAY + cs.getName() + ChatColor.BLUE + "!");
            t.setFoodLevel(20);
            t.setSaturation(20);
            return true;
        }
        return false;
    }

}
