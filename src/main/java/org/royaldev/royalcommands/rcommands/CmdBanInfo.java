package org.royaldev.royalcommands.rcommands;

import org.apache.commons.lang.BooleanUtils;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CmdBanInfo implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdBanInfo(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("baninfo")) {
            if (!plugin.isAuthorized(cs, "rcmds.baninfo")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            OfflinePlayer op = plugin.getServer().getPlayer(args[0]);
            if (op == null) op = plugin.getServer().getOfflinePlayer(args[0]);
            PConfManager pcm = PConfManager.getPConfManager(op);
            if (!pcm.exists()) {
                cs.sendMessage(ChatColor.RED + "That player has never played before!");
                return true;
            }
            if (!op.isBanned()) {
                cs.sendMessage(ChatColor.GRAY + op.getName() + ChatColor.RED + " is not banned!");
                return true;
            }
            cs.sendMessage(ChatColor.GRAY + op.getName() + ChatColor.BLUE + " is banned!");
            String banner = pcm.getString("banner", "Unknown");
            cs.sendMessage(ChatColor.BLUE + "Banned by " + ChatColor.GRAY + banner);
            String banReason = pcm.getString("banreason", "Unknown");
            cs.sendMessage(ChatColor.BLUE + "Banned for " + ChatColor.GRAY + banReason);
            long banDate = pcm.getLong("bannedat", -1L);
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, y hh:mm:ss a");
            String bannedAt = (banDate < 0L) ? "Unknown" : sdf.format(new Date(banDate));
            cs.sendMessage(ChatColor.BLUE + "Banned at " + ChatColor.GRAY + bannedAt);
            boolean isTempBan = pcm.get("bantime") != null;
            cs.sendMessage(ChatColor.BLUE + "Is tempban? " + ChatColor.GRAY + BooleanUtils.toStringYesNo(isTempBan));
            if (!isTempBan) return true;
            String expire = sdf.format(new Date(pcm.getLong("bantime")));
            cs.sendMessage(ChatColor.BLUE + "Tempban expires on " + ChatColor.GRAY + expire);
            return true;
        }
        return false;
    }

}
