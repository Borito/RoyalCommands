package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdTeleportHere extends BaseCommand {

    public CmdTeleportHere(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        Player t = plugin.getServer().getPlayer(args[0]);
        if (t == null || plugin.isVanished(t, cs)) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
            return true;
        }
        if (!RUtils.isTeleportAllowed(t) && !this.ah.isAuthorized(cs, "rcmds.tpoverride")) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player has teleportation off!");
            return true;
        }
        Player p = (Player) cs;
        p.sendMessage(MessageColor.POSITIVE + "Teleporting " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + " to you.");
        String error = RUtils.teleport(t, p);
        if (!error.isEmpty()) {
            p.sendMessage(MessageColor.NEGATIVE + error);
            return true;
        }
        return true;
    }
}
