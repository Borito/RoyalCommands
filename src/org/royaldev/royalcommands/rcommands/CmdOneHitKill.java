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

public class CmdOneHitKill implements CommandExecutor {

    RoyalCommands plugin;

    public CmdOneHitKill(RoyalCommands instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("onehitkill")) {
            if (!plugin.isAuthorized(cs, "rcmds.onehitkill")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length > 0) {
                Player t = plugin.getServer().getPlayer(args[0].trim());
                if (t == null || plugin.isVanished(t)) {
                    OfflinePlayer op = plugin.getServer().getOfflinePlayer(args[0].trim());
                    if (!PConfManager.getPConfExists(op)) {
                        cs.sendMessage(ChatColor.RED + "That player does not exist!");
                        return true;
                    }
                    Boolean ohk = PConfManager.getPValBoolean(op, "ohk");
                    if (!ohk) {
                        PConfManager.setPValBoolean(op, true, "ohk");
                        cs.sendMessage(ChatColor.BLUE + "You have enabled onehitkill mode for " + ChatColor.GRAY + op.getName() + ChatColor.BLUE + ".");
                        return true;
                    }
                    PConfManager.setPValBoolean(op, false, "ohk");
                    cs.sendMessage(ChatColor.BLUE + "You have disabled onehitkill mode for " + ChatColor.GRAY + op.getName() + ChatColor.BLUE + ".");
                    return true;
                }
                Player p = plugin.getServer().getPlayer(args[0].trim());
                if (!PConfManager.getPConfExists(p)) {
                    cs.sendMessage(ChatColor.RED + "That player does not exist!");
                    return true;
                }
                Boolean ohk = PConfManager.getPValBoolean(p, "ohk");
                if (!ohk) {
                    PConfManager.setPValBoolean(p, true, "ohk");
                    cs.sendMessage(ChatColor.BLUE + "You have enabled onehitkill mode for " + ChatColor.GRAY + p.getName() + ChatColor.BLUE + ".");
                    p.sendMessage(ChatColor.BLUE + "The player " + ChatColor.GRAY + cs.getName() + ChatColor.BLUE + " has enabled onehitkill for you.");
                    return true;
                }
                PConfManager.setPValBoolean(p, false, "ohk");
                cs.sendMessage(ChatColor.BLUE + "You have disabled onehitkill mode for " + ChatColor.GRAY + p.getName() + ChatColor.BLUE + ".");
                p.sendMessage(ChatColor.RED + "The player " + ChatColor.GRAY + cs.getName() + ChatColor.RED + " has disabled your onehitkill.");
                return true;
            }
            if (args.length < 1) {
                if (!(cs instanceof Player)) {
                    cs.sendMessage(cmd.getDescription());
                    return false;
                }
                Player p = (Player) cs;
                Boolean ohk = PConfManager.getPValBoolean(p, "ohk");
                if (ohk == null || !ohk) {
                    PConfManager.setPValBoolean(p, true, "ohk");
                    p.sendMessage(ChatColor.BLUE + "You have enabled onehitkill for yourself.");
                    return true;
                }
                PConfManager.setPValBoolean(p, false, "ohk");
                p.sendMessage(ChatColor.BLUE + "You have disabled onehitkill for yourself.");
                return true;
            }
        }
        return false;
    }

}
