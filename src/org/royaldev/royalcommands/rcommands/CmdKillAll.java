package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdKillAll implements CommandExecutor {

    RoyalCommands plugin;

    public CmdKillAll(RoyalCommands instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("killall")) {
            if (!plugin.isAuthorized(cs, "rcmds.killall")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            Player[] ps = plugin.getServer().getOnlinePlayers();
            for (Player p : ps) {
                if (plugin.isVanished(p) || plugin.isAuthorized(p, "rcmds.exempt.kill")) continue;
                if (cs instanceof Player) {
                    if (p == cs) continue;
                }
                p.setHealth(0);
            }
            cs.sendMessage(ChatColor.BLUE + "You have killed all the players.");
            return true;
        }
        return false;
    }
}
