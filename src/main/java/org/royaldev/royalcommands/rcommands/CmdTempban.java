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

    private RoyalCommands plugin;

    public CmdTempban(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tempban")) {
            if (!plugin.isAuthorized(cs, "rcmds.tempban")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 3) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            OfflinePlayer t = plugin.getServer().getPlayer(args[0]);
            if (t == null) t = plugin.getServer().getOfflinePlayer(args[0]);
            PConfManager pcm = PConfManager.getPConfManager(t);
            if (!pcm.exists()) {
                cs.sendMessage(ChatColor.RED + "That player doesn't exist!");
                return true;
            }
            if (plugin.isAuthorized(t, "rcmds.exempt.ban")) {
                cs.sendMessage(ChatColor.RED + "You cannot ban that player!");
                return true;
            }
            long time = (long) RUtils.timeFormatToSeconds(args[1]);
            if (time <= 0L) {
                cs.sendMessage(ChatColor.RED + "Invalid amount of time specified!");
                return true;
            }
            time++; // fix for always being a second short
            long curTime = new Date().getTime();
            String banreason = RoyalCommands.getFinalArg(args, 2);
            t.setBanned(true);
            pcm.set("bantime", (time * 1000L) + curTime);
            pcm.set("bannedat", curTime);
            pcm.set("banreason", banreason);
            pcm.set("banner", cs.getName());
            cs.sendMessage(ChatColor.BLUE + "You have banned " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + " for " + ChatColor.GRAY + banreason + ChatColor.BLUE + ".");
            String igMessage = RUtils.getInGameMessage(plugin.igTempbanFormat, banreason, t, cs);
            igMessage = igMessage.replace("{length}", RUtils.formatDateDiff((time * 1000L) + curTime).substring(1));
            plugin.getServer().broadcast(igMessage, "rcmds.see.ban");
            String message = RUtils.getMessage(plugin.tempbanFormat, banreason, cs);
            message = message.replace("{length}", RUtils.formatDateDiff((time * 1000L) + curTime).substring(1));
            if (t.isOnline()) ((Player) t).kickPlayer(message);
            RUtils.writeBanHistory(t);
            return true;
        }
        return false;
    }

}
