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
