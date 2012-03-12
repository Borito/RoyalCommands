package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@SuppressWarnings("unused")
public class Nick implements CommandExecutor {

    RoyalCommands plugin;

    public Nick(RoyalCommands instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("nick")) {
            if (!plugin.isAuthorized(cs, "rcmds.nick")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player) && args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            if (args.length < 1) {
                Player p = (Player) cs;
                String newname = p.getName();
                p.setDisplayName(newname);
                PConfManager.setPValString(p, p.getName(), "dispname");
                p.sendMessage(ChatColor.BLUE + "Your name has been reset to " + ChatColor.GRAY + newname + ChatColor.BLUE + ".");
                return true;
            }

            if (args.length == 1) {
                if (!plugin.isAuthorized(cs, "rcmds.nick.others")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                Player t = plugin.getServer().getPlayer(args[0].trim());
                if (t == null || plugin.isVanished(t)) {
                    cs.sendMessage(ChatColor.RED + "That player does not exist!");
                    return true;
                }
                if (plugin.isAuthorized(t, "rcmds.exempt.nick")) {
                    cs.sendMessage(ChatColor.RED + "You cannot change that player's nick!");
                    return true;
                }
                String newname = t.getName();
                t.setDisplayName(newname);
                PConfManager.setPValString(t, newname, "dispname");
                t.sendMessage(ChatColor.BLUE + "Your name has been reset to " + ChatColor.GRAY + newname + ChatColor.BLUE + ".");
                cs.sendMessage(ChatColor.BLUE + "You have reset " + ChatColor.GRAY + t.getName() + "\'s" + ChatColor.BLUE + " name to " + ChatColor.GRAY + newname + ChatColor.BLUE + ".");
                return true;
            }

            if (args.length > 1) {
                if (!plugin.isAuthorized(cs, "rcmds.nick.others")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                Player t = plugin.getServer().getPlayer(args[0].trim());
                if (t == null || plugin.isVanished(t)) {
                    cs.sendMessage(ChatColor.RED + "That player does not exist!");
                    return true;
                }
                if (plugin.isAuthorized(t, "rcmds.exempt.nick")) {
                    if (cs instanceof Player) {
                        Player p = (Player) cs;
                        if (t != p) {
                            cs.sendMessage(ChatColor.RED + "You cannot change that player's nick!");
                            return true;
                        }
                    } else {
                        cs.sendMessage(ChatColor.RED + "You cannot change that player's nick!");
                        return true;
                    }
                }
                String newname = plugin.getFinalArg(args, 1).trim();
                t.setDisplayName(newname);
                PConfManager.setPValString(t, newname, "dispname");
                t.sendMessage(ChatColor.BLUE + "Your display name has been changed to " + ChatColor.GRAY + newname + ChatColor.BLUE + ".");
                cs.sendMessage(ChatColor.BLUE + "You have changed " + ChatColor.GRAY + t.getName() + "\'s" + ChatColor.BLUE + " name to " + ChatColor.GRAY + newname + ChatColor.BLUE + ".");
                return true;
            }
        }
        return false;
    }
}