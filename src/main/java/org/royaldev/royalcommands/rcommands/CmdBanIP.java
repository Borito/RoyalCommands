package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.configuration.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdBanIP implements CommandExecutor {

    private RoyalCommands plugin;

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
            if (!plugin.ah.isAuthorized(cs, "rcmds.banip")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                cs.sendMessage(cmd.getUsage().replace("<command>", label));
                return true;
            }
            OfflinePlayer op = plugin.getServer().getOfflinePlayer(args[0]);
            String ip = (!op.hasPlayedBefore()) ? args[0] : PConfManager.getPConfManager(op).getString("ip");
            if (ip == null) ip = args[0];
            if (!isValid(ip)) {
                cs.sendMessage(MessageColor.NEGATIVE + "Invalid IP (" + MessageColor.NEUTRAL + ip + MessageColor.NEGATIVE + ").");
                return true;
            }
            plugin.getServer().banIP(ip);
            if (!op.hasPlayedBefore()) {
                cs.sendMessage(MessageColor.POSITIVE + "Banned IP " + MessageColor.NEUTRAL + ip + MessageColor.POSITIVE + ".");
                return true;
            } else {
                op.setBanned(true);
                RUtils.writeBanHistory(op);
                cs.sendMessage(MessageColor.POSITIVE + "Banned IP of " + MessageColor.NEUTRAL + op.getName() + MessageColor.POSITIVE + " (" + MessageColor.NEUTRAL + ip + MessageColor.POSITIVE + ").");
                return true;
            }
        }
        return false;
    }
}
