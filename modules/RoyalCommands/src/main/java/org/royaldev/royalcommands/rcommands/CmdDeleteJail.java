package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.ConfManager;

@ReflectCommand
public class CmdDeleteJail extends BaseCommand {

    public CmdDeleteJail(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        final ConfManager cm = ConfManager.getConfManager("jails.yml");
        if (!cm.isSet("jails." + args[0])) {
            cs.sendMessage(MessageColor.NEGATIVE + "That jail does not exist!");
            return true;
        }
        cm.set("jails." + args[0], null);
        cs.sendMessage(MessageColor.POSITIVE + "The jail \"" + MessageColor.NEUTRAL + args[0] + MessageColor.POSITIVE + "\" has been deleted.");
        return true;
    }
}
