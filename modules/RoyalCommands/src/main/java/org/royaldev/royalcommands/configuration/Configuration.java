package org.royaldev.royalcommands.configuration;

import org.royaldev.royalcommands.RoyalCommands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Configuration extends FileGeneralConfiguration {

    private static final Map<String, Configuration> confs = new HashMap<>();
    private final Object saveLock = new Object();
    private final String path;
    private final String name;
    private File pconfl = null;

    /**
     * Configuration file manager
     * <p/>
     * If file does not exist, it will be created.
     *
     * @param filename Filename (local) for the config
     */
    Configuration(String filename) {
        super();
        final File dataFolder = RoyalCommands.dataFolder;
        this.path = dataFolder + File.separator + filename;
        this.pconfl = new File(this.path);
        try {
            this.load(this.pconfl);
        } catch (Exception ignored) {
        }
        this.name = filename;
    }

    /**
     * Configuration file manager
     * <p/>
     * If file does not exist, it will be created.
     *
     * @param file File object for the config
     */
    Configuration(File file) {
        this(file.getName());
    }

    /**
     * Just to prevent construction outside of package.
     */
    @SuppressWarnings("unused")
    private Configuration() {
        this.path = "";
        this.name = "";
    }

    public static Collection<Configuration> getAllConfigurations() {
        synchronized (Configuration.confs) {
            return Collections.synchronizedCollection(Configuration.confs.values());
        }
    }

    public static Configuration getConfiguration(String s) {
        synchronized (Configuration.confs) {
            if (Configuration.confs.containsKey(s)) return Configuration.confs.get(s);
            final Configuration cm = new Configuration(s);
            Configuration.confs.put(s, cm);
            return cm;
        }
    }

    public static boolean isConfigurationCreated(String s) {
        synchronized (Configuration.confs) {
            return Configuration.confs.containsKey(s);
        }
    }

    public static int configurationsCreated() {
        synchronized (Configuration.confs) {
            return Configuration.confs.size();
        }
    }

    public static void removeAllConfigurations() {
        final Collection<Configuration> oldConfs = new ArrayList<>();
        oldConfs.addAll(Configuration.confs.values());
        synchronized (Configuration.confs) {
            for (final Configuration cm : oldConfs) cm.discard(false);
        }
    }

    public static void saveAllConfigurations() {
        synchronized (Configuration.confs) {
            for (final Configuration cm : Configuration.confs.values()) cm.forceSave();
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
        synchronized (Configuration.confs) {
            if (save) this.forceSave();
            Configuration.confs.remove(this.name);
        }
    }

    public boolean exists() {
        return this.pconfl.exists();
    }

    public void forceSave() {
        synchronized (this.saveLock) {
            try {
                this.save(this.pconfl);
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    public void reload() {
        forceSave();
        try {
            this.load(this.pconfl);
        } catch (Exception ignored) {
        }
    }
}
