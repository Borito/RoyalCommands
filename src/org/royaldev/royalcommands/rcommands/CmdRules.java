package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

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
            int tpage;
            if (args.length < 1) {
                tpage = 1;
            } else {
                try {
                    tpage = Integer.valueOf(args[0].trim());
                } catch (Exception e) {
                    cs.sendMessage(ChatColor.RED + "The page number was invalid!");
                    return true;
                }
            }
            int pages = 0;
            try {
                BufferedReader br = new BufferedReader(new FileReader(rulesf));
                String line;
                java.util.List<String> rules = new ArrayList<String>();
                while ((line = br.readLine()) != null) {
                    line = RUtils.colorize(line);
                    rules.add(line);
                    if (line.trim().equals("###")) pages++;
                }
                if (tpage > pages || tpage < 1) {
                    cs.sendMessage(ChatColor.RED + "No such page!");
                    return true;
                }
                if (tpage == pages) {
                    cs.sendMessage(ChatColor.GOLD + "Page " + ChatColor.GRAY + tpage + ChatColor.GOLD + " of " + ChatColor.GRAY + pages + ChatColor.GOLD + ".");
                } else {
                    cs.sendMessage(ChatColor.GOLD + "Page " + ChatColor.GRAY + tpage + ChatColor.GOLD + " of " + ChatColor.GRAY + pages + ChatColor.GOLD + ". " + ChatColor.GRAY + "/" + cmd.getName() + " " + (tpage + 1) + ChatColor.GOLD + " for next page.");
                }
                int cpage = 0;
                for (String s : rules) {
                    if (s.trim().equals("###")) {
                        cpage++;
                        s = "";
                    }
                    if (cpage == tpage && !s.equals("")) {
                        cs.sendMessage(s);
                    }
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
