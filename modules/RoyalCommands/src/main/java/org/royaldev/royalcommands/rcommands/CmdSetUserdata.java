package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

@ReflectCommand
public class CmdSetUserdata extends BaseCommand {

    public CmdSetUserdata(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (args.length < 3) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        String name = args[0];
        String node = args[1];
        String value = RoyalCommands.getFinalArg(args, 2);
        OfflinePlayer op = this.plugin.getServer().getOfflinePlayer(name);
        PConfManager pcm = PConfManager.getPConfManager(op);
        if (!pcm.exists() || !op.hasPlayedBefore()) {
            cs.sendMessage(MessageColor.NEGATIVE + "No such player!");
            return true;
        }
        pcm.set(node, value);
        cs.sendMessage(MessageColor.POSITIVE + "Set " + MessageColor.NEUTRAL + node + MessageColor.POSITIVE + " to " + MessageColor.NEUTRAL + value + MessageColor.POSITIVE + " for the userdata of " + MessageColor.NEUTRAL + name + MessageColor.POSITIVE + ".");
        return true;
    }
}
