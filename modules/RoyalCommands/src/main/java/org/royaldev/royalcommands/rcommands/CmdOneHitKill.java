package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

@ReflectCommand
public class CmdOneHitKill extends BaseCommand {

    public CmdOneHitKill(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (args.length > 0) {
            Player t = this.plugin.getServer().getPlayer(args[0]);
            if (t == null || this.plugin.isVanished(t, cs)) {
                OfflinePlayer op = this.plugin.getServer().getOfflinePlayer(args[0]);
                PConfManager pcm = PConfManager.getPConfManager(op);
                if (!pcm.exists()) {
                    cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                    return true;
                }
                boolean ohk = pcm.getBoolean("ohk");
                if (!ohk) {
                    pcm.set("ohk", true);
                    cs.sendMessage(MessageColor.POSITIVE + "You have enabled onehitkill mode for " + MessageColor.NEUTRAL + op.getName() + MessageColor.POSITIVE + ".");
                    return true;
                }
                pcm.set("ohk", false);
                cs.sendMessage(MessageColor.POSITIVE + "You have disabled onehitkill mode for " + MessageColor.NEUTRAL + op.getName() + MessageColor.POSITIVE + ".");
                return true;
            }
            Player p = this.plugin.getServer().getPlayer(args[0]);
            PConfManager pcm = PConfManager.getPConfManager(p);
            if (!pcm.exists()) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            boolean ohk = pcm.getBoolean("ohk");
            if (!ohk) {
                pcm.set("ohk", true);
                cs.sendMessage(MessageColor.POSITIVE + "You have enabled onehitkill mode for " + MessageColor.NEUTRAL + p.getName() + MessageColor.POSITIVE + ".");
                p.sendMessage(MessageColor.POSITIVE + "The player " + MessageColor.NEUTRAL + cs.getName() + MessageColor.POSITIVE + " has enabled onehitkill for you.");
                return true;
            }
            pcm.set("ohk", false);
            cs.sendMessage(MessageColor.POSITIVE + "You have disabled onehitkill mode for " + MessageColor.NEUTRAL + p.getName() + MessageColor.POSITIVE + ".");
            p.sendMessage(MessageColor.NEGATIVE + "The player " + MessageColor.NEUTRAL + cs.getName() + MessageColor.NEGATIVE + " has disabled your onehitkill.");
            return true;
        }
        if (args.length < 1) {
            if (!(cs instanceof Player)) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player p = (Player) cs;
            PConfManager pcm = PConfManager.getPConfManager(p);
            boolean ohk = pcm.getBoolean("ohk");
            if (!ohk) {
                pcm.set("ohk", true);
                p.sendMessage(MessageColor.POSITIVE + "You have enabled onehitkill for yourself.");
                return true;
            }
            pcm.set("ohk", false);
            p.sendMessage(MessageColor.POSITIVE + "You have disabled onehitkill for yourself.");
            return true;
        }
        return true;
    }
}
