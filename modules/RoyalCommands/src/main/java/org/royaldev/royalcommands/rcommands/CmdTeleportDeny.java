package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.teleport.TeleportRequest;

@ReflectCommand
public class CmdTeleportDeny extends BaseCommand {

    public CmdTeleportDeny(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players.");
            return true;
        }
        if (args.length < 1) {
            final TeleportRequest tr = TeleportRequest.getLatestRequest(cs.getName());
            if (tr == null) {
                cs.sendMessage(cmd.getDescription());
                return false;
            } else {
                tr.deny();
                return true;
            }
        }
        final TeleportRequest tr = TeleportRequest.getFirstRequest(RUtils.getOfflinePlayer(args[0]).getName(), cs.getName());
        if (tr == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "No such teleport request.");
            return true;
        }
        tr.deny();
        return true;
    }
}
