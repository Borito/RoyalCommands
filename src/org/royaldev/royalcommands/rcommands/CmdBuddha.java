package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdBuddha implements CommandExecutor {

    RoyalCommands plugin;

    public CmdBuddha(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("buddha")) {
            if (!plugin.isAuthorized(cs, "rcmds.buddha")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length > 0) {
                if (!plugin.isAuthorized(cs, "rcmds.others.buddha")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                Player t = plugin.getServer().getPlayer(args[0]);
                if (t == null || plugin.isVanished(t)) {
                    cs.sendMessage(ChatColor.RED + "That player does not exist!");
                    return true;
                }
                if (!PConfManager.getPValBoolean(t, "buddha")) {
                    PConfManager.setPValBoolean(t, true, "buddha");
                    t.sendMessage(ChatColor.BLUE + "Buddha mode enabled by " + ChatColor.GRAY + cs.getName() + ChatColor.BLUE + ".");
                    cs.sendMessage(ChatColor.BLUE + "Enabled buddha mode for " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
                } else {
                    PConfManager.setPValBoolean(t, false, "buddha");
                    t.sendMessage(ChatColor.BLUE + "Buddha mode disabled by " + ChatColor.GRAY + cs.getName() + ChatColor.BLUE + ".");
                    cs.sendMessage(ChatColor.BLUE + "Disabled buddha mode for " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
                }
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            if (!PConfManager.getPValBoolean(p, "buddha")) {
                PConfManager.setPValBoolean(p, true, "buddha");
                cs.sendMessage(ChatColor.BLUE + "Enabled buddha mode for yourself.");
            } else {
                PConfManager.setPValBoolean(p, false, "buddha");
                cs.sendMessage(ChatColor.BLUE + "Disabled buddha mode for yourself.");
            }
            return true;
        }
        return false;
    }

}
