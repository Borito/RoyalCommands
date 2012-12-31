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
            Set<OfflinePlayer> banList = plugin.getServer().getBannedPlayers();
            if (banList.isEmpty()) {
                cs.sendMessage(ChatColor.RED + "There are no banned players!");
                return true;
            }
            cs.sendMessage(ChatColor.BLUE + "There are " + ChatColor.GRAY + banList.size() + ChatColor.BLUE + " banned players:");
            StringBuilder sb = new StringBuilder();
            for (OfflinePlayer op : banList) {
                sb.append(ChatColor.GRAY);
                sb.append(op.getName());
                sb.append(ChatColor.RESET);
                sb.append(", ");
            }
            cs.sendMessage(sb.substring(0, sb.length() - 4));
            return true;
        }
        return false;
    }

}
