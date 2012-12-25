package org.royaldev.royalcommands.runners;

import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.playermanagers.YMLPConfManager;

public class UserdataSaver implements Runnable {

    private final RoyalCommands plugin;

    public UserdataSaver(RoyalCommands instance) {
        plugin = instance;
    }

    @Override
    public void run() {
        if (plugin.saveUDOnChange) return;
        for (YMLPConfManager ymlpcm : plugin.ymls.values()) {
            ymlpcm.forceSave();
        }
    }

}
