package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.Help;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdHelp implements CommandExecutor {

    RoyalCommands plugin;

    public CmdHelp(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("help")) {
            if (!plugin.isAuthorized(cs, "rcmds.help")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            for (String com : Help.helpdb.keySet()) {
                String desc = Help.helpdb.get(com);
                cs.sendMessage(ChatColor.BLUE + com + ChatColor.WHITE + ": " + ChatColor.GRAY + desc);
            }
            return true;
        }
        return false;
    }
}
