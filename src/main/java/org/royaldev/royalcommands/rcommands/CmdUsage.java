package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdUsage implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdUsage(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("usage")) {
            if (!plugin.isAuthorized(cs, "rcmds.usage")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            PluginCommand pc = plugin.getServer().getPluginCommand(args[0]);
            if (pc == null) {
                cs.sendMessage(ChatColor.RED + "No such command!");
                return true;
            }
            cs.sendMessage(pc.getPlugin().getName());
            cs.sendMessage(pc.getDescription());
            cs.sendMessage(pc.getUsage());
            return true;
        }
        return false;
    }

}
