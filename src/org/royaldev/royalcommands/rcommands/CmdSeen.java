package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdSeen implements CommandExecutor {

    RoyalCommands plugin;

    public CmdSeen(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("seen")) {
            if (!plugin.isAuthorized(cs, "rcmds.seen")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            OfflinePlayer t = plugin.getServer().getOfflinePlayer(args[0]);
            if (t.isOnline() && !plugin.isVanished((Player) t, cs)) {
                cs.sendMessage(ChatColor.BLUE + "The player " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + " was last seen " + ChatColor.GRAY + "now" + ChatColor.BLUE + ".");
                return true;
            }
            PConfManager pcm = new PConfManager(t);
            if (!pcm.exists()) {
                cs.sendMessage(ChatColor.RED + "That player doesn't exist!");
                return true;
            }
            if (pcm.get("seen") == null) {
                cs.sendMessage(ChatColor.RED + "I don't know when that player was last seen!");
                return true;
            }
            long seen = pcm.getLong("seen");
            if (seen < 1L) {
                cs.sendMessage(ChatColor.RED + "I don't know when that player was last seen!");
                return true;
            }
            String lastseen = RUtils.formatDateDiff(seen);
            cs.sendMessage(ChatColor.BLUE + "The player " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + " was last seen" + ChatColor.GRAY + lastseen + ChatColor.BLUE + " ago.");
            return true;
        }
        return false;
    }

}
