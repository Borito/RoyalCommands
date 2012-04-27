package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdBanreason implements CommandExecutor {

    RoyalCommands plugin;

    public CmdBanreason(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("banreason")) {
            if (!plugin.isAuthorized(cs, "rcmds.banreason")) {
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
            String banreason = PConfManager.getPValString(t, "banreason");
            cs.sendMessage(ChatColor.BLUE + "The player " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + " was banned for: " + ChatColor.GRAY + banreason + ChatColor.BLUE + ".");
            return true;
        }
        return false;
    }
}
