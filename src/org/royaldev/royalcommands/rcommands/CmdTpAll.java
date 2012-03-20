package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdTpAll implements CommandExecutor {

    public RoyalCommands plugin;

    public CmdTpAll(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tpall")) {
            if (!plugin.isAuthorized(cs, "rcmds.tpall")) {
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
                t.teleport(p);
            }
            p.sendMessage(ChatColor.BLUE + "All players teleported to you.");
            return true;
        }
        return false;
    }

}
