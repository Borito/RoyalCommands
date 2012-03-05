package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.Date;

public class CmdTempban implements CommandExecutor {

    RoyalCommands plugin;

    public CmdTempban(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tempban")) {
            if (!plugin.isAuthorized(cs, "rcmds.tempban")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 2) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            OfflinePlayer t = plugin.getServer().getOfflinePlayer(args[0]);
            if (!PConfManager.getPConfExists(t)) {
                cs.sendMessage(ChatColor.RED + "That player doesn't exist!");
                return true;
            }
            if (t.isOnline()) {
                Player t2 = (Player) t;
                if (plugin.isAuthorized(t2, "rcmds.exempt.ban")) {
                    cs.sendMessage(ChatColor.RED + "You cannot ban that player!");
                    return true;
                }
            }
            if (t.isOp()) {
                cs.sendMessage(ChatColor.RED + "You cannot ban that player!");
                return true;
            }
            long time;
            try {
                time = Long.valueOf(args[1]);
            } catch (Exception e) {
                cs.sendMessage(ChatColor.RED + "Invalid time!");
                return true;
            }
            if (time < 1) {
                cs.sendMessage(ChatColor.RED + "Time must be greater than 0!");
                return true;
            }
            String banreason = RUtils.formatDateDiff(new Date().getTime() + (time*1000));
            RUtils.setTimeStamp(t, time, "bantime");
            t.setBanned(true);
            PConfManager.setPValString(t, banreason, "banreason");
            PConfManager.setPValString(t, cs.getName(), "banner");
            cs.sendMessage(ChatColor.BLUE + "You have banned " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + " for" + ChatColor.GRAY + banreason + ChatColor.BLUE + ".");
            plugin.getServer().broadcast(ChatColor.RED + "The player " + ChatColor.GRAY + t.getName() + ChatColor.RED + " has been banned for" + ChatColor.GRAY + banreason + ChatColor.RED + " by " + ChatColor.GRAY + cs.getName() + ChatColor.RED + ".", "rcmds.see.ban");
            if (t.isOnline()) ((Player) t).kickPlayer("Banned for" + banreason);
            return true;
        }
        return false;
    }

}
