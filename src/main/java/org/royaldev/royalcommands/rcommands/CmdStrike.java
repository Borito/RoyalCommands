package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdStrike implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdStrike(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("strike")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.strike")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player) && args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                cs.sendMessage(cmd.getUsage().replace("<command>", label));
                return true;
            }
            if (args.length < 1) {
                Player p = (Player) cs;
                Block bb = RUtils.getTarget(p);
                if (bb == null) {
                    cs.sendMessage(MessageColor.NEGATIVE + "Can't strike there!");
                    return true;
                }
                p.getWorld().strikeLightning(bb.getLocation());
                return true;
            }
            if (!plugin.ah.isAuthorized(cs, "rcmds.others.strike")) {
                cs.sendMessage(MessageColor.NEGATIVE + "You don't have permission for that!");
                return true;
            }
            Player target = plugin.getServer().getPlayer(args[0]);
            if (target == null || plugin.isVanished(target, cs)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            if (plugin.ah.isAuthorized(target, "rcmds.exempt.strike")) {
                cs.sendMessage(MessageColor.NEGATIVE + "You can't strike that player!");
                return true;
            }
            cs.sendMessage(MessageColor.POSITIVE + "Smiting " + MessageColor.NEUTRAL + target.getName() + MessageColor.POSITIVE + ".");
            target.sendMessage(MessageColor.NEGATIVE + "You have been smited by " + MessageColor.NEUTRAL + cs.getName() + MessageColor.NEGATIVE + ".");
            target.getWorld().strikeLightning(target.getLocation());
            return true;
        }
        return false;
    }
}
