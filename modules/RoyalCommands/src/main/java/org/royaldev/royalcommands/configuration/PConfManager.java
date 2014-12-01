package org.royaldev.royalcommands.configuration;

import org.bukkit.OfflinePlayer;
import org.royaldev.royalcommands.RoyalCommands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PConfManager extends GeneralConfManager {

    private static final Map<UUID, PConfManager> pcms = new HashMap<>();
    private final Object saveLock = new Object();
    private final UUID playerUUID;
    private File pconfl = null;

    /**
     * Player configuration manager
     *
     * @param p Player to manage
     */
    PConfManager(OfflinePlayer p) {
        this(p.getUniqueId());
    }

    /**
     * Player configuration manager.
     *
     * @param u Player UUID to manage
     */
    PConfManager(UUID u) {
        super();
        final File dataFolder = RoyalCommands.dataFolder;
        this.pconfl = new File(dataFolder + File.separator + "userdata" + File.separator + u + ".yml");
        try {
            this.load(this.pconfl);
        } catch (Exception ignored) {
        }
        this.playerUUID = u;
    }

    /**
     * Just to prevent construction outside of package.
     */
    @SuppressWarnings("unused")
    private PConfManager() {
        this.playerUUID = null;
    }

    public static synchronized Collection<PConfManager> getAllManagers() {
        synchronized (PConfManager.pcms) {
            return Collections.synchronizedCollection(PConfManager.pcms.values());
        }
    }

    public static PConfManager getPConfManager(UUID u) {
        synchronized (PConfManager.pcms) {
            if (PConfManager.pcms.containsKey(u)) return PConfManager.pcms.get(u);
            final PConfManager pcm = new PConfManager(u);
            PConfManager.pcms.put(u, pcm);
            return pcm;
        }
    }

    public static PConfManager getPConfManager(OfflinePlayer p) {
        return PConfManager.getPConfManager(p.getUniqueId());
    }

    public static boolean isManagerCreated(UUID u) {
        synchronized (PConfManager.pcms) {
            return PConfManager.pcms.containsKey(u);
        }
    }

    public static boolean isManagerCreated(OfflinePlayer p) {
        return PConfManager.isManagerCreated(p.getUniqueId());
    }

    public static int managersCreated() {
        synchronized (PConfManager.pcms) {
            return PConfManager.pcms.size();
        }
    }

    public static void removeAllManagers() {
        final Collection<PConfManager> oldConfs = new ArrayList<>();
        synchronized (PConfManager.pcms) {
            oldConfs.addAll(PConfManager.pcms.values());
            for (final PConfManager pcm : oldConfs) pcm.discard(false);
        }
    }

    public static void saveAllManagers() {
        synchronized (PConfManager.pcms) {
            for (final PConfManager pcm : PConfManager.pcms.values()) pcm.forceSave();
        }
    }

    public boolean createFile() {
        try {
            return this.pconfl.createNewFile();
        } catch (IOException ignored) {
            return false;
        }
    }

    /**
     * Removes the reference to this manager without saving.
     */
    public void discard() {
        this.discard(false);
    }

    /**
     * Removes the reference to this manager.
     *
     * @param save Save manager before removing references?
     */
    public void discard(boolean save) {
        if (save) this.forceSave();
        synchronized (PConfManager.pcms) {
            PConfManager.pcms.remove(playerUUID);
        }
    }

    public boolean exists() {
        return this.pconfl.exists();
    }

    public void forceSave() {
        synchronized (this.saveLock) {
            try {
                this.save(this.pconfl);
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
    public UUID getManagerPlayerUUID() {
        return this.playerUUID;
    }

    /**
     * Gets if this is the player's first join.
     *
     * @return true or false
     */
    public boolean isFirstJoin() {
        return this.getBoolean("first_join", true);
    }

    public void setFirstJoin(boolean firstJoin) {
        this.set("first_join", firstJoin);
    }
}
