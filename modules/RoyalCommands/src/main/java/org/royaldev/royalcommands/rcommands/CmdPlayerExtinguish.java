package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdPlayerExtinguish extends BaseCommand {

    public CmdPlayerExtinguish(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (args.length < 1) {
            Player p;
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "You must be a player to use this command!");
                return true;
            }
            p = (Player) cs;
            cs.sendMessage(MessageColor.POSITIVE + "You have been extinguished.");
            p.setFireTicks(0);
            return true;
        } else {
            if (!this.ah.isAuthorized(cs, cmd, PermType.OTHERS)) {
                cs.sendMessage(MessageColor.NEGATIVE + "You don't have permission for that!");
                return true;
            }
            Player target = this.plugin.getServer().getPlayer(args[0]);
            if (target == null || this.plugin.isVanished(target, cs)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            cs.sendMessage(MessageColor.POSITIVE + "You have extinguished " + MessageColor.NEUTRAL + target.getName() + MessageColor.POSITIVE + ".");
            target.sendMessage(MessageColor.POSITIVE + "You have been extinguished by " + MessageColor.NEUTRAL + cs.getName() + MessageColor.POSITIVE + ".");
            target.setFireTicks(0);
            return true;
        }
    }
}
