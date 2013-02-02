package org.royaldev.royalcommands.runners;

import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RoyalCommands;

public class UserdataSaver implements Runnable {

    private final RoyalCommands plugin;

    public UserdataSaver(RoyalCommands instance) {
        plugin = instance;
    }

    @Override
    public void run() {
        synchronized (plugin.ymls) {
            for (PConfManager pcm : plugin.ymls.values()) {
                pcm.forceSave();
            }
        }
    }

}
