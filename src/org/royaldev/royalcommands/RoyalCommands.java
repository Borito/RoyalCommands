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

import com.smilingdevil.devilstats.api.DevilStats;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Logger;

public class RoyalCommands extends JavaPlugin {

    public static Permission permission = null;
    public static Economy economy = null;

    public String version = null;
    public String newVersion = null;

    public java.util.List<String> muteCmds = new ArrayList<String>();
    public java.util.List<String> blockedItems = new ArrayList<String>();
    public java.util.List<String> motd = new ArrayList<String>();

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

    public String banMessage = null;
    public String kickMessage = null;
    public String defaultWarn = null;
    public String welcomeMessage = null;
    public String noBuildMessage = null;
    public String bcastFormat = null;

    public Integer defaultStack = null;
    public Integer warnBan = null;
    public Integer spawnmobLimit = null;

    public Double maxNear = null;
    public Double defaultNear = null;

    public Float explodePower = null;
    public Float maxExplodePower = null;

    public RoyalCommands() {
        pconfm = new PConfManager(this);
    }

    // Permissions with Vault
    public Boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    private Boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

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
        if (vp == null) {
            vp = (VanishPlugin) Bukkit.getServer().getPluginManager().getPlugin("VanishNoPacket");
            return false;
        } else return vp.getManager().isVanished(p.getName());
    }

    public void reloadConfigVals() {
        showcommands = this.getConfig().getBoolean("view_commands");
        disablegetip = this.getConfig().getBoolean("disable_getip");
        useWelcome = this.getConfig().getBoolean("enable_welcome_message");
        buildPerm = this.getConfig().getBoolean("use_build_perm");
        backDeath = this.getConfig().getBoolean("back_on_death");
        motdLogin = this.getConfig().getBoolean("motd_on_login");
        dropExtras = this.getConfig().getBoolean("drop_extras");
        kitPerms = this.getConfig().getBoolean("use_exclusive_kit_perms");
        explodeFire = this.getConfig().getBoolean("explode_fire");
        sendToSpawn = this.getConfig().getBoolean("send_to_spawn");
        stsBack = this.getConfig().getBoolean("sts_back");
        stsNew = this.getConfig().getBoolean("send_to_spawn_new");


        banMessage = this.getConfig().getString("default_ban_message").replaceAll("(&([a-f0-9]))", "\u00A7$2");
        noBuildMessage = this.getConfig().getString("no_build_message").replaceAll("(&([a-f0-9]))", "\u00A7$2");
        kickMessage = this.getConfig().getString("default_kick_message").replaceAll("(&([a-f0-9]))", "\u00A7$2");
        defaultWarn = this.getConfig().getString("default_warn_message").replaceAll("(&([a-f0-9]))", "\u00A7$2");
        welcomeMessage = this.getConfig().getString("welcome_message").replaceAll("(&([a-f0-9]))", "\u00A7$2");
        bcastFormat = this.getConfig().getString("bcast_format").replaceAll("(&([a-f0-9]))", "\u00A7$2");

        defaultStack = this.getConfig().getInt("default_stack_size");
        warnBan = this.getConfig().getInt("max_warns_before_ban");
        spawnmobLimit = this.getConfig().getInt("spawnmob_limit");

        maxNear = this.getConfig().getDouble("max_near_radius");
        defaultNear = this.getConfig().getDouble("default_near_radius");

        explodePower = (float) this.getConfig().getDouble("explode_power");
        maxExplodePower = (float) this.getConfig().getDouble("max_explode_power");

        muteCmds = this.getConfig().getStringList("mute_blocked_commands");
        blockedItems = this.getConfig().getStringList("blocked_spawn_items");
        motd = this.getConfig().getStringList("motd");
    }

    public void loadConfiguration() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
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
        File rules = new File(this.getDataFolder() + File.separator + "rules.txt");
        if (!rules.exists()) {
            try {
                boolean success = new File(this.getDataFolder() + File.separator + "rules.txt").createNewFile();
                if (!success) {
                    log.severe("[RoyalCommands] Could not create rules.txt!");
                } else {
                    try {
                        BufferedWriter out = new BufferedWriter(new FileWriter(this.getDataFolder() + File.separator + "rules.txt"));
                        out.write("###");
                        out.write("1. Be kind");
                        out.write("2. Be courteous");
                        out.write("3. Be respectful");
                        out.write("###");
                        out.write("4. Be cool");
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
            if (i != start) {
                bldr.append(" ");
            }
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

    DevilStats stats;

    public void onEnable() {
        loadConfiguration();

        // DevilStats ftw
        try {
            stats = new DevilStats(this);
            stats.startup();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setupEconomy();

        version = this.getDescription().getVersion();

        // yet again, borrowed from MilkBowl
        this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                try {
                    newVersion = updateCheck(version);
                    String oldVersion = version;
                    if (!newVersion.contains(oldVersion)) {
                        log.warning(newVersion + " is out! You are running " + oldVersion);
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

        getCommand("level").setExecutor(new Level(this));
        getCommand("setlevel").setExecutor(new Setlevel(this));
        getCommand("sci").setExecutor(new Sci(this));
        getCommand("speak").setExecutor(new Speak(this));
        getCommand("facepalm").setExecutor(new Facepalm(this));
        getCommand("slap").setExecutor(new Slap(this));
        getCommand("harm").setExecutor(new Harm(this));
        getCommand("starve").setExecutor(new Starve(this));
        getCommand("banned").setExecutor(new Banned(this));
        getCommand("setarmor").setExecutor(new Setarmor(this));
        getCommand("getip").setExecutor(new GetIP(this));
        getCommand("compareip").setExecutor(new CompareIP(this));
        getCommand("ragequit").setExecutor(new RageQuit(this));
        getCommand("quit").setExecutor(new Quit(this));
        getCommand("rank").setExecutor(new Rank(this));
        getCommand("freeze").setExecutor(new Freeze(this));
        getCommand("fakeop").setExecutor(new Fakeop(this));
        getCommand("vtp").setExecutor(new Vtp(this));
        getCommand("vtphere").setExecutor(new Vtphere(this));
        getCommand("megastrike").setExecutor(new MegaStrike(this));
        getCommand("pext").setExecutor(new Pext(this));
        getCommand("item").setExecutor(new Item(this));
        getCommand("clearinventory").setExecutor(new ClearInventory(this));
        getCommand("weather").setExecutor(new Weather(this));
        getCommand("fixchunk").setExecutor(new FixChunk(this));
        getCommand("give").setExecutor(new Give(this));
        getCommand("message").setExecutor(new Message(this));
        getCommand("reply").setExecutor(new Reply(this));
        getCommand("gamemode").setExecutor(new Gamemode(this));
        getCommand("mute").setExecutor(new Mute(this));
        getCommand("ban").setExecutor(new Ban(this));
        getCommand("kick").setExecutor(new Kick(this));
        getCommand("time").setExecutor(new Time(this));
        getCommand("home").setExecutor(new Home(this));
        getCommand("sethome").setExecutor(new SetHome(this));
        getCommand("delhome").setExecutor(new DelHome(this));
        getCommand("listhome").setExecutor(new ListHome(this));
        getCommand("strike").setExecutor(new Strike(this));
        getCommand("jump").setExecutor(new Jump(this));
        getCommand("warn").setExecutor(new Warn(this));
        getCommand("clearwarns").setExecutor(new ClearWarns(this));
        getCommand("warp").setExecutor(new Warp(this));
        getCommand("setwarp").setExecutor(new SetWarp(this));
        getCommand("delwarp").setExecutor(new DelWarp(this));
        getCommand("repair").setExecutor(new Repair(this));
        getCommand("unban").setExecutor(new Unban(this));
        getCommand("heal").setExecutor(new Heal(this));
        getCommand("feed").setExecutor(new Feed(this));
        getCommand("god").setExecutor(new God(this));
        getCommand("banreason").setExecutor(new Banreason(this));
        getCommand("setspawn").setExecutor(new SetSpawn(this));
        getCommand("spawn").setExecutor(new Spawn(this));
        getCommand("banip").setExecutor(new BanIP(this));
        getCommand("unbanip").setExecutor(new UnbanIP(this));
        getCommand("list").setExecutor(new List(this));
        getCommand("back").setExecutor(new Back(this));
        getCommand("teleport").setExecutor(new Teleport(this));
        getCommand("teleporthere").setExecutor(new TeleportHere(this));
        getCommand("teleportrequest").setExecutor(new TeleportRequest(this));
        getCommand("tpaccept").setExecutor(new TpAccept(this));
        getCommand("tpdeny").setExecutor(new TpDeny(this));
        getCommand("listwarns").setExecutor(new ListWarns(this));
        getCommand("more").setExecutor(new More(this));
        getCommand("teleportrequesthere").setExecutor(new TeleportRequestHere(this));
        getCommand("spy").setExecutor(new Spy(this));
        getCommand("spawnmob").setExecutor(new SpawnMob(this));
        getCommand("afk").setExecutor(new Afk(this));
        getCommand("assign").setExecutor(new Assign(this));
        getCommand("onehitkill").setExecutor(new OneHitKill(this));
        getCommand("burn").setExecutor(new Burn(this));
        getCommand("kickall").setExecutor(new KickAll(this));
        getCommand("world").setExecutor(new CmdWorld(this));
        getCommand("jail").setExecutor(new Jail(this));
        getCommand("setjail").setExecutor(new SetJail(this));
        getCommand("less").setExecutor(new Less(this));
        getCommand("spawner").setExecutor(new Spawner(this));
        getCommand("tp2p").setExecutor(new Tp2p(this));
        getCommand("motd").setExecutor(new Motd(this));
        getCommand("deljail").setExecutor(new DelJail(this));
        getCommand("force").setExecutor(new Force(this));
        getCommand("ping").setExecutor(new CmdPing(this));
        getCommand("invsee").setExecutor(new CmdInvsee(this));
        getCommand("realname").setExecutor(new CmdRealName(this));
        getCommand("nick").setExecutor(new Nick(this));
        getCommand("ingot2block").setExecutor(new Ingot2Block(this));
        getCommand("near").setExecutor(new CmdNear(this));
        getCommand("kill").setExecutor(new CmdKill(this));
        getCommand("suicide").setExecutor(new Suicide(this));
        getCommand("killall").setExecutor(new KillAll(this));
        getCommand("muteall").setExecutor(new MuteAll(this));
        getCommand("kit").setExecutor(new CmdKit(this));
        getCommand("rules").setExecutor(new CmdRules(this));
        getCommand("broadcast").setExecutor(new CmdBroadcast(this));
        getCommand("hug").setExecutor(new CmdHug(this));
        getCommand("explode").setExecutor(new CmdExplode(this));
        getCommand("ride").setExecutor(new CmdRide(this));
        getCommand("rcmds").setExecutor(new Rcmds(this));
        getCommand("whobanned").setExecutor(new CmdWhoBanned(this));

        reloadConfigVals();

        log.info("[RoyalCommands] RoyalCommands v" + this.version + " initiated.");
    }

    public void onDisable() {

        if (stats != null) {
            stats.shutdown();
        }

        log.info("[RoyalCommands] RoyalCommands v" + this.version + " disabled.");
    }

}