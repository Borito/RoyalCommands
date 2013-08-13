package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

public class CmdClearWarns implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdClearWarns(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("clearwarns")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.clearwarns")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            OfflinePlayer op = plugin.getServer().getOfflinePlayer(args[0]);
            PConfManager pcm = PConfManager.getPConfManager(op);
            if (!pcm.exists()) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            if (pcm.get("warns") == null || pcm.getStringList("warns").isEmpty()) {
                cs.sendMessage(MessageColor.NEGATIVE + "There are no warnings for " + MessageColor.NEUTRAL + op.getName() + MessageColor.NEGATIVE + "!");
                return true;
            }
            pcm.set("warns", null);
            cs.sendMessage(MessageColor.POSITIVE + "You've cleared the warnings of " + MessageColor.NEUTRAL + op.getName() + MessageColor.POSITIVE + ".");
            return true;
        }
        return false;
    }

}
