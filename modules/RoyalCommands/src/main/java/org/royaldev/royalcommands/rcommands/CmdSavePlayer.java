package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdSavePlayer implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdSavePlayer(RoyalCommands instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("saveplayer")) {
            if (!this.plugin.ah.isAuthorized(cs, cmd)) {
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
            if (!t.getName().equals(cs.getName()) && !this.plugin.ah.isAuthorized(cs, cmd, PermType.OTHERS)) {
                cs.sendMessage(MessageColor.NEGATIVE + "You cannot save other players' data!");
                return true;
            }
            t.saveData();
            cs.sendMessage(MessageColor.POSITIVE + "Data saved.");
            return true;
        }
        return false;
    }
}
