package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdPing extends BaseCommand {

    public CmdPing(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!plugin.getNMSFace().hasSupport() || (!(cs instanceof Player) && args.length < 1)) {
            cs.sendMessage(MessageColor.POSITIVE + "Pong!");
            return true;
        }
        if (args.length > 0) {
            if (!this.ah.isAuthorized(cs, cmd, PermType.OTHERS)) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            Player p = plugin.getServer().getPlayer(args[0]);
            if (p == null || plugin.isVanished(p, cs)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            int ping = plugin.getNMSFace().getPing(p);
            String possessive = (p.getName().endsWith("s")) ? "'" : "'s";
            cs.sendMessage(MessageColor.NEUTRAL + p.getName() + possessive + MessageColor.POSITIVE + " ping: " + MessageColor.NEUTRAL + ping + "ms");
            return true;
        }
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.POSITIVE + "Pong!");
            return true;
        }
        Player p = (Player) cs;
        int ping = plugin.getNMSFace().getPing(p);
        p.sendMessage(MessageColor.POSITIVE + "Your ping: " + MessageColor.NEUTRAL + ping + "ms");
        return true;
    }
}
