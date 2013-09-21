package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdTpAccept implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdTpAccept(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tpaccept")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.tpaccept")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            if (CmdTeleportRequest.tprdb.containsKey(cs)) {
                Player t = (Player) CmdTeleportRequest.tprdb.get(cs);
                cs.sendMessage(MessageColor.POSITIVE + "Teleport request accepted.");
                t.sendMessage(MessageColor.POSITIVE + "Your teleport request was accepted.");
                String error = RUtils.teleport(t, p);
                if (!error.isEmpty()) {
                    p.sendMessage(MessageColor.NEGATIVE + error);
                    return true;
                }
                CmdTeleportRequest.tprdb.remove(cs);
                return true;
            }
            if (CmdTeleportRequestHere.tprhdb.containsKey(cs)) {
                Player t = (Player) CmdTeleportRequestHere.tprhdb.get(cs);
                cs.sendMessage(MessageColor.POSITIVE + "Teleport request accepted.");
                t.sendMessage(MessageColor.POSITIVE + "Your teleport request was accepted.");
                String error = RUtils.teleport(p, t);
                if (!error.isEmpty()) {
                    p.sendMessage(MessageColor.NEGATIVE + error);
                    return true;
                }
                CmdTeleportRequestHere.tprhdb.remove(cs);
                return true;
            }
            cs.sendMessage(MessageColor.NEGATIVE + "You have no requests pending.");
            return true;
        }
        return false;
    }
}
