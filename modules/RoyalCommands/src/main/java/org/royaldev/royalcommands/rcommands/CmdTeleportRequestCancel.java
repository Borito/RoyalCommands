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
public class CmdTeleportRequestCancel implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdTeleportRequestCancel(RoyalCommands instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("teleportrequestcancel")) {
            if (!this.plugin.ah.isAuthorized(cs, cmd)) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            final Player p = (Player) cs;
            final String targetName = args[0];
            final TeleportRequest tr = TeleportRequest.getFirstRequest(p.getName(), targetName);
            if (tr == null) {
                cs.sendMessage(MessageColor.NEGATIVE + "No such request!");
                return true;
            }
            tr.expire();
            cs.sendMessage(MessageColor.POSITIVE + "The request to teleport " + MessageColor.NEUTRAL + ((tr.getType() == TeleportRequest.TeleportType.TO) ? tr.getRequester() : tr.getTarget()) + MessageColor.POSITIVE + " to " + MessageColor.NEUTRAL + ((tr.getType() == TeleportRequest.TeleportType.TO) ? tr.getTarget() : tr.getRequester()) + MessageColor.POSITIVE + " was canceled.");
            return true;
        }
        return false;
    }
}
