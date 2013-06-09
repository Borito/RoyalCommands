package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
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
            if (!plugin.ah.isAuthorized(cs, "rcmds.rank")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                cs.sendMessage(cmd.getUsage());
                return true;
            }
            Player victim = plugin.getServer().getPlayer(args[0]);
            if (victim == null || plugin.isVanished(victim, cs)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            String rank;
            try {
                if (!plugin.vh.usingVault()) throw new Exception();
                rank = plugin.vh.getPermission().getPrimaryGroup(victim);
            } catch (Exception e) {
                rank = null;
            }
            if (rank == null) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player has no rank.");
                return true;
            }
            cs.sendMessage(MessageColor.POSITIVE + "The player " + MessageColor.NEUTRAL + victim.getName() + MessageColor.POSITIVE + " has the group " + MessageColor.NEUTRAL + rank + MessageColor.POSITIVE + ".");
            return true;
        }
        return false;
    }

}
