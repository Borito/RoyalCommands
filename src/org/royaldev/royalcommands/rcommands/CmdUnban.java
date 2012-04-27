package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdUnban implements CommandExecutor {

    RoyalCommands plugin;

    public CmdUnban(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("unban")) {
            if (!plugin.isAuthorized(cs, "rcmds.unban")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            OfflinePlayer t = plugin.getServer().getOfflinePlayer(
                    args[0].trim());
            if (!t.isBanned()) {
                cs.sendMessage(ChatColor.RED + "That player isn't banned!");
                return true;
            }
            t.setBanned(false);
            cs.sendMessage(ChatColor.BLUE + "You have unbanned "
                    + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
            plugin.getServer().broadcast(
                    ChatColor.BLUE + "The player " + ChatColor.GRAY
                            + cs.getName() + ChatColor.BLUE + " has unbanned "
                            + ChatColor.GRAY + t.getName() + ChatColor.BLUE
                            + ".", "rcmds.see.unban");
            return true;
        }
        return false;
    }

}
