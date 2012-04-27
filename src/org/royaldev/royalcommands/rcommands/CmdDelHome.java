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
import java.io.IOException;

public class CmdDelHome implements CommandExecutor {

    RoyalCommands plugin;

    public CmdDelHome(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("delhome")) {
            if (!plugin.isAuthorized(cs, "rcmds.delhome")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(ChatColor.RED + "Type \"" + ChatColor.GRAY + "/delhome home" + ChatColor.RED + "\" to delete your default home.");
                return true;
            }
            File pconfl = new File(plugin.getDataFolder() + "/userdata/" + cs.getName().toLowerCase() + ".yml");
            if (pconfl.exists()) {
                FileConfiguration pconf = YamlConfiguration.loadConfiguration(pconfl);
                if (pconf.get("home." + args[0]) == null) {
                    cs.sendMessage(ChatColor.RED + "That home does not exist!");
                    return true;
                }
                pconf.set("home." + args[0], null);
                try {
                    pconf.save(pconfl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cs.sendMessage(ChatColor.BLUE + "The home \"" + ChatColor.GRAY + args[0] + ChatColor.BLUE + "\" has been deleted.");
                return true;
            }
        }
        return false;
    }

}
