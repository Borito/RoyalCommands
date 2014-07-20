package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

import java.util.ArrayList;
import java.util.List;

@ReflectCommand
public class CmdDeleteBanHistory extends BaseCommand {

    public CmdDeleteBanHistory(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (args.length < 2) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        final OfflinePlayer op = RUtils.getOfflinePlayer(args[0]);
        final PConfManager pcm = PConfManager.getPConfManager(op);
        if (!pcm.exists()) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player has never played before!");
            return true;
        }
        final int banToRemove;
        try {
            banToRemove = Integer.parseInt(args[1]) - 1;
        } catch (NumberFormatException e) {
            cs.sendMessage(MessageColor.NEGATIVE + "The ban number given was invalid!");
            return true;
        }
        List<String> prevBans = pcm.getStringList("prevbans");
        if (prevBans == null) prevBans = new ArrayList<>();
        if (prevBans.size() < 1) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player has no previous bans.");
            return true;
        }
        if (banToRemove > prevBans.size() - 1 || banToRemove < 0) {
            cs.sendMessage(MessageColor.NEGATIVE + "No such ban!");
            return true;
        }
        prevBans.remove(banToRemove);
        pcm.set("prevbans", prevBans);
        cs.sendMessage(MessageColor.POSITIVE + "Removed ban " + MessageColor.NEUTRAL + (banToRemove + 1) + MessageColor.POSITIVE + " from " + MessageColor.NEUTRAL + op.getName() + MessageColor.POSITIVE + ".");
        return true;
    }
}
