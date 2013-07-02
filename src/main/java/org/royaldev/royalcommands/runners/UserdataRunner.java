package org.royaldev.royalcommands.runners;

import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.ConfManager;
import org.royaldev.royalcommands.configuration.PConfManager;

public class UserdataRunner implements Runnable {

    private final RoyalCommands plugin;

    public UserdataRunner(RoyalCommands instance) {
        plugin = instance;
    }

    @Override
    public void run() {
        PConfManager.saveAllManagers();
        ConfManager.saveAllManagers();
        if (!Config.purgeUnusedUserdata) return;
        final Object[] managers = PConfManager.getAllManagers().toArray();
        for (Object o : managers) {
            if (!(o instanceof PConfManager)) continue;
            final PConfManager pcm = (PConfManager) o;
            if (plugin.getServer().getPlayer(pcm.getManagerPlayerName()) != null) continue;
            pcm.discard(true);
        }
    }

}
