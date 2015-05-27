/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.runners;

import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class MailRunner implements Runnable {

    private final RoyalCommands plugin;

    public MailRunner(RoyalCommands instance) {
        this.plugin = instance;
    }

    @Override
    public void run() {
        for (final Player p : this.plugin.getServer().getOnlinePlayers()) {
            RUtils.checkMail(p);
        }
    }

}
