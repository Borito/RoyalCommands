package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdCompareIP implements CommandExecutor {

    RoyalCommands plugin;

    public CmdCompareIP(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("compareip")) {
            if (!plugin.isAuthorized(cs, "rcmds.compareip")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (plugin.getConfig().getBoolean("disable_getip")) {
                cs.sendMessage(ChatColor.RED + "/getip and /compareip have been disabled.");
                return true;
            }
            if (args.length < 2) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            OfflinePlayer player1;
            OfflinePlayer player2;
            player1 = plugin.getServer().getOfflinePlayer(args[0]);
            player2 = plugin.getServer().getOfflinePlayer(args[1]);

            if (PConfManager.getPConfExists(player1)) {
                if (PConfManager.getPConfExists(player2)) {
                    String p1ip = PConfManager.getPValString(player1, "ip");
                    String p2ip = PConfManager.getPValString(player2, "ip");

                    cs.sendMessage(ChatColor.GRAY + player1.getName() + ": " + p1ip);
                    cs.sendMessage(ChatColor.GRAY + player2.getName() + ": " + p2ip);
                    return true;
                } else {
                    cs.sendMessage(ChatColor.RED + "The player " + player2.getName() + " does not exist.");
                    return true;
                }
            } else {
                cs.sendMessage(ChatColor.RED + "The player " + player1.getName() + " does not exist.");
                return true;
            }
        }
        return false;
    }

}
