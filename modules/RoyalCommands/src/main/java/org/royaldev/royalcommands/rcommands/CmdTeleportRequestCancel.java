package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.teleport.TeleportRequest;
import org.royaldev.royalcommands.rcommands.teleport.TeleportRequest.TeleportType;

import java.util.ArrayList;
import java.util.List;

@ReflectCommand
public class CmdTeleportRequestCancel extends TabCommand {

    public CmdTeleportRequestCancel(final RoyalCommands instance, final String name) {
        super(instance, name, true, new Short[]{CompletionType.LIST.getShort()});
    }

    @Override
    protected List<String> customList(CommandSender cs, Command cmd, String label, String[] args, String arg) {
        final List<String> completions = new ArrayList<>();
        for (final List<TeleportRequest> entry : TeleportRequest.getRequests().values()) {
            for (final TeleportRequest request : entry) {
                if (!request.getRequester().equalsIgnoreCase(cs.getName())) continue;
                final String target = request.getTarget();
                if (completions.contains(target)) continue;
                completions.add(target);
            }
        }
        return completions;
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] eargs, CommandArguments ca) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        if (eargs.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        final Player p = (Player) cs;
        final String targetName = eargs[0];
        final TeleportRequest tr = TeleportRequest.getFirstRequest(p.getName(), targetName);
        if (tr == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "No such request!");
            return true;
        }
        tr.expire();
        cs.sendMessage(MessageColor.POSITIVE + "The request to teleport " + MessageColor.NEUTRAL + ((tr.getType() == TeleportType.TO) ? tr.getRequester() : tr.getTarget()) + MessageColor.POSITIVE + " to " + MessageColor.NEUTRAL + ((tr.getType() == TeleportType.TO) ? tr.getTarget() : tr.getRequester()) + MessageColor.POSITIVE + " was canceled.");
        return true;
    }
}
