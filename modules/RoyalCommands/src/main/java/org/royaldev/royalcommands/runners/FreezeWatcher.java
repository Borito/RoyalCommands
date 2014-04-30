package org.royaldev.royalcommands.runners;

import org.bukkit.World;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.ConfManager;

import java.util.List;

public class FreezeWatcher implements Runnable {

    private final RoyalCommands plugin;

    public FreezeWatcher(RoyalCommands instance) {
        plugin = instance;
    }

    @Override
    public void run() {
        List<World> ws = plugin.getServer().getWorlds();
        ConfManager cm = RoyalCommands.wm.getConfig();
        for (World w : ws) {
            boolean isFrozen = cm.getBoolean("worlds." + w.getName() + ".freezetime");
            if (!isFrozen) continue;
            Long frozenAt = cm.getLong("worlds." + w.getName() + ".frozenat");
            w.setTime(frozenAt);
        }
    }

}
