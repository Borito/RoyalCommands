package org.royaldev.royalcommands.rcommands;

import org.apache.commons.lang.BooleanUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

@ReflectCommand
public class CmdMobIgnore extends BaseCommand {

    public CmdMobIgnore(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            if (!(cs instanceof Player)) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player p = (Player) cs;
            PConfManager pcm = PConfManager.getPConfManager(p);
            boolean isHidden = pcm.getBoolean("mobignored");
            pcm.set("mobignored", !isHidden);
            String status = BooleanUtils.toStringOnOff(isHidden);
            cs.sendMessage(MessageColor.POSITIVE + "Toggled mob ignore " + MessageColor.NEUTRAL + status + MessageColor.POSITIVE + ".");
            return true;
        }
        Player t = plugin.getServer().getPlayer(args[0]);
        if (t == null || plugin.isVanished(t, cs)) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist.");
            return true;
        }
        PConfManager pcm = PConfManager.getPConfManager(t);
        boolean isHidden = pcm.getBoolean("mobignored", false);
        pcm.set("mobignored", !isHidden);
        String status = BooleanUtils.toStringOnOff(isHidden);
        cs.sendMessage(MessageColor.POSITIVE + "Toggled mob ignore " + MessageColor.NEUTRAL + status + MessageColor.POSITIVE + " for " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
        t.sendMessage(MessageColor.NEUTRAL + cs.getName() + MessageColor.POSITIVE + " toggled mob ignore " + MessageColor.NEUTRAL + status + MessageColor.POSITIVE + " for you.");
        return true;
    }
}
