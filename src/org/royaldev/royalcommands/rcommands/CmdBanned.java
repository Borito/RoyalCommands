package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdBanned implements CommandExecutor {

    RoyalCommands plugin;

    public CmdBanned(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("banned")) {
            if (!plugin.isAuthorized(cs, "rcmds.banned")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            OfflinePlayer t = plugin.getServer().getOfflinePlayer(args[0]);
            if (!t.isBanned()) {
                cs.sendMessage(ChatColor.GREEN + t.getName() + ChatColor.WHITE + " is not banned.");
                return true;
            }
            cs.sendMessage(ChatColor.RED + t.getName() + ChatColor.WHITE + " is banned.");
            return true;
        }
        return false;
    }

}
