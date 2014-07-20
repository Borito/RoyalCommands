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
public class CmdSeen extends BaseCommand {

    public CmdSeen(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        final OfflinePlayer t = RUtils.getOfflinePlayer(args[0]);
        if (t.isOnline() && !this.plugin.isVanished((Player) t, cs)) {
            cs.sendMessage(MessageColor.POSITIVE + "The player " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + " was last seen " + MessageColor.NEUTRAL + "now" + MessageColor.POSITIVE + ".");
            return true;
        }
        final PConfManager pcm = PConfManager.getPConfManager(t);
        if (!pcm.exists()) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player doesn't exist!");
            return true;
        }
        if (pcm.get("seen") == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "I don't know when that player was last seen!");
            return true;
        }
        final long seen = pcm.getLong("seen");
        if (seen < 1L) {
            cs.sendMessage(MessageColor.NEGATIVE + "I don't know when that player was last seen!");
            return true;
        }
        final String lastseen = RUtils.formatDateDiff(seen);
        cs.sendMessage(MessageColor.POSITIVE + "The player " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + " was last seen" + MessageColor.NEUTRAL + lastseen + MessageColor.POSITIVE + " ago.");
        return true;
    }
}
