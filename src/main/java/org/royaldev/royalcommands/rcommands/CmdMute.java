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

public class CmdMute implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdMute(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    // muted & mutetime
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("mute")) {
            if (!plugin.isAuthorized(cs, "rcmds.mute")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            OfflinePlayer t = plugin.getServer().getPlayer(args[0]);
            if (t == null) t = plugin.getServer().getOfflinePlayer(args[0]);
            PConfManager pcm = plugin.getUserdata(t);
            if (!pcm.exists()) {
                if (!t.isOnline() && !t.hasPlayedBefore()) {
                    cs.sendMessage(ChatColor.RED + "That player does not exist!");
                    return true;
                }
                pcm.createFile();
            }
            if (cs.getName().equalsIgnoreCase(t.getName())) {
                cs.sendMessage(ChatColor.RED + "You can't mute yourself!");
                return true;
            }
            if (plugin.isAuthorized(t, "rcmds.exempt.mute")) {
                cs.sendMessage(ChatColor.RED + "You can't mute that player!");
                return true;
            }
            Boolean isMuted = pcm.getBoolean("muted");
            if (isMuted == null) isMuted = false;
            long muteTime = 0L;
            if (args.length > 1) muteTime = (long) RUtils.timeFormatToSeconds(args[1]);
            if (muteTime < 0L) {
                cs.sendMessage(ChatColor.RED + "Invalid time format!");
                return true;
            }
            pcm.set("muted", !isMuted);
            if (muteTime > 0L && !isMuted) pcm.set("mutetime", muteTime);
            else if (isMuted) pcm.set("mutetime", null);
            pcm.set("mutedat", System.currentTimeMillis());
            String timePeriod = (muteTime > 0L && !isMuted) ? " for " + ChatColor.GRAY + RUtils.formatDateDiff((muteTime * 1000L) + System.currentTimeMillis()).substring(1) : "";
            cs.sendMessage(ChatColor.BLUE + "You have toggled mute " + ChatColor.GRAY + BooleanUtils.toStringOnOff(!isMuted) + ChatColor.BLUE + " for " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + timePeriod + ChatColor.BLUE + ".");
            if (t.isOnline()) {
                if (!isMuted) // if is being muted
                    t.getPlayer().sendMessage(ChatColor.RED + "You have been muted by " + ChatColor.GRAY + cs.getName() + ChatColor.RED + timePeriod + ChatColor.RED + ".");
                else
                    t.getPlayer().sendMessage(ChatColor.BLUE + "You have been unmuted by " + ChatColor.GRAY + cs.getName() + ChatColor.BLUE + ".");
            }
            return true;
        }
        return false;
    }
}
