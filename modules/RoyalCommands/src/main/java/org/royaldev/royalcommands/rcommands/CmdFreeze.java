package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

@ReflectCommand
public class CmdFreeze extends BaseCommand {

    public CmdFreeze(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        final OfflinePlayer t = RUtils.getOfflinePlayer(args[0]);
        final PConfManager pcm = PConfManager.getPConfManager(t);
        final boolean wasFrozen = pcm.getBoolean("frozen", false);
        pcm.set("frozen", !wasFrozen);
        final String status = (wasFrozen) ? "thawed" : "frozen";
        if (t.isOnline())
            ((Player) t).sendMessage(MessageColor.POSITIVE + "You have been " + MessageColor.NEUTRAL + status + MessageColor.POSITIVE + " by " + MessageColor.NEUTRAL + cs.getName() + MessageColor.POSITIVE + ".");
        cs.sendMessage(MessageColor.POSITIVE + "You have " + MessageColor.NEUTRAL + status + MessageColor.POSITIVE + " the player " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
        return true;
    }
}
