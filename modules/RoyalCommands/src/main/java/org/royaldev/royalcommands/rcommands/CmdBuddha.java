package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

@ReflectCommand
public class CmdBuddha extends BaseCommand {

    public CmdBuddha(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (args.length > 0) {
            if (!this.ah.isAuthorized(cs, cmd, PermType.OTHERS)) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            Player t = this.plugin.getServer().getPlayer(args[0]);
            if (t == null || this.plugin.isVanished(t, cs)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            PConfManager pcm = PConfManager.getPConfManager(t);
            if (!pcm.getBoolean("buddha")) {
                pcm.set("buddha", true);
                t.sendMessage(MessageColor.POSITIVE + "Buddha mode enabled by " + MessageColor.NEUTRAL + cs.getName() + MessageColor.POSITIVE + ".");
                cs.sendMessage(MessageColor.POSITIVE + "Enabled buddha mode for " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
            } else {
                pcm.set("buddha", false);
                t.sendMessage(MessageColor.POSITIVE + "Buddha mode disabled by " + MessageColor.NEUTRAL + cs.getName() + MessageColor.POSITIVE + ".");
                cs.sendMessage(MessageColor.POSITIVE + "Disabled buddha mode for " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
            }
            return true;
        }
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        Player p = (Player) cs;
        PConfManager pcm = PConfManager.getPConfManager(p);
        if (!pcm.getBoolean("buddha")) {
            pcm.set("buddha", true);
            cs.sendMessage(MessageColor.POSITIVE + "Enabled buddha mode for yourself.");
        } else {
            pcm.set("buddha", false);
            cs.sendMessage(MessageColor.POSITIVE + "Disabled buddha mode for yourself.");
        }
        return true;
    }
}
