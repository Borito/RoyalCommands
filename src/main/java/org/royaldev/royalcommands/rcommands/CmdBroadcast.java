package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdBroadcast implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdBroadcast(RoyalCommands instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("broadcast")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.broadcast")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                cs.sendMessage(cmd.getUsage().replace("<command>", label));
                return true;
            }
            String message = RUtils.colorize(RoyalCommands.getFinalArg(args, 0));
            String format = Config.bcastFormat;
            message = format + message;
            plugin.getServer().broadcastMessage(message);
            return true;
        }
        return false;
    }
}
