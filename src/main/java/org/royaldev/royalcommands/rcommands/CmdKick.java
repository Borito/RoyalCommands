package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdKick implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdKick(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("kick")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.kick")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                cs.sendMessage(cmd.getUsage().replace("<command>", label));
                return true;
            }
            Player t = plugin.getServer().getPlayer(args[0]);
            if (t == null || plugin.isVanished(t, cs)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            if (plugin.ah.isAuthorized(t, "rcmds.exempt.kick")) {
                cs.sendMessage(MessageColor.NEGATIVE + "You cannot kick that player!");
                return true;
            }
            if (args.length == 1) {
                plugin.getServer().broadcast(RUtils.getInGameMessage(Config.igKickFormat, Config.kickMessage, t, cs), "rcmds.see.kick");
                t.kickPlayer(RUtils.getMessage(Config.kickFormat, Config.kickMessage, cs));
                return true;
            } else if (args.length > 1) {
                String kickMessage = RUtils.colorize(RoyalCommands.getFinalArg(args, 1));
                plugin.getServer().broadcast(RUtils.getInGameMessage(Config.igKickFormat, kickMessage, t, cs), "rcmds.see.kick");
                t.kickPlayer(RUtils.getMessage(Config.kickFormat, kickMessage, cs));
                return true;
            }
        }
        return false;
    }
}
