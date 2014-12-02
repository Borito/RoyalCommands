package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.home.BaseHomeCommand;
import org.royaldev.royalcommands.rcommands.home.Home;

@ReflectCommand
public class CmdDeleteHome extends BaseHomeCommand {

    public CmdDeleteHome(final RoyalCommands instance, final String name) {
        super(instance, name, true, new Short[]{CompletionType.LIST.getShort()}, true);
        this.addExpectedFlag(this.playerFlag);
    }

    @Override
    protected boolean continueCommand(final CommandSender cs, final Player p, final Command cmd, final String label, final String[] eargs, final CommandArguments ca, final Home home) {
        if (eargs.length < 1 && home.getName().equalsIgnoreCase("home")) {
            cs.sendMessage(MessageColor.NEGATIVE + "Type " + MessageColor.NEUTRAL + "/" + label + " home" + MessageColor.NEGATIVE + " to delete your default home.");
            return true;
        }
        home.delete();
        cs.sendMessage(MessageColor.POSITIVE + "Deleted home " + MessageColor.NEUTRAL + home.getName() + MessageColor.POSITIVE + " for " + MessageColor.NEUTRAL + home.getRPlayer().getName() + MessageColor.POSITIVE + ".");
        return true;
    }
}
