package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdBurn implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdBurn(RoyalCommands instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("burn")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.burn")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                cs.sendMessage(cmd.getUsage().replace("<command>", label));
                return true;
            }
            Player t = plugin.getServer().getPlayer(args[0]);
            if (t == null || plugin.isVanished(t, cs)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            if (plugin.ah.isAuthorized(t, "rcmds.exempt.burn")) {
                cs.sendMessage(MessageColor.NEGATIVE + "You cannot burn that player!");
                return true;
            }
            int len = 5;
            if (args.length > 1) len = RUtils.timeFormatToSeconds(args[1]);
            if (len <= 0) {
                cs.sendMessage(MessageColor.NEGATIVE + "Invalid time format.");
                return true;
            }
            cs.sendMessage(MessageColor.POSITIVE + "You have set " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + " on fire for " + MessageColor.NEUTRAL + RUtils.formatDateDiff((len * 1000) + System.currentTimeMillis()).substring(1) + MessageColor.POSITIVE + ".");
            t.setFireTicks(len * 20);
            return true;
        }
        return false;
    }

}
