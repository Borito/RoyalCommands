package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdPlayerTime implements CommandExecutor {

    static RoyalCommands plugin;

    public CmdPlayerTime(RoyalCommands instance) {
        plugin = instance;
    }

    public static void smoothPlayerTimeChange(long time, final Player p) {
        if (time > 24000L) time = time % 24000L;
        if (time < 0L) time = 0L; // Clamp to 0 to prevent loop
        final long ftime = time;
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                for (long i = p.getPlayerTime() + 1; i != ftime; i++) {
                    if (i == 24001L) {
                        i = 0L;
                        if (ftime == 0L) break;
                    }
                    p.setPlayerTime(i, false);
                }
                p.setPlayerTime(ftime, false);
            }
        };
        plugin.getServer().getScheduler().runTask(plugin, r);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equals("playertime")) {
            if (!plugin.isAuthorized(cs, "rcmds.playertime")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player t = plugin.getServer().getPlayer(args[0]);
            if (t == null || plugin.isVanished(t, cs)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            String possessive = (t.getName().toLowerCase().endsWith("s")) ? "'" : "'s";
            Integer time = null;
            if (args.length > 1) {
                try {
                    time = Integer.valueOf(args[1]);
                } catch (Exception e) {
                    cs.sendMessage(MessageColor.NEGATIVE + "That time was invalid!");
                    return true;
                }
            }
            if (time == null) {
                t.resetPlayerTime();
                cs.sendMessage(MessageColor.POSITIVE + "Synced " + MessageColor.NEUTRAL + t.getName() + possessive + MessageColor.POSITIVE + " time with the server's.");
                return true;
            }
            if (Config.smoothTime) smoothPlayerTimeChange(time, t);
            t.setPlayerTime(time, false);
            cs.sendMessage(MessageColor.POSITIVE + "Set " + MessageColor.NEUTRAL + t.getName() + possessive + MessageColor.POSITIVE + " time to " + MessageColor.NEUTRAL + time + " ticks" + MessageColor.POSITIVE + ".");
            return true;
        }
        return false;
    }

}
