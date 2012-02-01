package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class UnbanIP implements CommandExecutor {

    RoyalCommands plugin;

    public UnbanIP(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("unbanip")) {
            if (!plugin.isAuthorized(cs, "rcmds.unbanip")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            OfflinePlayer t2 = plugin.getServer().getOfflinePlayer(
                    args[0].trim());
            if (!PConfManager.getPConfExists(t2)) {
                plugin.getServer().unbanIP(args[0].trim());
                cs.sendMessage(ChatColor.BLUE + "Unbanned IP address: "
                        + ChatColor.GRAY + args[0].trim() + ChatColor.BLUE
                        + ".");
                return true;
            }
            if (args.length > 0) {
                cs.sendMessage(ChatColor.BLUE + "You have pardoned that IP.");
                plugin.getServer()
                        .unbanIP(PConfManager.getPValString(t2, "ip"));
                return true;
            }
        }
        return false;
    }
}