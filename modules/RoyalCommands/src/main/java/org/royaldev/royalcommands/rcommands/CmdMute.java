package org.royaldev.royalcommands.rcommands;

import mkremins.fanciful.FancyMessage;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

// TODO: Mute message (-m)

@ReflectCommand
public class CmdMute extends CACommand {

    public CmdMute(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] eargs, CommandArguments ca) {
        if (eargs.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        final OfflinePlayer t = RUtils.getOfflinePlayer(eargs[0]);
        final PConfManager pcm = PConfManager.getPConfManager(t);
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
        if (this.ah.isAuthorized(t, cmd, PermType.EXEMPT)) {
            cs.sendMessage(MessageColor.NEGATIVE + "You can't mute that player!");
            return true;
        }
        final boolean wasMuted = pcm.getBoolean("muted");
        long muteTime = 0L;
        if (eargs.length > 1) muteTime = (long) RUtils.timeFormatToSeconds(eargs[1]);
        if (muteTime < 0L) {
            cs.sendMessage(MessageColor.NEGATIVE + "Invalid time format!");
            return true;
        }
        final String reason = RUtils.colorize(ca.getFlagString("r", "reason", "m", "msg", "message"));
        pcm.set("muted", !wasMuted);
        if (muteTime > 0L && !wasMuted) pcm.set("mutetime", muteTime);
        else if (wasMuted) pcm.set("mutetime", null);
        pcm.set("mutedat", System.currentTimeMillis());
        FancyMessage fm = new FancyMessage("You have toggled mute ").color(MessageColor.POSITIVE._()).then(wasMuted ? "off" : "on").color(MessageColor.NEUTRAL._()).then(" for ").color(MessageColor.POSITIVE._()).then(t.getName()).color(MessageColor.NEUTRAL._()).formattedTooltip(RUtils.getPlayerTooltip(t));
        if (muteTime > 0L && !wasMuted) {
            fm.then(" for ").color(MessageColor.POSITIVE._()).then(RUtils.formatDateDiff((muteTime * 1000L) + System.currentTimeMillis()).substring(1)).color(MessageColor.NEUTRAL._());
        }
        if (!reason.isEmpty()) {
            fm.then(" for ").color(MessageColor.POSITIVE._()).then(reason).color(MessageColor.NEUTRAL._());
        }
        fm.then(".").color(MessageColor.POSITIVE._());
        fm.send(cs);
        if (t.isOnline()) {
            fm = new FancyMessage("You have been ").color(MessageColor.POSITIVE._()).then(wasMuted ? "unmuted" : "muted").color(MessageColor.NEUTRAL._()).then(" by ").color(MessageColor.POSITIVE._()).then(cs.getName()).color(MessageColor.NEUTRAL._()).formattedTooltip(RUtils.getPlayerTooltip(cs));
            if (muteTime > 0L && !wasMuted) {
                fm.then(" for ").color(MessageColor.POSITIVE._()).then(RUtils.formatDateDiff((muteTime * 1000L) + System.currentTimeMillis()).substring(1)).color(MessageColor.NEUTRAL._());
            }
            if (!reason.isEmpty()) {
                fm.then(" for ").color(MessageColor.POSITIVE._()).then(reason).color(MessageColor.NEUTRAL._());
            }
            fm.then(".").color(MessageColor.POSITIVE._());
            fm.send(t.getPlayer());
        }
        return true;
    }
}
