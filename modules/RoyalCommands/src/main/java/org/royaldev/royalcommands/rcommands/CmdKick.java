package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

// TODO: Not set reason, but set leave message (cleaner)

@ReflectCommand
public class CmdKick implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdKick(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("kick")) {
            if (!this.plugin.ah.isAuthorized(cs, cmd)) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player t = plugin.getServer().getPlayer(args[0]);
            if (t == null || plugin.isVanished(t, cs)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            if (this.plugin.ah.isAuthorized(t, cmd, PermType.EXEMPT)) {
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
        }
        return false;
    }
}
