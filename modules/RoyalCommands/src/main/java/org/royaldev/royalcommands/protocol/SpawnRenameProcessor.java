/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.protocol;

import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RoyalCommands;
import org.shininet.bukkit.itemrenamer.AbstractRenameProcessor;
import org.shininet.bukkit.itemrenamer.api.RenamerSnapshot;

public class SpawnRenameProcessor extends AbstractRenameProcessor {

    private final RoyalCommands plugin;
    public SpawnRenameProcessor(RoyalCommands instance) {
        super("org.royaldev.royalcommands.spawninfo.TempSpawn");
        this.plugin = instance;
    }

    @Override
    protected void processSnapshot(Player player, RenamerSnapshot itemStacks) {
        // We don't use this at all.
    }
}
