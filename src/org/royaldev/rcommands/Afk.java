package org.royaldev.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.HashMap;

public class Afk implements CommandExecutor {

    RoyalCommands plugin;

    public Afk(RoyalCommands instance) {
        this.plugin = instance;
    }

    public static HashMap<Player, Boolean> afkdb = new HashMap<Player, Boolean>();

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("afk")) {
            if (!plugin.isAuthorized(cs, "rcmds.afk")) {
                cs.sendMessage(ChatColor.RED
                        + "You don't have permission for that!");
                plugin.log.warning("[RoyalCommands] " + cs.getName()
                        + " was denied access to the command!");
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED
                        + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            if (!afkdb.containsKey(p) || !afkdb.get(p)) {
                afkdb.put(p, true);
                plugin.getServer().broadcastMessage(
                        p.getName() + " is now AFK.");
                return true;
            }
            if (afkdb.containsKey(p) && afkdb.get(p)) {
                afkdb.remove(p);
                plugin.getServer().broadcastMessage(
                        p.getName() + " is no longer AFK.");
                return true;
            }
        }
        return false;
    }

}
