package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdUsage extends BaseCommand {

    public CmdUsage(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        Command c = RUtils.getCommand(args[0]);
        if (c == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "No such command!");
            return true;
        }
        if (c instanceof PluginCommand) cs.sendMessage(((PluginCommand) c).getPlugin().getName());
        cs.sendMessage(c.getDescription());
        cs.sendMessage(c.getUsage());
        return true;
    }
}
