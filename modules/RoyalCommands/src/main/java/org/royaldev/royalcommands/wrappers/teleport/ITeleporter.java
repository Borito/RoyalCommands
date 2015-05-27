/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.wrappers.teleport;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

public interface ITeleporter<T extends Entity> {

    public T getTeleportee();

    public String teleport(final Block block);

    public String teleport(final Entity entity);

    public String teleport(final Location location);

}
