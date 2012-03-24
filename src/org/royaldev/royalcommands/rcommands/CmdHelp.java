package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.Help;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class CmdHelp implements CommandExecutor {

    RoyalCommands plugin;

    public CmdHelp(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("help")) {
            if (!plugin.isAuthorized(cs, "rcmds.help")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (plugin.customHelp) {
                File rulesf = new File(plugin.getDataFolder() + File.separator + "help.txt");
                if (!rulesf.exists()) {
                    cs.sendMessage(ChatColor.RED + "The help.txt file was not found! Tell an admin.");
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
                    cs.sendMessage(ChatColor.RED + "The help.txt file was not found! Tell an admin.");
                    return true;
                }
                return true;
            }
            int i = 0;
            int page = 1;
            int wantedPage = 1;
            int pages;
            if (Help.helpdb.keySet().size() % plugin.helpAmount == 0) {
                pages = Help.helpdb.keySet().size() / plugin.helpAmount;
            } else {
                pages = (Help.helpdb.size() / plugin.helpAmount) + 1;
            }
            if (args.length > 0) {
                try {
                    wantedPage = Integer.parseInt(args[0]);
                } catch (Exception e) {
                    cs.sendMessage(ChatColor.RED + "That page was invalid!");
                    return true;
                }
            }
            if (wantedPage <= 0 || wantedPage > pages) {
                cs.sendMessage(ChatColor.RED + "That page was invalid!");
                return true;
            }
            cs.sendMessage(ChatColor.BLUE + "Help page " + ChatColor.GRAY + wantedPage + ChatColor.BLUE + "/" + ChatColor.GRAY + pages + ChatColor.BLUE + ":");
            for (String com : Help.helpdb.keySet()) {
                if (i == plugin.helpAmount) {
                    page++;
                    i = 0;
                }
                i++;
                if (page < wantedPage) continue;
                if (page == wantedPage) {
                    String desc = Help.helpdb.get(com);
                    cs.sendMessage(ChatColor.BLUE + "/" + com + ChatColor.WHITE + ": " + ChatColor.GRAY + desc);
                    if (i == plugin.helpAmount) break;
                }
            }
            return true;
        }
        return false;
    }
}
