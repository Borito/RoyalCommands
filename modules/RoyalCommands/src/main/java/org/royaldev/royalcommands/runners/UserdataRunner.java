package org.royaldev.royalcommands.runners;

import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.Configuration;
import org.royaldev.royalcommands.configuration.FilePlayerConfiguration;
import org.royaldev.royalcommands.configuration.PlayerConfiguration;
import org.royaldev.royalcommands.configuration.PlayerConfigurationManager;

public class UserdataRunner implements Runnable {

    private final RoyalCommands plugin;

    public UserdataRunner(RoyalCommands instance) {
        this.plugin = instance;
    }

    @Override
    public void run() {
        PlayerConfigurationManager.saveAllConfigurations();
        Configuration.saveAllConfigurations();
        if (!Config.purgeUnusedUserdata) return;
        final Object[] managers = PlayerConfigurationManager.getAllConfigurations().toArray();
        for (final Object o : managers) {
            if (!(o instanceof FilePlayerConfiguration)) continue;
            final PlayerConfiguration pcm = (PlayerConfiguration) o;
            if (this.plugin.getServer().getPlayer(pcm.getManagerPlayerUUID()) != null) continue;
            pcm.discard(true);
        }
    }

}
