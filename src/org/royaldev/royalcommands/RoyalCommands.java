package org.royaldev.royalcommands;
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

import com.griefcraft.lwc.LWCPlugin;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.apache.commons.lang.text.StrBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.kitteh.tag.TagAPI;
import org.kitteh.vanish.VanishPlugin;
import org.royaldev.royalcommands.api.RApiMain;
import org.royaldev.royalcommands.json.JSONArray;
import org.royaldev.royalcommands.json.JSONException;
import org.royaldev.royalcommands.json.JSONObject;
import org.royaldev.royalcommands.listeners.MonitorListener;
import org.royaldev.royalcommands.listeners.RoyalCommandsBlockListener;
import org.royaldev.royalcommands.listeners.RoyalCommandsEntityListener;
import org.royaldev.royalcommands.listeners.RoyalCommandsPlayerListener;
import org.royaldev.royalcommands.listeners.SignListener;
import org.royaldev.royalcommands.listeners.TagAPIListener;
import org.royaldev.royalcommands.opencsv.CSVReader;
import org.royaldev.royalcommands.playermanagers.H2PConfManager;
import org.royaldev.royalcommands.playermanagers.YMLPConfManager;
import org.royaldev.royalcommands.rcommands.*;
import org.royaldev.royalcommands.runners.AFKWatcher;
import org.royaldev.royalcommands.runners.BanWatcher;
import org.royaldev.royalcommands.runners.FreezeWatcher;
import org.royaldev.royalcommands.runners.WarnWatcher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RoyalCommands extends JavaPlugin {

    /*
    * TODO: Add weather global announcement
     */

    //--- Globals ---//

    public static Permission permission = null;
    public static Economy economy = null;
    public static Chat chat = null;
    public static Map<String, Map<String, Object>> commands = null;
    public static File dataFolder;
    public static ItemNameManager inm;
    public static WorldManager wm;

    public static RoyalCommands instance;

    public final Map<String, H2PConfManager> h2s = new HashMap<String, H2PConfManager>();
    public final Map<String, YMLPConfManager> ymls = new HashMap<String, YMLPConfManager>();
    public final Map<String, FileConfiguration> confs = new HashMap<String, FileConfiguration>();

    public ConfManager whl;
    public Logger log = Logger.getLogger("Minecraft");
    public String version = null;
    public String newVersion = null;
    public MetricsLite ml = null;

    public Connection c = null;

    //--- Privates ---//

    private int minVersion = 2287;

    private final RoyalCommandsPlayerListener playerListener = new RoyalCommandsPlayerListener(this);
    private final RoyalCommandsBlockListener blockListener = new RoyalCommandsBlockListener(this);
    private final RoyalCommandsEntityListener entityListener = new RoyalCommandsEntityListener(this);
    private final SignListener signListener = new SignListener(this);
    private final MonitorListener monitorListener = new MonitorListener(this);

    private RApiMain api;

    //--- Dependencies ---//

    private VanishPlugin vp = null;
    private WorldGuardPlugin wg = null;
    private LWCPlugin lwc = null;

    public static MultiverseCore mvc = null;

    //--- Configuration options ---//

    //-- String lists --//

    public List<String> muteCmds = new ArrayList<String>();
    public List<String> blockedItems = new ArrayList<String>();
    public List<String> motd = new ArrayList<String>();
    public List<String> commandCooldowns = new ArrayList<String>();
    public List<String> whitelist = new ArrayList<String>();
    public List<String> logBlacklist = new ArrayList<String>();
    public static List<String> disabledCommands = new ArrayList<String>();

    //-- ConfigurationSections --//

    public ConfigurationSection homeLimits = null;
    public ConfigurationSection warnActions = null;

    //-- Booleans --//

    public Boolean showcommands = null;
    public Boolean disablegetip = null;
    public Boolean useWelcome = null;
    public Boolean buildPerm = null;
    public Boolean backDeath = null;
    public Boolean motdLogin = null;
    public Boolean dropExtras = null;
    public Boolean kitPerms = null;
    public Boolean explodeFire = null;
    public Boolean sendToSpawn = null;
    public Boolean stsBack = null;
    public Boolean stsNew = null;
    public Boolean customHelp = null;
    public Boolean useVNP = null;
    public Boolean cooldownAliases = null;
    public Boolean useWhitelist = null;
    public Boolean smoothTime = null;
    public Boolean requireHelm = null;
    public Boolean checkVersion = null;
    public Boolean simpleList = null;
    public Boolean backpackReset = null;
    public Boolean changeNameTag = null;
    public Boolean dumpCreateChest = null;
    public Boolean dumpUseInv = null;
    public Boolean useH2 = null;
    public Boolean h2Convert = null;
    public Boolean ymlConvert = null;
    public Boolean wmShowEmptyWorlds = null;
    public Boolean timeBroadcast = null;
    public static Boolean multiverseNames = null;
    public static Boolean otherHelp = null;
    public static Boolean safeTeleport = null;

    //-- Strings --//

    public String banMessage = null;
    public String kickMessage = null;
    public String defaultWarn = null;
    public String welcomeMessage = null;
    public String noBuildMessage = null;
    public String bcastFormat = null;
    public String whoFormat = null;
    public String nickPrefix = null;
    public String whoGroupFormat = null;
    public String whitelistFormat = null;
    public String h2Path = null;
    public String h2User = null;
    public String h2Pass = null;

    //-- Integers --//

    public Integer spawnmobLimit = null;
    public Integer helpAmount = null;
    public Integer teleportWarmup = null;
    public static Integer defaultStack = null;

    //-- Doubles --//

    public Double maxNear = null;
    public Double defaultNear = null;
    public Double gTeleCd = null;

    //-- Longs --//

    public Long afkKickTime = null;
    public Long afkAutoTime = null;
    public Long warnExpireTime = null;

    //-- Floats --//

    public Float explodePower = null;
    public Float maxExplodePower = null;

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
        if (!useVNP) return false;
        if (vp == null) {
            vp = (VanishPlugin) Bukkit.getServer().getPluginManager().getPlugin("VanishNoPacket");
            return false;
        } else return vp.getManager().isVanished(p.getName());
    }

    public boolean isVanished(Player p, CommandSender cs) {
        if (!useVNP) return false;
        if (vp == null) {
            vp = (VanishPlugin) Bukkit.getServer().getPluginManager().getPlugin("VanishNoPacket");
            return false;
        }
        return !RoyalCommands.hasPerm(cs, "rcmds.seehidden") && vp.getManager().isVanished(p);
    }

    public int getNumberVanished() {
        int hid = 0;
        for (Player p : getServer().getOnlinePlayers()) if (isVanished(p)) hid++;
        return hid;
    }

    public boolean isAuthorized(final OfflinePlayer p, final String node) {
        String world = getServer().getWorlds().get(0).getName();
        return !(p instanceof Player) || (RoyalCommands.permission.has(world, p.getName(), "rcmds.admin") || RoyalCommands.permission.has(world, p.getName(), node));
    }

    public boolean isAuthorized(final Player player, final String node) {
        return !(player != null) || (RoyalCommands.permission.playerHas(player.getWorld(), player.getName(), "rcmds.admin") || RoyalCommands.permission.playerHas(player.getWorld(), player.getName(), node));
    }

    public boolean isAuthorized(final CommandSender player, final String node) {
        return !(player instanceof Player) || (RoyalCommands.permission.has((Player) player, "rcmds.admin") || RoyalCommands.permission.has(player, node));
    }

    //-- Static methods --//

    public static boolean hasPerm(final CommandSender player, final String node) {
        return !(player instanceof Player) || (RoyalCommands.permission.has(player, "rcmds.admin") || RoyalCommands.permission.has(player, node));
    }

    /**
     * Joins an array of strings with spaces
     *
     * @param array    Array to join
     * @param position Position to start joining from
     * @return Joined string
     */
    public static String getFinalArg(String[] array, int position) {
        StrBuilder sb = new StrBuilder();
        for (int i = position; i < array.length; i++) {
            sb.append(array[i]);
            sb.append(" ");
        }
        return sb.substring(0, sb.length() - 1);
    }

    //--- Private methods ---//

    private Boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) permission = permissionProvider.getProvider();
        return (permission != null);
    }

    private Boolean setupChat() {
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) chat = chatProvider.getProvider();
        return (chat != null);
    }

    private Boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) economy = economyProvider.getProvider();
        return (economy != null);
    }

    private void registerCommand(CommandExecutor ce, String command, JavaPlugin jp) {
        if (RoyalCommands.disabledCommands.contains(command)) return;
        jp.getCommand(command).setExecutor(ce);
    }

    private void createDefault(File f, String def) {
        if (!f.exists()) {
            try {
                boolean success = f.createNewFile();
                if (success) {
                    try {
                        FileWriter fstream = new FileWriter(f.getAbsolutePath());
                        BufferedWriter out = new BufferedWriter(fstream);
                        out.write(def);
                        out.close();
                    } catch (Exception e) {
                        log.severe("[RoyalCommands] Could not write to a config.");
                        e.printStackTrace();
                    }
                    log.info("[RoyalCommands] Created config file.");
                }
            } catch (Exception e) {
                log.severe("[RoyalCommands] Failed to create file!");
                e.printStackTrace();
            }
        }
    }

    private boolean versionCheck() {
        // If someone happens to be looking through this and knows a better way, let me know.
        if (!checkVersion) return true;
        Pattern p = Pattern.compile(".+b(\\d+)jnks.+");
        Matcher m = p.matcher(getServer().getVersion());
        if (!m.matches() || m.groupCount() < 1) {
            log.warning("[RoyalCommands] Could not get CraftBukkit version! No version checking will take place.");
            return true;
        }
        Integer currentVersion = RUtils.getInt(m.group(1));
        return currentVersion == null || currentVersion >= minVersion;
    }

    private JSONObject getNewestVersions() throws IOException, JSONException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new URL("http://cdn.royaldev.org/rcmdsversion.php").openStream()));
        StringBuilder data = new StringBuilder();
        String input;
        while ((input = br.readLine()) != null) data.append(input);
        return new JSONObject(data.toString());
    }

    //--- Reload configuration values ---//

    public void reloadConfigVals() {
        showcommands = getConfig().getBoolean("view_commands", true);
        disablegetip = getConfig().getBoolean("disable_getip", false);
        useWelcome = getConfig().getBoolean("enable_welcome_message", true);
        buildPerm = getConfig().getBoolean("use_build_perm", false);
        backDeath = getConfig().getBoolean("back_on_death", true);
        motdLogin = getConfig().getBoolean("motd_on_login", true);
        dropExtras = getConfig().getBoolean("drop_extras", false);
        kitPerms = getConfig().getBoolean("use_exclusive_kit_perms", false);
        explodeFire = getConfig().getBoolean("explode_fire", false);
        sendToSpawn = getConfig().getBoolean("send_to_spawn", false);
        stsBack = getConfig().getBoolean("sts_back", false);
        stsNew = getConfig().getBoolean("send_to_spawn_new", true);
        otherHelp = getConfig().getBoolean("other_plugins_in_help", true);
        customHelp = getConfig().getBoolean("use_custom_help", false);
        useVNP = getConfig().getBoolean("use_vanish", true);
        cooldownAliases = getConfig().getBoolean("cooldowns_match_aliases", true);
        useWhitelist = getConfig().getBoolean("use_whitelist", false);
        smoothTime = getConfig().getBoolean("use_smooth_time", true);
        requireHelm = getConfig().getBoolean("helm_require_item", false);
        safeTeleport = getConfig().getBoolean("safe_teleport", true);
        checkVersion = getConfig().getBoolean("version_check", true);
        simpleList = getConfig().getBoolean("simple_list", true);
        multiverseNames = getConfig().getBoolean("multiverse_world_names", true);
        backpackReset = getConfig().getBoolean("reset_backpack_death", false);
        changeNameTag = getConfig().getBoolean("change_nametag", false);
        dumpCreateChest = getConfig().getBoolean("dump_create_chest", true);
        dumpUseInv = getConfig().getBoolean("dump_use_inv", true);
        useH2 = getConfig().getBoolean("use_h2", false);
        h2Convert = getConfig().getBoolean("h2.convert", false);
        ymlConvert = getConfig().getBoolean("yml_convert", false);
        wmShowEmptyWorlds = getConfig().getBoolean("worldmanager.who.show_empty_worlds", false);
        timeBroadcast = getConfig().getBoolean("broadcast_time_changes", false);

        banMessage = RUtils.colorize(getConfig().getString("default_ban_message", "&4Banhammered!"));
        noBuildMessage = RUtils.colorize(getConfig().getString("no_build_message", "&cYou don't have permission to build!"));
        kickMessage = RUtils.colorize(getConfig().getString("default_kick_message", "Kicked from server."));
        defaultWarn = RUtils.colorize(getConfig().getString("default_warn_message", "You have been warned."));
        welcomeMessage = RUtils.colorize(getConfig().getString("welcome_message", "&5Welcome {name} to the server!"));
        bcastFormat = RUtils.colorize(getConfig().getString("bcast_format", "&b[&aBroadcast&b]&a "));
        whoFormat = getConfig().getString("who_format", "{prefix}{dispname}");
        nickPrefix = RUtils.colorize(getConfig().getString("nick_prefix", "*"));
        whoGroupFormat = getConfig().getString("who_group_format", "{prefix}{group}{suffix}");
        whitelistFormat = RUtils.colorize(getConfig().getString("whitelist_format", "You are not whitelisted on this server!"));
        h2Path = getConfig().getString("h2.path", "userdata");
        h2User = getConfig().getString("h2.user", "rcmds");
        h2Pass = getConfig().getString("h2.pass", "sdmcr");

        defaultStack = getConfig().getInt("default_stack_size", 64);
        spawnmobLimit = getConfig().getInt("spawnmob_limit", 15);
        helpAmount = getConfig().getInt("help_lines", 5);
        teleportWarmup = getConfig().getInt("teleport_warmup", 0);

        maxNear = getConfig().getDouble("max_near_radius", 2000D);
        defaultNear = getConfig().getDouble("default_near_radius", 50D);
        gTeleCd = getConfig().getDouble("global_teleport_cooldown", 0D);

        explodePower = (float) getConfig().getDouble("explode_power", 4F);
        maxExplodePower = (float) getConfig().getDouble("max_explode_power", 10F);

        afkKickTime = getConfig().getLong("afk_kick_time", 120L);
        afkAutoTime = getConfig().getLong("auto_afk_time", 300L);
        warnExpireTime = getConfig().getLong("warns_expire_after", 604800L);

        muteCmds = getConfig().getStringList("mute_blocked_commands");
        blockedItems = getConfig().getStringList("blocked_spawn_items");
        motd = getConfig().getStringList("motd");
        commandCooldowns = getConfig().getStringList("command_cooldowns");
        disabledCommands = getConfig().getStringList("disabled_commands");
        logBlacklist = getConfig().getStringList("command_log_blacklist");

        homeLimits = getConfig().getConfigurationSection("home_limits");
        warnActions = getConfig().getConfigurationSection("actions_on_warn");

        if (whl.exists()) whitelist = whl.getStringList("whitelist");

        Help.reloadHelp();

        wm.reloadConfig();

        try {
            Reader in = new FileReader(new File(getDataFolder() + File.separator + "items.csv"));
            inm = new ItemNameManager(new CSVReader(in).readAll());
        } catch (FileNotFoundException e) {
            log.warning("items.csv was not found! Item aliases will not be used.");
            inm = null;
        } catch (IOException e) {
            log.warning("Internal input/output error loading items.csv. Item aliases will not be used.");
            inm = null;
        }

    }

    //--- Load initial configuration ---//

    public void loadConfiguration() {
        if (!new File(getDataFolder() + File.separator + "config.yml").exists())
            saveDefaultConfig();
        if (!new File(getDataFolder() + File.separator + "items.csv").exists())
            saveResource("items.csv", false);
        File file = new File(getDataFolder() + File.separator + "userdata" + File.separator);
        boolean exists = file.exists();
        if (!exists) {
            try {
                boolean success = new File(getDataFolder() + File.separator + "userdata").mkdir();
                if (success) log.info("[RoyalCommands] Created userdata directory.");
            } catch (Exception e) {
                log.severe("[RoyalCommands] Failed to make userdata directory!");
                log.severe(e.getMessage());
            }
        }
        File rules = new File(getDataFolder() + File.separator + "rules.txt");
        if (!rules.exists()) {
            try {
                boolean success = new File(getDataFolder() + File.separator + "rules.txt").createNewFile();
                if (!success) log.severe("[RoyalCommands] Could not create rules.txt!");
                else {
                    try {
                        BufferedWriter out = new BufferedWriter(new FileWriter(getDataFolder() + File.separator + "rules.txt"));
                        out.write("###\n");
                        out.write("&2Page 1:\n");
                        out.write("  1. Be kind\n");
                        out.write("  2. Be courteous\n");
                        out.write("  3. Be respectful\n");
                        out.write("###\n");
                        out.write("&2Page 2:\n");
                        out.write("  4. Be cool\n");
                        out.close();
                    } catch (IOException e) {
                        //ignore
                    }
                }
            } catch (Exception e) {
                log.severe("[RoyalCommands] Could not create rules.txt!");
                e.printStackTrace();
            }
        }
        File help = new File(getDataFolder() + File.separator + "help.txt");
        if (!help.exists()) {
            try {
                boolean success = new File(getDataFolder() + File.separator + "help.txt").createNewFile();
                if (!success) log.severe("[RoyalCommands] Could not create help.txt!");
                else {
                    try {
                        BufferedWriter out = new BufferedWriter(new FileWriter(getDataFolder() + File.separator + "help.txt"));
                        out.write("###\n");
                        out.write("&2Page 1:\n");
                        out.write("  1. Do some awesome things\n");
                        out.write("  2. You must meow to join\n");
                        out.write("  3. The admins didn't change this\n");
                        out.write("###\n");
                        out.write("&2Page 2:\n");
                        out.write("  4. Tell them to\n");
                        out.close();
                    } catch (IOException e) {
                        //ignore
                    }
                }
            } catch (Exception e) {
                log.severe("[RoyalCommands] Could not create help.txt!");
                e.printStackTrace();
            }
        }
        createDefault(new File(getDataFolder() + File.separator + "warps.yml"), "warps:");
    }

    //--- onEnable() ---//

    @Override
    public void onEnable() {

        //-- Set globals --//

        instance = this;

        dataFolder = getDataFolder();

        whl = new ConfManager("whitelist.yml");

        commands = getDescription().getCommands();

        version = getDescription().getVersion();

        wm = new WorldManager();

        //-- Hidendra's Metrics --//

        try {
            ml = new MetricsLite(this);
            if (!ml.start())
                getLogger().info("You have Metrics off! I like to keep accurate usage statistics, but okay. :(");
        } catch (Exception ignore) {
            getLogger().warning("Could not start Metrics!");
        }

        //-- Get configs --//

        loadConfiguration();
        reloadConfigVals();

        //-- Download external libraries --//

        if (!new File("lib", "h2.jar").exists() && useH2) {
            getLogger().info("Downloading H2 driver...");
            if (RUtils.downloadFile("http://cdn.royaldev.org/plugindeps/h2.jar", "lib" + File.separator + "h2.jar")) {
                getLogger().info("Finished downloading.");
                getLogger().warning("Please restart CraftBukkit to load the H2 driver! Disabling plugin.");
                setEnabled(false);
                return;
            } else getLogger().severe("Could not download h2.jar!");
        }

        //-- Check CB version --//

        if (!versionCheck()) {
            log.severe("[RoyalCommands] This version of CraftBukkit is too old to run RoyalCommands!");
            log.severe("[RoyalCommands] This version of RoyalCommands needs at least CraftBukkit " + minVersion + ".");
            log.severe("[RoyalCommands] Disabling plugin. You can turn this check off in the config.");
            setEnabled(false);
            return;
        }

        //-- Set up Vault --//

        setupEconomy();
        setupChat();
        setupPermissions();

        //-- Schedule tasks --//

        getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jo = getNewestVersions();
                    String stable = jo.getString("stable");
                    String dev = jo.getString("dev");
                    String currentVersion = getDescription().getVersion().toLowerCase();
                    if (!dev.equalsIgnoreCase(currentVersion) && currentVersion.contains("pre")) {
                        getLogger().warning("A newer version of RoyalCommands is available!");
                        getLogger().warning("Currently installed: v" + currentVersion + ", newest: v" + dev);
                        getLogger().warning("Development builds are available at http://ci.royaldev.org/");
                    } else if (!stable.equalsIgnoreCase(currentVersion) && !currentVersion.equalsIgnoreCase(dev)) {
                        getLogger().warning("A newer version of RoyalCommands is available!");
                        getLogger().warning("Currently installed: v" + currentVersion + ", newest: v" + stable);
                        getLogger().warning("Stable builds are available at http://dev.bukkit.org/server-mods/royalcommands");
                    } else if (!stable.equalsIgnoreCase(currentVersion) && currentVersion.replace("pre", "").equalsIgnoreCase(stable)) {
                        getLogger().warning("A newer version of RoyalCommands is available!");
                        getLogger().warning("Currently installed: v" + currentVersion + ", newest: v" + stable);
                        getLogger().warning("Stable builds are available at http://dev.bukkit.org/server-mods/royalcommands");
                    }
                } catch (Exception ignored) {
                    // ignore exceptions
                }
            }

        }, 0, 36000);

        getServer().getScheduler().scheduleAsyncRepeatingTask(this, new AFKWatcher(this), 0, 200);
        getServer().getScheduler().scheduleAsyncRepeatingTask(this, new BanWatcher(this), 20, 600);
        getServer().getScheduler().scheduleAsyncRepeatingTask(this, new WarnWatcher(this), 20, 12000);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new FreezeWatcher(this), 20, 100);

        //-- Get dependencies --//

        vp = (VanishPlugin) getServer().getPluginManager().getPlugin("VanishNoPacket");
        wg = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
        lwc = (LWCPlugin) getServer().getPluginManager().getPlugin("LWC");
        mvc = (MultiverseCore) getServer().getPluginManager().getPlugin("Multiverse-Core");
        TagAPI ta = (TagAPI) getServer().getPluginManager().getPlugin("TagAPI");

        //-- Register events --//

        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(playerListener, this);
        pm.registerEvents(entityListener, this);
        pm.registerEvents(blockListener, this);
        pm.registerEvents(signListener, this);
        pm.registerEvents(monitorListener, this);
        if (ta != null && changeNameTag)
            pm.registerEvents(new TagAPIListener(this), this);

        //-- Register commands --//

        registerCommand(new CmdLevel(this), "level", this);
        registerCommand(new CmdSetlevel(this), "setlevel", this);
        registerCommand(new CmdSci(this), "sci", this);
        registerCommand(new CmdSpeak(this), "speak", this);
        registerCommand(new CmdFacepalm(this), "facepalm", this);
        registerCommand(new CmdSlap(this), "slap", this);
        registerCommand(new CmdHarm(this), "harm", this);
        registerCommand(new CmdStarve(this), "starve", this);
        registerCommand(new CmdBanned(this), "banned", this);
        registerCommand(new CmdSetarmor(this), "setarmor", this);
        registerCommand(new CmdGetIP(this), "getip", this);
        registerCommand(new CmdCompareIP(this), "compareip", this);
        registerCommand(new CmdRageQuit(this), "ragequit", this);
        registerCommand(new CmdQuit(this), "quit", this);
        registerCommand(new CmdRank(this), "rank", this);
        registerCommand(new CmdFreeze(this), "freeze", this);
        registerCommand(new CmdFakeop(this), "fakeop", this);
        registerCommand(new CmdVtp(this), "vtp", this);
        registerCommand(new CmdVtphere(this), "vtphere", this);
        registerCommand(new CmdMegaStrike(this), "megastrike", this);
        registerCommand(new CmdPext(this), "pext", this);
        registerCommand(new CmdItem(this), "item", this);
        registerCommand(new CmdClearInventory(this), "clearinventory", this);
        registerCommand(new CmdWeather(this), "weather", this);
        registerCommand(new CmdFixChunk(this), "fixchunk", this);
        registerCommand(new CmdGive(this), "give", this);
        registerCommand(new CmdMessage(this), "message", this);
        registerCommand(new CmdReply(this), "reply", this);
        registerCommand(new CmdGamemode(this), "gamemode", this);
        registerCommand(new CmdMute(this), "mute", this);
        registerCommand(new CmdBan(this), "ban", this);
        registerCommand(new CmdKick(this), "kick", this);
        registerCommand(new CmdTime(this), "time", this);
        registerCommand(new CmdHome(this), "home", this);
        registerCommand(new CmdSetHome(this), "sethome", this);
        registerCommand(new CmdDelHome(this), "delhome", this);
        registerCommand(new CmdListHome(this), "listhome", this);
        registerCommand(new CmdStrike(this), "strike", this);
        registerCommand(new CmdJump(this), "jump", this);
        registerCommand(new CmdWarn(this), "warn", this);
        registerCommand(new CmdClearWarns(this), "clearwarns", this);
        registerCommand(new CmdWarp(this), "warp", this);
        registerCommand(new CmdSetWarp(this), "setwarp", this);
        registerCommand(new CmdDelWarp(this), "delwarp", this);
        registerCommand(new CmdRepair(this), "repair", this);
        registerCommand(new CmdUnban(this), "unban", this);
        registerCommand(new CmdHeal(this), "heal", this);
        registerCommand(new CmdFeed(this), "feed", this);
        registerCommand(new CmdGod(this), "god", this);
        registerCommand(new CmdBanreason(this), "banreason", this);
        registerCommand(new CmdSetSpawn(this), "setspawn", this);
        registerCommand(new CmdSpawn(this), "spawn", this);
        registerCommand(new CmdBanIP(this), "banip", this);
        registerCommand(new CmdUnbanIP(this), "unbanip", this);
        registerCommand(new CmdList(this), "list", this);
        registerCommand(new CmdBack(this), "back", this);
        registerCommand(new CmdTeleport(this), "teleport", this);
        registerCommand(new CmdTeleportHere(this), "teleporthere", this);
        registerCommand(new CmdTeleportRequest(this), "teleportrequest", this);
        registerCommand(new CmdTpAccept(this), "tpaccept", this);
        registerCommand(new CmdTpDeny(this), "tpdeny", this);
        registerCommand(new CmdListWarns(this), "listwarns", this);
        registerCommand(new CmdMore(this), "more", this);
        registerCommand(new CmdTeleportRequestHere(this), "teleportrequesthere", this);
        registerCommand(new CmdSpy(this), "spy", this);
        registerCommand(new CmdSpawnMob(this), "spawnmob", this);
        registerCommand(new CmdAfk(this), "afk", this);
        registerCommand(new CmdAssign(this), "assign", this);
        registerCommand(new CmdOneHitKill(this), "onehitkill", this);
        registerCommand(new CmdBurn(this), "burn", this);
        registerCommand(new CmdKickAll(this), "kickall", this);
        registerCommand(new CmdWorld(this), "world", this);
        registerCommand(new CmdJail(this), "jail", this);
        registerCommand(new CmdSetJail(this), "setjail", this);
        registerCommand(new CmdLess(this), "less", this);
        registerCommand(new CmdSpawner(this), "spawner", this);
        registerCommand(new CmdTp2p(this), "tp2p", this);
        registerCommand(new CmdMotd(this), "motd", this);
        registerCommand(new CmdDelJail(this), "deljail", this);
        registerCommand(new CmdForce(this), "force", this);
        registerCommand(new CmdPing(this), "ping", this);
        registerCommand(new CmdInvsee(this), "invsee", this);
        registerCommand(new CmdRealName(this), "realname", this);
        registerCommand(new CmdNick(this), "nick", this);
        registerCommand(new CmdIngot2Block(this), "ingot2block", this);
        registerCommand(new CmdNear(this), "near", this);
        registerCommand(new CmdKill(this), "kill", this);
        registerCommand(new CmdSuicide(this), "suicide", this);
        registerCommand(new CmdKillAll(this), "killall", this);
        registerCommand(new CmdMuteAll(this), "muteall", this);
        registerCommand(new CmdKit(this), "kit", this);
        registerCommand(new CmdRules(this), "rules", this);
        registerCommand(new CmdBroadcast(this), "broadcast", this);
        registerCommand(new CmdHug(this), "hug", this);
        registerCommand(new CmdExplode(this), "explode", this);
        registerCommand(new CmdRide(this), "ride", this);
        registerCommand(new CmdWhoBanned(this), "whobanned", this);
        registerCommand(new CmdTppos(this), "tppos", this);
        registerCommand(new CmdIgnore(this), "ignore", this);
        registerCommand(new CmdHelp(this), "help", this);
        registerCommand(new CmdCoords(this), "coords", this);
        registerCommand(new CmdTpAll(this), "tpall", this);
        registerCommand(new CmdTpaAll(this), "tpaall", this);
        registerCommand(new CmdVip(this), "vip", this);
        registerCommand(new CmdDump(this), "dump", this);
        registerCommand(new CmdSeen(this), "seen", this);
        registerCommand(new CmdTempban(this), "tempban", this);
        registerCommand(new CmdTpToggle(this), "tptoggle", this);
        registerCommand(new CmdKits(this), "kits", this);
        registerCommand(new CmdLag(this), "lag", this);
        registerCommand(new CmdMem(this), "mem", this);
        registerCommand(new CmdEntities(this), "entities", this);
        registerCommand(new CmdInvmod(this), "invmod", this);
        registerCommand(new CmdWorkbench(this), "workbench", this);
        registerCommand(new CmdEnchantingTable(this), "enchantingtable", this);
        registerCommand(new CmdTrade(this), "trade", this);
        registerCommand(new CmdFurnace(this), "furnace", this);
        registerCommand(new CmdEnchant(this), "enchant", this);
        registerCommand(new CmdWhitelist(this), "wl", this);
        registerCommand(new CmdFireball(this), "fireball", this);
        registerCommand(new CmdFly(this), "fly", this);
        registerCommand(new CmdPlayerTime(this), "playertime", this);
        registerCommand(new CmdCompass(this), "compass", this);
        registerCommand(new CmdHelmet(this), "helmet", this);
        registerCommand(new CmdWorldManager(this), "worldmanager", this);
        registerCommand(new CmdBiome(this), "biome", this);
        registerCommand(new CmdGetID(this), "getid", this);
        registerCommand(new CmdBuddha(this), "buddha", this);
        registerCommand(new CmdErase(this), "erase", this);
        registerCommand(new CmdWhois(this), "whois", this);
        registerCommand(new CmdMobIgnore(this), "mobignore", this);
        registerCommand(new CmdMonitor(this), "monitor", this);
        registerCommand(new CmdGarbageCollector(this), "garbagecollector", this);
        registerCommand(new CmdBackpack(this), "backpack", this);
        registerCommand(new CmdUsage(this), "usage", this);
        registerCommand(new CmdPluginManager(this), "pluginmanager", this);
        registerCommand(new CmdFreezeTime(this), "freezetime", this);
        registerCommand(new CmdBanInfo(this), "baninfo", this);
        registerCommand(new CmdBanList(this), "banlist", this);
        registerCommand(new CmdFlySpeed(this), "flyspeed", this);
        registerCommand(new CmdWalkSpeed(this), "walkspeed", this);
        registerCommand(new CmdRcmds(this), "rcmds", this);

        //-- Config converter (YML -> H2) --//

        if (h2Convert) {
            useH2 = false;
            PConfManager.updateH2Status();
            for (OfflinePlayer op : getServer().getOfflinePlayers()) {
                PConfManager pcm = new PConfManager(op);
                if (!pcm.exists()) continue;
                getLogger().info("Converting userdata for " + op.getName() + "...");
                H2PConfManager h2pcm;
                try {
                    h2pcm = new H2PConfManager(op);
                } catch (SQLException e) {
                    getLogger().warning("Could not convert userdata for " + op.getName() + ": " + e.getMessage());
                    continue;
                } catch (JSONException e) {
                    getLogger().warning("Could not convert userdata for " + op.getName() + ": " + e.getMessage());
                    continue;
                }
                Set<String> data = ((YMLPConfManager) pcm.getRealManager()).getConfigurationSection("").getKeys(true);
                for (String key : data) {
                    Object value = pcm.get(key);
                    if (value instanceof MemorySection) {
                        MemorySection ms = (MemorySection) value;
                        Set<String> keys = ms.getKeys(true);
                        for (String s : keys) {
                            try {
                                h2pcm.set(ms.get(s), ms.getCurrentPath() + "." + s);
                            } catch (JSONException ignored) {
                            } catch (SQLException ignored) {
                            }
                        }
                        continue;
                    }
                    try {
                        h2pcm.set(value, key);
                    } catch (JSONException ignored) {
                    } catch (SQLException ignored) {
                    }
                }
            }
            useH2 = true;
            h2s.clear();
            ymls.clear();
            PConfManager.updateH2Status();
            getLogger().info("YML -> H2 userdata conversion complete. Please restart with convert set to false.");
        }

        //-- Config converter (H2 -> YML) --//

        if (ymlConvert) {
            useH2 = true;
            PConfManager.updateH2Status();
            for (OfflinePlayer op : getServer().getOfflinePlayers()) {
                PConfManager pcm = new PConfManager(op);
                getLogger().info("Converting userdata for " + op.getName() + "...");
                YMLPConfManager ymlpcm = new YMLPConfManager(op);
                if (!ymlpcm.exists()) if (!ymlpcm.createFile()) continue;
                JSONArray names = ((H2PConfManager) pcm.getRealManager()).getJSONObject("").names();
                for (int i = 0; i < names.length(); i++) {
                    String key = names.optString(i);
                    if (key == null) continue;
                    Object value = pcm.get(key);
                    if (value instanceof JSONObject) {
                        JSONObject jo = (JSONObject) value;
                        JSONArray ks = jo.names();
                        for (int x = 0; x < ks.length(); x++) {
                            String k = ks.optString(x);
                            if (k == null) continue;
                            String path = key + "." + k;
                            Object v = jo.opt(k);
                            if (v instanceof JSONObject) { // I really don't want to nest this much
                                JSONObject j = (JSONObject) v;
                                JSONArray js = j.names();
                                for (int z = 0; z < js.length(); z++) {
                                    String q = js.optString(z);
                                    if (q == null) continue;
                                    ymlpcm.set(j.opt(q), path + "." + q);
                                }
                                continue;
                            }
                            ymlpcm.set(v, path);
                        }
                        continue;
                    }
                    ymlpcm.set(value, key);
                }
                ymlpcm.forceSave();
            }
            useH2 = false;
            h2s.clear();
            ymls.clear();
            PConfManager.updateH2Status();
            getLogger().info("H2 -> YML userdata conversion complete. Please restart with convert set to false.");
        }

        //-- Make the API --//

        api = new RApiMain();

        //-- We're done! --//

        log.info("[RoyalCommands] RoyalCommands v" + version + " initiated.");
    }

    //--- onDisable() ---//

    @Override
    public void onDisable() {

        //-- Cancel scheduled tasks --//

        getServer().getScheduler().cancelTasks(this);

        //-- Restart the H2 database if being used --//

        try {
            if (useH2 && c != null) c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //-- Remove userdata handlers --//

        h2s.clear();
        ymls.clear();

        //-- We're done! --//

        log.info("[RoyalCommands] RoyalCommands v" + version + " disabled.");
    }

}
