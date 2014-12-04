package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

@ReflectCommand
public class CmdUnbanIP extends BaseCommand {

    public CmdUnbanIP(final RoyalCommands instance, final String name) {
        super(instance, name, true);
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
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        OfflinePlayer op = this.plugin.getServer().getOfflinePlayer(args[0]);
        String ip = (!op.hasPlayedBefore()) ? args[0] : PConfManager.getPConfManager(op).getString("ip");
        if (ip == null) ip = args[0];
        if (!isValid(ip)) {
            cs.sendMessage(MessageColor.NEGATIVE + "Invalid IP (" + MessageColor.NEUTRAL + ip + MessageColor.NEGATIVE + ").");
            return true;
        }
        this.plugin.getServer().unbanIP(ip);
        if (!op.hasPlayedBefore()) {
            cs.sendMessage(MessageColor.POSITIVE + "Unbanned IP " + MessageColor.NEUTRAL + ip + MessageColor.POSITIVE + ".");
            return true;
        } else {
            RUtils.unbanPlayer(op);
            cs.sendMessage(MessageColor.POSITIVE + "Unbanned IP of " + MessageColor.NEUTRAL + op.getName() + MessageColor.POSITIVE + " (" + MessageColor.NEUTRAL + ip + MessageColor.POSITIVE + ").");
            return true;
        }
    }
}
