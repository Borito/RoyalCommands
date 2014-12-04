package org.royaldev.royalcommands.rcommands;

import org.bukkit.BanList.Type;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

import java.util.Date;

@ReflectCommand
public class CmdTemporaryBan extends BaseCommand {

    public CmdTemporaryBan(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (args.length < 3) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        OfflinePlayer t = this.plugin.getServer().getPlayer(args[0]);
        if (t == null) t = this.plugin.getServer().getOfflinePlayer(args[0]);
        PConfManager pcm = PConfManager.getPConfManager(t);
        if (!pcm.exists()) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player doesn't exist!");
            return true;
        }
        if (this.ah.isAuthorized(t, "rcmds.exempt.ban")) {
            cs.sendMessage(MessageColor.NEGATIVE + "You cannot ban that player!");
            return true;
        }
        long time = (long) RUtils.timeFormatToSeconds(args[1]);
        if (time <= 0L) {
            cs.sendMessage(MessageColor.NEGATIVE + "Invalid amount of time specified!");
            return true;
        }
        time++; // fix for always being a second short;
        long curTime = new Date().getTime();
        String banreason = RoyalCommands.getFinalArg(args, 2);
        this.plugin.getServer().getBanList(Type.NAME).addBan(t.getName(), banreason, null, cs.getName());
        pcm.set("bantime", (time * 1000L) + curTime);
        pcm.set("bannedat", curTime);
        pcm.set("banreason", banreason);
        pcm.set("banner", cs.getName());
        cs.sendMessage(MessageColor.POSITIVE + "You have banned " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + " for " + MessageColor.NEUTRAL + banreason + MessageColor.POSITIVE + ".");
        String igMessage = RUtils.getInGameMessage(Config.igTempbanFormat, banreason, t, cs);
        igMessage = igMessage.replace("{length}", RUtils.formatDateDiff((time * 1000L) + curTime).substring(1));
        this.plugin.getServer().broadcast(igMessage, "rcmds.see.ban");
        String message = RUtils.getMessage(Config.tempbanFormat, banreason, cs);
        message = message.replace("{length}", RUtils.formatDateDiff((time * 1000L) + curTime).substring(1));
        if (t.isOnline()) ((Player) t).kickPlayer(message);
        RUtils.writeBanHistory(t);
        return true;
    }
}
