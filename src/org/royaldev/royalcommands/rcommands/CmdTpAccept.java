package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdTpAccept implements CommandExecutor {

    RoyalCommands plugin;

    public CmdTpAccept(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,String[] args) {
        if (cmd.getName().equalsIgnoreCase("tpaccept")) {
            if (!plugin.isAuthorized(cs, "rcmds.tpaccept")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            if (CmdTeleportRequest.tprdb.containsKey(cs)) {
                Player t = (Player) CmdTeleportRequest.tprdb.get(cs);
                cs.sendMessage(ChatColor.BLUE + "Teleport request accepted.");
                t.sendMessage(ChatColor.BLUE + "Your teleport request was accepted.");
                t.teleport(p.getLocation());
                CmdTeleportRequest.tprdb.remove(cs);
                return true;
            }
            if (CmdTeleportRequestHere.tprhdb.containsKey(cs)) {
                Player t = (Player) CmdTeleportRequestHere.tprhdb.get(cs);
                cs.sendMessage(ChatColor.BLUE + "Teleport request accepted.");
                t.sendMessage(ChatColor.BLUE + "Your teleport request was accepted.");
                p.teleport(t.getLocation());
                CmdTeleportRequestHere.tprhdb.remove(cs);
                return true;
            }
            cs.sendMessage(ChatColor.RED + "You have no requests pending.");
            return true;
        }
        return false;
    }
}
