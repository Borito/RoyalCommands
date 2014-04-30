package org.royaldev.royalcommands.rcommands;

import org.apache.commons.lang.BooleanUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdFly implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdFly(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("fly")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.fly")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
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
                if (!plugin.ah.isAuthorized(cs, "rcmds.others.fly")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                Player t = plugin.getServer().getPlayer(args[0]);
                if (t == null || plugin.isVanished(t, cs)) {
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
        return false;
    }

}
