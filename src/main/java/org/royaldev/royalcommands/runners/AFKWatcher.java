package org.royaldev.royalcommands.runners;

import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AFKUtils;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.Date;
import java.util.logging.Logger;

// TODO: Make this sync vs async

public class AFKWatcher implements Runnable {

    private RoyalCommands plugin;

    public AFKWatcher(RoyalCommands instance) {
        plugin = instance;
    }

    @Override
    public void run() {
        long afkKickTime = Config.afkKickTime;
        long afkAutoTime = Config.afkAutoTime;
        long currentTime = new Date().getTime();
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (p == null) continue;
            if (!AFKUtils.isAfk(p)) {
                if (plugin.ah.isAuthorized(p, "rcmds.exempt.autoafk")) continue;
                if (plugin.isVanished(p)) continue;
                if (!AFKUtils.moveTimesContains(p)) continue;
                if (afkAutoTime <= 0) continue;
                long lastMove = AFKUtils.getLastMove(p);
                if ((lastMove + (afkAutoTime * 1000)) < currentTime) {
                    AFKUtils.setAfk(p, currentTime);
                    plugin.getServer().broadcastMessage(RUtils.colorize(RUtils.replaceVars(Config.afkFormat, p)));
                    continue;
                }
            }
            if (!AFKUtils.isAfk(p)) continue;
            if (afkKickTime <= 0) continue;
            if (plugin.ah.isAuthorized(p, "rcmds.exempt.afkkick")) return;
            long afkAt = AFKUtils.getAfkTime(p);
            if (afkAt + (afkKickTime * 1000) < currentTime) {
                try {
                    RUtils.scheduleKick(p, "You have been AFK for too long!");
                } catch (IllegalArgumentException e) {
                    Logger.getLogger("Minecraft").warning("[RoyalCommands] Could not kick " + p.getName() + " for being AFK: " + e.getMessage());
                } catch (NullPointerException e) {
                    Logger.getLogger("Minecraft").warning("[RoyalCommands] Could not kick " + p.getName() + " for being AFK: " + e.getMessage());
                }
            }
        }
    }

}
