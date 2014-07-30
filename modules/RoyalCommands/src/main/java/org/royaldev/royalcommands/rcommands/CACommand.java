package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.RoyalCommands;

public abstract class CACommand extends BaseCommand {

    public CACommand(final RoyalCommands instance, final String name, final boolean checkPermissions) {
        super(instance, name, checkPermissions);
    }

    @Override
    protected boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        final CommandArguments ca = this.getCommandArguments(args);
        return this.runCommand(cs, cmd, label, ca.getExtraParameters(), ca);
    }

    protected abstract boolean runCommand(CommandSender cs, Command cmd, String label, String[] eargs, CommandArguments ca);
}
