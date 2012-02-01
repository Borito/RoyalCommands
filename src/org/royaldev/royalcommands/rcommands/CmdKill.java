package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdKill implements CommandExecutor {

    RoyalCommands plugin;

    public CmdKill(RoyalCommands instance) {
        this.plugin = instance;
    }


    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("kill")) {
            if (!plugin.isAuthorized(cs, "rcmds.kill")) {
                RUtils.dispNoPerms(cs);
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
            if (plugin.isAuthorized(t, "rcmds.exempt.kill")) {
                cs.sendMessage(ChatColor.RED + "You cannot kill that player!");
                return true;
            }
            t.setHealth(0);
            cs.sendMessage(ChatColor.BLUE + "You have killed " + ChatColor.GRAY + t.getDisplayName() + ChatColor.BLUE + ".");
            return true;
        }
        return false;
    }
}
