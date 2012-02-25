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

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Mute implements CommandExecutor {

    RoyalCommands plugin;

    public Mute(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    public String getTime(long time) {
        if (time > 86399) {
            long days = TimeUnit.SECONDS.toDays(time);
            if (days == 1) {
                return days + " day";
            }
            return days + " days";
        } else if (time > 3599) {
            long hours = TimeUnit.SECONDS.toHours(time);
            if (hours == 1) {
                return hours + " hour";
            }
            return hours + " hours";
        } else if (time > 59) {
            long minutes = TimeUnit.SECONDS.toMinutes(time);
            if (minutes == 1) {
                return minutes + " minute";
            }
            return minutes + " minutes";
        } else {
            if (time == 1) {
                return time + "second";
            }
            return time + " seconds";
        }
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("mute")) {
            if (!plugin.isAuthorized(cs, "rcmds.mute")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            if (args.length == 1) {
                Player t = plugin.getServer().getPlayer(args[0].trim());
                if (t == null) {
                    OfflinePlayer t2 = plugin.getServer().getOfflinePlayer(args[0].trim());
                    if (!PConfManager.getPConfExists(t2)) {
                        cs.sendMessage(ChatColor.RED + "That player does not exist!");
                        return true;
                    }
                    if (t2.isOp()) {
                        cs.sendMessage(ChatColor.RED + "You cannot mute that player!");
                        return true;
                    }
                    if (PConfManager.getPValBoolean(t2, "muted")) {
                        PConfManager.setPValBoolean(t2, false, "muted");
                        cs.sendMessage(ChatColor.BLUE + "You have unmuted " + ChatColor.GRAY + t2.getName() + ChatColor.BLUE + ".");
                        return true;
                    } else {
                        PConfManager.setPValBoolean(t2, true, "muted");
                        cs.sendMessage(ChatColor.BLUE + "You have muted " + ChatColor.GRAY + t2.getName() + ChatColor.BLUE + ".");
                        return true;
                    }

                }
                if (plugin.isAuthorized(t, "rcmds.exempt.mute")) {
                    cs.sendMessage(ChatColor.RED + "You cannot mute that player!");
                    return true;
                }
                if (PConfManager.getPValBoolean(t, "muted")) {
                    PConfManager.setPValBoolean(t, false, "muted");
                    t.sendMessage(ChatColor.BLUE + "You have been unmuted by " + ChatColor.GRAY + cs.getName() + ChatColor.BLUE + ".");
                    cs.sendMessage(ChatColor.BLUE + "You have unmuted " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
                    return true;
                } else {
                    PConfManager.setPValBoolean(t, true, "muted");
                    t.sendMessage(ChatColor.RED + "You have been muted by " + ChatColor.GRAY + cs.getName() + ChatColor.RED + ".");
                    cs.sendMessage(ChatColor.BLUE + "You have muted " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
                    return true;
                }
            }
            if (args.length > 1) {
                Player t = plugin.getServer().getPlayer(args[0].trim());
                Long time;
                try {
                    time = Long.valueOf(args[1]);
                } catch (Exception e) {
                    cs.sendMessage(ChatColor.RED + "That time was invalid!");
                    return true;
                }
                if (t == null) {
                    OfflinePlayer t2 = plugin.getServer().getOfflinePlayer(args[0].trim());
                    if (!PConfManager.getPConfExists(t2)) {
                        cs.sendMessage(ChatColor.RED + "That player does not exist!");
                        return true;
                    }
                    if (t2.isOp()) {
                        cs.sendMessage(ChatColor.RED + "You cannot mute that player!");
                        return true;
                    }
                    if (PConfManager.getPValBoolean(t2, "muted")) {
                        PConfManager.setPValBoolean(t2, false, "muted");
                        cs.sendMessage(ChatColor.BLUE + "You have unmuted " + ChatColor.GRAY + t2.getName() + ChatColor.BLUE + ".");
                        return true;
                    } else {
                        PConfManager.setPValBoolean(t2, true, "muted");
                        PConfManager.setPValLong(t2, time*1000, "mutelength");
                        PConfManager.setPValLong(t2, new Date().getTime(), "mutestart");
                        cs.sendMessage(ChatColor.BLUE + "You have muted " + ChatColor.GRAY + t2.getName() + ChatColor.BLUE + ".");
                        return true;
                    }
                } else {
                    if (!PConfManager.getPConfExists(t)) {
                        cs.sendMessage(ChatColor.RED + "That player does not exist!");
                        return true;
                    }
                    if (PConfManager.getPValBoolean(t, "muted")) {
                        PConfManager.setPValBoolean(t, false, "muted");
                        t.sendMessage(ChatColor.BLUE + "You have been unmuted by " + ChatColor.GRAY + cs.getName() + ChatColor.BLUE + ".");
                        cs.sendMessage(ChatColor.BLUE + "You have unmuted " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
                        return true;
                    } else {
                        PConfManager.setPValBoolean(t, true, "muted");
                        PConfManager.setPValLong(t, time*1000, "mutelength");
                        PConfManager.setPValLong(t, new Date().getTime(), "mutestart");
                        t.sendMessage(ChatColor.RED + "You have been muted by " + ChatColor.GRAY + cs.getName() + ChatColor.RED + " for " + ChatColor.GRAY + getTime(time) + ChatColor.RED + ".");
                        cs.sendMessage(ChatColor.BLUE + "You have muted " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + " for " + ChatColor.GRAY + getTime(time) + ChatColor.BLUE + ".");
                        return true;
                    }
                }
            }
        }
        return false;
    }
}