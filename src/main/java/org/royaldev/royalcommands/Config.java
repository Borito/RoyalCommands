package org.royaldev.royalcommands;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.royaldev.royalcommands.opencsv.CSVReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class Config {

    public static boolean assignPublicOnGeneric;
    public static boolean assignUseDisplayNames;
    public static boolean assignUseDurability;

    //-- Booleans --//
    public static boolean backDeath;
    public static boolean backpackReset;
    public static boolean buildPerm;
    public static boolean changeNameTag;
    public static boolean checkVersion;
    public static boolean cooldownAliases;
    public static boolean customHelp;
    public static boolean differentGamemodeTrade;
    public static boolean disablegetip;
    public static boolean dropExtras;
    public static boolean dumpCreateChest;
    public static boolean dumpUseInv;
    public static boolean explodeFire;
    public static boolean h2Convert;
    public static boolean itemSpawnTag;
    public static boolean kitPerms;
    public static boolean motdLogin;
    public static boolean multiverseNames;
    public static boolean overrideRespawn;
    public static boolean purgeUnusedUserdata;
    public static boolean removePotionEffects;
    public static boolean requireHelm;
    public static boolean safeTeleport;
    public static boolean sendToSpawn;
    public static boolean separateEnder;
    public static boolean separateInv;
    public static boolean separateXP;
    public static boolean showcommands;
    public static boolean simpleList;
    public static boolean smoothTime;
    public static boolean stsBack;
    public static boolean stsNew;
    public static boolean teleportSoundEnabled;
    public static boolean timeBroadcast;
    public static boolean updateCheck;
    public static boolean useProtocolLib;
    public static boolean useVNP;
    public static boolean useWelcome;
    public static boolean useWhitelist;
    public static boolean useWorldManager;
    public static boolean vehicleCrossWorldTeleport;
    public static boolean vehicleTeleportAnimals;
    public static boolean vehicleTeleportEnabled;
    public static boolean vehicleTeleportPlayers;
    public static boolean vehicleTeleportVehicles;
    public static boolean warpPermissions;
    public static boolean wmShowEmptyWorlds;
    public static boolean worldAccessPerm;
    public static boolean ymlConvert;
    public static ConfigurationSection warnActions;
    public static ConfigurationSection commandCooldowns;
    public static double defaultNear;

    //-- ConfigurationSections --//
    public static double findIpPercent;
    public static double globalTeleportCooldown;

    //-- Doubles --//
    public static double maxNear;
    public static float explodePower;
    public static float maxExplodePower;
    public static float teleportSoundPitch;

    //-- Floats --//
    public static float teleportSoundVolume;
    public static int defaultStack;
    public static int helpAmount;
    public static int maxBackStack;

    //-- Integers --//
    public static int spawnmobLimit;
    public static int teleportWarmup;
    public static List<String> blockedItems;
    public static List<String> disabledBackWorlds;
    public static List<String> disabledCommands;

    //-- String lists --//
    public static List<String> itemSpawnTagLore;
    public static List<String> logBlacklist;
    public static List<String> motd;
    public static List<String> muteCmds;
    public static List<String> onBanActions;
    public static List<String> whitelist;
    public static long afkAutoTime;
    public static long afkKickTime;
    public static long warnExpireTime;

    //-- Longs --//
    public static String afkFormat;
    public static String banFormat;
    public static String banMessage;

    //-- Strings --//
    public static String bcastFormat;
    public static String defaultWarn;
    public static String igBanFormat;
    public static String igKickFormat;
    public static String igTempbanFormat;
    public static String igUnbanFormat;
    public static String ipBanFormat;
    public static String kickFormat;
    public static String kickMessage;
    public static String mailCheckTime;
    public static String nickChangeLimit;
    public static String nickPrefix;
    public static String nickRegex;
    public static String noBuildMessage;
    public static String returnFormat;
    public static String saveInterval;
    public static String teleportSoundName;
    public static String tempbanFormat;
    public static String welcomeMessage;
    public static String whitelistMessage;
    public static String whoFormat;
    public static String whoGroupFormat;
    public static String positiveChatColor;
    public static String negativeChatColor;
    public static String neutralChatColor;
    public static String resetChatColor;

    private final RoyalCommands plugin;

    public Config(RoyalCommands instance) {
        plugin = instance;
        File config = new File(plugin.getDataFolder(), "config.yml");
        if (!config.exists()) {
            if (!config.getParentFile().mkdirs()) plugin.getLogger().warning("Could not create config.yml directory.");
            plugin.saveDefaultConfig();
        }
        this.reloadConfiguration();
    }

    public void reloadConfiguration() {
        plugin.reloadConfig();
        final FileConfiguration c = plugin.getConfig();

        //-- Booleans --//

        assignPublicOnGeneric = c.getBoolean("assign.public.allow_on_generic_items", false);
        assignUseDisplayNames = c.getBoolean("assign.lore_and_display_names", true);
        assignUseDurability = c.getBoolean("assign.durability", false);
        backDeath = c.getBoolean("teleports.back.death", true);
        backpackReset = c.getBoolean("backpack.reset_on_death", false);
        buildPerm = c.getBoolean("general.use_build_perm", false);
        changeNameTag = c.getBoolean("nicknames.change_nametag", false);
        checkVersion = c.getBoolean("updates.version_check", true);
        cooldownAliases = c.getBoolean("commands.cooldowns.options.match_aliases", true);
        customHelp = c.getBoolean("help.custom.enabled", false);
        differentGamemodeTrade = c.getBoolean("trade.between_gamemodes", false);
        disablegetip = c.getBoolean("security.disable_getip", false);
        dropExtras = c.getBoolean("items.spawn.drop_extras", false);
        dumpCreateChest = c.getBoolean("dump.create_chest", true);
        dumpUseInv = c.getBoolean("dump.use_inv", true);
        explodeFire = c.getBoolean("explode.fire", false);
        h2Convert = c.getBoolean("h2.convert", false);
        itemSpawnTag = c.getBoolean("items.spawn.tag.enabled", false);
        kitPerms = c.getBoolean("use_exclusive_kit_perms", false);
        motdLogin = c.getBoolean("motd.options.display_on_login", true);
        multiverseNames = c.getBoolean("worldmanager.multiverse_world_names", true);
        overrideRespawn = c.getBoolean("general.override_respawn", true);
        purgeUnusedUserdata = c.getBoolean("userdata.saving.purge_unused_userdata_handlers", true);
        removePotionEffects = c.getBoolean("remove_potion_effects", true);
        requireHelm = c.getBoolean("helm.require_item", false);
        safeTeleport = c.getBoolean("teleports.options.safe", true);
        sendToSpawn = c.getBoolean("teleports.spawn.login.send_all.enabled", false);
        separateEnder = c.getBoolean("worldmanager.inventory_separation.separate_ender_chests", true);
        separateInv = c.getBoolean("worldmanager.inventory_separation.enabled", false);
        separateXP = c.getBoolean("worldmanager.inventory_separation.separate_xp", true);
        showcommands = c.getBoolean("commands.logging.view_commands", true);
        simpleList = c.getBoolean("playerlist.simple", true);
        smoothTime = c.getBoolean("general.use_smooth_time", true);
        stsBack = c.getBoolean("teleports.spawn.login.send_all.register_back", false);
        stsNew = c.getBoolean("teleports.spawn.login.send_new", true);
        teleportSoundEnabled = c.getBoolean("teleports.sound.enabled", false);
        timeBroadcast = c.getBoolean("messages.options.broadcast_time_changes", false);
        updateCheck = c.getBoolean("updates.update_check", false);
        useProtocolLib = c.getBoolean("items.spawn.tag.plugins.protocollib", true);
        useVNP = c.getBoolean("plugins.use_vanish", true);
        useWelcome = c.getBoolean("messages.options.enable_welcome_message", true);
        useWhitelist = c.getBoolean("whitelist.enabled", false);
        useWorldManager = c.getBoolean("worldmanager.enabled", true);
        vehicleCrossWorldTeleport = c.getBoolean("teleports.vehicles.options.cross_world", false);
        vehicleTeleportAnimals = c.getBoolean("teleports.vehicles.options.animals", true);
        vehicleTeleportEnabled = c.getBoolean("teleports.vehicles.enabled", true);
        vehicleTeleportPlayers = c.getBoolean("teleports.vehicles.options.players", false);
        vehicleTeleportVehicles = c.getBoolean("teleports.vehicles.options.vehicles", true);
        warpPermissions = c.getBoolean("warps.explicit_permissions", false);
        wmShowEmptyWorlds = c.getBoolean("worldmanager.who.show_empty_worlds", false);
        worldAccessPerm = c.getBoolean("teleports.worlds.worldaccess_perm", false);
        ymlConvert = c.getBoolean("yml_convert", false);

        //-- ConfigurationSections --//

        commandCooldowns = c.getConfigurationSection("commands.cooldowns.list");
        warnActions = c.getConfigurationSection("warns.actions");

        //-- Doubles --//

        defaultNear = c.getDouble("near.default_radius", 50D);
        findIpPercent = c.getDouble("general.findip_alert_percentage", 25D);
        globalTeleportCooldown = c.getDouble("teleports.options.global_cooldown", 0D);
        maxNear = c.getDouble("near.max_radius", 2000D);

        //-- Floats --//

        explodePower = (float) c.getDouble("explode.default_power", 4F);
        maxExplodePower = (float) c.getDouble("explode.max_power", 10F);
        teleportSoundPitch = (float) c.getDouble("teleports.sound.pitch", 1F);
        teleportSoundVolume = (float) c.getDouble("teleports.sound.volume", 1F);

        //-- Integers --//

        defaultStack = c.getInt("items.spawn.default_stack_size", 64);
        helpAmount = c.getInt("help.lines", 5);
        maxBackStack = c.getInt("teleports.back.max_stack", 5);
        spawnmobLimit = c.getInt("spawnmob.spawn_limit", 15);
        teleportWarmup = c.getInt("teleports.options.warmup", 0);

        //-- String lists --//

        blockedItems = c.getStringList("items.spawn.blocked");
        disabledBackWorlds = c.getStringList("teleports.back.disabled_worlds");
        disabledCommands = c.getStringList("commands.disabled");
        itemSpawnTagLore = c.getStringList("items.spawn.tag.lore");
        logBlacklist = c.getStringList("commands.logging.blacklist");
        motd = c.getStringList("motd.content");
        muteCmds = c.getStringList("commands.mute_blocked");
        onBanActions = c.getStringList("bans.actions");

        //-- Longs --//

        afkAutoTime = c.getLong("afk.auto.times.afk", 120L);
        afkKickTime = c.getLong("afk.auto.times.kick", 300L);
        warnExpireTime = c.getLong("warns.expire_after", 604800L);

        //-- Strings --//

        afkFormat = c.getString("afk.messages.afk_format", "{dispname} is now AFK.");
        banFormat = c.getString("bans.permanent.messages.format", "&4Banned&r: {reason}&rnBy {dispname}");
        banMessage = RUtils.colorize(c.getString("bans.permanent.messages.default", "&4Banhammered!"));
        bcastFormat = RUtils.colorize(c.getString("messages.bcast_format", "&b[&aBroadcast&b]&a "));
        defaultWarn = RUtils.colorize(c.getString("warns.default_message", "You have been warned."));
        igBanFormat = c.getString("bans.permanent.messages.ingame_format", "&7{kdispname}&c was banned by &7{dispname}&c for &7{reason}&c.");
        igKickFormat = c.getString("kicks.messages.ingame_format", "&7{kdispname}&c was kicked by &7{dispname}&c for &7{reason}&c.");
        igTempbanFormat = c.getString("bans.temp.messages.ingame_format", "&7{kdispname}&c was tempbanned by &7{dispname}&c for &7{length}&c for &7{reason}&c.");
        igUnbanFormat = c.getString("bans.unban.messages.ingame_format", "&7{kdispname}&9 was unbanned by &7{dispname}&9.");
        ipBanFormat = c.getString("bans.ip.messages.format", "&4IP Banned&r: &7{ip}&r has been banned from this server.");
        kickFormat = c.getString("kicks.messages.format", "&4Kicked&r: {reason}&rnBy {dispname}");
        kickMessage = RUtils.colorize(c.getString("kicks.messages.default", "Kicked from server."));
        mailCheckTime = c.getString("mail.check_interval", "10m");
        nickChangeLimit = c.getString("nicknames.change_limit", "24h");
        nickPrefix = RUtils.colorize(c.getString("nicknames.prefix", "*"));
        nickRegex = c.getString("nicknames.regex", "[\\w]{2,16}");
        noBuildMessage = RUtils.colorize(c.getString("messages.no_build", "&cYou don't have permission to build!"));
        returnFormat = c.getString("afk.messages.return_format", "{dispname} is no longer AFK.");
        saveInterval = c.getString("userdata.saving.save_on_interval", "10m");
        teleportSoundName = c.getString("teleports.sound.sound", "ENDERMAN_TELEPORT");
        tempbanFormat = c.getString("bans.temp.messages.format", "&4Tempbanned&r: {length}&rnFor {reason}&r by {dispname}");
        welcomeMessage = RUtils.colorize(c.getString("messages.welcome", "&5Welcome {name} to the server!"));
        whitelistMessage = RUtils.colorize(c.getString("whitelist.message", "You are not whitelisted on this server!"));
        whoFormat = c.getString("playerlist.format", "{prefix}{dispname}");
        whoGroupFormat = c.getString("playerlist.group_format", "{prefix}{group}{suffix}");
        positiveChatColor = c.getString("colors.positive", "BLUE");
        negativeChatColor = c.getString("colors.negative", "RED");
        neutralChatColor = c.getString("colors.neutral", "GRAY");
        resetChatColor = c.getString("colors.reset", "RESET");

        if (plugin.whl.exists()) whitelist = plugin.whl.getStringList("whitelist");

        plugin.h.reloadHelp();

        if (RoyalCommands.wm == null) RoyalCommands.wm = new WorldManager();
        RoyalCommands.wm.reloadConfig();

        Reader in = null;
        try {
            in = new FileReader(new File(plugin.getDataFolder() + File.separator + "items.csv"));
            RoyalCommands.inm = new ItemNameManager(new CSVReader(in).readAll());
        } catch (FileNotFoundException e) {
            plugin.getLogger().warning("items.csv was not found! Item aliases will not be used.");
            RoyalCommands.inm = null;
        } catch (IOException e) {
            plugin.getLogger().warning("Internal input/output error loading items.csv. Item aliases will not be used.");
            RoyalCommands.inm = null;
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException ignored) {
            }
        }

    }


}
