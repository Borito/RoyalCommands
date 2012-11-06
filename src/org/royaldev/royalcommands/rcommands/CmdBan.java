package org.royaldev.royalcommands.rcommands;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
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

public class CmdBan implements CommandExecutor {

    RoyalCommands plugin;

    public CmdBan(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    /**
     * Bans a player. Message is not sent to banned player or person who banned.
     * Message is broadcasted to those with rcmds.see.ban
     * Kicks banned player if they're online
     *
     * @param t      Player to ban
     * @param cs     CommandSender who issued the ban
     * @param reason Reason for the ban
     */
    public static void banPlayer(OfflinePlayer t, CommandSender cs, String reason) {
        reason = RUtils.colorize(reason);
        t.setBanned(true);
        Bukkit.getServer().broadcast(RUtils.getInGameMessage(RoyalCommands.instance.igBanFormat, reason, t, cs), "rcmds.see.ban");
        if (t.isOnline()) ((Player) t).kickPlayer(RUtils.getMessage(RoyalCommands.instance.banFormat, reason, cs));
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ban")) {
            if (!plugin.isAuthorized(cs, "rcmds.ban")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            OfflinePlayer t = plugin.getServer().getPlayer(args[0]);
            if (t == null) t = plugin.getServer().getOfflinePlayer(args[0]);
            PConfManager pcm = new PConfManager(t);
            if (!pcm.getConfExists()) {
                if (args.length > 1 && args[1].equalsIgnoreCase("true")) {
                    args = (String[]) ArrayUtils.remove(args, 1);
                } else {
                    cs.sendMessage(ChatColor.RED + "That player does not exist!");
                    return true;
                }
            }
            if (plugin.isAuthorized(t, "rcmds.exempt.ban")) {
                cs.sendMessage(ChatColor.RED + "You can't ban that player!");
                return true;
            }
            String banreason = (args.length > 1) ? RoyalCommands.getFinalArg(args, 1) : plugin.banMessage;
            banreason = RUtils.colorize(banreason);
            pcm.setString(banreason, "banreason");
            pcm.setString(cs.getName(), "banner");
            pcm.setLong(new Date().getTime(), "bannedat");
            pcm.set(null, "bantime");
            cs.sendMessage(ChatColor.BLUE + "You have banned " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
            banPlayer(t, cs, banreason);
            return true;
        }
        return false;
    }
}
