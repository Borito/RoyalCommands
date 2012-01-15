package org.royaldev.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.royaldev.royalcommands.RoyalCommands;

import java.io.File;

public class CompareIP implements CommandExecutor {

    RoyalCommands plugin;

    public CompareIP(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("compareip")) {
            if (!plugin.isAuthorized(cs, "rcmds.compareip")) {
                cs.sendMessage(ChatColor.RED
                        + "You don't have permission for that!");
                plugin.log.warning("[RoyalCommands] " + cs.getName()
                        + " was denied access to the command!");
                return true;
            }
            if (plugin.getConfig().getBoolean("disable_getip")) {
                cs.sendMessage(ChatColor.RED
                        + "/getip and /compareip have been disabled.");
                return true;
            }
            if (args.length < 2) {
                return false;
            }
            OfflinePlayer player1;
            OfflinePlayer player2;
            player1 = plugin.getServer().getOfflinePlayer(args[0]);
            player2 = plugin.getServer().getOfflinePlayer(args[1]);

            File p1confl = new File(plugin.getDataFolder()
                    + "/userdata/" + player1.getName().toLowerCase() + ".yml");
            File p2confl = new File(plugin.getDataFolder()
                    + "/userdata/" + player2.getName().toLowerCase() + ".yml");
            if (p1confl.exists()) {
                if (p2confl.exists()) {
                    FileConfiguration p1conf = YamlConfiguration
                            .loadConfiguration(p1confl);
                    FileConfiguration p2conf = YamlConfiguration
                            .loadConfiguration(p2confl);
                    String p1ip = p1conf.getString("ip");
                    String p2ip = p2conf.getString("ip");

                    cs.sendMessage(ChatColor.GRAY
                            + player1.getName() + ": " + p1ip);
                    cs.sendMessage(ChatColor.GRAY
                            + player2.getName() + ": " + p2ip);
                    return true;
                } else {
                    cs.sendMessage(ChatColor.RED + "The player "
                            + player2.getName()
                            + " does not exist.");
                    return true;
                }
            } else {
                cs.sendMessage(ChatColor.RED + "The player "
                        + player1.getName() + " does not exist.");
                return true;
            }
        }
        return false;
    }

}
