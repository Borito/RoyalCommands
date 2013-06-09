package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdKill implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdKill(RoyalCommands instance) {
        this.plugin = instance;
    }


    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("kill")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.kill")) {
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
            if (plugin.ah.isAuthorized(t, "rcmds.exempt.kill")) {
                cs.sendMessage(MessageColor.NEGATIVE + "You cannot kill that player!");
                return true;
            }
            t.setHealth(0);
            cs.sendMessage(MessageColor.POSITIVE + "You have killed " + MessageColor.NEUTRAL + t.getDisplayName() + MessageColor.POSITIVE + ".");
            return true;
        }
        return false;
    }
}
