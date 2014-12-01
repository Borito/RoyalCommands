package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.home.BaseHomeCommand;
import org.royaldev.royalcommands.rcommands.home.Home;

@ReflectCommand
public class CmdHome extends BaseHomeCommand {

    public CmdHome(final RoyalCommands instance, final String name) {
        super(instance, name, true, new Short[]{CompletionType.LIST.getShort()});
    }

    @Override
    public boolean continueCommand(final Player p, final Command cmd, final String label, final String[] eargs, final CommandArguments ca, final Home home) {
        final String error = RUtils.teleport(p, home.getLocation());
        if (!error.isEmpty()) p.sendMessage(MessageColor.NEGATIVE + error);
        else {
            p.sendMessage(MessageColor.POSITIVE + "Teleported to home " + MessageColor.NEUTRAL + home.getName() + MessageColor.POSITIVE + " for " + MessageColor.NEUTRAL + home.getRPlayer().getName() + MessageColor.POSITIVE + ".");
        }
        return true;
    }
}
