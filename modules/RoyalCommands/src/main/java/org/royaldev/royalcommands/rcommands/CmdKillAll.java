package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdKillAll implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdKillAll(RoyalCommands instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("killall")) {
            if (!this.plugin.ah.isAuthorized(cs, cmd)) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            Player[] ps = plugin.getServer().getOnlinePlayers();
            for (Player p : ps) {
                if (plugin.isVanished(p, cs) || this.plugin.ah.isAuthorized(p, cmd, PermType.EXEMPT))
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
