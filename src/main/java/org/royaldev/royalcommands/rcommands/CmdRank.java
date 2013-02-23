package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdRank implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdRank(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("rank")) {
            if (!plugin.isAuthorized(cs, "rcmds.rank")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player victim = plugin.getServer().getPlayer(args[0]);
            if (victim == null || plugin.isVanished(victim, cs)) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            String rank;
            try {
                rank = RoyalCommands.permission.getPrimaryGroup(victim);
            } catch (Exception e) {
                rank = null;
            }
            if (rank == null) {
                cs.sendMessage(ChatColor.RED + "That player has no rank.");
                return true;
            }
            cs.sendMessage(ChatColor.BLUE + "The player " + ChatColor.GRAY + victim.getName() + ChatColor.BLUE + " has the group " + ChatColor.GRAY + rank + ChatColor.BLUE + ".");
            return true;
        }
        return false;
    }

}
