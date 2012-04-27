package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdGetIP implements CommandExecutor {

    RoyalCommands plugin;

    public CmdGetIP(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("getip")) {
            if (!plugin.isAuthorized(cs, "rcmds.getip")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (plugin.getConfig().getBoolean("disable_getip")) {
                cs.sendMessage(ChatColor.RED + "/getip and /compareip have been disabled.");
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            OfflinePlayer oplayer = plugin.getServer().getOfflinePlayer(args[0].trim());
            if (PConfManager.getPConfExists(oplayer))
                cs.sendMessage(ChatColor.GRAY + oplayer.getName() + ": " + PConfManager.getPValString(oplayer, "ip"));
            else cs.sendMessage(ChatColor.RED + "The player " + oplayer.getName() + " does not exist.");
            return true;
        }
        return false;
    }

}
