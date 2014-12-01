package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.teleport.TeleportRequest;

import java.util.ArrayList;
import java.util.List;

@ReflectCommand
public class CmdTeleportDeny extends TabCommand {

    public CmdTeleportDeny(final RoyalCommands instance, final String name) {
        super(instance, name, true, new Short[]{CompletionType.LIST.getShort()});
    }

    @Override
    protected List<String> customList(CommandSender cs, Command cmd, String label, String[] args, String arg) {
        final List<String> completions = new ArrayList<>();
        final List<TeleportRequest> requests = TeleportRequest.getRequests().get(cs.getName());
        if (requests == null) return completions;
        for (final TeleportRequest request : requests) {
            final String requester = request.getRequester();
            if (this.plugin.getServer().getPlayer(requester) == null || completions.contains(requester)) continue;
            completions.add(requester);
        }
        return completions;
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] eargs, final CommandArguments ca) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players.");
            return true;
        }
        if (eargs.length < 1) {
            final TeleportRequest tr = TeleportRequest.getLatestRequest(cs.getName());
            if (tr == null) {
                cs.sendMessage(cmd.getDescription());
                return false;
            } else {
                tr.deny();
                return true;
            }
        }
        final TeleportRequest tr = TeleportRequest.getFirstRequest(RUtils.getOfflinePlayer(eargs[0]).getName(), cs.getName());
        if (tr == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "No such teleport request.");
            return true;
        }
        tr.deny();
        return true;
    }
}
