package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdLoadPlayer implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdLoadPlayer(RoyalCommands instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("loadplayer")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.loadplayer")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            final Player t = plugin.getServer().getPlayer(args[0]);
            if (t == null) {
                cs.sendMessage(MessageColor.NEGATIVE + "No such player!");
                return true;
            }
            if (!t.getName().equals(cs.getName()) && !plugin.ah.isAuthorized(cs, "rcmds.others.loadplayer")) {
                cs.sendMessage(MessageColor.NEGATIVE + "You cannot load other players' data!");
                return true;
            }
            t.loadData();
            cs.sendMessage(MessageColor.POSITIVE + "Data loaded.");
            return true;
        }
        return false;
    }
}
