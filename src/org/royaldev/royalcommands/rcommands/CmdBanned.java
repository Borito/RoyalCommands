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
            OfflinePlayer t = plugin.getServer().getPlayer(args[0]);
            if (t == null) t = plugin.getServer().getOfflinePlayer(args[0]);
            if (!t.isBanned()) {
                cs.sendMessage(ChatColor.GRAY + t.getName() + ChatColor.BLUE + " is not banned.");
                return true;
            }
            cs.sendMessage(ChatColor.GRAY + t.getName() + ChatColor.RED + " is banned.");
            return true;
        }
        return false;
    }

}
