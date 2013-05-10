package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.configuration.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdReply implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdReply(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("reply")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.reply")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
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
            OfflinePlayer t = plugin.getServer().getOfflinePlayer(target);

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
            Player[] ps = plugin.getServer().getOnlinePlayers();
            for (Player p1 : ps) {
                if (PConfManager.getPConfManager(p1).getBoolean("spy")) {
                    if (t == p1 || cs == p1) continue;
                    p1.sendMessage(MessageColor.NEUTRAL + "[" + MessageColor.POSITIVE + cs.getName() + MessageColor.NEUTRAL + " -> " + MessageColor.POSITIVE + p.getName() + MessageColor.NEUTRAL + "] " + m);
                }
            }
            return true;
        }
        return false;
    }

}
