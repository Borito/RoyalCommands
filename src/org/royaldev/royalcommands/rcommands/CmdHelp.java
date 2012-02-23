package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.Help;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

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
