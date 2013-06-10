package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdForce implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdForce(RoyalCommands instance) {
        this.plugin = instance;
    }


    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("force")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.force")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 2) {
                cs.sendMessage(cmd.getDescription());
                cs.sendMessage(cmd.getUsage().replace("<command>", label));
                return true;
            }
            Player t = plugin.getServer().getPlayer(args[0]);
            if (t == null || plugin.isVanished(t, cs)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            if (plugin.ah.isAuthorized(t, "rcmds.exempt.force")) {
                cs.sendMessage(MessageColor.NEGATIVE + "You cannot make that player run commands!");
                return true;
            }
            String command = RoyalCommands.getFinalArg(args, 1).trim();
            cs.sendMessage(MessageColor.POSITIVE + "Executing command " + MessageColor.NEUTRAL + "/" + command + MessageColor.POSITIVE + " from user " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
            t.performCommand(command);
            return true;
        }
        return false;
    }
}
