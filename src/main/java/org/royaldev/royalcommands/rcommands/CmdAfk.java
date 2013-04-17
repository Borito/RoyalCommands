package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AFKUtils;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CmdAfk implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdAfk(RoyalCommands instance) {
        this.plugin = instance;
    }

    public final static Map<Player, Long> afkdb = new HashMap<Player, Long>();
    public final static Map<Player, Long> movetimes = new HashMap<Player, Long>();

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("afk")) {
            if (!plugin.isAuthorized(cs, "rcmds.afk")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            if (plugin.isVanished(p)) {
                cs.sendMessage(MessageColor.NEGATIVE + "You are vanished! The cloak of illusion would be lost if you went AFK!");
                return true;
            }
            if (!AFKUtils.isAfk(p)) {
                AFKUtils.setAfk(p, new Date().getTime());
                plugin.getServer().broadcastMessage(RUtils.colorize(RUtils.replaceVars(Config.afkFormat, p)));
                return true;
            }
            AFKUtils.unsetAfk(p);
            plugin.getServer().broadcastMessage(RUtils.colorize(RUtils.replaceVars(Config.returnFormat, p)));
            return true;
        }
        return false;
    }

}
