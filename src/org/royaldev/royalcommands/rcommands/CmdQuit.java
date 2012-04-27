package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdQuit implements CommandExecutor {

    RoyalCommands plugin;

    public CmdQuit(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("quit")) {
            if (!plugin.isAuthorized(cs, "rcmds.quit")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            ((Player) cs).kickPlayer("You have left the game.");
            return true;
        }
        return false;
    }

}
