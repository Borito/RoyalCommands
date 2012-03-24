package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdBroadcast implements CommandExecutor {

    RoyalCommands plugin;

    public CmdBroadcast(RoyalCommands instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("broadcast")) {
            if (!plugin.isAuthorized(cs, "rcmds.broadcast")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            String message = RUtils.colorize(plugin.getFinalArg(args, 0));
            String format = plugin.bcastFormat;
            message = format + message;
            plugin.getServer().broadcastMessage(message);
            return true;
        }
        return false;
    }
}
