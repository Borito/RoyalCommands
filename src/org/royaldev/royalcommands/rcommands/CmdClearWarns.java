package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.io.File;
import java.io.IOException;

public class CmdClearWarns implements CommandExecutor {

    RoyalCommands plugin;

    public CmdClearWarns(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("clearwarns")) {
            if (!plugin.isAuthorized(cs, "rcmds.clearwarns")) {
                RUtils.dispNoPerms(cs);
                return true;
            }

            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player t = plugin.getServer().getPlayer(args[0].trim());
            if (t != null) {
                File pconfl = new File(plugin.getDataFolder() + "/userdata/" + t.getName().toLowerCase() + ".yml");
                if (pconfl.exists()) {
                    FileConfiguration pconf = YamlConfiguration.loadConfiguration(pconfl);
                    if (pconf.get("warns") == null) {
                        cs.sendMessage(ChatColor.RED + "That player has no warnings!");
                        return true;
                    }
                    pconf.set("warns", null);
                    try {
                        pconf.save(pconfl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    cs.sendMessage(ChatColor.BLUE + "The warnings on " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + " have been cleared.");
                    t.sendMessage(ChatColor.BLUE + "Your warnings have been cleared by " + ChatColor.GRAY + cs.getName() + ChatColor.BLUE + ".");
                    return true;
                } else {
                    cs.sendMessage(ChatColor.RED + "That user does not exist!");
                    return true;
                }
            }
            OfflinePlayer t2 = plugin.getServer().getOfflinePlayer(args[0].trim());
            File pconfl = new File(plugin.getDataFolder() + "/userdata/" + t2.getName().toLowerCase() + ".yml");
            if (pconfl.exists()) {
                FileConfiguration pconf = YamlConfiguration.loadConfiguration(pconfl);
                if (pconf.get("warns") == null) {
                    cs.sendMessage(ChatColor.RED + "That player has no warnings!");
                    return true;
                }
                pconf.set("warns", null);
                try {
                    pconf.save(pconfl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cs.sendMessage(ChatColor.BLUE + "The warnings on " + ChatColor.GRAY + t2.getName() + ChatColor.BLUE + " have been cleared.");
                if (t2.isOnline()) {
                    ((Player) t2).sendMessage(ChatColor.BLUE + "Your warnings have been cleared by " + ChatColor.GRAY + cs.getName() + ChatColor.BLUE + ".");
                }
                return true;
            } else {
                cs.sendMessage(ChatColor.RED + "That user does not exist!");
                return true;
            }
        }
        return false;
    }

}
