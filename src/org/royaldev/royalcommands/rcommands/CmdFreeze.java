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

public class CmdFreeze implements CommandExecutor {

    RoyalCommands plugin;

    public CmdFreeze(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("freeze")) {
            if (!plugin.isAuthorized(cs, "rcmds.freeze")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player t = plugin.getServer().getPlayer(args[0]);
            if (t != null) {
                if (!RUtils.canActAgainst(cs, t, "freeze")) {
                    cs.sendMessage(ChatColor.RED + "You can't freeze that player!");
                    return true;
                }
                PConfManager pcm = new PConfManager(t);
                if (!pcm.getBoolean("frozen")) {
                    pcm.setBoolean(true, "frozen");
                    cs.sendMessage(ChatColor.BLUE + "You have frozen " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + "!");
                    t.sendMessage(ChatColor.RED + "You have been frozen!");
                    return true;
                } else {
                    pcm.setBoolean(false, "frozen");
                    cs.sendMessage(ChatColor.BLUE + "You have thawed " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + "!");
                    t.sendMessage(ChatColor.BLUE + "You have been thawed!");
                    return true;
                }
            } else {
                OfflinePlayer t2 = plugin.getServer().getOfflinePlayer(args[0].trim());
                PConfManager pcm = new PConfManager(t2);
                if (t2.isOp()) {
                    cs.sendMessage(ChatColor.RED + "You can't freeze that player!");
                    return true;
                }
                if (!pcm.exists()) {
                    cs.sendMessage(ChatColor.RED + "That player does not exist!");
                    return true;
                }
                if (!pcm.getBoolean("frozen")) {
                    pcm.setBoolean(true, "frozen");
                    cs.sendMessage(ChatColor.BLUE + "You have frozen " + ChatColor.GRAY + t2.getName() + ChatColor.BLUE + "!");
                    return true;
                } else {
                    pcm.setBoolean(false, "frozen");
                    cs.sendMessage(ChatColor.BLUE + "You have thawed " + ChatColor.GRAY + t2.getName() + ChatColor.BLUE + "!");
                    return true;
                }
            }
        }
        return false;
    }
}
