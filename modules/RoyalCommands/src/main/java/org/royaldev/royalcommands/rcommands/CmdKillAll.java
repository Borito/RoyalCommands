package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdKillAll extends BaseCommand {

    public CmdKillAll(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        for (Player p : this.plugin.getServer().getOnlinePlayers()) {
            if (this.plugin.isVanished(p, cs) || this.ah.isAuthorized(p, cmd, PermType.EXEMPT)) continue;
            if (cs instanceof Player) {
                if (p == cs) continue;
            }
            p.setHealth(0);
        }
        cs.sendMessage(MessageColor.POSITIVE + "You have killed all the players.");
        return true;
    }
}
