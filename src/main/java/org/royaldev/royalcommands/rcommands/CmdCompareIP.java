package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.configuration.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdCompareIP implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdCompareIP(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("compareip")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.compareip")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (Config.disablegetip) {
                cs.sendMessage(MessageColor.NEGATIVE + "/getip and /compareip have been disabled.");
                return true;
            }
            if (args.length < 2) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            OfflinePlayer player1;
            OfflinePlayer player2;
            player1 = plugin.getServer().getOfflinePlayer(args[0]);
            player2 = plugin.getServer().getOfflinePlayer(args[1]);
            PConfManager pcm1 = PConfManager.getPConfManager(player1);
            PConfManager pcm2 = PConfManager.getPConfManager(player2);
            if (pcm1.exists()) {
                if (pcm2.exists()) {
                    String p1ip = pcm1.getString("ip");
                    String p2ip = pcm2.getString("ip");

                    cs.sendMessage(MessageColor.NEUTRAL + player1.getName() + ": " + p1ip);
                    cs.sendMessage(MessageColor.NEUTRAL + player2.getName() + ": " + p2ip);
                    return true;
                } else {
                    cs.sendMessage(MessageColor.NEGATIVE + "The player " + player2.getName() + " does not exist.");
                    return true;
                }
            } else {
                cs.sendMessage(MessageColor.NEGATIVE + "The player " + player1.getName() + " does not exist.");
                return true;
            }
        }
        return false;
    }

}
