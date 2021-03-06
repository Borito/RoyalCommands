package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.ArrayList;

public class Afk implements CommandExecutor {

    RoyalCommands plugin;

    public Afk(RoyalCommands instance) {
        this.plugin = instance;
    }

    public static java.util.List<Player> afkdb = new ArrayList<Player>();

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("afk")) {
            if (!plugin.isAuthorized(cs, "rcmds.afk")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            if (plugin.isVanished(p)) {
                cs.sendMessage(ChatColor.RED + "You are vanished! The cloak of illusion would be lost if you went AFK!");
                return true;
            }
            if (!afkdb.contains(p)) {
                afkdb.add(p);
                plugin.getServer().broadcastMessage(p.getName() + " is now AFK.");
                return true;
            }
            afkdb.remove(p);
            plugin.getServer().broadcastMessage(p.getName() + " is no longer AFK.");
            return true;
        }
        return false;
    }

}
