package org.royaldev.royalcommands.runners;

import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AFKUtils;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.logging.Logger;

// TODO: Make this sync vs async

public class AFKWatcher implements Runnable {

    private final RoyalCommands plugin;

    public AFKWatcher(RoyalCommands instance) {
        plugin = instance;
    }

    @Override
    public void run() {
        long afkKickTime = Config.afkKickTime;
        long afkAutoTime = Config.afkAutoTime;
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (p == null) continue;
            final long currentTime = System.currentTimeMillis();
            if (!AFKUtils.isAfk(p)) {
                if (plugin.ah.isAuthorized(p, "rcmds.exempt.autoafk")) continue;
                if (plugin.isVanished(p)) continue;
                if (!AFKUtils.moveTimesContains(p)) continue;
                if (afkAutoTime <= 0) continue;
                final long lastMove = AFKUtils.getLastMove(p);
                if ((lastMove + (afkAutoTime * 1000)) < currentTime) {
                    AFKUtils.setAfk(p, currentTime);
                    plugin.getServer().broadcastMessage(RUtils.colorize(RUtils.replaceVars(Config.afkFormat, p)));
                }
                continue;
            }
            if (afkKickTime <= 0) continue;
            if (plugin.ah.isAuthorized(p, "rcmds.exempt.afkkick")) return;
            final long afkAt = AFKUtils.getAfkTime(p);
            if (afkAt + (afkKickTime * 1000) < currentTime) {
                try {
                    RUtils.scheduleKick(p, "You have been AFK for too long!");
                } catch (IllegalArgumentException | NullPointerException e) {
                    Logger.getLogger("Minecraft").warning("[RoyalCommands] Could not kick " + p.getName() + " for being AFK: " + e.getMessage());
                }
            }
        }
    }

}
