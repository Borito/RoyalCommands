package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdMobIgnore implements CommandExecutor {

    RoyalCommands plugin;

    public CmdMobIgnore(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("mobignore")) {
            if (!plugin.isAuthorized(cs, "rcmds.mobignore")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                if (!(cs instanceof Player)) {
                    cs.sendMessage(cmd.getDescription());
                    return false;
                }
                Player p = (Player) cs;
                PConfManager pcm = new PConfManager(p);
                Boolean isHidden = pcm.getBoolean("mobignored");
                if (isHidden == null) isHidden = false;
                pcm.setBoolean(!isHidden, "mobignored");
                if (isHidden) cs.sendMessage(ChatColor.BLUE + "Toggled mob ignore off.");
                else cs.sendMessage(ChatColor.BLUE + "Toggled mob ignore on.");
                return true;
            }
            Player t = plugin.getServer().getPlayer(args[0]);
            if (t == null || plugin.isVanished(t, cs)) {
                cs.sendMessage(ChatColor.RED + "That player does not exist.");
                return true;
            }
            PConfManager pcm = new PConfManager(t);
            Boolean isHidden = pcm.getBoolean("mobignored");
            if (isHidden == null) isHidden = false;
            pcm.setBoolean(!isHidden, "mobignored");
            if (isHidden) {
                cs.sendMessage(ChatColor.BLUE + "Toggled mob ignore off for " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
                t.sendMessage(ChatColor.GRAY + cs.getName() + ChatColor.BLUE + " toggled mob ignore off for you.");
            } else {
                cs.sendMessage(ChatColor.BLUE + "Toggled mob ignore on for " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
                t.sendMessage(ChatColor.GRAY + cs.getName() + ChatColor.BLUE + " toggled mob ignore on for you.");
            }
            return true;
        }
        return false;
    }

}
