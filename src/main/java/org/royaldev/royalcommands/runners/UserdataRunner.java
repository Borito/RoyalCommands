package org.royaldev.royalcommands.runners;

import org.royaldev.royalcommands.ConfManager;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RoyalCommands;

public class UserdataRunner implements Runnable {

    private final RoyalCommands plugin;

    public UserdataRunner(RoyalCommands instance) {
        plugin = instance;
    }

    @Override
    public void run() {
        PConfManager.saveAllManagers();
        ConfManager.saveAllManagers();
        if (!plugin.purgeUnusedUserdata) return;
        Object[] managers = PConfManager.getAllManagers().toArray();
        for (Object o : managers) {
            if (!(o instanceof PConfManager)) continue;
            PConfManager pcm = (PConfManager) o;
            if (plugin.getServer().getPlayer(pcm.getManagerPlayerName()) != null) continue;
            pcm.discard(true);
        }
    }

}
