package org.royaldev.royalcommands.rcommands;

import org.apache.commons.lang.BooleanUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdFly extends BaseCommand {

    public CmdFly(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (!(cs instanceof Player) & args.length < 1) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        if (args.length < 1 && cs instanceof Player) {
            Player p = (Player) cs;
            p.setAllowFlight(!p.getAllowFlight());
            String status = BooleanUtils.toStringOnOff(p.getAllowFlight());
            p.sendMessage(MessageColor.POSITIVE + "Toggled flight to " + MessageColor.NEUTRAL + status + MessageColor.POSITIVE + ".");
        } else {
            if (!this.ah.isAuthorized(cs, cmd, PermType.OTHERS)) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            Player t = this.plugin.getServer().getPlayer(args[0]);
            if (t == null || this.plugin.isVanished(t, cs)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            t.setAllowFlight(!t.getAllowFlight());
            String status = BooleanUtils.toStringOnOff(t.getAllowFlight());
            cs.sendMessage(MessageColor.POSITIVE + "Toggled flight to " + MessageColor.NEUTRAL + status + MessageColor.POSITIVE + " on " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
            t.sendMessage(MessageColor.POSITIVE + "You have had flight toggled to " + MessageColor.NEUTRAL + status + MessageColor.POSITIVE + ".");
        }
        return true;
    }
}
