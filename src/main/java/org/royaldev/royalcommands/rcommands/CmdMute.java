package org.royaldev.royalcommands.rcommands;

import org.apache.commons.lang.BooleanUtils;
import org.royaldev.royalcommands.MessageColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.configuration.PConfManager;
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
            PConfManager pcm = PConfManager.getPConfManager(t);
            if (!pcm.exists()) {
                if (!t.isOnline() && !t.hasPlayedBefore()) {
                    cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                    return true;
                }
                pcm.createFile();
            }
            if (cs.getName().equalsIgnoreCase(t.getName())) {
                cs.sendMessage(MessageColor.NEGATIVE + "You can't mute yourself!");
                return true;
            }
            if (plugin.isAuthorized(t, "rcmds.exempt.mute")) {
                cs.sendMessage(MessageColor.NEGATIVE + "You can't mute that player!");
                return true;
            }
            Boolean isMuted = pcm.getBoolean("muted");
            if (isMuted == null) isMuted = false;
            long muteTime = 0L;
            if (args.length > 1) muteTime = (long) RUtils.timeFormatToSeconds(args[1]);
            if (muteTime < 0L) {
                cs.sendMessage(MessageColor.NEGATIVE + "Invalid time format!");
                return true;
            }
            pcm.set("muted", !isMuted);
            if (muteTime > 0L && !isMuted) pcm.set("mutetime", muteTime);
            else if (isMuted) pcm.set("mutetime", null);
            pcm.set("mutedat", System.currentTimeMillis());
            String timePeriod = (muteTime > 0L && !isMuted) ? " for " + MessageColor.NEUTRAL + RUtils.formatDateDiff((muteTime * 1000L) + System.currentTimeMillis()).substring(1) : "";
            cs.sendMessage(MessageColor.POSITIVE + "You have toggled mute " + MessageColor.NEUTRAL + BooleanUtils.toStringOnOff(!isMuted) + MessageColor.POSITIVE + " for " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + timePeriod + MessageColor.POSITIVE + ".");
            if (t.isOnline()) {
                if (!isMuted) // if is being muted
                    t.getPlayer().sendMessage(MessageColor.NEGATIVE + "You have been muted by " + MessageColor.NEUTRAL + cs.getName() + MessageColor.NEGATIVE + timePeriod + MessageColor.NEGATIVE + ".");
                else
                    t.getPlayer().sendMessage(MessageColor.POSITIVE + "You have been unmuted by " + MessageColor.NEUTRAL + cs.getName() + MessageColor.POSITIVE + ".");
            }
            return true;
        }
        return false;
    }
}
