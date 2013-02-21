package org.royaldev.royalcommands.runners;

import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class MailRunner implements Runnable {

    private final RoyalCommands plugin;

    public MailRunner(RoyalCommands instance) {
        plugin = instance;
    }

    @Override
    public void run() {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            RUtils.checkMail(p);
        }
    }

}
