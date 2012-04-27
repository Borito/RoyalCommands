package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdTpaAll implements CommandExecutor {

    public RoyalCommands plugin;

    public CmdTpaAll(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tpaall")) {
            if (!plugin.isAuthorized(cs, "rcmds.tpaall")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            for (Player t : plugin.getServer().getOnlinePlayers()) {
                if (!RUtils.isTeleportAllowed(t) && !plugin.isAuthorized(p, "rcmds.tpoverride")) continue;
                if (t.equals(p)) continue;
                CmdTeleportRequest.sendTpRequest(t, p);
            }
            p.sendMessage(ChatColor.BLUE + "You have sent a teleport request to all players.");
            return true;
        }
        return false;
    }

}
