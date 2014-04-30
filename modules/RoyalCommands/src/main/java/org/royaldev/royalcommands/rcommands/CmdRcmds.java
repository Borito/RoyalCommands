package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.ConfManager;
import org.royaldev.royalcommands.configuration.PConfManager;

@ReflectCommand
public class CmdRcmds implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdRcmds(RoyalCommands plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("rcmds")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.rcmds")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
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
        return false;
    }
}
