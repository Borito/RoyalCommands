package org.royaldev.royalcommands.rcommands;

import org.apache.commons.lang.BooleanUtils;
import org.royaldev.royalcommands.MessageColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.configuration.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdMobIgnore implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdMobIgnore(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("mobignore")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.mobignore")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                if (!(cs instanceof Player)) {
                    cs.sendMessage(cmd.getDescription());
                    cs.sendMessage(cmd.getUsage().replace("<command>", label));
                    return true;
                }
                Player p = (Player) cs;
                PConfManager pcm = PConfManager.getPConfManager(p);
                Boolean isHidden = pcm.getBoolean("mobignored");
                if (isHidden == null) isHidden = false;
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
            Boolean isHidden = pcm.getBoolean("mobignored");
            if (isHidden == null) isHidden = false;
            pcm.set("mobignored", !isHidden);
            String status = BooleanUtils.toStringOnOff(isHidden);
            cs.sendMessage(MessageColor.POSITIVE + "Toggled mob ignore " + MessageColor.NEUTRAL + status + MessageColor.POSITIVE + " for " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
            t.sendMessage(MessageColor.NEUTRAL + cs.getName() + MessageColor.POSITIVE + " toggled mob ignore " + MessageColor.NEUTRAL + status + MessageColor.POSITIVE + " for you.");
            return true;
        }
        return false;
    }

}
