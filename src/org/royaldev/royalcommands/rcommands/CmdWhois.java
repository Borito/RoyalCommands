package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdWhois implements CommandExecutor {

    RoyalCommands plugin;

    public CmdWhois(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("whois")) {
            if (!plugin.isAuthorized(cs, "rcmds.whois")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            OfflinePlayer t = plugin.getServer().getOfflinePlayer(args[0]);
            if (!t.hasPlayedBefore()) {
                cs.sendMessage(ChatColor.RED + "That player has never played before!");
                return true;
            }
            String ip = PConfManager.getPValString(t, "ip");
            String name = t.getName();
            String dispname = PConfManager.getPValString(t, "dispname");
            cs.sendMessage(ChatColor.BLUE + "=====================");
            cs.sendMessage(ChatColor.BLUE + "Whois for " + ChatColor.GRAY + name);
            cs.sendMessage(ChatColor.BLUE + "Nickname: " + ChatColor.GRAY + dispname);
            cs.sendMessage(ChatColor.BLUE + "IP: " + ChatColor.GRAY + ip);
            cs.sendMessage(ChatColor.BLUE + "Is VIP: " + ChatColor.GRAY + ((PConfManager.getPValBoolean(t, "vip")) ? "yes" : "no"));
            long timestamp = RUtils.getTimeStamp(t, "seen");
            String lastseen = (timestamp < 0) ? "unknown" : RUtils.formatDateDiff(timestamp);
            cs.sendMessage(ChatColor.BLUE + "Last seen: " + ChatColor.GRAY + lastseen);
            cs.sendMessage(ChatColor.BLUE + "=====================");
            return true;
        }
        return false;
    }

}
