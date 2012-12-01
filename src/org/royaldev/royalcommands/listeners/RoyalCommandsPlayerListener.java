package org.royaldev.royalcommands.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.royaldev.royalcommands.AFKUtils;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.playermanagers.YMLPConfManager;
import org.royaldev.royalcommands.rcommands.CmdBackpack;
import org.royaldev.royalcommands.rcommands.CmdMotd;
import org.royaldev.royalcommands.rcommands.CmdSpawn;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Dynamic tempban times

@SuppressWarnings("unused")
public class RoyalCommandsPlayerListener implements Listener {

    public static RoyalCommands plugin;

    public RoyalCommandsPlayerListener(RoyalCommands instance) {
        plugin = instance;
    }

    Logger log = Logger.getLogger("Minecraft");

    public void setCooldown(String command, OfflinePlayer p) {
        ConfigurationSection cmdCds = plugin.getConfig().getConfigurationSection("command_cooldowns");
        if (cmdCds == null) return;
        boolean contains = cmdCds.getKeys(false).contains(command);
        if (plugin.cooldownAliases) if (plugin.getCommand(command) != null)
            for (String alias : plugin.getCommand(command).getAliases())
                if (cmdCds.getKeys(false).contains(alias)) {
                    contains = true;
                    break;
                }
        if (contains) {
            long cooldown = cmdCds.getLong(command);
            new PConfManager(p).setLong(new Date().getTime() + (cooldown * 1000), "command_cooldowns." + command);
        }
    }

    public void setTeleCooldown(OfflinePlayer p) {
        double seconds = plugin.gTeleCd;
        if (seconds <= 0) return;
        new PConfManager(p).setDouble((seconds * 1000) + new Date().getTime(), "teleport_cooldown");
    }

    @EventHandler
    public void deafenMessages(AsyncPlayerChatEvent e) {
        if (e.isCancelled()) return;
        List<Player> toRemove = new ArrayList<Player>();
        for (Player t : e.getRecipients()) {
            PConfManager pcm = new PConfManager(t);
            Boolean isDeaf = pcm.getBoolean("deaf");
            if (isDeaf == null || !isDeaf) continue;
            if (e.getPlayer().getName().equals(t.getName())) continue; // don't remove own messages
            toRemove.add(t);
        }
        e.getRecipients().removeAll(toRemove); // remove deaf players
    }

    @EventHandler
    public void worldPerms(PlayerTeleportEvent e) {
        if (e.isCancelled()) return;
        if (!plugin.worldAccessPerm) return;
        World from = e.getFrom().getWorld();
        World to = e.getTo().getWorld();
        if (!from.equals(to)) return;
        Player p = e.getPlayer();
        if (plugin.isAuthorized(p, "rcmds.worldaccess." + to.getName())) return;
        e.setTo(e.getFrom());
        p.sendMessage(ChatColor.RED + "You do not have permission to access the world \"" + RUtils.getMVWorldName(to) + ".\"");
    }

    @EventHandler
    public void lastPosition(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        PConfManager pcm = new PConfManager(p);
        Location l = p.getLocation();
        String lastPos = "lastposition.";
        pcm.setDouble(l.getX(), lastPos + "x");
        pcm.setDouble(l.getY(), lastPos + "y");
        pcm.setDouble(l.getZ(), lastPos + "z");
        pcm.setFloat(l.getPitch(), lastPos + "pitch");
        pcm.setFloat(l.getYaw(), lastPos + "yaw");
        pcm.setString(l.getWorld().getName(), lastPos + "world");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void saveData(PlayerQuitEvent e) {
        PConfManager pcm = new PConfManager(e.getPlayer());
        Object rm = pcm.getRealManager();
        if (!(rm instanceof YMLPConfManager)) return;
        YMLPConfManager ypcm = (YMLPConfManager) rm;
        ypcm.forceSave();
    }

    @EventHandler
    public void whitelistMessage(PlayerLoginEvent e) {
        if (!plugin.getServer().hasWhitelist()) return;
        if (e.getResult() != Result.KICK_WHITELIST) return;
        if (!e.getPlayer().isWhitelisted()) e.disallow(Result.KICK_WHITELIST, plugin.whitelistFormat);
    }

    @EventHandler
    public void teleWarmup(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Location to = e.getTo();
        Location from = e.getFrom();
        if (to.getX() == from.getX() && to.getY() == from.getY() && to.getZ() == from.getZ())
            return;
        PConfManager pcm = new PConfManager(p);
        Long l = pcm.getLong("teleport_warmup");
        if (l == null) return;
        int toAdd = plugin.teleportWarmup * 1000;
        l = l + toAdd;
        long c = new Date().getTime();
        if (l > c) {
            p.sendMessage(ChatColor.RED + "You moved! Teleport cancelled!");
            pcm.setLong(-1L, "teleport_warmup");
        }
    }

    @EventHandler
    public void afk(PlayerMoveEvent e) {
        AFKUtils.setLastMove(e.getPlayer(), new Date().getTime());
    }

    @EventHandler
    public void whitelist(PlayerLoginEvent e) {
        if (!plugin.useWhitelist) return;
        if (!plugin.whitelist.contains(e.getPlayer().getName()))
            e.disallow(Result.KICK_WHITELIST, plugin.whitelistFormat);
    }

    @EventHandler
    public void commandCooldown(PlayerCommandPreprocessEvent e) {
        if (e.isCancelled()) return;
        String command = e.getMessage().split(" ")[0].toLowerCase().substring(1);
        if (plugin.getCommand(command) != null)
            command = plugin.getCommand(command).getName();
        Player p = e.getPlayer();
        if (plugin.isAuthorized(p, "rcmds.exempt.cooldown.commands")) return;
        Long currentcd = new PConfManager(p).getLong("command_cooldowns." + command);
        if (currentcd != null) {
            if (currentcd <= new Date().getTime()) {
                setCooldown(command, p);
                return;
            }
            p.sendMessage(ChatColor.RED + "You can't use that command for" + ChatColor.GRAY + RUtils.formatDateDiff(currentcd) + ChatColor.RED + ".");
            e.setCancelled(true);
            return;
        }
        setCooldown(command, p);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void teleCooldown(PlayerTeleportEvent e) {
        if (e.isCancelled()) return;
        Player p = e.getPlayer();
        if (plugin.isAuthorized(p, "rcmds.exempt.cooldown.teleports")) return;
        Long currentcd = new PConfManager(p).getLong("teleport_cooldown");
        if (currentcd != null) {
            if (currentcd <= new Date().getTime()) {
                setTeleCooldown(p);
                return;
            }
            p.sendMessage(ChatColor.RED + "You can't teleport for" + ChatColor.GRAY + RUtils.formatDateDiff(currentcd) + ChatColor.RED + ".");
            e.setCancelled(true);
            return;
        }
        setTeleCooldown(p);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTeleport(PlayerTeleportEvent e) {
        if (e.isCancelled()) return;
        if (new PConfManager(e.getPlayer()).getBoolean("jailed")) {
            e.getPlayer().sendMessage(ChatColor.RED + "You are jailed and may not teleport.");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        new PConfManager(e.getPlayer()).setLong(new Date().getTime(), "seen");
        if (AFKUtils.isAfk(e.getPlayer())) AFKUtils.unsetAfk(e.getPlayer());
        if (AFKUtils.moveTimesContains(e.getPlayer()))
            AFKUtils.removeLastMove(e.getPlayer());
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        new PConfManager(e.getPlayer()).setLong(new Date().getTime(), "seen");
        if (AFKUtils.isAfk(e.getPlayer())) AFKUtils.unsetAfk(e.getPlayer());
        if (AFKUtils.moveTimesContains(e.getPlayer()))
            AFKUtils.removeLastMove(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) return;
        Player p = event.getPlayer();
        PConfManager pcm = new PConfManager(p);
        if (plugin.showcommands) {
            String command = event.getMessage().split(" ")[0].toLowerCase();
            if (!plugin.logBlacklist.contains(command.substring(1)))
                log.info("[PLAYER_COMMAND] " + p.getName() + ": " + event.getMessage());
        }
        if (pcm.getBoolean("muted")) {
            if (pcm.get("mutetime") != null && !RUtils.isTimeStampValid(p, "mutetime"))
                pcm.setBoolean(false, "muted");
            for (String command : plugin.muteCmds) {
                if (!(event.getMessage().toLowerCase().startsWith(command.toLowerCase() + " ") || event.getMessage().equalsIgnoreCase(command.toLowerCase())))
                    continue;
                p.sendMessage(ChatColor.RED + "You are muted.");
                log.info("[RoyalCommands] " + p.getName() + " tried to use that command, but is muted.");
                event.setCancelled(true);
            }
        }

        if (pcm.getBoolean("jailed")) {
            p.sendMessage(ChatColor.RED + "You are jailed.");
            log.info("[RoyalCommands] " + p.getName() + " tried to use that command, but is jailed.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (!plugin.backpackReset) return;
        if (!CmdBackpack.invs.containsKey(e.getEntity().getName())) return;
        CmdBackpack.invs.get(e.getEntity().getName()).clear();
        RUtils.saveHash(CmdBackpack.invs, plugin.getDataFolder() + File.separator + "backpacks.sav");
    }

    @EventHandler
    public void invClose(InventoryCloseEvent e) {
        if (!e.getInventory().getName().equals("Backpack")) return;
        InventoryHolder ih = e.getInventory().getHolder();
        if (!(ih instanceof CommandSender)) return;
        Player p = (Player) ih;
        if (!CmdBackpack.invs.containsKey(p.getName())) return;
        CmdBackpack.invs.get(p.getName()).setContents(e.getInventory().getContents());
        RUtils.saveHash(CmdBackpack.invs, plugin.getDataFolder() + File.separator + "backpacks.sav");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void vipLogin(PlayerLoginEvent e) {
        if (e.getResult() != Result.KICK_FULL) return;
        Player p = e.getPlayer();
        PConfManager pcm = new PConfManager(p);
        if (!pcm.getConfExists()) return;
        if (p.isBanned()) return;
        if (pcm.get("vip") != null && pcm.getBoolean("vip")) e.allow();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) return;
        Player p = event.getPlayer();
        AFKUtils.setLastMove(p, new Date().getTime());
        if (AFKUtils.isAfk(p)) {
            AFKUtils.unsetAfk(p);
            plugin.getServer().broadcastMessage(RUtils.colorize(RUtils.replaceVars(plugin.returnFormat, p)));
        }
        PConfManager pcm = new PConfManager(p);
        if (pcm.getBoolean("muted")) {
            if (pcm.get("mutetime") != null && !RUtils.isTimeStampValidAddTime(p, "mutetime")) {
                pcm.setBoolean(false, "muted");
                return;
            }
            String howLong = "";
            if (pcm.get("mutetime") != null && pcm.get("mutedat") != null) {
                long mutedAt = pcm.getLong("mutedat");
                long muteTime = pcm.getLong("mutetime") * 1000L;
                howLong = " for " + ChatColor.GRAY + RUtils.formatDateDiff(muteTime + mutedAt).substring(1) + ChatColor.RED;
            }
            p.sendMessage(ChatColor.RED + "You are muted and cannot speak" + howLong + ".");
            plugin.log.info("[RoyalCommands] " + p.getName() + " is muted but tried to say \"" + event.getMessage() + "\"");
            event.setFormat("");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void heMan(AsyncPlayerChatEvent e) {
        if (e.isCancelled()) return;
        if (!e.getMessage().matches("(?i)by the power of gr[ae]yskull!?")) return;
        Player p = e.getPlayer();
        if (!plugin.isAuthorized(p, "rcmds.heman")) return;
        ItemStack is = p.getItemInHand();
        if (is.getType() != Material.DIAMOND_SWORD) return;
        if (is.getEnchantments().isEmpty()) return;
        e.setCancelled(true);
        p.getWorld().strikeLightningEffect(p.getLocation());
        Matcher m = Pattern.compile("(?i)by the power of gr[ae]yskull!?").matcher(e.getMessage());
        StringBuilder sb = new StringBuilder();
        int last = 0;
        while (m.find()) {
            sb.append(e.getMessage().substring(last, m.start()));
            sb.append(m.group(0).toUpperCase());
            last = m.end();
        }
        sb.append(e.getMessage().substring(last));
        plugin.getServer().broadcastMessage(e.getFormat().replaceAll("(?i)by the power of gr[ae]yskull!?", sb.toString()));
        e.setFormat("");
        List<PotionEffect> effects = new ArrayList<PotionEffect>();
        effects.add(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1200, 2));
        effects.add(new PotionEffect(PotionEffectType.REGENERATION, 1200, 2));
        effects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1200, 2));
        effects.add(new PotionEffect(PotionEffectType.SPEED, 1200, 2));
        p.addPotionEffects(effects);
    }

    @EventHandler()
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        PConfManager pcm = new PConfManager(p);
        if (pcm.get("ignoredby") == null) return;
        Set<Player> recpts = e.getRecipients();
        ArrayList<String> ignores = (ArrayList<String>) pcm.getStringList("ignoredby");
        if (ignores == null) return;
        Set<Player> ignore = new HashSet<Player>();
        for (Player pl : recpts)
            for (String ignoree : ignores)
                if (pl.getName().equalsIgnoreCase(ignoree.toLowerCase())) ignore.add(pl);
        e.getRecipients().removeAll(ignore);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void afkWatch(PlayerMoveEvent event) {
        if (event.isCancelled()) return;
        if (!AFKUtils.isAfk(event.getPlayer())) return;
        AFKUtils.unsetAfk(event.getPlayer());
        plugin.getServer().broadcastMessage(RUtils.colorize(RUtils.replaceVars(plugin.returnFormat, event.getPlayer())));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void freezeWatch(PlayerMoveEvent e) {
        if (e.isCancelled()) return;
        if (new PConfManager(e.getPlayer()).getBoolean("frozen")) e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (new PConfManager(event.getPlayer()).getBoolean("jailed"))
            event.setCancelled(true);
        Action act = event.getAction();
        if (act.equals(Action.PHYSICAL)) return;
        ItemStack id = event.getItem();
        if (id == null) return;
        int idn = id.getTypeId();
        if (idn == 0) return;
        List<String> cmds = new PConfManager(event.getPlayer()).getStringList("assign." + idn);
        if (cmds == null) return;
        for (String s : cmds) {
            if (s.toLowerCase().trim().startsWith("c:"))
                event.getPlayer().chat(s.trim().substring(2));
            else event.getPlayer().performCommand(s.trim());
        }
    }

    @EventHandler
    public void onPInt(PlayerInteractEvent event) {
        if (new PConfManager(event.getPlayer()).getBoolean("frozen"))
            event.setCancelled(true);
        if (plugin.buildPerm) if (!plugin.isAuthorized(event.getPlayer(), "rcmds.build"))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH) // run after others
    public void ipBans(PlayerLoginEvent e) {
        String ip = e.getAddress().toString().replace("/", "");
        if (!RUtils.isIPBanned(ip)) return;
        String message = plugin.ipBanFormat;
        message = message.replace("{ip}", ip);
        e.disallow(Result.KICK_BANNED, message);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (RUtils.isIPBanned(event.getAddress().toString().replace("/", ""))) return;
        // Define the player
        Player p = event.getPlayer();
        // If player is null, stop
        if (p == null) return;
        // Create new config manager for player
        PConfManager pcm = new PConfManager(p);
        // Check if player is banned
        if (!p.isBanned()) return;
        // Check to see that they have a bantime, and that if they do, if the timestamp is invalid.
        if (pcm.get("bantime") != null && !RUtils.isTimeStampValid(p, "bantime")) {
            // Set them unbanned
            p.setBanned(false);
            // Allow the event
            event.allow();
            // Stop the method
            return;
        }
        // Get the banreason from the player's userdata file
        String reason = pcm.getString("banreason"); // Returns string or null
        // Check if there was none, and if there wasn't, set it to default ban message.
        if (reason == null) reason = plugin.banMessage;
        String kicker = pcm.getString("banner");
        if (kicker == null) kicker = "Unknown";
        String kickMessage;
        if (pcm.get("bantime") != null) { // if tempban
            kickMessage = RUtils.getMessage(plugin.tempbanFormat, reason, kicker);
            long banTime = pcm.getLong("bantime");
            kickMessage = kickMessage.replace("{length}", RUtils.formatDateDiff(banTime).substring(1));
        } else
            kickMessage = RUtils.getMessage(plugin.banFormat, reason, kicker);
        // Set the kick message to the ban reason
        event.setKickMessage(kickMessage);
        // Disallow the event
        event.disallow(Result.KICK_BANNED, kickMessage);
    }

    @EventHandler
    public void displayNames(PlayerLoginEvent e) {
        if (e.getResult() != Result.ALLOWED) return;
        Player p = e.getPlayer();
        if (p == null) return;
        String dispname = new PConfManager(p).getString("dispname");
        if (dispname == null || dispname.equals("")) dispname = p.getName().trim();
        dispname = dispname.trim();
        if (dispname == null) return;
        p.setDisplayName(dispname);
        if (dispname.length() <= 16) p.setPlayerListName(dispname);
        else p.setPlayerListName(dispname.substring(0, 16));
    }

    @EventHandler()
    public void onPJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (plugin.newVersion == null) return;
        if (!plugin.newVersion.contains(plugin.version) && !plugin.version.contains("pre") && plugin.isAuthorized(p, "rcmds.updates")) {
            String newV = plugin.newVersion.split("RoyalCommands")[1].trim().substring(1);
            p.sendMessage(ChatColor.BLUE + "RoyalCommands " + ChatColor.GRAY + "v" + newV + ChatColor.BLUE + " is out! You are running " + ChatColor.GRAY + "v" + plugin.version + ChatColor.BLUE + ".");
            p.sendMessage(ChatColor.BLUE + "Get the new version at " + ChatColor.DARK_AQUA + "http://dev.bukkit.org/server-mods/royalcommands" + ChatColor.BLUE + ".");
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void showMotd(PlayerJoinEvent e) {
        if (!plugin.motdLogin) return;
        CmdMotd.showMotd(e.getPlayer());
    }

    @EventHandler
    public void welcomeNewPlayers(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (plugin.useWelcome && !p.hasPlayedBefore()) {
            String welcomemessage = plugin.welcomeMessage;
            welcomemessage = welcomemessage.replace("{name}", p.getName());
            welcomemessage = welcomemessage.replace("{dispname}", p.getDisplayName());
            welcomemessage = welcomemessage.replace("{world}", p.getWorld().getName());
            plugin.getServer().broadcastMessage(welcomemessage);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer() == null) return;
        PConfManager pcm = new PConfManager(event.getPlayer());
        if (!pcm.exists() || (plugin.useH2 && pcm.getJSONObject("").length() < 1)) {
            log.info("[RoyalCommands] Creating userdata for " + event.getPlayer().getName() + ".");
            String dispname = event.getPlayer().getDisplayName();
            if (dispname == null || dispname.trim().equals(""))
                dispname = event.getPlayer().getName();
            boolean success = plugin.useH2 || pcm.createFile();
            if (!success)
                log.warning("[RoyalCommands] Userdata file not created. Tell the developer error code 1a.");
            else {
                pcm.setString(event.getPlayer().getName(), "name");
                pcm.setString(dispname, "dispname");
                pcm.setString(event.getPlayer().getAddress().getAddress().toString().replace("/", ""), "ip");
                pcm.setString("", "banreason");
                pcm.setBoolean(true, "allow-tp");
                log.info("[RoyalCommands] Userdata creation finished.");
            }
            if (plugin.stsNew)
                RUtils.silentTeleport(event.getPlayer(), CmdSpawn.getWorldSpawn(event.getPlayer().getWorld()));
        } else {
            log.info("[RoyalCommands] Updating the IP for " + event.getPlayer().getName() + ".");
            String playerip = event.getPlayer().getAddress().getAddress().toString();
            playerip = playerip.replace("/", "");
            pcm.setString(playerip, "ip");
        }
        if (plugin.sendToSpawn) {
            if (plugin.stsBack)
                RUtils.teleport(event.getPlayer(), CmdSpawn.getWorldSpawn(event.getPlayer().getWorld()));
            else
                RUtils.silentTeleport(event.getPlayer(), CmdSpawn.getWorldSpawn(event.getPlayer().getWorld()));
        }
    }

    @EventHandler
    public void silentKicks(PlayerKickEvent e) {
        if (e.isCancelled()) return;
        String reason = e.getReason();
        if (!reason.endsWith("\00-silent")) return;
        e.setLeaveMessage(null);
        e.setReason(reason.replace("\00-silent", ""));
    }

}
