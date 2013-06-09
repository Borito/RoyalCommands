package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.configuration.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdGetIP implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdGetIP(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("getip")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.getip")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (plugin.getConfig().getBoolean("disable_getip")) {
                cs.sendMessage(MessageColor.NEGATIVE + "/getip and /compareip have been disabled.");
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                cs.sendMessage(cmd.getUsage().replace("<command>", label));
                return true;
            }
            OfflinePlayer oplayer = plugin.getServer().getOfflinePlayer(args[0]);
            PConfManager pcm = PConfManager.getPConfManager(oplayer);
            if (pcm.exists())
                cs.sendMessage(MessageColor.NEUTRAL + oplayer.getName() + ": " + pcm.getString("ip"));
            else
                cs.sendMessage(MessageColor.NEGATIVE + "The player " + oplayer.getName() + " does not exist.");
            return true;
        }
        return false;
    }

}
