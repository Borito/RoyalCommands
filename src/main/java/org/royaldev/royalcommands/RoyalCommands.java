/*
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.

 This plugin was written by jkcclemens <jkc.clemens@gmail.com>.
 If forked and not credited, alert him.
 */
package org.royaldev.royalcommands;

import com.griefcraft.lwc.LWCPlugin;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.craftbukkit.libs.com.google.gson.reflect.TypeToken;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.kitteh.tag.TagAPI;
import org.kitteh.vanish.VanishPlugin;
import org.royaldev.royalcommands.api.RApiMain;
import org.royaldev.royalcommands.configuration.ConfManager;
import org.royaldev.royalcommands.configuration.PConfManager;
import org.royaldev.royalcommands.listeners.MonitorListener;
import org.royaldev.royalcommands.listeners.RoyalCommandsBlockListener;
import org.royaldev.royalcommands.listeners.RoyalCommandsEntityListener;
import org.royaldev.royalcommands.listeners.RoyalCommandsPlayerListener;
import org.royaldev.royalcommands.listeners.ServerListener;
import org.royaldev.royalcommands.listeners.SignListener;
import org.royaldev.royalcommands.listeners.TagAPIListener;
import org.royaldev.royalcommands.nms.api.NMSFace;
import org.royaldev.royalcommands.runners.AFKWatcher;
import org.royaldev.royalcommands.runners.BanWatcher;
import org.royaldev.royalcommands.runners.FreezeWatcher;
import org.royaldev.royalcommands.runners.MailRunner;
import org.royaldev.royalcommands.runners.UserdataRunner;
import org.royaldev.royalcommands.runners.WarnWatcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RoyalCommands extends JavaPlugin {

    //--- Globals ---//

    public static ConfigurationSection commands = null;
    public static File dataFolder;
    public static ItemNameManager inm;
    public static WorldManager wm = null;

    public static RoyalCommands instance;

    public ConfManager whl;
    public Logger log = Logger.getLogger("Minecraft");
    public String version = null;
    public String newVersion = null;
    public Metrics m = null;

    public NMSFace nmsFace;
    public Config c;
    public Help h;

    //--- Privates ---//

    private final int minVersion = 2645;

    private CommandMap cm = null;
    private YamlConfiguration pluginYml = null;

    private final RoyalCommandsPlayerListener playerListener = new RoyalCommandsPlayerListener(this);
    private final RoyalCommandsBlockListener blockListener = new RoyalCommandsBlockListener(this);
    private final RoyalCommandsEntityListener entityListener = new RoyalCommandsEntityListener(this);
    private final SignListener signListener = new SignListener(this);
    private final MonitorListener monitorListener = new MonitorListener(this);
    private final ServerListener serverListener = new ServerListener(this);

    private final Pattern versionPattern = Pattern.compile("(\\d+\\.\\d+\\.\\d+)(\\-SNAPSHOT)?(\\-local\\-(\\d{8}\\.\\d{6})|\\-(\\d+))?");

    private RApiMain api;

    //--- Handlders ---//

    public final AuthorizationHandler ah = new AuthorizationHandler(this);
    public final VaultHandler vh = new VaultHandler(this);

    //--- Dependencies ---//

    private VanishPlugin vp = null;
    private WorldGuardPlugin wg = null;
    private LWCPlugin lwc = null;

    public static MultiverseCore mvc = null;

    //--- Public methods ---//

    @SuppressWarnings("unused")
    public RApiMain getAPI() {
        return api;
    }

    @SuppressWarnings("unused")
    public boolean canBuild(Player p, Location l) {
        return wg == null || wg.canBuild(p, l);
    }

    public boolean canBuild(Player p, Block b) {
        return wg == null || wg.canBuild(p, b);
    }

    public boolean canAccessChest(Player p, Block b) {
        return lwc == null || lwc.getLWC().canAccessProtection(p, b);
    }

    public boolean isVanished(Player p) {
        if (!Config.useVNP) return false;
        if (vp == null) {
            vp = (VanishPlugin) Bukkit.getServer().getPluginManager().getPlugin("VanishNoPacket");
            return false;
        } else return vp.getManager().isVanished(p);
    }

    public boolean isVanished(Player p, CommandSender cs) {
        if (!Config.useVNP) return false;
        if (vp == null) {
            vp = (VanishPlugin) Bukkit.getServer().getPluginManager().getPlugin("VanishNoPacket");
            return false;
        }
        return !ah.isAuthorized(cs, "rcmds.seehidden") && vp.getManager().isVanished(p);
    }

    public int getNumberVanished() {
        int hid = 0;
        for (Player p : getServer().getOnlinePlayers()) if (isVanished(p)) hid++;
        return hid;
    }

    //-- Static methods --//

    /**
     * Joins an array of strings with spaces
     *
     * @param array    Array to join
     * @param position Position to start joining from
     * @return Joined string
     */
    public static String getFinalArg(String[] array, int position) {
        final StringBuilder sb = new StringBuilder();
        for (int i = position; i < array.length; i++) sb.append(array[i]).append(" ");
        return sb.substring(0, sb.length() - 1);
    }

    //--- Private methods ---//

    private CommandMap getCommandMap() {
        if (cm != null) return cm;
        Field map;
        try {
            map = getServer().getPluginManager().getClass().getDeclaredField("commandMap");
            map.setAccessible(true);
            cm = (CommandMap) map.get(getServer().getPluginManager());
            return cm;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private ConfigurationSection getCommands() {
        return pluginYml.getConfigurationSection("reflectcommands");
    }

    private ConfigurationSection getCommandInfo(String command) {
        final ConfigurationSection ci = getCommands().getConfigurationSection(command);
        if (ci == null) throw new IllegalArgumentException("No such command registered!");
        return ci;
    }

    private List<String> getAliases(String command) {
        final List<String> aliasesList = getCommandInfo(command).getStringList("aliases");
        if (aliasesList == null) return new ArrayList<String>();
        return aliasesList;
    }

    private String getUsage(String command) {
        return getCommandInfo(command).getString("usage", "/<command>");
    }

    private String getDescription(String command) {
        return getCommandInfo(command).getString("description", command);
    }

    /*private boolean unregisterCommand(String command) {
        final Command c = getCommandMap().getCommand(command);
        return c != null && c.unregister(getCommandMap());
    } save for overriding commands in the config*/

    /**
     * Registers a command in the server's CommandMap.
     *
     * @param ce      CommandExecutor to be registered
     * @param command Command name as specified in plugin.yml
     */
    private void registerCommand(CommandExecutor ce, String command) {
        if (Config.disabledCommands.contains(command.toLowerCase())) return;
        try {
            final Constructor c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            c.setAccessible(true);
            final PluginCommand pc = (PluginCommand) c.newInstance(command, this);
            pc.setExecutor(ce);
            pc.setAliases(getAliases(command));
            pc.setDescription(getDescription(command));
            pc.setUsage(getUsage(command));
            getCommandMap().register(getDescription().getName(), pc);
        } catch (Exception e) {
            getLogger().warning("Could not register command \"" + command + "\" - an error occurred: " + e.getMessage() + ".");
        }
    }

    private boolean versionCheck() {
        // If someone happens to be looking through this and knows a better way, let me know.
        if (!Config.checkVersion) return true;
        Pattern p = Pattern.compile(".+b(\\d+)jnks.+");
        Matcher m = p.matcher(getServer().getVersion());
        if (!m.matches() || m.groupCount() < 1) {
            log.warning("[RoyalCommands] Could not get CraftBukkit version! No version checking will take place.");
            return true;
        }
        Integer currentVersion = RUtils.getInt(m.group(1));
        return currentVersion == null || currentVersion >= minVersion;
    }

    private Map<String, String> getNewestVersions() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new URL("https://cdn.royaldev.org/rcmdsversion.php").openStream()));
        StringBuilder data = new StringBuilder();
        String input;
        while ((input = br.readLine()) != null) data.append(input);
        return new Gson().fromJson(data.toString(), new TypeToken<Map<String, String>>() {}.getType());
    }

    //--- Load initial configuration ---//

    public void loadConfiguration() {
        if (!new File(getDataFolder(), "config.yml").exists()) saveDefaultConfig();
        if (!new File(getDataFolder(), "items.csv").exists()) saveResource("items.csv", false);
        if (!new File(getDataFolder(), "rules.txt").exists()) saveResource("rules.txt", false);
        if (!new File(getDataFolder(), "help.txt").exists()) saveResource("help.txt", false);
        if (!new File(getDataFolder(), "warps.yml").exists()) saveResource("warps.yml", false);
        final File file = new File(getDataFolder(), "userdata");
        if (!file.exists()) {
            try {
                boolean success = file.mkdir();
                if (success) log.info("[RoyalCommands] Created userdata directory.");
            } catch (Exception e) {
                log.severe("[RoyalCommands] Failed to make userdata directory!");
                log.severe(e.getMessage());
            }
        }
    }

    //--- onEnable() ---//

    @Override
    public void onEnable() {

        //-- Set globals --//

        instance = this;
        pluginYml = YamlConfiguration.loadConfiguration(getResource("plugin.yml"));
        dataFolder = getDataFolder();
        whl = ConfManager.getConfManager("whitelist.yml");
        commands = pluginYml.getConfigurationSection("reflectcommands");
        version = getDescription().getVersion();

        //-- Initialize ConfManagers if not made --//

        String[] cms = new String[]{"whitelist.yml", "warps.yml", "publicassignments.yml"};
        for (String name : cms) {
            ConfManager cm = ConfManager.getConfManager(name);
            if (!cm.exists()) cm.createFile();
            cm.forceSave();
        }

        //-- Work out NMS magic using mbaxter's glorious methods --//

        // Get full package string of CraftServer.
        // org.bukkit.craftbukkit.versionstring (or for pre-refactor, just org.bukkit.craftbukkit
        String packageName = getServer().getClass().getPackage().getName();
        // Get the last element of the package
        // If the last element of the package was "craftbukkit" we are now pre-refactor
        String versionNMS = packageName.substring(packageName.lastIndexOf('.') + 1);
        if (versionNMS.equals("craftbukkit")) versionNMS = "PreSafeGuard";
        try {
            // Check if we have a NMSHandler class at that location.
            final Class<?> clazz = Class.forName("org.royaldev.royalcommands.nms." + versionNMS + ".NMSHandler");
            // Make sure it actually implements NMS and set our handler
            if (NMSFace.class.isAssignableFrom(clazz)) nmsFace = (NMSFace) clazz.getConstructor().newInstance();
        } catch (final Exception e) {
            getLogger().warning("Could not find support for this CraftBukkit version.");
            getLogger().info("The BukkitDev page has links to the newest development builds to fix this.");
            getLogger().info("For now, NMS/CB internal support will be disabled.");
            nmsFace = new org.royaldev.royalcommands.nms.NoSupport.NMSHandler();
        }
        if (nmsFace.hasSupport()) getLogger().info("Loaded support for " + nmsFace.getVersion() + ".");

        //-- Hidendra's Metrics --//

        try {
            Matcher matcher = versionPattern.matcher(version);
            matcher.matches();
            // 1 = base version
            // 2 = -SNAPSHOT
            // 5 = build #
            String versionMinusBuild = (matcher.group(1) == null) ? "Unknown" : matcher.group(1);
            String build = (matcher.group(5) == null) ? "local build" : matcher.group(5);
            if (matcher.group(2) == null) build = "release";
            m = new Metrics(this);
            Metrics.Graph g = m.createGraph("Version"); // get our custom version graph
            g.addPlotter(
                    new Metrics.Plotter(versionMinusBuild + "~=~" + build) {
                        @Override
                        public int getValue() {
                            return 1; // this value doesn't matter
                        }
                    }
            ); // add the donut graph with major version inside and build outside
            m.addGraph(g); // add the graph
            if (!m.start())
                getLogger().info("You have Metrics off! I like to keep accurate usage statistics, but okay. :(");
            else getLogger().info("Metrics enabled. Thank you!");
        } catch (Exception ignore) {
            getLogger().warning("Could not start Metrics!");
        }

        //-- Get help --//

        h = new Help(this);

        //-- Get configs --//

        loadConfiguration();
        c = new Config(this);
        c.reloadConfiguration();

        //-- Check CB version --//

        if (!versionCheck()) {
            log.severe("[RoyalCommands] This version of CraftBukkit is too old to run RoyalCommands!");
            log.severe("[RoyalCommands] This version of RoyalCommands needs at least CraftBukkit " + minVersion + ".");
            log.severe("[RoyalCommands] Disabling plugin. You can turn this check off in the config.");
            setEnabled(false);
            return;
        }

        //-- Set up Vault --//

        vh.setupVault();

        //-- Schedule tasks --//

        BukkitScheduler bs = getServer().getScheduler();
        bs.runTaskTimerAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                if (!Config.updateCheck) return;
                try {
                    Matcher m = versionPattern.matcher(version);
                    m.matches();
                    StringBuilder useVersion = new StringBuilder();
                    if (m.group(1) != null) useVersion.append(m.group(1)); // add base version #
                    if (m.group(2) != null) useVersion.append(m.group(2)); // add SNAPSHOT status
                    /*
                    This does not need to compare build numbers. Everyone would be out of date all the time if it did.
                    This method will compare root versions.
                     */
                    Map<String, String> jo = getNewestVersions();
                    String stable = jo.get("stable");
                    String dev = jo.get("dev");
                    String currentVersion = useVersion.toString().toLowerCase();
                    if (!dev.equalsIgnoreCase(currentVersion) && currentVersion.contains("-SNAPSHOT")) {
                        getLogger().warning("A newer version of RoyalCommands is available!");
                        getLogger().warning("Currently installed: v" + currentVersion + ", newest: v" + dev);
                        getLogger().warning("Development builds are available at http://ci.royaldev.org/");
                    } else if (!stable.equalsIgnoreCase(currentVersion) && !currentVersion.equalsIgnoreCase(dev)) {
                        getLogger().warning("A newer version of RoyalCommands is available!");
                        getLogger().warning("Currently installed: v" + currentVersion + ", newest: v" + stable);
                        getLogger().warning("Stable builds are available at http://dev.bukkit.org/server-mods/royalcommands");
                    } else if (!stable.equalsIgnoreCase(currentVersion) && currentVersion.replace("-SNAPSHOT", "").equalsIgnoreCase(stable)) {
                        getLogger().warning("A newer version of RoyalCommands is available!");
                        getLogger().warning("Currently installed: v" + currentVersion + ", newest: v" + stable);
                        getLogger().warning("Stable builds are available at http://dev.bukkit.org/server-mods/royalcommands");
                    }
                } catch (Exception ignored) {
                    // ignore exceptions
                }
            }
        }, 0L, 36000L);
        bs.scheduleSyncDelayedTask(this, new Runnable() { // load after server starts up
            @Override
            public void run() {
                h.reloadHelp();
                getLogger().info("Help loaded for all plugins.");
            }
        });
        bs.runTaskTimerAsynchronously(this, new AFKWatcher(this), 0L, 200L);
        bs.runTaskTimerAsynchronously(this, new BanWatcher(this), 20L, 600L);
        bs.runTaskTimerAsynchronously(this, new WarnWatcher(this), 20L, 12000L);
        bs.scheduleSyncRepeatingTask(this, new FreezeWatcher(this), 20L, 100L);
        long mail = RUtils.timeFormatToSeconds(Config.mailCheckTime);
        if (mail > 0L) bs.scheduleSyncRepeatingTask(this, new MailRunner(this), 20L, mail * 20L);

        long every = RUtils.timeFormatToSeconds(Config.saveInterval);
        if (every < 1L) every = 600L; // 600s = 10m
        bs.runTaskTimerAsynchronously(this, new UserdataRunner(this), 20L, every * 20L); // tick = 1/20s

        //-- Get dependencies --//

        vp = (VanishPlugin) getServer().getPluginManager().getPlugin("VanishNoPacket");
        wg = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
        lwc = (LWCPlugin) getServer().getPluginManager().getPlugin("LWC");
        mvc = (MultiverseCore) getServer().getPluginManager().getPlugin("Multiverse-Core");
        TagAPI ta = (TagAPI) getServer().getPluginManager().getPlugin("TagAPI");

        //-- Register events --//

        final PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(playerListener, this);
        pm.registerEvents(entityListener, this);
        pm.registerEvents(blockListener, this);
        pm.registerEvents(signListener, this);
        pm.registerEvents(monitorListener, this);
        pm.registerEvents(serverListener, this);
        if (ta != null && Config.changeNameTag) pm.registerEvents(new TagAPIListener(this), this);

        //-- Register commands --//

        for (String command : getCommands().getValues(false).keySet()) {
            final ConfigurationSection ci = getCommandInfo(command);
            if (ci == null) continue;
            final String className = ci.getString("class");
            if (className == null) continue;
            try {
                final Class<?> clazz = Class.forName("org.royaldev.royalcommands.rcommands." + className);
                final Constructor c = clazz.getConstructor(RoyalCommands.class);
                final Object o = c.newInstance(this);
                if (!(o instanceof CommandExecutor)) continue;
                registerCommand((CommandExecutor) o, command);
            } catch (Exception e) {
                getLogger().warning("Could not register command \"" + command + "\" - an error occurred: " + e.getMessage() + ".");
            }
        }

        //-- Make the API --//

        api = new RApiMain();

        //-- Convert old backpacks --//

        pm.registerEvents(new BackpackConverter(), this);

        //-- We're done! --//

        log.info("[RoyalCommands] RoyalCommands v" + version + " initiated.");
    }

    private class BackpackConverter implements Listener {
        @EventHandler
        public void onJoin(PlayerJoinEvent e) {
            Player p = e.getPlayer();
            final PConfManager pcm = PConfManager.getPConfManager(p);
            if (!pcm.isSet("backpack.item")) return;
            if (!pcm.exists()) pcm.createFile();
            int invSize = pcm.getInt("backpack.size", -1);
            if (invSize < 9) invSize = 36;
            if (invSize % 9 != 0) invSize = 36;
            final Inventory i = Bukkit.createInventory(p, invSize, "Backpack");
            for (int slot = 0; slot < invSize; slot++) {
                ItemStack is = pcm.getItemStack("backpack.item." + slot);
                if (is == null) continue;
                i.setItem(slot, is);
            }
            RUtils.saveBackpack(p, i);
            pcm.set("backpack.item", null);
            pcm.set("backpack.size", null);
        }
    }

    //--- onDisable() ---//

    @Override
    public void onDisable() {

        //-- Cancel scheduled tasks --//

        getServer().getScheduler().cancelTasks(this);

        //-- Save inventories --//

        WorldManager.il.saveAllInventories();

        //-- Save all userdata --//

        getLogger().info("Saving userdata and configurations (" + (PConfManager.managersCreated() + ConfManager.managersCreated()) + " files in all)...");
        PConfManager.saveAllManagers();
        ConfManager.saveAllManagers();
        getLogger().info("Userdata saved.");

        //-- We're done! --//

        log.info("[RoyalCommands] RoyalCommands v" + version + " disabled.");
    }

}
