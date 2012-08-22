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

public class CmdGod implements CommandExecutor {

    RoyalCommands plugin;

    public CmdGod(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("god")) {
            if (!plugin.isAuthorized(cs, "rcmds.god")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                if (!(cs instanceof Player)) {
                    cs.sendMessage(cmd.getDescription());
                    return false;
                }
                Player t = (Player) cs;
                PConfManager pcm = new PConfManager(t);
                t.setHealth(20);
                t.setFoodLevel(20);
                if (!pcm.getBoolean("godmode")) {
                    cs.sendMessage(ChatColor.BLUE + "You have enabled godmode for yourself.");
                    pcm.setBoolean(true, "godmode");
                    return true;
                } else {
                    cs.sendMessage(ChatColor.BLUE + "You have disabled godmode for yourself.");
                    pcm.setBoolean(false, "godmode");
                    return true;
                }
            }
            if (args.length > 0) {
                if (!plugin.isAuthorized(cs, "rcmds.others.god")) {
                    cs.sendMessage(ChatColor.RED + "You don't have permission for that!");
                    plugin.log.warning("[RoyalCommands] " + cs.getName() + " was denied access to the command!");
                    return true;
                }
                Player t = plugin.getServer().getPlayer(args[0]);
                PConfManager pcm = new PConfManager(t);
                if (t != null) {
                    if (!pcm.getBoolean("godmode")) {
                        if (!pcm.exists()) {
                            cs.sendMessage(ChatColor.RED + "That player doesn't exist!");
                            return true;
                        }
                        t.setHealth(20);
                        t.setFoodLevel(20);
                        t.setSaturation(20F);
                        t.sendMessage(ChatColor.BLUE + "The player " + ChatColor.GRAY + cs.getName() + ChatColor.BLUE + " has enabled godmode for you!");
                        cs.sendMessage(ChatColor.BLUE + "You have enabled godmode for " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
                        pcm.setBoolean(true, "godmode");
                        return true;
                    } else {
                        t.setHealth(20);
                        t.setFoodLevel(20);
                        t.sendMessage(ChatColor.RED + "The player " + ChatColor.GRAY + cs.getName() + ChatColor.RED + " has disabled godmode for you!");
                    }
                    cs.sendMessage(ChatColor.BLUE + "You have disabled godmode for " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
                    pcm.setBoolean(false, "godmode");
                    return true;
                }
            }
            OfflinePlayer t2 = plugin.getServer().getOfflinePlayer(args[0].trim());
            PConfManager pcm = new PConfManager(t2);
            if (!pcm.getBoolean("godmode")) {
                if (!pcm.exists()) {
                    cs.sendMessage(ChatColor.RED + "That player doesn't exist!");
                    return true;
                }
                if (t2.isOnline()) {
                    ((Player) t2).setHealth(20);
                    ((Player) t2).setFoodLevel(20);
                    ((Player) t2).setSaturation(20F);
                    ((Player) t2).sendMessage(ChatColor.BLUE + "The player " + ChatColor.GRAY + cs.getName() + ChatColor.BLUE + " has enabled godmode for you!");
                }
                cs.sendMessage(ChatColor.BLUE + "You have enabled godmode for " + ChatColor.GRAY + t2.getName() + ChatColor.BLUE + ".");
                pcm.setBoolean(true, "godmode");
                return true;
            } else {
                if (t2.isOnline()) {
                    ((Player) t2).setHealth(20);
                    ((Player) t2).setFoodLevel(20);
                    ((Player) t2).setSaturation(20F);
                    ((Player) t2).sendMessage(ChatColor.RED + "The player " + ChatColor.GRAY + cs.getName() + ChatColor.RED + " has disabled godmode for you!");
                }
                cs.sendMessage(ChatColor.BLUE + "You have disabled godmode for " + ChatColor.GRAY + t2.getName() + ChatColor.BLUE + ".");
                pcm.setBoolean(false, "godmode");
                return true;
            }
        }
        return false;
    }

}
