package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.teleport.TeleportRequest;
import org.royaldev.royalcommands.rcommands.teleport.TeleportRequest.TeleportType;

@ReflectCommand
public class CmdTeleportRequestAll extends BaseCommand {

    public CmdTeleportRequestAll(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        Player p = (Player) cs;
        for (Player t : plugin.getServer().getOnlinePlayers()) {
            if (!RUtils.isTeleportAllowed(t) && !this.ah.isAuthorized(cs, "rcmds.tpoverride")) continue;
            if (t.equals(p)) continue;
            TeleportRequest.send(p, t, TeleportType.HERE, false);
        }
        p.sendMessage(MessageColor.POSITIVE + "You have sent a teleport request to all players.");
        return true;
    }
}
