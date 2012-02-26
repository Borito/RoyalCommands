package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.net.InetAddress;

public class UnbanIP implements CommandExecutor {

    RoyalCommands plugin;

    public UnbanIP(RoyalCommands plugin) {
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
        if (cmd.getName().equalsIgnoreCase("unbanip")) {
            if (!plugin.isAuthorized(cs, "rcmds.unbanip")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            OfflinePlayer t2 = plugin.getServer().getOfflinePlayer(args[0].trim());
            if (!PConfManager.getPConfExists(t2)) {
                InetAddress ip = getAddress(args[0]);
                if (ip == null) {
                    cs.sendMessage(ChatColor.RED + "Invalid IP address!");
                    return true;
                }
                plugin.getServer().unbanIP(ip.getHostAddress());
                cs.sendMessage(ChatColor.BLUE + "Unbanned IP address: " + ChatColor.GRAY + ip.getHostAddress() + ChatColor.BLUE + ".");
                return true;
            }
            if (args.length > 0) {
                cs.sendMessage(ChatColor.BLUE + "You have pardoned that IP.");
                plugin.getServer().unbanIP(PConfManager.getPValString(t2, "ip"));
                return true;
            }
        }
        return false;
    }
}