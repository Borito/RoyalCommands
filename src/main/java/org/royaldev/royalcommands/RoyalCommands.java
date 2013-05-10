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
import org.apache.commons.lang.text.StrBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.craftbukkit.libs.com.google.gson.reflect.TypeToken;
import org.bukkit.entity.Player;
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
import org.royaldev.royalcommands.rcommands.*;
import org.royaldev.royalcommands.runners.AFKWatcher;
import org.royaldev.royalcommands.runners.BanWatcher;
import org.royaldev.royalcommands.runners.FreezeWatcher;
import org.royaldev.royalcommands.runners.MailRunner;
import org.royaldev.royalcommands.runners.UserdataRunner;
import org.royaldev.royalcommands.runners.WarnWatcher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RoyalCommands extends JavaPlugin {

    /*
    * TODO: Add weather global announcement
     */

    //--- Globals ---//

    public static Map<String, Map<String, Object>> commands = null;
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

    //--- Privates ---//

    private final int minVersion = 2645;

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
        } else return vp.getManager().isVanished(p.getName());
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
        StrBuilder sb = new StrBuilder();
        for (int i = position; i < array.length; i++) {
            sb.append(array[i]);
            sb.append(" ");
        }
        return sb.substring(0, sb.length() - 1);
    }

    //--- Private methods ---//

    /**
     * Registers a command in the server. If the command isn't defined in plugin.yml
     * the NPE is caught, and a warning line is sent to the console.
     *
     * @param ce      CommandExecutor to be registered
     * @param command Command name as specified in plugin.yml
     * @param jp      Plugin to register under
     */
    private void registerCommand(CommandExecutor ce, String command, JavaPlugin jp) {
        if (Config.disabledCommands.contains(command.toLowerCase())) return;
        try {
            jp.getCommand(command).setExecutor(ce);
        } catch (NullPointerException e) {
            getLogger().warning("Could not register command \"" + command + "\" - not registered in plugin.yml (" + e.getMessage() + ")");
        }
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
        BufferedReader br = new BufferedReader(new InputStreamReader(new URL("http://cdn.royaldev.org/rcmdsversion.php").openStream()));
        StringBuilder data = new StringBuilder();
        String input;
        while ((input = br.readLine()) != null) data.append(input);
        return new Gson().fromJson(data.toString(), new TypeToken<Map<String, String>>() {}.getType());
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

        whl = ConfManager.getConfManager("whitelist.yml");

        commands = getDescription().getCommands();

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
            getLogger().severe("Could not find support for this CraftBukkit version.");
            getLogger().info("The BukkitDev page has links to the newest development builds to fix this.");
            getLogger().info("For now, NMS/CB internal support will be disabled.");
            nmsFace = new org.royaldev.royalcommands.nms.NoSupport.NMSHandler();
        }
        if (nmsFace.hasSupport())
            getLogger().info("Loaded support for " + nmsFace.getVersion() + ".");

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

        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(playerListener, this);
        pm.registerEvents(entityListener, this);
        pm.registerEvents(blockListener, this);
        pm.registerEvents(signListener, this);
        pm.registerEvents(monitorListener, this);
        pm.registerEvents(serverListener, this);
        if (ta != null && Config.changeNameTag) pm.registerEvents(new TagAPIListener(this), this);

        //-- Register commands --//

        registerCommand(new CmdLevel(this), "level", this);
        registerCommand(new CmdSetlevel(this), "setlevel", this);
        registerCommand(new CmdSci(this), "sci", this);
        registerCommand(new CmdSpeak(this), "speak", this);
        registerCommand(new CmdFacepalm(this), "facepalm", this);
        registerCommand(new CmdSlap(this), "slap", this);
        registerCommand(new CmdHarm(this), "harm", this);
        registerCommand(new CmdStarve(this), "starve", this);
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
        registerCommand(new CmdAccountStatus(this), "accountstatus", this);
        registerCommand(new CmdPlayerSearch(this), "playersearch", this);
        registerCommand(new CmdDeafen(this), "deafen", this);
        registerCommand(new CmdRename(this), "rename", this);
        registerCommand(new CmdLore(this), "lore", this);
        registerCommand(new CmdSetUserdata(this), "setuserdata", this);
        registerCommand(new CmdFirework(this), "firework", this);
        registerCommand(new CmdRocket(this), "rocket", this);
        registerCommand(new CmdEffect(this), "effect", this);
        registerCommand(new CmdBanHistory(this), "banhistory", this);
        registerCommand(new CmdMail(this), "mail", this);
        registerCommand(new CmdSignEdit(this), "signedit", this);
        registerCommand(new CmdPotion(this), "potion", this);
        registerCommand(new CmdSetCharacteristic(this), "setcharacteristic", this);
        registerCommand(new CmdNameEntity(this), "nameentity", this);
        registerCommand(new CmdHead(this), "head", this);
        registerCommand(new CmdFindIP(this), "findip", this);
        registerCommand(new CmdMap(this), "map", this);
        registerCommand(new CmdDeleteBanHistory(this), "deletebanhistory", this);
        registerCommand(new CmdPublicAssign(this), "publicassign", this);
        registerCommand(new CmdRcmds(this), "rcmds", this);

        //-- Make the API --//

        api = new RApiMain();

        System.out.println(ah.isAuthorized(getServer().getOfflinePlayer("jkcclemens"), "rcmds.op"));

        //-- We're done! --//

        log.info("[RoyalCommands] RoyalCommands v" + version + " initiated.");
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
