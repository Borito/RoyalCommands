package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

import java.text.SimpleDateFormat;
import java.util.Date;

@ReflectCommand
public class CmdBanInfo implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdBanInfo(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("baninfo")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.baninfo")) {
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
                cs.sendMessage(MessageColor.NEGATIVE + "That player has never played before!");
                return true;
            }
            if (!op.isBanned()) {
                cs.sendMessage(MessageColor.NEUTRAL + op.getName() + MessageColor.NEGATIVE + " is not banned!");
                return true;
            }
            cs.sendMessage(MessageColor.NEUTRAL + op.getName() + MessageColor.POSITIVE + " is banned!");
            String banner = pcm.getString("banner", "Unknown");
            cs.sendMessage(MessageColor.POSITIVE + "Banned by " + MessageColor.NEUTRAL + banner);
            String banReason = pcm.getString("banreason", "Unknown");
            cs.sendMessage(MessageColor.POSITIVE + "Banned for " + MessageColor.NEUTRAL + banReason);
            long banDate = pcm.getLong("bannedat", -1L);
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, y hh:mm:ss a");
            String bannedAt = (banDate < 0L) ? "Unknown" : sdf.format(new Date(banDate));
            cs.sendMessage(MessageColor.POSITIVE + "Banned at " + MessageColor.NEUTRAL + bannedAt);
            boolean isTempBan = pcm.get("bantime") != null;
            cs.sendMessage(MessageColor.POSITIVE + "Is tempban? " + MessageColor.NEUTRAL + ((isTempBan) ? "Yes" : "No"));
            if (!isTempBan) return true;
            String expire = sdf.format(new Date(pcm.getLong("bantime")));
            cs.sendMessage(MessageColor.POSITIVE + "Tempban expires on " + MessageColor.NEUTRAL + expire);
            return true;
        }
        return false;
    }

}
