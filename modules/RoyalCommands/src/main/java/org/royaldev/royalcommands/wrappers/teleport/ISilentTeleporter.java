package org.royaldev.royalcommands.wrappers.teleport;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

public interface ISilentTeleporter<T extends Entity> {

    public T getTeleportee();

    public String teleport(final Block block, final boolean silent);

    public String teleport(final Entity entity, final boolean silent);

    public String teleport(final Location location, final boolean silent);

}
