package org.royaldev.royalcommands.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.royaldev.royalcommands.AFKUtils;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.ConfManager;
import org.royaldev.royalcommands.configuration.PConfManager;
import org.royaldev.royalcommands.rcommands.CmdMotd;
import org.royaldev.royalcommands.rcommands.CmdNameEntity;
import org.royaldev.royalcommands.rcommands.CmdSpawn;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class RoyalCommandsPlayerListener implements Listener {

    public static RoyalCommands plugin;

    public RoyalCommandsPlayerListener(RoyalCommands instance) {
        plugin = instance;
    }

    Logger log = Logger.getLogger("Minecraft");

    public void setCooldown(String command, OfflinePlayer p) {
        ConfigurationSection cmdCds = plugin.getConfig().getConfigurationSection("commands.cooldowns.list");
        if (cmdCds == null) return;
        boolean contains = cmdCds.getKeys(false).contains(command);
        if (Config.cooldownAliases) if (plugin.getCommand(command) != null)
            for (String alias : plugin.getCommand(command).getAliases())
                if (cmdCds.getKeys(false).contains(alias)) {
                    contains = true;
                    break;
                }
        if (contains) {
            long cooldown = cmdCds.getLong(command);
            PConfManager.getPConfManager(p).set("commands.cooldowns.list." + command, new Date().getTime() + (cooldown * 1000));
        }
    }

    public void setTeleCooldown(OfflinePlayer p) {
        double seconds = Config.gTeleCd;
        if (seconds <= 0) return;
        PConfManager.getPConfManager(p).set("teleport_cooldown", (seconds * 1000) + new Date().getTime());
    }

    @EventHandler
    public void deafenMessages(AsyncPlayerChatEvent e) {
        if (e.isCancelled()) return;
        List<Player> toRemove = new ArrayList<Player>();
        for (Player t : e.getRecipients()) {
            PConfManager pcm = PConfManager.getPConfManager(t);
            boolean isDeaf = pcm.getBoolean("deaf", false);
            if (!isDeaf) continue;
            if (e.getPlayer().getName().equals(t.getName())) continue; // don't remove own messages
            toRemove.add(t);
        }
        e.getRecipients().removeAll(toRemove); // remove deaf players
    }

    @EventHandler
    public void worldPerms(PlayerTeleportEvent e) {
        if (e.isCancelled()) return;
        if (!Config.worldAccessPerm) return;
        World from = e.getFrom().getWorld();
        World to = e.getTo().getWorld();
        if (from.equals(to)) return;
        Player p = e.getPlayer();
        if (plugin.ah.isAuthorized(p, "rcmds.worldaccess." + to.getName())) return;
        e.setTo(e.getFrom());
        p.sendMessage(MessageColor.NEGATIVE + "You do not have permission to access the world \"" + RUtils.getMVWorldName(to) + ".\"");
    }

    @EventHandler
    public void lastPosition(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        PConfManager pcm = PConfManager.getPConfManager(p);
        Location l = p.getLocation();
        String lastPos = "lastposition.";
        pcm.set(lastPos + "x", l.getX());
        pcm.set(lastPos + "y", l.getY());
        pcm.set(lastPos + "z", l.getZ());
        pcm.set(lastPos + "pitch", l.getPitch());
        pcm.set(lastPos + "yaw", l.getYaw());
        pcm.set(lastPos + "world", l.getWorld().getName());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void saveData(PlayerQuitEvent e) {
        PConfManager.getPConfManager(e.getPlayer()).forceSave();
    }

    @EventHandler
    public void whitelistMessage(PlayerLoginEvent e) {
        if (!plugin.getServer().hasWhitelist()) return;
        if (e.getResult() != Result.KICK_WHITELIST) return;
        if (!e.getPlayer().isWhitelisted()) e.disallow(Result.KICK_WHITELIST, Config.whitelistMessage);
    }

    @EventHandler
    public void teleWarmup(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Location to = e.getTo();
        Location from = e.getFrom();
        if (to.getX() == from.getX() && to.getY() == from.getY() && to.getZ() == from.getZ()) return;
        PConfManager pcm = PConfManager.getPConfManager(p);
        long l = pcm.getLong("teleport_warmup", -1L);
        int toAdd = Config.teleportWarmup * 1000;
        l = l + toAdd;
        long c = System.currentTimeMillis();
        if (l > c) {
            p.sendMessage(MessageColor.NEGATIVE + "You moved! Teleport cancelled!");
            pcm.set("teleport_warmup", -1L);
        }
    }

    @EventHandler
    public void afk(PlayerMoveEvent e) {
        AFKUtils.setLastMove(e.getPlayer(), new Date().getTime());
    }

    @EventHandler
    public void whitelist(PlayerLoginEvent e) {
        if (!Config.useWhitelist) return;
        if (!Config.whitelist.contains(e.getPlayer().getName()))
            e.disallow(Result.KICK_WHITELIST, Config.whitelistMessage);
    }

    @EventHandler
    public void commandCooldown(PlayerCommandPreprocessEvent e) {
        if (e.isCancelled()) return;
        String command = e.getMessage().split(" ")[0].toLowerCase().substring(1);
        if (plugin.getCommand(command) != null) command = plugin.getCommand(command).getName();
        Player p = e.getPlayer();
        if (plugin.ah.isAuthorized(p, "rcmds.exempt.cooldown.commands")) return;
        long currentcd = PConfManager.getPConfManager(p).getLong("command_cooldowns." + command, -1L);
        if (currentcd > 0L) {
            if (currentcd <= new Date().getTime()) {
                setCooldown(command, p);
                return;
            }
            p.sendMessage(MessageColor.NEGATIVE + "You can't use that command for" + MessageColor.NEUTRAL + RUtils.formatDateDiff(currentcd) + MessageColor.NEGATIVE + ".");
            e.setCancelled(true);
            return;
        }
        setCooldown(command, p);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void teleCooldown(PlayerTeleportEvent e) {
        if (e.isCancelled()) return;
        Player p = e.getPlayer();
        if (plugin.ah.isAuthorized(p, "rcmds.exempt.cooldown.teleports")) return;
        long currentcd = PConfManager.getPConfManager(p).getLong("teleport_cooldown", -1L);
        if (currentcd > 0L) {
            if (currentcd <= new Date().getTime()) {
                setTeleCooldown(p);
                return;
            }
            p.sendMessage(MessageColor.NEGATIVE + "You can't teleport for" + MessageColor.NEUTRAL + RUtils.formatDateDiff(currentcd) + MessageColor.NEGATIVE + ".");
            e.setCancelled(true);
            return;
        }
        setTeleCooldown(p);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTeleport(PlayerTeleportEvent e) {
        if (e.isCancelled()) return;
        if (PConfManager.getPConfManager(e.getPlayer()).getBoolean("jailed")) {
            e.getPlayer().sendMessage(MessageColor.NEGATIVE + "You are jailed and may not teleport.");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        PConfManager.getPConfManager(e.getPlayer()).set("seen", new Date().getTime());
        if (AFKUtils.isAfk(e.getPlayer())) AFKUtils.unsetAfk(e.getPlayer());
        if (AFKUtils.moveTimesContains(e.getPlayer())) AFKUtils.removeLastMove(e.getPlayer());
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        PConfManager.getPConfManager(e.getPlayer()).set("seen", new Date().getTime());
        if (AFKUtils.isAfk(e.getPlayer())) AFKUtils.unsetAfk(e.getPlayer());
        if (AFKUtils.moveTimesContains(e.getPlayer())) AFKUtils.removeLastMove(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) return;
        Player p = event.getPlayer();
        PConfManager pcm = PConfManager.getPConfManager(p);
        if (Config.showcommands) {
            String command = event.getMessage().split(" ")[0].toLowerCase();
            if (!Config.logBlacklist.contains(command.substring(1)))
                log.info("[PLAYER_COMMAND] " + p.getName() + ": " + event.getMessage());
        }
        if (pcm.getBoolean("muted")) {
            if (pcm.get("mutetime") != null && !RUtils.isTimeStampValidAddTime(p, "mutetime", "mutedat"))
                pcm.set("muted", false);
            for (String command : Config.muteCmds) {
                if (!(event.getMessage().toLowerCase().startsWith(command.toLowerCase() + " ") || event.getMessage().equalsIgnoreCase(command.toLowerCase())))
                    continue;
                p.sendMessage(MessageColor.NEGATIVE + "You are muted.");
                log.info("[RoyalCommands] " + p.getName() + " tried to use that command, but is muted.");
                event.setCancelled(true);
            }
        }

        if (pcm.getBoolean("jailed")) {
            p.sendMessage(MessageColor.NEGATIVE + "You are jailed.");
            log.info("[RoyalCommands] " + p.getName() + " tried to use that command, but is jailed.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (!Config.backpackReset) return;
        Player p = e.getEntity();
        Inventory backpack = RUtils.getBackpack(p);
        backpack.clear();
        RUtils.saveBackpack(p, backpack);
    }

    @EventHandler
    public void invClose(InventoryCloseEvent e) {
        if (e.getInventory() == null || e.getInventory().getName() == null) return; // modpacks
        if (!e.getInventory().getName().equals("Backpack")) return;
        InventoryHolder ih = e.getInventory().getHolder();
        if (!(ih instanceof CommandSender)) return;
        Player p = (Player) ih;
        Inventory backpack = RUtils.getBackpack(p);
        backpack.setContents(e.getInventory().getContents());
        RUtils.saveBackpack(p, backpack);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void vipLogin(PlayerLoginEvent e) {
        if (e.getResult() != Result.KICK_FULL) return;
        Player p = e.getPlayer();
        PConfManager pcm = PConfManager.getPConfManager(p);
        if (p.isBanned()) return;
        if (pcm.getBoolean("vip", false)) e.allow();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) return;
        Player p = event.getPlayer();
        AFKUtils.setLastMove(p, new Date().getTime());
        if (AFKUtils.isAfk(p)) {
            AFKUtils.unsetAfk(p);
            plugin.getServer().broadcastMessage(RUtils.colorize(RUtils.replaceVars(Config.returnFormat, p)));
        }
        PConfManager pcm = PConfManager.getPConfManager(p);
        if (pcm.getBoolean("muted", false)) {
            if (pcm.get("mutetime") != null && !RUtils.isTimeStampValidAddTime(p, "mutetime", "mutedat")) {
                pcm.set("muted", false);
                return;
            }
            String howLong = "";
            if (pcm.get("mutetime") != null && pcm.get("mutedat") != null) {
                long mutedAt = pcm.getLong("mutedat");
                long muteTime = pcm.getLong("mutetime") * 1000L;
                howLong = " for " + MessageColor.NEUTRAL + RUtils.formatDateDiff(muteTime + mutedAt).substring(1) + MessageColor.NEGATIVE;
            }
            p.sendMessage(MessageColor.NEGATIVE + "You are muted and cannot speak" + howLong + ".");
            plugin.log.info("[RoyalCommands] " + p.getName() + " is muted but tried to say \"" + event.getMessage() + "\"");
            event.setFormat("");
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void heMan(AsyncPlayerChatEvent e) {
        if (e.isCancelled()) return;
        if (!e.getMessage().matches("(?i)by the power of gr[ae]yskull!?")) return;
        Player p = e.getPlayer();
        if (!plugin.ah.isAuthorized(p, "rcmds.heman")) return;
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
        PConfManager pcm = PConfManager.getPConfManager(p);
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
        Location to = event.getTo();
        Location from = event.getFrom();
        if (to.getX() == from.getX() && to.getY() == from.getY() && to.getZ() == from.getZ()) return;
        AFKUtils.unsetAfk(event.getPlayer());
        plugin.getServer().broadcastMessage(RUtils.colorize(RUtils.replaceVars(Config.returnFormat, event.getPlayer())));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void freezeWatch(PlayerMoveEvent e) {
        if (e.isCancelled()) return;
        if (PConfManager.getPConfManager(e.getPlayer()).getBoolean("frozen")) e.setTo(e.getFrom());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void assign(PlayerInteractEvent event) {
        //if (event.isCancelled()) return;
        if (PConfManager.getPConfManager(event.getPlayer()).getBoolean("jailed")) event.setCancelled(true);
        Action act = event.getAction();
        if (act.equals(Action.PHYSICAL)) return;
        ItemStack id = event.getItem();
        if (id == null) return;
        final List<String> cmds = new ArrayList<String>();
        final List<String> personalAssigns = RUtils.getAssignment(id, PConfManager.getPConfManager(event.getPlayer()));
        final List<String> publicAssigns = RUtils.getAssignment(id, ConfManager.getConfManager("publicassignments.yml"));
        if (personalAssigns != null) cmds.addAll(personalAssigns);
        if (publicAssigns != null) cmds.addAll(publicAssigns);
        if (cmds.isEmpty()) return;
        for (String s : cmds) {
            if (s.toLowerCase().trim().startsWith("c:"))
                event.getPlayer().chat(s.trim().substring(2));
            else {
                event.getPlayer().performCommand(s.trim());
                if (Config.showcommands) {
                    String[] parts = s.split(" ");
                    if (parts.length > 0) {
                        String command = parts[0].toLowerCase();
                        if (!Config.logBlacklist.contains(command.substring(1)))
                            log.info("[PLAYER_COMMAND] " + event.getPlayer().getName() + ": /" + s);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onAssignInteractPlayer(PlayerInteractEntityEvent e) {
        //if (e.isCancelled()) return;
        if (PConfManager.getPConfManager(e.getPlayer()).getBoolean("jailed")) e.setCancelled(true);
        ItemStack id = e.getPlayer().getItemInHand();
        if (id == null) return;
        final List<String> cmds = new ArrayList<String>();
        final List<String> personalAssigns = RUtils.getAssignment(id, PConfManager.getPConfManager(e.getPlayer()));
        final List<String> publicAssigns = RUtils.getAssignment(id, ConfManager.getConfManager("publicassignments.yml"));
        if (personalAssigns != null) cmds.addAll(personalAssigns);
        if (publicAssigns != null) cmds.addAll(publicAssigns);
        if (cmds.isEmpty()) return;
        Player clicked = null;
        if (e.getRightClicked() instanceof Player) clicked = (Player) e.getRightClicked();
        for (String s : cmds) {
            if (clicked != null) s = s.replace("{player}", clicked.getName());
            if (s.toLowerCase().trim().startsWith("c:"))
                e.getPlayer().chat(s.trim().substring(2));
            else {
                e.getPlayer().performCommand(s.trim());
                if (Config.showcommands) {
                    String[] parts = s.split(" ");
                    if (parts.length > 0) {
                        String command = parts[0].toLowerCase();
                        if (!Config.logBlacklist.contains(command.substring(1)))
                            log.info("[PLAYER_COMMAND] " + e.getPlayer().getName() + ": /" + s);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onAssignHitPlayer(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;
        Player p = (Player) e.getDamager();
        onAssignInteractPlayer(new PlayerInteractEntityEvent(p, e.getEntity()));
    }

    @EventHandler
    public void onPInt(PlayerInteractEvent event) {
        if (PConfManager.getPConfManager(event.getPlayer()).getBoolean("frozen")) event.setCancelled(true);
        if (Config.buildPerm && !plugin.ah.isAuthorized(event.getPlayer(), "rcmds.build")) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH) // run after others
    public void ipBans(PlayerLoginEvent e) {
        String ip = e.getAddress().toString().replace("/", "");
        if (!RUtils.isIPBanned(ip)) return;
        String message = Config.ipBanFormat;
        message = message.replace("{ip}", ip);
        e.disallow(Result.KICK_BANNED, RUtils.colorize(message));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (RUtils.isIPBanned(event.getAddress().toString().replace("/", ""))) return;
        // Define the player
        Player p = event.getPlayer();
        // If player is null, stop
        if (p == null) return;
        // Create new config manager for player
        PConfManager pcm = PConfManager.getPConfManager(p);
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
        if (reason == null) reason = Config.banMessage;
        String kicker = pcm.getString("banner");
        if (kicker == null) kicker = "Unknown";
        String kickMessage;
        if (pcm.get("bantime") != null) { // if tempban
            kickMessage = RUtils.getMessage(Config.tempbanFormat, reason, kicker);
            long banTime = pcm.getLong("bantime");
            kickMessage = kickMessage.replace("{length}", RUtils.formatDateDiff(banTime).substring(1));
        } else
            kickMessage = RUtils.getMessage(Config.banFormat, reason, kicker);
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
        String dispname = PConfManager.getPConfManager(p).getString("dispname");
        if (dispname == null || dispname.equals("")) dispname = p.getName();
        dispname = dispname.trim();
        p.setDisplayName(dispname);
        if (dispname.length() <= 16) p.setPlayerListName(dispname);
        else p.setPlayerListName(dispname.substring(0, 16));
    }

    @EventHandler()
    public void onPJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (plugin.newVersion == null) return;
        if (!plugin.newVersion.contains(plugin.version) && !plugin.version.contains("-SNAPSHOT") && plugin.ah.isAuthorized(p, "rcmds.updates")) {
            String newV = plugin.newVersion.split("RoyalCommands")[1].trim().substring(1);
            p.sendMessage(MessageColor.POSITIVE + "RoyalCommands " + MessageColor.NEUTRAL + "v" + newV + MessageColor.POSITIVE + " is out! You are running " + MessageColor.NEUTRAL + "v" + plugin.version + MessageColor.POSITIVE + ".");
            p.sendMessage(MessageColor.POSITIVE + "Get the new version at " + ChatColor.DARK_AQUA + "http://dev.bukkit.org/server-mods/royalcommands" + MessageColor.POSITIVE + ".");
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void showMotd(PlayerJoinEvent e) {
        if (!Config.motdLogin) return;
        CmdMotd.showMotd(e.getPlayer());
    }

    @EventHandler
    public void welcomeNewPlayers(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (Config.useWelcome && !p.hasPlayedBefore()) {
            String welcomemessage = Config.welcomeMessage;
            welcomemessage = welcomemessage.replace("{name}", p.getName());
            welcomemessage = welcomemessage.replace("{dispname}", p.getDisplayName());
            welcomemessage = welcomemessage.replace("{world}", p.getWorld().getName());
            plugin.getServer().broadcastMessage(welcomemessage);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer() == null) return;
        final PConfManager pcm = PConfManager.getPConfManager(event.getPlayer());
        if (pcm.isFirstJoin()) {
            String dispname = event.getPlayer().getDisplayName();
            if (dispname == null || dispname.trim().equals("")) dispname = event.getPlayer().getName();
            pcm.set(event.getPlayer().getName(), "name");
            pcm.set("dispname", dispname);
            pcm.set("ip", event.getPlayer().getAddress().getAddress().toString().replace("/", ""));
            pcm.set("banreason", "");
            pcm.set("allow-tp", true);
            if (Config.stsNew)
                RUtils.silentTeleport(event.getPlayer(), CmdSpawn.getWorldSpawn(event.getPlayer().getWorld()));
            pcm.setFirstJoin(false);
        } else {
            log.info("[RoyalCommands] Updating the IP for " + event.getPlayer().getName() + ".");
            String playerip = event.getPlayer().getAddress().getAddress().toString();
            playerip = playerip.replace("/", "");
            pcm.set("ip", playerip);
        }
        if (Config.sendToSpawn) {
            if (Config.stsBack)
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

    @EventHandler
    public void leRespawn(PlayerRespawnEvent e) {
        if (!Config.overrideRespawn) return;
        e.setRespawnLocation(CmdSpawn.getWorldSpawn(e.getPlayer().getWorld()));
    }

    @EventHandler
    public void renameEntity(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        if (!CmdNameEntity.isNaming(p)) return;
        Entity rightClicked = e.getRightClicked();
        if (rightClicked instanceof Player) return;
        if (!(rightClicked instanceof LivingEntity)) return;
        LivingEntity le = (LivingEntity) rightClicked;
        String newName = CmdNameEntity.getNamingName(p);
        if (newName == null) return;
        le.setCustomName(newName);
        le.setCustomNameVisible(!newName.isEmpty());
        CmdNameEntity.cancelNaming(p);
        if (newName.isEmpty())
            p.sendMessage(MessageColor.POSITIVE + "Successfully removed the name from that " + MessageColor.NEUTRAL + le.getType().name().toLowerCase().replace("_", " ") + MessageColor.POSITIVE + ".");
        else
            p.sendMessage(MessageColor.POSITIVE + "Successfully renamed that " + MessageColor.NEUTRAL + le.getType().name().toLowerCase().replace("_", " ") + MessageColor.POSITIVE + " to " + MessageColor.NEUTRAL + newName + MessageColor.POSITIVE + ".");
    }

}
