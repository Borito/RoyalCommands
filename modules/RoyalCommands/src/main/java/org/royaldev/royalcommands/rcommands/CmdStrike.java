package org.royaldev.royalcommands.rcommands;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdStrike implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdStrike(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("strike")) {
            if (!this.plugin.ah.isAuthorized(cs, cmd)) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player) && args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            if (args.length < 1 && cs instanceof Player) {
                Player p = (Player) cs;
                Block bb = RUtils.getTarget(p);
                if (bb == null) {
                    cs.sendMessage(MessageColor.NEGATIVE + "Can't strike there!");
                    return true;
                }
                p.getWorld().strikeLightning(bb.getLocation());
                return true;
            }
            if (!this.plugin.ah.isAuthorized(cs, cmd, PermType.OTHERS)) {
                cs.sendMessage(MessageColor.NEGATIVE + "You don't have permission for that!");
                return true;
            }
            Player t = plugin.getServer().getPlayer(args[0]);
            if (t == null || plugin.isVanished(t, cs)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            if (this.plugin.ah.isAuthorized(t, cmd, PermType.EXEMPT)) {
                cs.sendMessage(MessageColor.NEGATIVE + "You can't strike that player!");
                return true;
            }
            cs.sendMessage(MessageColor.POSITIVE + "Smiting " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
            t.sendMessage(MessageColor.NEGATIVE + "You have been smited by " + MessageColor.NEUTRAL + cs.getName() + MessageColor.NEGATIVE + ".");
            t.getWorld().strikeLightning(t.getLocation());
            return true;
        }
        return false;
    }
}
