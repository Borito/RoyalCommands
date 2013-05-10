package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdKillAll implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdKillAll(RoyalCommands instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("killall")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.killall")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            Player[] ps = plugin.getServer().getOnlinePlayers();
            for (Player p : ps) {
                if (plugin.isVanished(p, cs) || plugin.ah.isAuthorized(p, "rcmds.exempt.kill"))
                    continue;
                if (cs instanceof Player) {
                    if (p == cs) continue;
                }
                p.setHealth(0);
            }
            cs.sendMessage(MessageColor.POSITIVE + "You have killed all the players.");
            return true;
        }
        return false;
    }
}
