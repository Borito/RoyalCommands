package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.Help;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class CmdHelp implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdHelp(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("help")) {
            if (!plugin.isAuthorized(cs, "rcmds.help")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (Config.customHelp) {
                File rulesf = new File(plugin.getDataFolder() + File.separator + "help.txt");
                if (!rulesf.exists()) {
                    cs.sendMessage(MessageColor.NEGATIVE + "The help.txt file was not found! Tell an admin.");
                    return true;
                }
                int tpage;
                if (args.length < 1) {
                    tpage = 1;
                } else {
                    try {
                        tpage = Integer.valueOf(args[0]);
                    } catch (Exception e) {
                        cs.sendMessage(MessageColor.NEGATIVE + "The page number was invalid!");
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
                        cs.sendMessage(MessageColor.NEGATIVE + "No such page!");
                        return true;
                    }
                    if (tpage == pages) {
                        cs.sendMessage(ChatColor.GOLD + "Page " + MessageColor.NEUTRAL + tpage + ChatColor.GOLD + " of " + MessageColor.NEUTRAL + pages + ChatColor.GOLD + ".");
                    } else {
                        cs.sendMessage(ChatColor.GOLD + "Page " + MessageColor.NEUTRAL + tpage + ChatColor.GOLD + " of " + MessageColor.NEUTRAL + pages + ChatColor.GOLD + ". " + MessageColor.NEUTRAL + "/" + cmd.getName() + " " + (tpage + 1) + ChatColor.GOLD + " for next page.");
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
                    cs.sendMessage(MessageColor.NEGATIVE + "The help.txt file was not found! Tell an admin.");
                    return true;
                }
                return true;
            }
            int i = 0;
            int page = 1;
            int wantedPage = 1;
            int pages;
            if (Help.helpdb.keySet().size() % Config.helpAmount == 0) {
                pages = Help.helpdb.keySet().size() / Config.helpAmount;
            } else {
                pages = (Help.helpdb.size() / Config.helpAmount) + 1;
            }
            if (args.length > 0) {
                try {
                    wantedPage = Integer.parseInt(args[0]);
                } catch (Exception e) {
                    cs.sendMessage(MessageColor.NEGATIVE + "That page was invalid!");
                    return true;
                }
            }
            if (wantedPage <= 0 || wantedPage > pages) {
                cs.sendMessage(MessageColor.NEGATIVE + "That page was invalid!");
                return true;
            }
            cs.sendMessage(MessageColor.POSITIVE + "Help page " + MessageColor.NEUTRAL + wantedPage + MessageColor.POSITIVE + "/" + MessageColor.NEUTRAL + pages + MessageColor.POSITIVE + ":");
            for (String com : Help.helpdb.keySet()) {
                if (i == Config.helpAmount) {
                    page++;
                    i = 0;
                }
                i++;
                if (page < wantedPage) continue;
                if (page == wantedPage) {
                    String desc = Help.helpdb.get(com);
                    cs.sendMessage(MessageColor.POSITIVE + "/" + com + MessageColor.RESET + ": " + MessageColor.NEUTRAL + desc);
                    if (i == Config.helpAmount) break;
                }
            }
            return true;
        }
        return false;
    }
}
