package org.royaldev.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class TpAccept implements CommandExecutor {

    RoyalCommands plugin;

    public TpAccept(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("tpaccept")) {
            if (!plugin.isAuthorized(cs, "rcmds.tpaccept")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (TeleportRequest.tprdb.containsKey(cs)) {
                Player t = (Player) TeleportRequest.tprdb.get(cs);
                cs.sendMessage(ChatColor.BLUE + "Teleport request accepted.");
                t.sendMessage(ChatColor.BLUE
                        + "Your teleport request was accepted.");
                Back.backdb.put(t, t.getLocation());
                t.teleport(((Player) cs).getLocation());
                return true;
            }
            if (TeleportRequestHere.tprhdb.containsKey(cs)) {
                Player t = (Player) TeleportRequestHere.tprhdb.get(cs);
                cs.sendMessage(ChatColor.BLUE + "Teleport request accepted.");
                t.sendMessage(ChatColor.BLUE
                        + "Your teleport request was accepted.");
                Back.backdb.put(((Player) cs), ((Player) cs).getLocation());
                t.teleport(((Player) cs).getLocation());
                return true;
            }
            cs.sendMessage(ChatColor.RED + "You have no requests pending.");
            return true;
        }
        return false;
    }
}
