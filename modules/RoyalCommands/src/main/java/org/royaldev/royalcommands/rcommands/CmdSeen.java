package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

@ReflectCommand
public class CmdSeen implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdSeen(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("seen")) {
            if (!this.plugin.ah.isAuthorized(cs, cmd)) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            OfflinePlayer t = plugin.getServer().getOfflinePlayer(args[0]);
            if (t.isOnline() && !plugin.isVanished((Player) t, cs)) {
                cs.sendMessage(MessageColor.POSITIVE + "The player " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + " was last seen " + MessageColor.NEUTRAL + "now" + MessageColor.POSITIVE + ".");
                return true;
            }
            PConfManager pcm = PConfManager.getPConfManager(t);
            if (!pcm.exists()) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player doesn't exist!");
                return true;
            }
            if (pcm.get("seen") == null) {
                cs.sendMessage(MessageColor.NEGATIVE + "I don't know when that player was last seen!");
                return true;
            }
            long seen = pcm.getLong("seen");
            if (seen < 1L) {
                cs.sendMessage(MessageColor.NEGATIVE + "I don't know when that player was last seen!");
                return true;
            }
            String lastseen = RUtils.formatDateDiff(seen);
            cs.sendMessage(MessageColor.POSITIVE + "The player " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + " was last seen" + MessageColor.NEUTRAL + lastseen + MessageColor.POSITIVE + " ago.");
            return true;
        }
        return false;
    }

}
