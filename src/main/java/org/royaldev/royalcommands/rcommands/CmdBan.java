package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

import java.util.Date;

public class CmdBan implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdBan(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ban")) {
            if (!plugin.isAuthorized(cs, "rcmds.ban")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            OfflinePlayer t = plugin.getServer().getPlayer(args[0]);
            if (t == null) t = plugin.getServer().getOfflinePlayer(args[0]);
            PConfManager pcm = PConfManager.getPConfManager(t);
/*            if (!pcm.getConfExists()) {
                if (args.length > 1 && args[1].equalsIgnoreCase("true")) {
                    args = (String[]) ArrayUtils.remove(args, 1);
                } else {
                    cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                    return true;
                }
            }*/
            if (!pcm.exists()) pcm.createFile();
            if (plugin.isAuthorized(t, "rcmds.exempt.ban")) {
                cs.sendMessage(MessageColor.NEGATIVE + "You can't ban that player!");
                return true;
            }
            String banreason = (args.length > 1) ? RoyalCommands.getFinalArg(args, 1) : Config.banMessage;
            banreason = RUtils.colorize(banreason);
            pcm.set("banreason", banreason);
            pcm.set("banner", cs.getName());
            pcm.set("bannedat", new Date().getTime());
            pcm.set("bantime", null);
            cs.sendMessage(MessageColor.POSITIVE + "You have banned " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
            RUtils.banPlayer(t, cs, banreason);
            return true;
        }
        return false;
    }
}
