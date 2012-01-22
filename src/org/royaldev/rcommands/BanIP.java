package org.royaldev.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class BanIP implements CommandExecutor {

    RoyalCommands plugin;

    public BanIP(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("banip")) {
            if (!plugin.isAuthorized(cs, "rcmds.banip")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            if (args[0].contains(".")) {

            }
            String banreason;
            Player t = plugin.getServer().getPlayer(args[0].trim());
            if (t != null) {
                if (!PConfManager.getPConfExists(t)) {
                    cs.sendMessage(ChatColor.RED
                            + "That player does not exist!");
                    return true;
                }
                if (plugin.isAuthorized(t, "rcmds.exempt.ban")) {
                    cs.sendMessage(ChatColor.RED + "You can't ban that player!");
                    return true;
                }
                if (args.length == 1) {
                    banreason = plugin.banMessage;
                    PConfManager.setPValString(t, banreason,
                            "banreason");
                    cs.sendMessage(ChatColor.BLUE + "You have banned "
                            + ChatColor.RED + t.getName() + ChatColor.BLUE
                            + ".");
                    plugin.getServer().broadcast(ChatColor.RED + "The player " + ChatColor.GRAY + t.getName() + ChatColor.RED + " has been banned for " + ChatColor.GRAY + banreason + ChatColor.RED + " by " + ChatColor.GRAY + cs.getName() + ChatColor.RED + ".", "rcmds.see.ipban");
                    plugin.getServer().banIP(PConfManager.getPValString(t, "ip"));
                    t.kickPlayer(banreason);
                    return true;
                }
                if (args.length > 1) {
                    banreason = plugin.getFinalArg(args, 1).replaceAll(
                            "(&([a-f0-9]))", "\u00A7$2");
                    PConfManager.setPValString(t, banreason,
                            "banreason");
                    cs.sendMessage(ChatColor.BLUE + "You have IP banned "
                            + ChatColor.RED + t.getName() + ChatColor.BLUE
                            + ".");
                    plugin.getServer().broadcast(
                            ChatColor.RED + "The player " + ChatColor.GRAY
                                    + t.getName() + ChatColor.RED
                                    + " has been IP banned for "
                                    + ChatColor.GRAY + banreason
                                    + ChatColor.RED + " by " + ChatColor.GRAY
                                    + cs.getName() + ChatColor.RED + ".",
                            "rcmds.see.ipban");
                    plugin.getServer()
                            .banIP(PConfManager.getPValString(t, "ip"));
                    t.kickPlayer(banreason);
                    return true;
                }
            } else {
                OfflinePlayer t2 = plugin.getServer().getOfflinePlayer(
                        args[0].trim());
                if (!PConfManager.getPConfExists(t2)) {
                    plugin.getServer().banIP(args[0].trim());
                    cs.sendMessage(ChatColor.BLUE + "Banned IP address: "
                            + ChatColor.GRAY + args[0].trim() + ChatColor.BLUE
                            + ".");
                    return true;
                }
                if (t2.isOp()) {
                    cs.sendMessage(ChatColor.RED + "You can't ban that player!");
                    return true;
                }
                if (args.length == 1) {
                    banreason = plugin.banMessage;
                    PConfManager.setPValString(t2, banreason, "banreason");
                    cs.sendMessage(ChatColor.BLUE + "You have IP banned "
                            + ChatColor.RED + t2.getName() + ChatColor.BLUE
                            + ".");
                    plugin.getServer().broadcast(
                            ChatColor.RED + "The player " + ChatColor.GRAY
                                    + t2.getName() + ChatColor.RED
                                    + " has been IP banned for "
                                    + ChatColor.GRAY + banreason
                                    + ChatColor.RED + " by " + ChatColor.GRAY
                                    + cs.getName() + ChatColor.RED + ".",
                            "rcmds.see.ipban");
                    plugin.getServer().banIP(
                            PConfManager.getPValString(t2, "ip"));
                    return true;
                }
                if (args.length > 1) {
                    banreason = plugin.getFinalArg(args, 1).replaceAll(
                            "(&([a-f0-9]))", "\u00A7$2");
                    PConfManager.setPValString(t2, banreason, "banreason");
                    cs.sendMessage(ChatColor.BLUE + "You have IP banned "
                            + ChatColor.RED + t2.getName() + ChatColor.BLUE
                            + ".");
                    plugin.getServer().broadcast(
                            ChatColor.RED + "The player " + ChatColor.GRAY
                                    + t2.getName() + ChatColor.RED
                                    + " has been IP banned for "
                                    + ChatColor.GRAY + banreason
                                    + ChatColor.RED + " by " + ChatColor.GRAY
                                    + cs.getName() + ChatColor.RED + ".",
                            "rcmds.see.ipban");
                    plugin.getServer().banIP(
                            PConfManager.getPValString(t2, "ip"));
                    return true;
                }
            }
        }
        return false;
    }
}