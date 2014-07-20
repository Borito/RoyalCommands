package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdTeleportPlayerToPlayer extends BaseCommand {

    public CmdTeleportPlayerToPlayer(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (args.length < 2) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        Player t1 = this.plugin.getServer().getPlayer(args[0]);
        Player t2 = this.plugin.getServer().getPlayer(args[1]);
        if (t1 == null || t2 == null || this.plugin.isVanished(t1, cs) || this.plugin.isVanished(t2, cs)) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
            return true;
        }
        if (!RUtils.isTeleportAllowed(t1) && !this.ah.isAuthorized(cs, "rcmds.tpoverride")) {
            cs.sendMessage(MessageColor.NEGATIVE + "The player " + MessageColor.NEUTRAL + t1.getName() + MessageColor.NEGATIVE + " has teleportation off!");
            return true;
        }
        if (!RUtils.isTeleportAllowed(t2) && !this.ah.isAuthorized(cs, "rcmds.tpoverride")) {
            cs.sendMessage(MessageColor.NEGATIVE + "The player " + MessageColor.NEUTRAL + t2.getName() + MessageColor.NEGATIVE + " has teleportation off!");
            return true;
        }
        String error = RUtils.teleport(t1, t2);
        if (!error.isEmpty()) {
            cs.sendMessage(MessageColor.NEGATIVE + error);
            return true;
        }
        cs.sendMessage(MessageColor.POSITIVE + "You have teleported " + MessageColor.NEUTRAL + t1.getName() + MessageColor.POSITIVE + " to " + MessageColor.NEUTRAL + t2.getName() + MessageColor.POSITIVE + ".");
        return true;
    }
}
