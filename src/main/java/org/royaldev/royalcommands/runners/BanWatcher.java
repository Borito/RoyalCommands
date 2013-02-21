package org.royaldev.royalcommands.runners;

import org.bukkit.OfflinePlayer;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class BanWatcher implements Runnable {

    private RoyalCommands plugin;

    public BanWatcher(RoyalCommands instance) {
        plugin = instance;
    }

    @Override
    public void run() {
        OfflinePlayer[] players = plugin.getServer().getOfflinePlayers();
        for (OfflinePlayer op : players) {
            if (!op.isBanned()) continue;
            PConfManager pcm = plugin.getUserdata(op);
            if (!pcm.exists()) continue;
            if (pcm.get("bantime") != null && !RUtils.isTimeStampValid(op, "bantime")) {
                op.setBanned(false);
                pcm.set("bantime", null);
            }
        }
    }

}
