package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdTpDeny implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdTpDeny(RoyalCommands plugin) {
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
            if (CmdTeleportRequest.tprdb.containsKey(cs)) {
                Player t = (Player) CmdTeleportRequest.tprdb.get(cs);
                cs.sendMessage(MessageColor.POSITIVE + "Teleport request denied.");
                t.sendMessage(MessageColor.POSITIVE + "Your teleport request was denied.");
                CmdTeleportRequest.tprdb.remove(cs);
                return true;
            }
            cs.sendMessage(MessageColor.NEGATIVE + "You have no requests pending.");
            return true;
        }
        return false;
    }
}
