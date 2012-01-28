package org.royaldev.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class CmdRules implements CommandExecutor {

    RoyalCommands plugin;

    public CmdRules(RoyalCommands instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("rules")) {
            if (!plugin.isAuthorized(cs, "rcmds.rules")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            File rulesf = new File(plugin.getDataFolder() + File.separator + "rules.txt");
            if (!rulesf.exists()) {
                cs.sendMessage(ChatColor.RED + "The rules.txt file was not found! Tell an admin.");
                return true;
            }
            try {
                BufferedReader br = new BufferedReader(new FileReader(rulesf));
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim().replaceAll("(&([a-f0-9]))", "\u00A7$2");
                    cs.sendMessage(line);
                }
            } catch (Exception e) {
                cs.sendMessage(ChatColor.RED + "The rules.txt file was not found! Tell an admin.");
                return true;
            }
            return true;
        }
        return false;
    }
}
