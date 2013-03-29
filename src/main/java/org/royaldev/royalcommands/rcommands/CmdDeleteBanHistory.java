package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.ArrayList;
import java.util.List;

public class CmdDeleteBanHistory implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdDeleteBanHistory(RoyalCommands instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("deletebanhistory")) {
            if (!plugin.isAuthorized(cs, "rcmds.deletebanhistory")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 2) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            OfflinePlayer op = plugin.getServer().getPlayer(args[0]);
            if (op == null) op = plugin.getServer().getOfflinePlayer(args[0]);
            PConfManager pcm = PConfManager.getPConfManager(op);
            if (!pcm.exists()) {
                cs.sendMessage(ChatColor.RED + "That player has never played before!");
                return true;
            }
            int banToRemove;
            try {
                banToRemove = Integer.parseInt(args[1]);
                banToRemove--;
            } catch (NumberFormatException e) {
                cs.sendMessage(ChatColor.RED + "The ban number given was invalid!");
                return true;
            }
            List<String> prevBans = pcm.getStringList("prevbans");
            if (prevBans == null) prevBans = new ArrayList<String>();
            if (prevBans.size() < 1) {
                cs.sendMessage(ChatColor.RED + "That player has no previous bans.");
                return true;
            }
            if (banToRemove > prevBans.size() - 1 || banToRemove < 0) {
                cs.sendMessage(ChatColor.RED + "No such ban!");
                return true;
            }
            prevBans.remove(banToRemove);
            pcm.set("prevbans", prevBans);
            cs.sendMessage(ChatColor.BLUE + "Removed ban " + ChatColor.GRAY + (banToRemove + 1) + ChatColor.BLUE + " from " + ChatColor.GRAY + op.getName() + ChatColor.BLUE + ".");
            return true;
        }
        return false;
    }

}
