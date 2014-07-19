package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

// TODO: Not set reason, but set leave message (cleaner)

@ReflectCommand
public class CmdKick extends BaseCommand {

    public CmdKick(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        Player t = plugin.getServer().getPlayer(args[0]);
        if (t == null || plugin.isVanished(t, cs)) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
            return true;
        }
        if (this.ah.isAuthorized(t, cmd, PermType.EXEMPT)) {
            cs.sendMessage(MessageColor.NEGATIVE + "You cannot kick that player!");
            return true;
        }
        if (args.length == 1) {
            plugin.getServer().broadcast(RUtils.getInGameMessage(Config.igKickFormat, Config.kickMessage, t, cs), "rcmds.see.kick");
            RUtils.kickPlayer(t, cs, Config.kickMessage);
            return true;
        } else if (args.length > 1) {
            String kickMessage = RUtils.colorize(RoyalCommands.getFinalArg(args, 1));
            plugin.getServer().broadcast(RUtils.getInGameMessage(Config.igKickFormat, kickMessage, t, cs), "rcmds.see.kick");
            RUtils.kickPlayer(t, cs, kickMessage);
            return true;
        }
        return true;
    }
}
