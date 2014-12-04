package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.ConfManager;

@ReflectCommand
public class CmdDeleteWarp extends BaseCommand {

    public CmdDeleteWarp(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        final ConfManager cm = ConfManager.getConfManager("warps.yml");
        if (!cm.isSet("warps." + args[0])) {
            cs.sendMessage(MessageColor.NEGATIVE + "That warp does not exist!");
            return true;
        }
        cm.set("warps." + args[0], null);
        cs.sendMessage(MessageColor.POSITIVE + "The warp \"" + MessageColor.NEUTRAL + args[0] + MessageColor.POSITIVE + "\" has been deleted.");
        return true;
    }
}
