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

// import org.yaml.snakeyaml <- SnakeYAML start

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.kitteh.vanish.VanishPlugin;
import org.royaldev.royalcommands.listeners.RoyalCommandsBlockListener;
import org.royaldev.royalcommands.listeners.RoyalCommandsEntityListener;
import org.royaldev.royalcommands.listeners.RoyalCommandsPlayerListener;
import org.royaldev.royalcommands.listeners.SignListener;
import org.royaldev.royalcommands.rcommands.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class RoyalCommands extends JavaPlugin {

    public ConfigManager whl;

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

    public String banMessage = null;
    public String kickMessage = null;
    public String defaultWarn = null;
    public String welcomeMessage = null;
    public String noBuildMessage = null;
    public String bcastFormat = null;
    public String whoFormat = null;

    public static Integer defaultStack = null;
    public Integer warnBan = null;
    public Integer spawnmobLimit = null;
    public Integer helpAmount = null;

    public Double maxNear = null;
    public Double defaultNear = null;
    public Double gTeleCd = null;

    public Float explodePower = null;
    public Float maxExplodePower = null;

    public RoyalCommands() {
        pconfm = new PConfManager(this);
    }

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
    public final PConfManager pconfm;

    public Logger log = Logger.getLogger("Minecraft");

    VanishPlugin vp = null;

    public boolean isVanished(Player p) {
        if (!useVNP) return false;
        if (vp == null) {
            vp = (VanishPlugin) Bukkit.getServer().getPluginManager().getPlugin("VanishNoPacket");
            return false;
        } else return vp.getManager().isVanished(p.getName());
    }

    public void reloadConfigVals() {
        if (whl != null) whl.load();
        showcommands = getConfig().getBoolean("view_commands");
        disablegetip = getConfig().getBoolean("disable_getip");
        useWelcome = getConfig().getBoolean("enable_welcome_message");
        buildPerm = getConfig().getBoolean("use_build_perm");
        backDeath = getConfig().getBoolean("back_on_death");
        motdLogin = getConfig().getBoolean("motd_on_login");
        dropExtras = getConfig().getBoolean("drop_extras");
        kitPerms = getConfig().getBoolean("use_exclusive_kit_perms");
        explodeFire = getConfig().getBoolean("explode_fire");
        sendToSpawn = getConfig().getBoolean("send_to_spawn");
        stsBack = getConfig().getBoolean("sts_back");
        stsNew = getConfig().getBoolean("send_to_spawn_new");
        otherHelp = getConfig().getBoolean("other_plugins_in_help");
        customHelp = getConfig().getBoolean("use_custom_help");
        useVNP = getConfig().getBoolean("use_vanish");
        cooldownAliases = getConfig().getBoolean("cooldowns_match_aliases");
        useWhitelist = getConfig().getBoolean("use_whitelist");
        smoothTime = getConfig().getBoolean("use_smooth_time");

        banMessage = RUtils.colorize(getConfig().getString("default_ban_message"));
        noBuildMessage = RUtils.colorize(getConfig().getString("no_build_message"));
        kickMessage = RUtils.colorize(getConfig().getString("default_kick_message"));
        defaultWarn = RUtils.colorize(getConfig().getString("default_warn_message"));
        welcomeMessage = RUtils.colorize(getConfig().getString("welcome_message"));
        bcastFormat = RUtils.colorize(getConfig().getString("bcast_format"));
        whoFormat = getConfig().getString("who_format");

        defaultStack = getConfig().getInt("default_stack_size");
        warnBan = getConfig().getInt("max_warns_before_ban");
        spawnmobLimit = getConfig().getInt("spawnmob_limit");
        helpAmount = getConfig().getInt("help_lines");

        maxNear = getConfig().getDouble("max_near_radius");
        defaultNear = getConfig().getDouble("default_near_radius");
        gTeleCd = getConfig().getDouble("global_teleport_cooldown");

        explodePower = (float) getConfig().getDouble("explode_power");
        maxExplodePower = (float) getConfig().getDouble("max_explode_power");

        muteCmds = getConfig().getStringList("mute_blocked_commands");
        blockedItems = getConfig().getStringList("blocked_spawn_items");
        motd = getConfig().getStringList("motd");
        commandCooldowns = getConfig().getStringList("command_cooldowns");
        if (whl != null) whitelist = whl.getStringList("whitelist");

        Help.reloadHelp();
    }

    public void loadConfiguration() {
        // This until I get a custom YAML wrapper using SnakeYAML going.
        if (!new File(getDataFolder() + File.separator + "config.yml").exists()) saveDefaultConfig();
        File file = new File(this.getDataFolder() + File.separator + "userdata" + File.separator);
        boolean exists = file.exists();
        if (!exists) {
            try {
                boolean success = new File(this.getDataFolder() + File.separator + "userdata").mkdir();
                if (success) {
                    log.info("[RoyalCommands] Created userdata directory.");
                }
            } catch (Exception e) {
                log.severe("[RoyalCommands] Failed to make userdata directory!");
                log.severe(e.getMessage());
            }
        }
        File whitelist = new File(getDataFolder() + File.separator + "whitelist.yml");
        if (!whitelist.exists()) {
            try {
                boolean success = whitelist.createNewFile();
                if (!success) {
                    log.severe("[RoyalCommands] Could not create whitelist.yml!");
                } else {
                    try {
                        BufferedWriter out = new BufferedWriter(new FileWriter(whitelist.getAbsolutePath()));
                        out.write("whitelist:\n");
                        out.write("- jkcclemens\n");
                        out.write("- other_guy\n");
                        out.close();
                    } catch (IOException e) {
                        //ignore
                    }
                }
            } catch (Exception e) {
                log.severe("[RoyalCommands] Could not create whitelist.yml!");
                e.printStackTrace();
            }
        }
        File rules = new File(this.getDataFolder() + File.separator + "rules.txt");
        if (!rules.exists()) {
            try {
                boolean success = new File(this.getDataFolder() + File.separator + "rules.txt").createNewFile();
                if (!success) {
                    log.severe("[RoyalCommands] Could not create rules.txt!");
                } else {
                    try {
                        BufferedWriter out = new BufferedWriter(new FileWriter(this.getDataFolder() + File.separator + "rules.txt"));
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
        File help = new File(this.getDataFolder() + File.separator + "help.txt");
        if (!help.exists()) {
            try {
                boolean success = new File(this.getDataFolder() + File.separator + "help.txt").createNewFile();
                if (!success) {
                    log.severe("[RoyalCommands] Could not create help.txt!");
                } else {
                    try {
                        BufferedWriter out = new BufferedWriter(new FileWriter(this.getDataFolder() + File.separator + "help.txt"));
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
        File warps = new File(this.getDataFolder() + File.separator + "warps.yml");
        if (!warps.exists()) {
            try {
                boolean success = new File(this.getDataFolder() + File.separator + "warps.yml").createNewFile();
                if (success) {
                    try {
                        FileWriter fstream = new FileWriter(this.getDataFolder() + File.separator + "warps.yml");
                        BufferedWriter out = new BufferedWriter(fstream);
                        out.write("warps:");
                        out.close();
                    } catch (Exception e) {
                        log.severe("[RoyalCommands] Could not write to warps file.");
                    }
                    log.info("[RoyalCommands] Created warps file.");
                }
            } catch (Exception e) {
                log.severe("[RoyalCommands] Failed to make warps file!");
                log.severe(e.getMessage());
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

    public boolean isAuthorized(final Player player, final String node) {
        return player instanceof ConsoleCommandSender || this.setupPermissions() && (RoyalCommands.permission.has(player, "rcmds.admin") || RoyalCommands.permission.has(player, node));
    }

    public boolean isAuthorized(final CommandSender player, final String node) {
        return player instanceof ConsoleCommandSender || this.setupPermissions() && (RoyalCommands.permission.has((Player) player, "rcmds.admin") || RoyalCommands.permission.has(player, node));
    }

    public void onEnable() {
        commands = getDescription().getCommands();
        plugins = getServer().getPluginManager().getPlugins();

        loadConfiguration();
        reloadConfigVals();
        try {
            whl = new ConfigManager(getDataFolder().getAbsolutePath() + File.separator + "whitelist.yml");
        } catch (FileNotFoundException e) {
            log.warning("[RoyalCommands] Could not find whitelist.yml!");
            whl = null;
        }

        setupEconomy();
        setupChat();

        try {
            m = new Metrics(this);
            m.start();
        } catch (Exception ignore) {
            log.warning("[RoyalCommands] Could not start Metrics!");
        }

        version = getDescription().getVersion();

        // yet again, borrowed from MilkBowl
        this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
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

        vp = (VanishPlugin) Bukkit.getServer().getPluginManager().getPlugin("VanishNoPacket");

        PluginManager pm = this.getServer().getPluginManager();

        pm.registerEvents(playerListener, this);
        pm.registerEvents(entityListener, this);
        pm.registerEvents(blockListener, this);
        pm.registerEvents(signListener, this);

        getCommand("level").setExecutor(new CmdLevel(this));
        getCommand("setlevel").setExecutor(new CmdSetlevel(this));
        getCommand("sci").setExecutor(new CmdSci(this));
        getCommand("speak").setExecutor(new CmdSpeak(this));
        getCommand("facepalm").setExecutor(new CmdFacepalm(this));
        getCommand("slap").setExecutor(new CmdSlap(this));
        getCommand("harm").setExecutor(new CmdHarm(this));
        getCommand("starve").setExecutor(new CmdStarve(this));
        getCommand("banned").setExecutor(new CmdBanned(this));
        getCommand("setarmor").setExecutor(new CmdSetarmor(this));
        getCommand("getip").setExecutor(new CmdGetIP(this));
        getCommand("compareip").setExecutor(new CmdCompareIP(this));
        getCommand("ragequit").setExecutor(new CmdRageQuit(this));
        getCommand("quit").setExecutor(new CmdQuit(this));
        getCommand("rank").setExecutor(new CmdRank(this));
        getCommand("freeze").setExecutor(new CmdFreeze(this));
        getCommand("fakeop").setExecutor(new CmdFakeop(this));
        getCommand("vtp").setExecutor(new CmdVtp(this));
        getCommand("vtphere").setExecutor(new CmdVtphere(this));
        getCommand("megastrike").setExecutor(new CmdMegaStrike(this));
        getCommand("pext").setExecutor(new CmdPext(this));
        getCommand("item").setExecutor(new CmdItem(this));
        getCommand("clearinventory").setExecutor(new CmdClearInventory(this));
        getCommand("weather").setExecutor(new CmdWeather(this));
        getCommand("fixchunk").setExecutor(new CmdFixChunk(this));
        getCommand("give").setExecutor(new CmdGive(this));
        getCommand("message").setExecutor(new CmdMessage(this));
        getCommand("reply").setExecutor(new CmdReply(this));
        getCommand("gamemode").setExecutor(new CmdGamemode(this));
        getCommand("mute").setExecutor(new CmdMute(this));
        getCommand("ban").setExecutor(new CmdBan(this));
        getCommand("kick").setExecutor(new CmdKick(this));
        getCommand("time").setExecutor(new CmdTime(this));
        getCommand("home").setExecutor(new CmdHome(this));
        getCommand("sethome").setExecutor(new CmdSetHome(this));
        getCommand("delhome").setExecutor(new CmdDelHome(this));
        getCommand("listhome").setExecutor(new CmdListHome(this));
        getCommand("strike").setExecutor(new CmdStrike(this));
        getCommand("jump").setExecutor(new CmdJump(this));
        getCommand("warn").setExecutor(new CmdWarn(this));
        getCommand("clearwarns").setExecutor(new CmdClearWarns(this));
        getCommand("warp").setExecutor(new CmdWarp(this));
        getCommand("setwarp").setExecutor(new CmdSetWarp(this));
        getCommand("delwarp").setExecutor(new CmdDelWarp(this));
        getCommand("repair").setExecutor(new CmdRepair(this));
        getCommand("unban").setExecutor(new CmdUnban(this));
        getCommand("heal").setExecutor(new CmdHeal(this));
        getCommand("feed").setExecutor(new CmdFeed(this));
        getCommand("god").setExecutor(new CmdGod(this));
        getCommand("banreason").setExecutor(new CmdBanreason(this));
        getCommand("setspawn").setExecutor(new CmdSetSpawn(this));
        getCommand("spawn").setExecutor(new CmdSpawn(this));
        getCommand("banip").setExecutor(new CmdBanIP(this));
        getCommand("unbanip").setExecutor(new CmdUnbanIP(this));
        getCommand("list").setExecutor(new CmdList(this));
        getCommand("back").setExecutor(new CmdBack(this));
        getCommand("teleport").setExecutor(new CmdTeleport(this));
        getCommand("teleporthere").setExecutor(new CmdTeleportHere(this));
        getCommand("teleportrequest").setExecutor(new CmdTeleportRequest(this));
        getCommand("tpaccept").setExecutor(new CmdTpAccept(this));
        getCommand("tpdeny").setExecutor(new CmdTpDeny(this));
        getCommand("listwarns").setExecutor(new CmdListWarns(this));
        getCommand("more").setExecutor(new CmdMore(this));
        getCommand("teleportrequesthere").setExecutor(new CmdTeleportRequestHere(this));
        getCommand("spy").setExecutor(new CmdSpy(this));
        getCommand("spawnmob").setExecutor(new CmdSpawnMob(this));
        getCommand("afk").setExecutor(new CmdAfk(this));
        getCommand("assign").setExecutor(new CmdAssign(this));
        getCommand("onehitkill").setExecutor(new CmdOneHitKill(this));
        getCommand("burn").setExecutor(new CmdBurn(this));
        getCommand("kickall").setExecutor(new CmdKickAll(this));
        getCommand("world").setExecutor(new CmdWorld(this));
        getCommand("jail").setExecutor(new CmdJail(this));
        getCommand("setjail").setExecutor(new CmdSetJail(this));
        getCommand("less").setExecutor(new CmdLess(this));
        getCommand("spawner").setExecutor(new CmdSpawner(this));
        getCommand("tp2p").setExecutor(new CmdTp2p(this));
        getCommand("motd").setExecutor(new CmdMotd(this));
        getCommand("deljail").setExecutor(new CmdDelJail(this));
        getCommand("force").setExecutor(new CmdForce(this));
        getCommand("ping").setExecutor(new CmdPing(this));
        getCommand("invsee").setExecutor(new CmdInvsee(this));
        getCommand("realname").setExecutor(new CmdRealName(this));
        getCommand("nick").setExecutor(new CmdNick(this));
        getCommand("ingot2block").setExecutor(new CmdIngot2Block(this));
        getCommand("near").setExecutor(new CmdNear(this));
        getCommand("kill").setExecutor(new CmdKill(this));
        getCommand("suicide").setExecutor(new CmdSuicide(this));
        getCommand("killall").setExecutor(new CmdKillAll(this));
        getCommand("muteall").setExecutor(new CmdMuteAll(this));
        getCommand("kit").setExecutor(new CmdKit(this));
        getCommand("rules").setExecutor(new CmdRules(this));
        getCommand("broadcast").setExecutor(new CmdBroadcast(this));
        getCommand("hug").setExecutor(new CmdHug(this));
        getCommand("explode").setExecutor(new CmdExplode(this));
        getCommand("ride").setExecutor(new CmdRide(this));
        getCommand("whobanned").setExecutor(new CmdWhoBanned(this));
        getCommand("tppos").setExecutor(new CmdTppos(this));
        getCommand("ignore").setExecutor(new CmdIgnore(this));
        getCommand("help").setExecutor(new CmdHelp(this));
        getCommand("coords").setExecutor(new CmdCoords(this));
        getCommand("tpall").setExecutor(new CmdTpAll(this));
        getCommand("tpaall").setExecutor(new CmdTpaAll(this));
        getCommand("vip").setExecutor(new CmdVip(this));
        getCommand("dump").setExecutor(new CmdDump(this));
        getCommand("seen").setExecutor(new CmdSeen(this));
        getCommand("tempban").setExecutor(new CmdTempban(this));
        getCommand("tptoggle").setExecutor(new CmdTpToggle(this));
        getCommand("kits").setExecutor(new CmdKits(this));
        getCommand("lag").setExecutor(new CmdLag(this));
        getCommand("mem").setExecutor(new CmdMem(this));
        getCommand("entities").setExecutor(new CmdEntities(this));
        getCommand("invmod").setExecutor(new CmdInvmod(this));
        getCommand("workbench").setExecutor(new CmdWorkbench(this));
        getCommand("enchantingtable").setExecutor(new CmdEnchantingTable(this));
        getCommand("trade").setExecutor(new CmdTrade(this));
        getCommand("furnace").setExecutor(new CmdFurnace(this));
        getCommand("enchant").setExecutor(new CmdEnchant(this));
        getCommand("whitelist").setExecutor(new CmdWhitelist(this));
        getCommand("fireball").setExecutor(new CmdFireball(this));
        getCommand("fly").setExecutor(new CmdFly(this));
        getCommand("playertime").setExecutor(new CmdPlayerTime(this));
        getCommand("compass").setExecutor(new CmdCompass(this));
        getCommand("helmet").setExecutor(new CmdHelmet(this));
        getCommand("worldmanager").setExecutor(new CmdWorldManager(this));
        getCommand("biome").setExecutor(new CmdBiome(this));
        getCommand("rcmds").setExecutor(new CmdRcmds(this));

        log.info("[RoyalCommands] RoyalCommands v" + version + " initiated.");
    }

    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        log.info("[RoyalCommands] RoyalCommands v" + version + " disabled.");
    }

}