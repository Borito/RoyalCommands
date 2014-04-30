package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdUsage implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdUsage(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("usage")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.usage")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
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
        return false;
    }

}
