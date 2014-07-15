package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

@ReflectCommand
public class CmdMessageSpy extends BaseCommand {

    public CmdMessageSpy(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        final OfflinePlayer op = (args.length > 0) ? RUtils.getOfflinePlayer(args[0]) : (Player) cs;
        boolean isSamePerson = !op.getName().equalsIgnoreCase(cs.getName());
        if (!isSamePerson && !this.ah.isAuthorized(cs, cmd, PermType.OTHERS)) {
            cs.sendMessage(MessageColor.NEGATIVE + "You don't have permission to toggle chat spy for other players.");
            return true;
        }
        if (!isSamePerson && this.ah.isAuthorized(op, cmd, PermType.EXEMPT)) {
            cs.sendMessage(MessageColor.NEGATIVE + "You can't toggle chat spy for this player.");
            return true;
        }
        PConfManager pcm = PConfManager.getPConfManager(op);
        boolean messageSpy = pcm.getBoolean("messagespy", false);
        pcm.set("messagespy", !messageSpy);
        cs.sendMessage(MessageColor.POSITIVE + "Chat spy mode " + MessageColor.NEUTRAL + ((!messageSpy) ? "enabled" : "disabled") + MessageColor.POSITIVE + " for " + MessageColor.NEUTRAL + op.getName() + MessageColor.POSITIVE + ".");
        if (op.isOnline() && !op.getName().equalsIgnoreCase(cs.getName()))
            ((Player) op).sendMessage(MessageColor.POSITIVE + "Chat spy mode has been " + MessageColor.NEUTRAL + ((!messageSpy) ? "enabled" : "disabled") + MessageColor.POSITIVE + " for you by " + MessageColor.NEUTRAL + cs.getName() + MessageColor.POSITIVE + ".");
        return true;
    }
}
