package org.royaldev.royalcommands.runners;

import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.CmdAfk;

import java.util.Date;

public class AFKWatcher implements Runnable {

    RoyalCommands plugin;

    public AFKWatcher(RoyalCommands instance) {
        plugin = instance;
    }

    @Override
    public void run() {
        long afkKickTime = plugin.afkKickTime;
        long afkAutoTime = plugin.afkAutoTime;
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            long currentTime = new Date().getTime();
            if (!CmdAfk.afkdb.containsKey(p)) {
                if (!CmdAfk.movetimes.containsKey(p)) continue;
                if (afkAutoTime <= 0) continue;
                long lastMove = CmdAfk.movetimes.get(p);
                if ((lastMove + (afkAutoTime * 1000)) < currentTime) {
                    CmdAfk.afkdb.put(p, currentTime);
                    plugin.getServer().broadcastMessage(p.getName() + " is now AFK.");
                    continue;
                }
            }
            if (!CmdAfk.afkdb.containsKey(p)) continue;
            if (afkKickTime <= 0) continue;
            long afkAt = CmdAfk.afkdb.get(p);
            if (afkAt + (afkKickTime * 1000) >= currentTime) p.kickPlayer("You have been AFK for too long!");
        }
    }

}
