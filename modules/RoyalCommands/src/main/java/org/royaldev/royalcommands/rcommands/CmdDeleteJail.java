package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.ConfManager;

@ReflectCommand
public class CmdDeleteJail implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdDeleteJail(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("deljail")) {
            if (!this.plugin.ah.isAuthorized(cs, cmd)) {
                RUtils.dispNoPerms(cs);
                return true;
            }
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
        return false;
    }

}
