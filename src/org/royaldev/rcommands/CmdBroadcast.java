package org.royaldev.rcommands;

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
            String message = plugin.getFinalArg(args, 0).trim().replaceAll("(&([a-f0-9]))", "\u00A7$2");
            String format = plugin.bcastFormat;
            //if (!format.endsWith(" ")) format = format + " "; <- not sure if I want to do this
            message = format + message;
            plugin.getServer().broadcastMessage(message);
            return true;
        }
        return false;
    }
}
