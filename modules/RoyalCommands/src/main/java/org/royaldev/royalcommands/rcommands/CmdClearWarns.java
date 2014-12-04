package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

@ReflectCommand
public class CmdClearWarns extends BaseCommand {

    public CmdClearWarns(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        OfflinePlayer op = this.plugin.getServer().getOfflinePlayer(args[0]);
        PConfManager pcm = PConfManager.getPConfManager(op);
        if (!pcm.exists()) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
            return true;
        }
        if (pcm.get("warns") == null || pcm.getStringList("warns").isEmpty()) {
            cs.sendMessage(MessageColor.NEGATIVE + "There are no warnings for " + MessageColor.NEUTRAL + op.getName() + MessageColor.NEGATIVE + "!");
            return true;
        }
        pcm.set("warns", null);
        cs.sendMessage(MessageColor.POSITIVE + "You've cleared the warnings of " + MessageColor.NEUTRAL + op.getName() + MessageColor.POSITIVE + ".");
        return true;
    }
}
