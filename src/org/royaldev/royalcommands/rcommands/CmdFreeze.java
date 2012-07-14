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
            Player victim = plugin.getServer().getPlayer(args[0]);
            if (victim != null) {
                if (plugin.isAuthorized(victim, "rcmds.exempt.freeze")) {
                    cs.sendMessage(ChatColor.RED + "You can't freeze that player!");
                    return true;
                }
                PConfManager pcm = new PConfManager(victim);
                if (!pcm.getBoolean("frozen")) {
                    pcm.setBoolean(true, "frozen");
                    cs.sendMessage(ChatColor.BLUE + "You have frozen " + ChatColor.GRAY + victim.getName() + ChatColor.BLUE + "!");
                    victim.sendMessage(ChatColor.RED + "You have been frozen!");
                    return true;
                } else {
                    pcm.setBoolean(false, "frozen");
                    cs.sendMessage(ChatColor.BLUE + "You have thawed " + ChatColor.GRAY + victim.getName() + ChatColor.BLUE + "!");
                    victim.sendMessage(ChatColor.BLUE + "You have been thawed!");
                    return true;
                }
            } else {
                OfflinePlayer victim2 = plugin.getServer().getOfflinePlayer(args[0].trim());
                PConfManager pcm = new PConfManager(victim2);
                if (victim2.isOp()) {
                    cs.sendMessage(ChatColor.RED + "You can't freeze that player!");
                    return true;
                }
                if (!pcm.exists()) {
                    cs.sendMessage(ChatColor.RED + "That player does not exist!");
                    return true;
                }
                if (!pcm.getBoolean("frozen")) {
                    pcm.setBoolean(true, "frozen");
                    cs.sendMessage(ChatColor.BLUE + "You have frozen " + ChatColor.GRAY + victim2.getName() + ChatColor.BLUE + "!");
                    return true;
                } else {
                    pcm.setBoolean(false, "frozen");
                    cs.sendMessage(ChatColor.BLUE + "You have thawed " + ChatColor.GRAY + victim2.getName() + ChatColor.BLUE + "!");
                    return true;
                }
            }
        }
        return false;
    }
}
