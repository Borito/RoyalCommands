package org.royaldev.royalcommands.runners;

import org.bukkit.OfflinePlayer;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WarnWatcher implements Runnable {

    private RoyalCommands plugin;

    public WarnWatcher(RoyalCommands instance) {
        plugin = instance;
    }

    @Override
    public void run() {
        if (plugin.warnExpireTime < 1L) return;
        OfflinePlayer[] players = plugin.getServer().getOfflinePlayers();
        for (OfflinePlayer p : players) {
            PConfManager pcm = PConfManager.getPConfManager(p);
            if (!pcm.exists()) continue;
            if (pcm.get("warns") == null) continue;
            List<String> warns = pcm.getStringList("warns");
            List<String> warnsToRemove = new ArrayList<String>();
            if (warns == null) return;
            for (String s : warns) {
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
                if (timeExpires <= currentTime) warnsToRemove.add(s);
            }
            warns.removeAll(warnsToRemove);
            pcm.set("warns", warns);
        }
    }
}
