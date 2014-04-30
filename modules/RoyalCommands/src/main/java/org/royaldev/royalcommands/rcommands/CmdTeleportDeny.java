package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.teleport.TeleportRequest;

@ReflectCommand
public class CmdTeleportDeny implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdTeleportDeny(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tpdeny")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.tpdeny")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
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
        return false;
    }
}
