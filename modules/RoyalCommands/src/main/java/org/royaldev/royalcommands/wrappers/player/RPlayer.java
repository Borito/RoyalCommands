/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.wrappers.player;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PlayerConfiguration;
import org.royaldev.royalcommands.rcommands.home.Home;
import org.royaldev.royalcommands.rcommands.nick.Nick;
import org.royaldev.royalcommands.wrappers.teleport.PlayerTeleporter;

import java.util.List;
import java.util.UUID;

public interface RPlayer {

    Inventory getBackpack(World w);

    int getHomeLimit();

    List<String> getHomeNames();

    List<Home> getHomes();

    String getName();

    Nick getNick();

    OfflinePlayer getOfflinePlayer();

    Player getPlayer();

    PlayerConfiguration getPlayerConfiguration();

    RoyalCommands getRoyalCommands();

    PlayerTeleporter getTeleporter();

    UUID getUUID();

    boolean isOnline();

    boolean isSameAs(UUID uuid);

    boolean isSameAs(OfflinePlayer op);

    void sendMessage(final String message);
}
