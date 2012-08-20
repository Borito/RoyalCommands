package org.royaldev.royalcommands.runners;

import org.bukkit.OfflinePlayer;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class WarnWatcher implements Runnable {

    RoyalCommands plugin;

    public WarnWatcher(RoyalCommands instance) {
        plugin = instance;
    }

    @Override
    public void run() {
        if (plugin.warnExpireTime < 1) return;
        OfflinePlayer[] players = plugin.getServer().getOfflinePlayers();
        for (OfflinePlayer p : players) {
            PConfManager pcm = new PConfManager(p);
            if (!pcm.exists()) continue;
            if (pcm.get("warns") == null) continue;
            CopyOnWriteArrayList<String> cowal = new CopyOnWriteArrayList<String>();
            List<String> warns = pcm.getStringList("warns");
            if (warns == null) return;
            cowal.addAll(warns);
            for (String s : cowal) {
                String[] reason = s.split("\\u00b5");
                if (reason.length < 2) continue;
                long timeSet;
                try {
                    timeSet = Long.valueOf(reason[1]);
                } catch (NumberFormatException e) {
                    continue;
                }
                long currentTime = new Date().getTime();
                long timeExpires = timeSet + (plugin.warnExpireTime * 1000);
                if (timeExpires <= currentTime) cowal.remove(s);
            }
            pcm.setStringList(cowal, "warns");
        }
    }
}
