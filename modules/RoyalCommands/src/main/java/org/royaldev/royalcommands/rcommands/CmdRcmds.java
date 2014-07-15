package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.ConfManager;
import org.royaldev.royalcommands.configuration.PConfManager;

@ReflectCommand
public class CmdRcmds extends BaseCommand {

    public CmdRcmds(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("true")) {
            PConfManager.saveAllManagers();
            ConfManager.saveAllManagers();
            cs.sendMessage(MessageColor.POSITIVE + "Saved all configurations to disk.");
        }
        PConfManager.removeAllManagers();
        ConfManager.removeAllManagers();
        plugin.c.reloadConfiguration();
        RoyalCommands.wm.reloadConfig();
        cs.sendMessage(MessageColor.POSITIVE + "RoyalCommands " + MessageColor.NEUTRAL + "v" + plugin.version + MessageColor.POSITIVE + " reloaded.");
        return true;
    }
}
