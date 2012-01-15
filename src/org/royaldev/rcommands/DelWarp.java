package org.royaldev.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RoyalCommands;

import java.io.File;
import java.io.IOException;

public class DelWarp implements CommandExecutor {

    RoyalCommands plugin;

    public DelWarp(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("delwarp")) {
            if (!plugin.isAuthorized(cs, "rcmds.delwarp")) {
                cs.sendMessage(ChatColor.RED
                        + "You don't have permission for that!");
                plugin.log.warning("[RoyalCommands] " + cs.getName()
                        + " was denied access to the command!");
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED
                        + "This command is only available to players!");
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            File pconfl = new File(plugin.getDataFolder() + "/warps.yml");
            if (pconfl.exists()) {
                FileConfiguration pconf = YamlConfiguration
                        .loadConfiguration(pconfl);
                if (pconf.get("warps." + args[0]) == null) {
                    cs.sendMessage(ChatColor.RED + "That warp does not exist!");
                    return true;
                }
                pconf.set("warps." + args[0], null);
                try {
                    pconf.save(pconfl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cs.sendMessage(ChatColor.BLUE + "The warp \"" + ChatColor.GRAY
                        + args[0] + ChatColor.BLUE + "\" has been deleted.");
                return true;
            }
        }
        return false;
    }

}
