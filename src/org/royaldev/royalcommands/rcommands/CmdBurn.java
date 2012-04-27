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
            String pname = args[0];
            Player t = plugin.getServer().getPlayer(pname.trim());
            if (t == null || plugin.isVanished(t)) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            if (plugin.isAuthorized(t, "rcmds.exempt.burn")) {
                cs.sendMessage(ChatColor.RED + "You cannot burn that player!");
                return true;
            }
            if (args.length == 1) {
                cs.sendMessage(ChatColor.BLUE + "You have set " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + " on fire for " + ChatColor.GRAY + "5" + ChatColor.BLUE + " seconds.");
                t.setFireTicks(100);
                return true;
            }
            if (args.length == 2) {
                Integer len;
                try {
                    len = Integer.parseInt(args[1].trim());
                } catch (Exception e) {
                    cs.sendMessage(ChatColor.RED + "Invalid number!");
                    return true;
                }
                if (len == null || len < 1) {
                    cs.sendMessage(ChatColor.RED + "Invalid number!");
                    return true;
                }
                cs.sendMessage(ChatColor.BLUE + "You have set " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + " on fire for " + ChatColor.GRAY + len + ChatColor.BLUE + " seconds.");
                t.setFireTicks(len * 20);
                return true;
            }
        }
        return false;
    }

}
