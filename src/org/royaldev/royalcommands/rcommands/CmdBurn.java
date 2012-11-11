package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdBurn implements CommandExecutor {

    RoyalCommands plugin;

    public CmdBurn(RoyalCommands instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("burn")) {
            if (!plugin.isAuthorized(cs, "rcmds.burn")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player t = plugin.getServer().getPlayer(args[0]);
            if (t == null || plugin.isVanished(t, cs)) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            if (plugin.isAuthorized(t, "rcmds.exempt.burn")) {
                cs.sendMessage(ChatColor.RED + "You cannot burn that player!");
                return true;
            }
            int len = 5;
            if (args.length > 1) len = RUtils.timeFormatToSeconds(args[1]);
            if (len <= 0) {
                cs.sendMessage(ChatColor.RED + "Invalid time format.");
                return true;
            }
            cs.sendMessage(ChatColor.BLUE + "You have set " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + " on fire for " + ChatColor.GRAY + RUtils.formatDateDiff((len * 1000) + System.currentTimeMillis()).substring(1) + ChatColor.BLUE + ".");
            t.setFireTicks(len * 20);
            return true;
        }
        return false;
    }

}
