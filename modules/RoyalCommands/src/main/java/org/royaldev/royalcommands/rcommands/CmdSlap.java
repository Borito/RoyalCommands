package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.Random;

@ReflectCommand
public class CmdSlap extends BaseCommand {

    public CmdSlap(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        Player t;
        t = plugin.getServer().getPlayer(args[0]);
        if (t == null || plugin.isVanished(t, cs)) {
            cs.sendMessage(MessageColor.NEGATIVE + "That person is not online!");
            return true;
        }
        if (this.ah.isAuthorized(t, cmd, PermType.EXEMPT)) {
            cs.sendMessage(MessageColor.NEGATIVE + "You may not slap that player.");
            return true;
        }
        Random r = new Random();
        Vector push = new Vector();
        push.setY(r.nextInt(2));
        push.setX(r.nextInt(4) - 2);
        push.setZ(r.nextInt(4) - 2);
        t.setVelocity(push);
        plugin.getServer().broadcastMessage(ChatColor.GOLD + cs.getName() + MessageColor.RESET + " slaps " + MessageColor.NEGATIVE + t.getName() + MessageColor.RESET + "!");
        return true;
    }
}
