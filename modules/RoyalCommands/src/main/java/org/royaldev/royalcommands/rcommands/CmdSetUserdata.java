package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

@ReflectCommand
public class CmdSetUserdata implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdSetUserdata(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("setuserdata")) {
            if (!this.plugin.ah.isAuthorized(cs, cmd)) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 3) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            String name = args[0];
            String node = args[1];
            String value = RoyalCommands.getFinalArg(args, 2);
            OfflinePlayer op = plugin.getServer().getOfflinePlayer(name);
            PConfManager pcm = PConfManager.getPConfManager(op);
            if (!pcm.exists() || !op.hasPlayedBefore()) {
                cs.sendMessage(MessageColor.NEGATIVE + "No such player!");
                return true;
            }
            pcm.set(node, value);
            cs.sendMessage(MessageColor.POSITIVE + "Set " + MessageColor.NEUTRAL + node + MessageColor.POSITIVE + " to " + MessageColor.NEUTRAL + value + MessageColor.POSITIVE + " for the userdata of " + MessageColor.NEUTRAL + name + MessageColor.POSITIVE + ".");
            return true;
        }
        return false;
    }

}
