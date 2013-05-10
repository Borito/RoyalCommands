package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

@SuppressWarnings("unused")
public class CmdNick implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdNick(RoyalCommands instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("nick")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.nick")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 2) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            OfflinePlayer t = plugin.getServer().getOfflinePlayer(args[0]);
            if (!t.equals(cs) && !plugin.ah.isAuthorized(cs, "rcmds.others.nick")) {
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
            String newName = Config.nickPrefix + args[1];
            if (plugin.ah.isAuthorized(cs, "rcmds.nick.color"))
                newName = RUtils.colorize(newName);
            pcm.set("dispname", newName);
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
        return false;
    }
}
