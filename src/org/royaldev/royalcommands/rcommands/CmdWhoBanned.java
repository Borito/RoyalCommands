package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdWhoBanned implements CommandExecutor {

    RoyalCommands plugin;

    public CmdWhoBanned(RoyalCommands instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("whobanned")) {
            if (!plugin.isAuthorized(cs, "rcmds.whobanned")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            OfflinePlayer t = plugin.getServer().getOfflinePlayer(args[0].trim());
            if (!PConfManager.getPConfExists(t)) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            if (!t.isBanned()) {
                cs.sendMessage(ChatColor.RED + "That player is not banned!");
                return true;
            }
            String banreason = PConfManager.getPValString(t, "banner");
            cs.sendMessage(ChatColor.BLUE + "The player " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + " was banned by: " + ChatColor.GRAY + banreason + ChatColor.BLUE + ".");
            return true;
        }
        return false;
    }

}
