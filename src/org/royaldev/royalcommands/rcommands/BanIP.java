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

import java.net.InetAddress;

public class BanIP implements CommandExecutor {

    RoyalCommands plugin;

    public BanIP(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    public InetAddress getAddress(String address) {
        String[] ips = address.split("\\.");
        if (ips.length != 4) {
            plugin.log.info(String.valueOf(ips.length));
            return null;
        }
        byte b1 = Byte.valueOf(ips[0]);
        byte b2 = Byte.valueOf(ips[1]);
        byte b3 = Byte.valueOf(ips[2]);
        byte b4 = Byte.valueOf(ips[3]);
        try {
            return InetAddress.getByAddress(address, new byte[]{b1, b2, b3, b4});
        } catch (Exception e) {
            plugin.log.info("dat null");
            return null;
        }
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("banip")) {
            if (!plugin.isAuthorized(cs, "rcmds.banip")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            String banreason;
            Player t = plugin.getServer().getPlayer(args[0].trim());
            if (t != null) {
                if (!PConfManager.getPConfExists(t)) {
                    cs.sendMessage(ChatColor.RED + "That player does not exist!");
                    return true;
                }
                if (plugin.isAuthorized(t, "rcmds.exempt.ban")) {
                    cs.sendMessage(ChatColor.RED + "You can't ban that player!");
                    return true;
                }
                if (args.length == 1) {
                    banreason = plugin.banMessage;
                    PConfManager.setPValString(t, banreason, "banreason");
                    PConfManager.setPValString(t, cs.getName(), "banner");
                    cs.sendMessage(ChatColor.BLUE + "You have banned " + ChatColor.RED + t.getName() + ChatColor.BLUE + ".");
                    plugin.getServer().broadcast(ChatColor.RED + "The player " + ChatColor.GRAY + t.getName() + ChatColor.RED + " has been banned for " + ChatColor.GRAY + banreason + ChatColor.RED + " by " + ChatColor.GRAY + cs.getName() + ChatColor.RED + ".", "rcmds.see.ipban");
                    plugin.getServer().banIP(PConfManager.getPValString(t, "ip"));
                    t.kickPlayer(banreason);
                    return true;
                }
                if (args.length > 1) {
                    banreason = plugin.getFinalArg(args, 1).replaceAll("(&([a-f0-9]))", "\u00A7$2");
                    PConfManager.setPValString(t, banreason, "banreason");
                    PConfManager.setPValString(t, cs.getName(), "banner");
                    cs.sendMessage(ChatColor.BLUE + "You have IP banned " + ChatColor.RED + t.getName() + ChatColor.BLUE + ".");
                    plugin.getServer().broadcast(ChatColor.RED + "The player " + ChatColor.GRAY + t.getName() + ChatColor.RED + " has been IP banned for " + ChatColor.GRAY + banreason + ChatColor.RED + " by " + ChatColor.GRAY + cs.getName() + ChatColor.RED + ".", "rcmds.see.ipban");
                    plugin.getServer().banIP(PConfManager.getPValString(t, "ip"));
                    t.kickPlayer(banreason);
                    return true;
                }
            } else {
                OfflinePlayer t2 = plugin.getServer().getOfflinePlayer(args[0].trim());
                if (!PConfManager.getPConfExists(t2)) {
                    InetAddress ip = getAddress(args[0]);
                    if (ip == null) {
                        cs.sendMessage(ChatColor.RED + "Invalid IP address!");
                        return true;
                    }
                    plugin.getServer().banIP(ip.getHostAddress());
                    cs.sendMessage(ChatColor.BLUE + "Banned IP address: " + ChatColor.GRAY + ip.getHostAddress() + ChatColor.BLUE + ".");
                    return true;
                }
                if (t2.isOp()) {
                    cs.sendMessage(ChatColor.RED + "You can't ban that player!");
                    return true;
                }
                if (args.length == 1) {
                    banreason = plugin.banMessage;
                    PConfManager.setPValString(t2, banreason, "banreason");
                    PConfManager.setPValString(t2, cs.getName(), "banner");
                    cs.sendMessage(ChatColor.BLUE + "You have IP banned " + ChatColor.RED + t2.getName() + ChatColor.BLUE + ".");
                    plugin.getServer().broadcast(ChatColor.RED + "The player " + ChatColor.GRAY + t2.getName() + ChatColor.RED + " has been IP banned for " + ChatColor.GRAY + banreason + ChatColor.RED + " by " + ChatColor.GRAY + cs.getName() + ChatColor.RED + ".", "rcmds.see.ipban");
                    plugin.getServer().banIP(PConfManager.getPValString(t2, "ip"));
                    return true;
                }
                if (args.length > 1) {
                    banreason = plugin.getFinalArg(args, 1).replaceAll("(&([a-f0-9]))", "\u00A7$2");
                    PConfManager.setPValString(t2, banreason, "banreason");
                    PConfManager.setPValString(t2, cs.getName(), "banner");
                    cs.sendMessage(ChatColor.BLUE + "You have IP banned " + ChatColor.RED + t2.getName() + ChatColor.BLUE + ".");
                    plugin.getServer().broadcast(ChatColor.RED + "The player " + ChatColor.GRAY + t2.getName() + ChatColor.RED + " has been IP banned for " + ChatColor.GRAY + banreason + ChatColor.RED + " by " + ChatColor.GRAY + cs.getName() + ChatColor.RED + ".", "rcmds.see.ipban");
                    plugin.getServer().banIP(PConfManager.getPValString(t2, "ip"));
                    return true;
                }
            }
        }

        return false;
    }
}