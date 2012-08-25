package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.Set;

public class CmdBanList implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdBanList(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("banlist")) {
            if (!plugin.isAuthorized(cs, "rcmds.banlist")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            cs.sendMessage(ChatColor.BLUE + "All banned players:");
            Set<OfflinePlayer> banList = plugin.getServer().getBannedPlayers();
            if (banList.isEmpty()) {
                cs.sendMessage(ChatColor.RED + "There are no banned players!");
                return true;
            }
            for (OfflinePlayer op : banList)
                cs.sendMessage(ChatColor.GRAY + op.getName());
            return true;
        }
        return false;
    }

}
