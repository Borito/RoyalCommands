package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdTpDeny implements CommandExecutor {

    RoyalCommands plugin;

    public CmdTpDeny(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("tpdeny")) {
            if (!plugin.isAuthorized(cs, "rcmds.tpdeny")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (CmdTeleportRequest.tprdb.containsKey(cs)) {
                Player t = (Player) CmdTeleportRequest.tprdb.get(cs);
                cs.sendMessage(ChatColor.BLUE + "Teleport request denied.");
                t.sendMessage(ChatColor.BLUE
                        + "Your teleport request was denied.");
                CmdTeleportRequest.tprdb.remove(cs);
                return true;
            }
            cs.sendMessage(ChatColor.RED + "You have no requests pending.");
            return true;
        }
        return false;
    }
}
