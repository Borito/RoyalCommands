package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdRcmds implements CommandExecutor {

    RoyalCommands plugin;

    public CmdRcmds(RoyalCommands plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("rcmds")) {
            if (!plugin.isAuthorized(cs, "rcmds.rcmds")) {
                RUtils.dispNoPerms(cs);
                return true;
            }

            plugin.reloadConfig();
            plugin.reloadConfigVals();

            cs.sendMessage(ChatColor.GREEN + "RoyalCommands " + ChatColor.BLUE + "v" + plugin.version + " reloaded.");
            return true;
        }
        return false;
    }
}
