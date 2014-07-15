package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdSavePlayer extends BaseCommand {

    public CmdSavePlayer(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        final Player t = plugin.getServer().getPlayer(args[0]);
        if (t == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "No such player!");
            return true;
        }
        if (!t.getName().equals(cs.getName()) && !this.ah.isAuthorized(cs, cmd, PermType.OTHERS)) {
            cs.sendMessage(MessageColor.NEGATIVE + "You cannot save other players' data!");
            return true;
        }
        t.saveData();
        cs.sendMessage(MessageColor.POSITIVE + "Data saved.");
        return true;
    }
}
