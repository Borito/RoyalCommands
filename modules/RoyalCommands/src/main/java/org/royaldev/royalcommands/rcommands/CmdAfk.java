package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AFKUtils;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ReflectCommand
public class CmdAfk extends BaseCommand {

    public final static Map<Player, Long> afkdb = new HashMap<>();
    public final static Map<Player, Long> movetimes = new HashMap<>();

    public CmdAfk(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        final Player p = (Player) cs;
        if (this.plugin.isVanished(p)) {
            cs.sendMessage(MessageColor.NEGATIVE + "You are vanished! The cloak of illusion would be lost if you went AFK!");
            return true;
        }
        if (!AFKUtils.isAfk(p)) {
            AFKUtils.setAfk(p, new Date().getTime());
            this.plugin.getServer().broadcastMessage(RUtils.colorize(RUtils.replaceVars(Config.afkFormat, p)));
            return true;
        }
        AFKUtils.unsetAfk(p);
        this.plugin.getServer().broadcastMessage(RUtils.colorize(RUtils.replaceVars(Config.returnFormat, p)));
        return true;
    }

}
