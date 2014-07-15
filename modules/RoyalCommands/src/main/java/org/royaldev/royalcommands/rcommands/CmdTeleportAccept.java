package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.teleport.TeleportRequest;

@ReflectCommand
public class CmdTeleportAccept extends BaseCommand {

    public CmdTeleportAccept(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        final Player p = (Player) cs;
        if (args.length < 1) {
            final TeleportRequest tr = TeleportRequest.getLatestRequest(p.getName());
            if (tr == null) {
                cs.sendMessage(cmd.getDescription());
                return false;
            } else {
                tr.accept();
                return true;
            }
        }
        final Player t = plugin.getServer().getPlayer(args[0]);
        if (t == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player is not online.");
            return true;
        }
        final TeleportRequest tr = TeleportRequest.getFirstRequest(t.getName(), p.getName());
        if (tr == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "No matching teleport requests!");
            return true;
        }
        tr.accept();
        return true;
    }
}
