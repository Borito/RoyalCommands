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

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.kitteh.vanish.VanishPlugin;
import org.royaldev.royalcommands.listeners.*;
import org.royaldev.royalcommands.rcommands.*;
import org.royaldev.royalcommands.runners.AFKWatcher;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RoyalCommands extends JavaPlugin {

    public ConfManager whl;

    public static File dataFolder;

    public static Permission permission = null;
    public static Economy economy = null;
    public static Chat chat = null;

    public String version = null;
    public String newVersion = null;

    public List<String> muteCmds = new ArrayList<String>();
    public List<String> blockedItems = new ArrayList<String>();
    public List<String> motd = new ArrayList<String>();
    public List<String> commandCooldowns = new ArrayList<String>();
    public List<String> whitelist = new ArrayList<String>();
    public List<String> logBlacklist = new ArrayList<String>();
    public static List<String> disabledCommands = new ArrayList<String>();

    public ConfigurationSection homeLimits = null;

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
    public static Boolean otherHelp = null;
    public Boolean customHelp = null;
    public Boolean useVNP = null;
    public Boolean cooldownAliases = null;
    public Boolean useWhitelist = null;
    public Boolean smoothTime = null;
    public Boolean requireHelm = null;
    public static Boolean safeTeleport = null;
    public Boolean checkVersion = null;
    public Boolean simpleList = null;
    public Boolean backpackReset = null;

    public String banMessage = null;
    public String kickMessage = null;
    public String defaultWarn = null;
    public String welcomeMessage = null;
    public String noBuildMessage = null;
    public String bcastFormat = null;
    public String whoFormat = null;
    public String nickPrefix = null;
    public String whoGroupFormat = null;

    public static Integer defaultStack = null;
    public Integer warnBan = null;
    public Integer spawnmobLimit = null;
    public Integer helpAmount = null;

    public Double maxNear = null;
    public Double defaultNear = null;
    public Double gTeleCd = null;

    public Long afkKickTime = null;
    public Long afkAutoTime = null;

    public Float explodePower = null;
    public Float maxExplodePower = null;

    private int minVersion = 2244;

    public static Map<String, Map<String, Object>> commands = null;
    public static Plugin[] plugins = null;

    public Metrics m = null;

    // Permissions with Vault
    public Boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) permission = permissionProvider.getProvider();
        return (permission != null);
    }

    public Boolean setupChat() {
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) chat = chatProvider.getProvider();
        return (chat != null);
    }

    private Boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) economy = economyProvider.getProvider();
        return (economy != null);
    }

    private final RoyalCommandsPlayerListener playerListener = new RoyalCommandsPlayerListener(this);
    private final RoyalCommandsBlockListener blockListener = new RoyalCommandsBlockListener(this);
    private final RoyalCommandsEntityListener entityListener = new RoyalCommandsEntityListener(this);
    private final SignListener signListener = new SignListener(this);
    private final MonitorListener monitorListener = new MonitorListener(this);

    public Logger log = Logger.getLogger("Minecraft");

    VanishPlugin vp = null;

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
        backpackReset = getConfig().getBoolean("reset_backpack_death", false);

        banMessage = RUtils.colorize(getConfig().getString("default_ban_message", "&4Banhammered!"));
        noBuildMessage = RUtils.colorize(getConfig().getString("no_build_message", "&cYou don't have permission to build!"));
        kickMessage = RUtils.colorize(getConfig().getString("default_kick_message", "Kicked from server."));
        defaultWarn = RUtils.colorize(getConfig().getString("default_warn_message", "You have been warned."));
        welcomeMessage = RUtils.colorize(getConfig().getString("welcome_message", "&5Welcome {name} to the server!"));
        bcastFormat = RUtils.colorize(getConfig().getString("bcast_format", "&b[&aBroadcast&b]&a "));
        whoFormat = getConfig().getString("who_format", "{prefix}{dispname}");
        nickPrefix = RUtils.colorize(getConfig().getString("nick_prefix", "*"));
        whoGroupFormat = getConfig().getString("who_group_format", "{prefix}{group}{suffix}");

        defaultStack = getConfig().getInt("default_stack_size", 64);
        warnBan = getConfig().getInt("max_warns_before_ban", 3);
        spawnmobLimit = getConfig().getInt("spawnmob_limit", 15);
        helpAmount = getConfig().getInt("help_lines", 5);

        maxNear = getConfig().getDouble("max_near_radius", 2000);
        defaultNear = getConfig().getDouble("default_near_radius", 50);
        gTeleCd = getConfig().getDouble("global_teleport_cooldown", 0);

        explodePower = (float) getConfig().getDouble("explode_power", 4);
        maxExplodePower = (float) getConfig().getDouble("max_explode_power", 10);

        afkKickTime = getConfig().getLong("afk_kick_time", 120);
        afkAutoTime = getConfig().getLong("auto_afk_time", 300);

        muteCmds = getConfig().getStringList("mute_blocked_commands");
        blockedItems = getConfig().getStringList("blocked_spawn_items");
        motd = getConfig().getStringList("motd");
        commandCooldowns = getConfig().getStringList("command_cooldowns");
        disabledCommands = getConfig().getStringList("disabled_commands");
        logBlacklist = getConfig().getStringList("command_log_blacklist");

        homeLimits = getConfig().getConfigurationSection("home_limits");

        if (whl.exists()) whitelist = whl.getStringList("whitelist");

        Help.reloadHelp();
    }

    public void loadConfiguration() {
        if (!new File(getDataFolder() + File.separator + "config.yml").exists()) saveDefaultConfig();
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

    public void createDefault(File f, String def) {
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

    // getFinalArg taken from EssentialsCommand.java - Essentials by
    // EssentialsTeam
    public String getFinalArg(final String[] args, final int start) {
        final StringBuilder bldr = new StringBuilder();
        for (int i = start; i < args.length; i++) {
            if (i != start) bldr.append(" ");
            bldr.append(args[i]);
        }
        return bldr.toString();
    }

    // updateCheck() from MilkBowl's Vault
    public String updateCheck(String currentVersion) throws Exception {
        String pluginUrlString = "http://dev.bukkit.org/server-mods/royalcommands/files.rss";
        try {
            URL url = new URL(pluginUrlString);
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url.openConnection().getInputStream());
            doc.getDocumentElement().normalize();
            NodeList nodes = doc.getElementsByTagName("item");
            Node firstNode = nodes.item(0);
            if (firstNode.getNodeType() == 1) {
                Element firstElement = (Element) firstNode;
                NodeList firstElementTagName = firstElement.getElementsByTagName("title");
                Element firstNameElement = (Element) firstElementTagName.item(0);
                NodeList firstNodes = firstNameElement.getChildNodes();
                return firstNodes.item(0).getNodeValue();
            }
        } catch (Exception ignored) {
        }
        return currentVersion;
    }

    public static boolean hasPerm(final CommandSender player, final String node) {
        return player instanceof RemoteConsoleCommandSender || player instanceof ConsoleCommandSender || (RoyalCommands.permission.has(player, "rcmds.admin") || RoyalCommands.permission.has(player, node));
    }

    public boolean isAuthorized(final Player player, final String node) {
        return player instanceof RemoteConsoleCommandSender || player instanceof ConsoleCommandSender || (RoyalCommands.permission.playerHas(player.getWorld(), player.getName(), "rcmds.admin") || RoyalCommands.permission.playerHas(player.getWorld(), player.getName(), node));
    }

    public boolean isAuthorized(final CommandSender player, final String node) {
        return player instanceof RemoteConsoleCommandSender || player instanceof ConsoleCommandSender || (RoyalCommands.permission.has((Player) player, "rcmds.admin") || RoyalCommands.permission.has(player, node));
    }

    public void registerCommand(CommandExecutor ce, String command, JavaPlugin jp) {
        if (RoyalCommands.disabledCommands.contains(command)) return;
        jp.getCommand(command).setExecutor(ce);
    }

    public boolean versionCheck() {
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

    public void onEnable() {

        dataFolder = getDataFolder();

        whl = new ConfManager("whitelist.yml");

        commands = getDescription().getCommands();
        plugins = getServer().getPluginManager().getPlugins();

        loadConfiguration();
        reloadConfigVals();

        if (!versionCheck()) {
            log.severe("[RoyalCommands] This version of CraftBukkit is too old to run RoyalCommands!");
            log.severe("[RoyalCommands] This version of RoyalCommands needs at least CraftBukkit " + minVersion + ".");
            log.severe("[RoyalCommands] Disabling plugin. You can turn this check off in the config.");
            getPluginLoader().disablePlugin(this);
            return;
        }

        setupEconomy();
        setupChat();
        setupPermissions();

        try {
            m = new Metrics(this);
            m.start();
        } catch (Exception ignore) {
            log.warning("[RoyalCommands] Could not start Metrics!");
        }

        version = getDescription().getVersion();

        // yet again, borrowed from MilkBowl
        getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                try {
                    newVersion = updateCheck(version);
                    String oldVersion = version;
                    Integer nVI = Integer.valueOf(newVersion.replaceAll("\\D+", ""));
                    Integer oVI = Integer.valueOf(version.replaceAll("\\D+", ""));
                    if (nVI > oVI || (oldVersion.contains("pre") && nVI.equals(oVI))) {
                        log.warning(newVersion + " is out! You are running v" + oldVersion);
                        log.warning("Update RoyalCommands at: http://dev.bukkit.org/server-mods/royalcommands");
                    }
                } catch (Exception ignored) {
                    // ignore exceptions
                }
            }

        }, 0, 36000);

        getServer().getScheduler().scheduleAsyncRepeatingTask(this, new AFKWatcher(this), 0, 200);

        vp = (VanishPlugin) Bukkit.getServer().getPluginManager().getPlugin("VanishNoPacket");

        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(playerListener, this);
        pm.registerEvents(entityListener, this);
        pm.registerEvents(blockListener, this);
        pm.registerEvents(signListener, this);
        pm.registerEvents(monitorListener, this);

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
        registerCommand(new CmdWhitelist(this), "whitelist", this);
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
        registerCommand(new CmdRcmds(this), "rcmds", this);

        log.info("[RoyalCommands] RoyalCommands v" + version + " initiated.");
    }

    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        log.info("[RoyalCommands] RoyalCommands v" + version + " disabled.");
    }

}
