package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdBanIP implements CommandExecutor {

    RoyalCommands plugin;

    public CmdBanIP(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    private boolean isValid(String address) {
        if (address == null) return false;
        String[] ips = address.split("\\.");
        if (ips.length != 4) return false;
        for (String s : ips) {
            int ip;
            try {
                ip = Integer.valueOf(s);
            } catch (Exception e) {
                return false;
            }
            if (ip > 255) return false;
        }
        return true;
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
            OfflinePlayer op = plugin.getServer().getOfflinePlayer(args[0]);
            String ip = (!op.hasPlayedBefore()) ? args[0] : PConfManager.getPValString(op, "ip");
            if (ip == null) ip = args[0];
            if (!isValid(ip)) {
                cs.sendMessage(ChatColor.RED + "Invalid IP (" + ChatColor.GRAY + ip + ChatColor.RED + ").");
                return true;
            }
            plugin.getServer().banIP(ip);
            if (!op.hasPlayedBefore()) {
                cs.sendMessage(ChatColor.BLUE + "Banned IP " + ChatColor.GRAY + ip + ChatColor.BLUE + ".");
                return true;
            } else {
                op.setBanned(true);
                cs.sendMessage(ChatColor.BLUE + "Banned IP of " + ChatColor.GRAY + op.getName() + ChatColor.BLUE + " (" + ChatColor.GRAY + ip + ChatColor.BLUE + ").");
                return true;
            }
        }
        return false;
    }
}