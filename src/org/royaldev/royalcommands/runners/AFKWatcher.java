package org.royaldev.royalcommands.runners;

import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AFKUtils;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.Date;

// TODO: Make this sync vs async

public class AFKWatcher implements Runnable {

    RoyalCommands plugin;

    public AFKWatcher(RoyalCommands instance) {
        plugin = instance;
    }

    @Override
    public void run() {
        long afkKickTime = plugin.afkKickTime;
        long afkAutoTime = plugin.afkAutoTime;
        long currentTime = new Date().getTime();
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (p == null) continue;
            if (!AFKUtils.isAfk(p)) {
                if (plugin.isAuthorized(p, "rcmds.exempt.autoafk")) continue;
                if (plugin.isVanished(p)) continue;
                if (!AFKUtils.moveTimesContains(p)) continue;
                if (afkAutoTime <= 0) continue;
                long lastMove = AFKUtils.getLastMove(p);
                if ((lastMove + (afkAutoTime * 1000)) < currentTime) {
                    AFKUtils.setAfk(p, currentTime);
                    plugin.getServer().broadcastMessage(p.getName() + " is now AFK.");
                    continue;
                }
            }
            if (!AFKUtils.isAfk(p)) continue;
            if (afkKickTime <= 0) continue;
            if (plugin.isAuthorized(p, "rcmds.exempt.afkkick")) return;
            long afkAt = AFKUtils.getAfkTime(p);
            if (afkAt + (afkKickTime * 1000) < currentTime)
                RUtils.kickPlayer(p, "You have been AFK for too long!");
        }
    }

}
