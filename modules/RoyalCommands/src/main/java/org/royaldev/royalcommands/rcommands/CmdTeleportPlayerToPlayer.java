package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.wrappers.player.RPlayer;

@ReflectCommand
public class CmdTeleportPlayerToPlayer extends BaseCommand {

    public CmdTeleportPlayerToPlayer(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (args.length < 2) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        final Player teleportee = this.plugin.getServer().getPlayer(args[0]);
        final Player destination = this.plugin.getServer().getPlayer(args[1]);
        if (teleportee == null || destination == null || this.plugin.isVanished(teleportee, cs) || this.plugin.isVanished(destination, cs)) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
            return true;
        }
        if (!RUtils.isTeleportAllowed(teleportee) && !this.ah.isAuthorized(cs, "rcmds.tpoverride")) {
            cs.sendMessage(MessageColor.NEGATIVE + "The player " + MessageColor.NEUTRAL + teleportee.getName() + MessageColor.NEGATIVE + " has teleportation off!");
            return true;
        }
        if (!RUtils.isTeleportAllowed(destination) && !this.ah.isAuthorized(cs, "rcmds.tpoverride")) {
            cs.sendMessage(MessageColor.NEGATIVE + "The player " + MessageColor.NEUTRAL + destination.getName() + MessageColor.NEGATIVE + " has teleportation off!");
            return true;
        }
        final RPlayer rp = RPlayer.getRPlayer(teleportee);
        final String error = rp.getTeleporter().teleport(destination);
        if (!error.isEmpty()) {
            cs.sendMessage(MessageColor.NEGATIVE + error);
            return true;
        }
        cs.sendMessage(MessageColor.POSITIVE + "You have teleported " + MessageColor.NEUTRAL + teleportee.getName() + MessageColor.POSITIVE + " to " + MessageColor.NEUTRAL + destination.getName() + MessageColor.POSITIVE + ".");
        return true;
    }
}
