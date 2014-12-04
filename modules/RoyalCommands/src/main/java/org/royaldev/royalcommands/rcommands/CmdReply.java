package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

import java.util.Collection;

// TODO: Rewrite, for the love of god.

@ReflectCommand
public class CmdReply extends BaseCommand {

    public CmdReply(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        synchronized (CmdMessage.replydb) {
            if (!CmdMessage.replydb.containsKey(cs.getName())) {
                cs.sendMessage(MessageColor.NEGATIVE + "You have no one to reply to!");
                return true;
            }
        }
        String target = CmdMessage.replydb.get(cs.getName());
        OfflinePlayer t = this.plugin.getServer().getOfflinePlayer(target);
        if (!t.isOnline()) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player is offline!");
            return true;
        }
        synchronized (CmdMessage.replydb) {
            CmdMessage.replydb.put(t.getName(), cs.getName());
        }
        Player p = (Player) t;
        String m = RoyalCommands.getFinalArg(args, 0).trim();
        cs.sendMessage(MessageColor.NEUTRAL + "[" + MessageColor.POSITIVE + "You" + MessageColor.NEUTRAL + " -> " + MessageColor.POSITIVE + p.getName() + MessageColor.NEUTRAL + "] " + m);
        p.sendMessage(MessageColor.NEUTRAL + "[" + MessageColor.POSITIVE + cs.getName() + MessageColor.NEUTRAL + " -> " + MessageColor.POSITIVE + "You" + MessageColor.NEUTRAL + "] " + m);
        for (final Player p1 : this.plugin.getServer().getOnlinePlayers()) {
            if (PConfManager.getPConfManager(p1).getBoolean("messagespy")) {
                if (t == p1 || cs == p1) continue;
                p1.sendMessage(MessageColor.NEUTRAL + "[" + MessageColor.POSITIVE + cs.getName() + MessageColor.NEUTRAL + " -> " + MessageColor.POSITIVE + p.getName() + MessageColor.NEUTRAL + "] " + m);
            }
        }
        return true;
    }
}
