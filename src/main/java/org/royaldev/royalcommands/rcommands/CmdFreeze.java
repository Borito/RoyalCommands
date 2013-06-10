package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

public class CmdFreeze implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdFreeze(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("freeze")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.freeze")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                cs.sendMessage(cmd.getUsage().replace("<command>", label));
                return true;
            }
            Player victim = plugin.getServer().getPlayer(args[0]);
            if (victim != null) {
                if (plugin.ah.isAuthorized(victim, "rcmds.exempt.freeze")) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You can't freeze that player!");
                    return true;
                }
                PConfManager pcm = PConfManager.getPConfManager(victim);
                if (!pcm.getBoolean("frozen")) {
                    pcm.set("frozen", true);
                    cs.sendMessage(MessageColor.POSITIVE + "You have frozen " + MessageColor.NEUTRAL + victim.getName() + MessageColor.POSITIVE + "!");
                    victim.sendMessage(MessageColor.NEGATIVE + "You have been frozen!");
                    return true;
                } else {
                    pcm.set("frozen", false);
                    cs.sendMessage(MessageColor.POSITIVE + "You have thawed " + MessageColor.NEUTRAL + victim.getName() + MessageColor.POSITIVE + "!");
                    victim.sendMessage(MessageColor.POSITIVE + "You have been thawed!");
                    return true;
                }
            } else {
                OfflinePlayer victim2 = plugin.getServer().getOfflinePlayer(args[0]);
                PConfManager pcm = PConfManager.getPConfManager(victim2);
                if (victim2.isOp()) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You can't freeze that player!");
                    return true;
                }
                if (!pcm.exists()) {
                    cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                    return true;
                }
                if (!pcm.getBoolean("frozen")) {
                    pcm.set("frozen", true);
                    cs.sendMessage(MessageColor.POSITIVE + "You have frozen " + MessageColor.NEUTRAL + victim2.getName() + MessageColor.POSITIVE + "!");
                    return true;
                } else {
                    pcm.set("frozen", false);
                    cs.sendMessage(MessageColor.POSITIVE + "You have thawed " + MessageColor.NEUTRAL + victim2.getName() + MessageColor.POSITIVE + "!");
                    return true;
                }
            }
        }
        return false;
    }
}
