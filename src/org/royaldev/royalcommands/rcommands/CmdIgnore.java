package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdIgnore implements CommandExecutor {

    RoyalCommands plugin;

    public CmdIgnore(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ignore")) {
            if (!plugin.isAuthorized(cs, "rcmds.ignore")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            String name = args[0].trim().toLowerCase();

            Player t = plugin.getServer().getPlayer(name);
            if (t == null || plugin.isVanished(t)) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            if (plugin.isAuthorized(t, "rcmds.exempt.ignore")) {
                cs.sendMessage(ChatColor.RED + "You cannot ignore that player!");
                return true;
            }
            java.util.List<String> players = PConfManager.getPValStringList(t, "ignoredby");
            for (String ignored : players) {
                if (ignored.toLowerCase().equals(cs.getName().toLowerCase())) {
                    players.remove(ignored);
                    PConfManager.setPValStringList(t, players, "ignoredby");
                    cs.sendMessage(ChatColor.BLUE + "You have stopped ignoring " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
                    return true;
                }
            }
            players.add(cs.getName());
            PConfManager.setPValStringList(t, players, "ignoredby");
            cs.sendMessage(ChatColor.BLUE + "You are now ignoring " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
            return true;
        }
        return false;
    }

}
