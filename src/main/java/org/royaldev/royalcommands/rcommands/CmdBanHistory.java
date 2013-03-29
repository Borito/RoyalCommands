package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CmdBanHistory implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdBanHistory(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("banhistory")) {
            if (!plugin.isAuthorized(cs, "rcmds.banhistory")) {
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
            List<String> prevBans = pcm.getStringList("prevbans");
            if (prevBans == null) prevBans = new ArrayList<String>();
            if (args.length < 2) {
                if (prevBans.size() < 1) {
                    cs.sendMessage(ChatColor.RED + "That player has no previous bans.");
                    return true;
                }
                cs.sendMessage(ChatColor.GRAY + op.getName() + ChatColor.BLUE + " has " + ChatColor.GRAY + prevBans.size() + ChatColor.BLUE + " bans.");
                cs.sendMessage(ChatColor.BLUE + "Please do " + ChatColor.GRAY + "/" + label + " " + op.getName() + " #" + ChatColor.BLUE + " to view a specific ban.");
                return true;
            }
            int number;
            try {
                number = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                cs.sendMessage(ChatColor.RED + "The ban selected must be a number!");
                return true;
            }
            number -= 1; // lists 'n stuff
            String ban;
            try {
                ban = prevBans.get(number);
            } catch (IndexOutOfBoundsException e) {
                cs.sendMessage(ChatColor.RED + "Invalid ban number!");
                return true;
            }
            // banner,banreason,bannedat,istempban
            String[] baninfo = ban.split("\\u00b5");
            cs.sendMessage(ChatColor.BLUE + "Ban log for ban " + ChatColor.GRAY + (number + 1) + ChatColor.BLUE + " of " + ChatColor.GRAY + prevBans.size() + ChatColor.BLUE + " for " + ChatColor.GRAY + op.getName() + ChatColor.BLUE + ".");
            String banner = baninfo[0];
            if (banner.equals("null")) banner = "Unknown";
            cs.sendMessage(ChatColor.BLUE + "Banned by " + ChatColor.GRAY + banner);
            String banReason = baninfo[1];
            if (banReason.equals("null")) banReason = "Unknown";
            cs.sendMessage(ChatColor.BLUE + "Banned for " + ChatColor.GRAY + banReason);
            String banDateString = baninfo[2];
            if (banDateString.equals("null")) banDateString = "-1";
            long banDate;
            try {
                banDate = Long.parseLong(banDateString);
            } catch (NumberFormatException e) {
                banDate = -1L;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, y hh:mm:ss a");
            String bannedAt = (banDate < 0L) ? "Unknown" : sdf.format(new Date(banDate));
            cs.sendMessage(ChatColor.BLUE + "Banned at " + ChatColor.GRAY + bannedAt);
            boolean isTempBan = baninfo[3].equalsIgnoreCase("true");
            cs.sendMessage(ChatColor.BLUE + "Was tempban? " + ChatColor.GRAY + ((isTempBan) ? "Yes" : "No"));
            return true;
        }
        return false;
    }

}
