package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdPlayerSearch implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdPlayerSearch(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(final CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("playersearch")) {
            if (!plugin.isAuthorized(cs, "rcmds.playersearch")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            final String search = RoyalCommands.getFinalArg(args, 0);
            final OfflinePlayer[] ops = plugin.getServer().getOfflinePlayers();
            final Runnable r = new Runnable() {
                @Override
                public void run() {
                    int found = 0;
                    for (OfflinePlayer op : ops) {
                        if (!op.getName().toLowerCase().contains(search.toLowerCase())) continue;
                        PConfManager pcm = PConfManager.getPConfManager(op);
                        if (!pcm.exists()) continue;
                        Long seen = pcm.getLong("seen");
                        if (seen == null || seen < 1L) continue;
                        found++;
                        String lastseen = (op.isOnline()) ? " now" : RUtils.formatDateDiff(seen) + ChatColor.BLUE + " ago";
                        cs.sendMessage(ChatColor.BLUE + String.valueOf(found) + ". " + ChatColor.GRAY + op.getName() + ChatColor.BLUE + " - Last seen" + ChatColor.GRAY + lastseen + ChatColor.BLUE + ".");
                    }
                    cs.sendMessage(ChatColor.BLUE + "Search completed. " + ChatColor.GRAY + found + ChatColor.BLUE + " results found.");
                }
            };
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, r);
            return true;
        }
        return false;
    }

}
