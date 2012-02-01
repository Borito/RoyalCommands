package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdPing implements CommandExecutor {

    RoyalCommands plugin;

    public CmdPing(RoyalCommands instance) {
        this.plugin = instance;
    }


    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ping")) {
            if (!plugin.isAuthorized(cs, "rcmds.ping")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            cs.sendMessage("Pong!");
            return true;
        }
        return false;
    }
}
