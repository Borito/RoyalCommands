/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.wrappers.teleport;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.util.Vector;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.RUtils;

public class EntityTeleporter implements ITeleporter<Entity> {

    private final Entity teleportee;

    public EntityTeleporter(final Entity teleportee) {
        this.teleportee = teleportee;
    }

    private Location getSafeLocation(final Location location) {
        if (!Config.safeTeleport) return location;
        return RUtils.getSafeLocation(location);
    }

    private Entity getVehicleToTeleport() {
        if (!Config.vehicleTeleportEnabled) return null;
        final Entity vehicle = this.getTeleportee().getVehicle();
        if (vehicle == null) return null;
        if (Config.vehicleTeleportVehicles && vehicle instanceof Vehicle) return vehicle;
        if (Config.vehicleTeleportAnimals && vehicle instanceof Animals) return vehicle;
        if (Config.vehicleTeleportPlayers && vehicle instanceof Player) return vehicle;
        return null;
    }

    private void loadChunk(final Chunk chunk) {
        if (!chunk.isLoaded()) chunk.load(true);
    }

    private void mitigateFallDamage(final Entity e) {
        e.setFallDistance(0F);
        e.setVelocity(new Vector(0, 0, 0));
    }

    @Override
    public Entity getTeleportee() {
        return this.teleportee;
    }

    @Override
    public String teleport(final Block block) {
        return this.teleport(block.getLocation());
    }

    @Override
    public String teleport(final Entity entity) {
        return this.teleport(entity.getLocation());
    }

    @Override
    public String teleport(final Location location) {
        final Entity vehicle = this.getVehicleToTeleport();
        final Entity teleportee = vehicle == null ? this.getTeleportee() : vehicle;
        final Location teleportTo = this.getSafeLocation(location);
        if (teleportTo == null) return "There is no ground below.";
        this.loadChunk(teleportTo.getChunk());
        this.mitigateFallDamage(teleportee);
        teleportee.teleport(teleportTo);
        return "";
    }

}
