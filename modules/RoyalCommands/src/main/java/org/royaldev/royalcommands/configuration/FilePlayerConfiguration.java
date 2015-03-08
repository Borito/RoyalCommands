package org.royaldev.royalcommands.configuration;

import org.bukkit.OfflinePlayer;
import org.royaldev.royalcommands.RoyalCommands;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FilePlayerConfiguration extends FileGeneralConfiguration implements PlayerConfiguration {

    private final Object saveLock = new Object();
    private final UUID playerUUID;
    private File file = null;

    /**
     * Player configuration manager
     *
     * @param p Player to manage
     */
    FilePlayerConfiguration(final OfflinePlayer p) {
        this(p.getUniqueId());
    }

    /**
     * Player configuration manager.
     *
     * @param u Player UUID to manage
     */
    FilePlayerConfiguration(final UUID u) {
        super();
        final File dataFolder = RoyalCommands.dataFolder;
        this.file = new File(dataFolder + File.separator + "userdata" + File.separator + u + ".yml");
        try {
            this.load(this.file);
        } catch (final Exception ignored) {
        }
        this.playerUUID = u;
    }

    /**
     * Just to prevent construction outside of package.
     */
    @SuppressWarnings("unused")
    private FilePlayerConfiguration() {
        this.playerUUID = null;
    }

    @Override
    public boolean createFile() {
        try {
            return this.file.createNewFile();
        } catch (final IOException ignored) {
            return false;
        }
    }

    /**
     * Removes the reference to this manager without saving.
     */
    @Override
    public void discard() {
        this.discard(false);
    }

    /**
     * Removes the reference to this manager.
     *
     * @param save Save manager before removing references?
     */
    @Override
    public void discard(final boolean save) {
        if (save) this.forceSave();
        PlayerConfigurationManager.discard(this);
    }

    @Override
    public boolean exists() {
        return this.file.exists();
    }

    @Override
    public void forceSave() {
        synchronized (this.saveLock) {
            try {
                this.save(this.file);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets the name of the player this manager was created for.
     *
     * @return Player name
     */
    @Override
    public UUID getManagerPlayerUUID() {
        return this.playerUUID;
    }

    /**
     * Gets if this is the player's first join.
     *
     * @return true or false
     */
    @Override
    public boolean isFirstJoin() {
        return this.getBoolean("first_join", true);
    }

    @Override
    public void setFirstJoin(boolean firstJoin) {
        this.set("first_join", firstJoin);
    }

    @Override
    public String toString() {
        return String.format("PConfManager@%s[playerUUID=%s, file=%s]", this.hashCode(), this.playerUUID, this.file);
    }
}
