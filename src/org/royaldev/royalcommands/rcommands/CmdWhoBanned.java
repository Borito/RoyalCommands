package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdWhoBanned implements CommandExecutor {

    RoyalCommands plugin;

    public CmdWhoBanned(RoyalCommands instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("whobanned")) {
            cs.sendMessage(ChatColor.RED + "This command is deprecated: use " + ChatColor.GRAY + "/baninfo" + ChatColor.RED + " instead.");
            return true;
        }
        return false;
    }

}
