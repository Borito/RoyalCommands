package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.io.File;
import java.util.Map;

public class CmdListWarns implements CommandExecutor {

    RoyalCommands plugin;

    public CmdListWarns(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("listwarns")) {
            if (!plugin.isAuthorized(cs, "rcmds.listwarns")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                if (!(cs instanceof Player)) {
                    cs.sendMessage(cmd.getDescription());
                    return false;
                }
                File pconfl = new File(plugin.getDataFolder() + "/userdata/" + cs.getName().toLowerCase() + ".yml");
                if (pconfl.exists()) {
                    FileConfiguration pconf = YamlConfiguration.loadConfiguration(pconfl);
                    if (pconf.get("warns") == null) {
                        cs.sendMessage(ChatColor.RED + "You have no warnings!");
                        return true;
                    }
                    final Map<String, Object> opts = pconf.getConfigurationSection("warns").getValues(true);
                    if (opts.values().isEmpty()) {
                        cs.sendMessage(ChatColor.RED + "You have no warnings!");
                        return true;
                    }
                    String homes = opts.values().toString();
                    homes = homes.substring(1, homes.length() - 1);
                    cs.sendMessage(ChatColor.RED + "Warnings:");
                    homes = RUtils.colorize(homes);
                    cs.sendMessage(homes);
                    return true;
                }
            }
            if (args.length > 0) {
                if (!plugin.isAuthorized(cs, "rcmds.others.listwarns")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                File pconfl = new File(plugin.getDataFolder() + "/userdata/" + args[0].toLowerCase() + ".yml");
                if (pconfl.exists()) {
                    FileConfiguration pconf = YamlConfiguration.loadConfiguration(pconfl);
                    if (pconf.get("warns") == null) {
                        cs.sendMessage(ChatColor.RED + "That user has no warnings!");
                        return true;
                    }
                    final Map<String, Object> opts = pconf.getConfigurationSection("warns").getValues(true);
                    if (opts.values().isEmpty()) {
                        cs.sendMessage(ChatColor.RED + "That user has no warnings!");
                        return true;
                    }
                    String homes = opts.values().toString();
                    homes = homes.substring(1, homes.length() - 1);
                    cs.sendMessage(ChatColor.RED + "User's warnings:");
                    homes = RUtils.colorize(homes);
                    cs.sendMessage(homes);
                    return true;
                }
            }
        }
        return false;
    }
}
