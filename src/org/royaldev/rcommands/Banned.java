package org.royaldev.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class Banned implements CommandExecutor {

    RoyalCommands plugin;

    public Banned(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("banned")) {
            if (!plugin.isAuthorized(cs, "rcmds.banned")) {
                RUtils.dispNoPerms(cs);
                return true;
            } else {
                if (args.length < 1) {
                    cs.sendMessage(cmd.getDescription());
                    return false;
                }
                OfflinePlayer dude;
                dude = plugin.getServer().getOfflinePlayer(
                        args[0]);
                boolean banned = dude.isBanned();
                if (!banned) {
                    cs.sendMessage(ChatColor.GREEN + dude.getName() + ChatColor.WHITE + " is not banned.");
                    return true;
                } else {
                    cs.sendMessage(ChatColor.RED + dude.getName() + ChatColor.WHITE + " is banned.");
                    return true;
                }
            }
        }
        return false;
    }

}
