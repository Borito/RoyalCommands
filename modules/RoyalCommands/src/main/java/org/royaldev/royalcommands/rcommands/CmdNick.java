package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

@ReflectCommand
public class CmdNick extends BaseCommand {

    public CmdNick(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (args.length < 2) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        OfflinePlayer t = this.plugin.getServer().getOfflinePlayer(args[0]);
        if (!t.equals(cs) && !this.ah.isAuthorized(cs, cmd, PermType.OTHERS)) {
            RUtils.dispNoPerms(cs);
            return true;
        }
        PConfManager pcm = PConfManager.getPConfManager(t);
        if (!pcm.exists()) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player doesn't exist!");
            return true;
        }
        if (args[1].equalsIgnoreCase("off")) {
            pcm.set("dispname", t.getName());
            if (t.isOnline()) {
                Player p = (Player) t;
                p.setDisplayName(t.getName());
                if (t.getName().length() <= 16) p.setPlayerListName(t.getName());
                if (!(cs instanceof Player) || !cs.equals(p))
                    p.sendMessage(MessageColor.POSITIVE + "Your nickname was reset by " + MessageColor.NEUTRAL + cs.getName() + MessageColor.POSITIVE + ".");
            }
            cs.sendMessage(MessageColor.POSITIVE + "You reset the nickname of " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
            return true;
        }
        if (t.getName().equalsIgnoreCase(cs.getName())) {
            final long allowedAfter = pcm.getLong("nick.lastchange", 0L) + ((long) RUtils.timeFormatToSeconds(Config.nickChangeLimit) * 1000L);
            if (allowedAfter > System.currentTimeMillis() && !this.ah.isAuthorized(cs, "rcmds.exempt.nickchangelimit")) {
                cs.sendMessage(MessageColor.NEGATIVE + "You have to wait " + MessageColor.NEUTRAL + RUtils.formatDateDiff(allowedAfter) + MessageColor.NEGATIVE + "to change your nick again.");
                return true;
            }
        }
        if (cs instanceof Player && !this.ah.isAuthorized(cs, "rcmds.exempt.nickspecialcharacters") && !args[1].matches(Config.nickRegex)) {
            cs.sendMessage(MessageColor.NEGATIVE + "That nickname contains invalid characters!");
            return true;
        }
        String newName = Config.nickPrefix + args[1];
        if (this.ah.isAuthorized(cs, "rcmds.nick.color")) newName = RUtils.colorize(newName);
        else newName = RUtils.decolorize(newName);
        pcm.set("dispname", newName);
        pcm.set("nick.lastchange", System.currentTimeMillis());
        if (t.isOnline()) {
            Player p = (Player) t;
            p.setDisplayName(newName);
            if (newName.length() <= 16) p.setPlayerListName(newName);
            if (!(cs instanceof Player) || !cs.equals(p))
                p.sendMessage(MessageColor.POSITIVE + "Your nickname was changed to " + MessageColor.NEUTRAL + newName + MessageColor.POSITIVE + " by " + MessageColor.NEUTRAL + cs.getName() + MessageColor.POSITIVE + ".");
        }
        cs.sendMessage(MessageColor.POSITIVE + "Changed the nick of " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + " to " + MessageColor.NEUTRAL + newName + MessageColor.POSITIVE + ".");
        return true;
    }
}
