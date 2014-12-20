package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.Configuration;
import org.royaldev.royalcommands.configuration.PlayerConfigurationManager;

@ReflectCommand
public class CmdRoyalCommands extends BaseCommand {

    public CmdRoyalCommands(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("true")) {
            PlayerConfigurationManager.saveAllConfigurations();
            Configuration.saveAllConfigurations();
            cs.sendMessage(MessageColor.POSITIVE + "Saved all configurations to disk.");
        }
        PlayerConfigurationManager.removeAllConfigurations();
        Configuration.removeAllConfigurations();
        this.plugin.c.reloadConfiguration();
        RoyalCommands.wm.reloadConfig();
        cs.sendMessage(MessageColor.POSITIVE + "RoyalCommands " + MessageColor.NEUTRAL + "v" + this.plugin.version + MessageColor.POSITIVE + " reloaded.");
        return true;
    }
}
