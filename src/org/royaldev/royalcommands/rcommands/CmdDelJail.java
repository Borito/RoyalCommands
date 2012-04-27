package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.io.File;
import java.io.IOException;

public class CmdDelJail implements CommandExecutor {

    RoyalCommands plugin;

    public CmdDelJail(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("deljail")) {
            if (!plugin.isAuthorized(cs, "rcmds.deljail")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            File pconfl = new File(plugin.getDataFolder() + "/jails.yml");
            if (pconfl.exists()) {
                FileConfiguration pconf = YamlConfiguration.loadConfiguration(pconfl);
                if (pconf.get("jails." + args[0]) == null) {
                    cs.sendMessage(ChatColor.RED + "That jail does not exist!");
                    return true;
                }
                pconf.set("jails." + args[0], null);
                try {
                    pconf.save(pconfl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cs.sendMessage(ChatColor.BLUE + "The jail \"" + ChatColor.GRAY + args[0] + ChatColor.BLUE + "\" has been deleted.");
                return true;
            }
        }
        return false;
    }

}
