package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

public class CmdUnban implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdUnban(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("unban")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.unban")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                cs.sendMessage(cmd.getUsage());
                return true;
            }
            OfflinePlayer t = plugin.getServer().getOfflinePlayer(args[0]);
            PConfManager pcm = PConfManager.getPConfManager(t);
            if (!t.isBanned()) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player isn't banned!");
                return true;
            }
            t.setBanned(false);
            if (pcm.exists()) pcm.set("bantime", null);
            cs.sendMessage(MessageColor.POSITIVE + "You have unbanned " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
            String message = RUtils.getInGameMessage(Config.igUnbanFormat, "", t, cs); // "" because there is no reason for unbans
            plugin.getServer().broadcast(message, "rcmds.see.unban");
            return true;
        }
        return false;
    }

}
