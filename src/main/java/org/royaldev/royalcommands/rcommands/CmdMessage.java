package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

import java.util.HashMap;

public class CmdMessage implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdMessage(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    public final static HashMap<String, String> replydb = new HashMap<String, String>();

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("message")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.message")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 2) {
                return false;
            }
            Player t = plugin.getServer().getPlayer(args[0]);
            String m = RoyalCommands.getFinalArg(args, 1).trim();
            if (t == null || t.getName().trim().equals("")) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player is not online!");
                return true;
            }
            if (plugin.isVanished(t, cs)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            synchronized (replydb) {
                replydb.put(t.getName(), cs.getName());
                replydb.put(cs.getName(), t.getName());
            }

            if (m == null || m.equals("")) {
                cs.sendMessage(MessageColor.NEGATIVE + "You entered no message!");
                return true;
            }
            t.sendMessage(MessageColor.NEUTRAL + "[" + MessageColor.POSITIVE + cs.getName() + MessageColor.NEUTRAL + " -> " + MessageColor.POSITIVE + "You" + MessageColor.NEUTRAL + "] " + m);
            cs.sendMessage(MessageColor.NEUTRAL + "[" + MessageColor.POSITIVE + "You" + MessageColor.NEUTRAL + " -> " + MessageColor.POSITIVE + t.getName() + MessageColor.NEUTRAL + "] " + m);
            Player[] ps = plugin.getServer().getOnlinePlayers();
            for (Player p1 : ps) {
                if (PConfManager.getPConfManager(p1).getBoolean("spy")) {
                    if (t == p1 || cs == p1) continue;
                    p1.sendMessage(MessageColor.NEUTRAL + "[" + MessageColor.POSITIVE + cs.getName() + MessageColor.NEUTRAL + " -> " + MessageColor.POSITIVE + t.getName() + MessageColor.NEUTRAL + "] " + m);
                }
            }
            return true;
        }
        return false;
    }

}
