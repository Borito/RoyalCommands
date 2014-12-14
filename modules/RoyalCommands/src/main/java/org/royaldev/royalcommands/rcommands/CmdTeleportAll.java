package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.wrappers.player.RPlayer;

@ReflectCommand
public class CmdTeleportAll extends BaseCommand {

    public CmdTeleportAll(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        final Player p = (Player) cs;
        for (final Player t : this.plugin.getServer().getOnlinePlayers()) {
            if (!RUtils.isTeleportAllowed(t) && !this.ah.isAuthorized(cs, "rcmds.tpoverride")) continue;
            if (t.equals(p)) continue;
            final RPlayer rp = RPlayer.getRPlayer(t);
            final String error = rp.getTeleporter().teleport(p);
            if (!error.isEmpty()) {
                p.sendMessage(MessageColor.NEGATIVE + error);
                return true;
            }
        }
        p.sendMessage(MessageColor.POSITIVE + "All players teleported to you.");
        return true;
    }
}
