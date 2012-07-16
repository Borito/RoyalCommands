package org.royaldev.royalcommands.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.royaldev.royalcommands.AFKUtils;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.CmdMotd;
import org.royaldev.royalcommands.rcommands.CmdSpawn;

import java.io.File;
import java.util.*;
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
        ConfigurationSection cmdCds = plugin.getConfig().getConfigurationSection("command_cooldowns");
        if (cmdCds == null) return;
        boolean contains = cmdCds.getKeys(false).contains(command);
        if (plugin.cooldownAliases)
            if (plugin.getCommand(command) != null)
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
    public void afk(PlayerMoveEvent e) {
        AFKUtils.setLastMove(e.getPlayer(), new Date().getTime());
    }

    @EventHandler
    public void whitelist(PlayerLoginEvent e) {
        if (!plugin.useWhitelist) return;
        if (!plugin.whitelist.contains(e.getPlayer().getName()))
            e.disallow(Result.KICK_WHITELIST, "You are not whitelisted on this server!");
    }

    @EventHandler
    public void commandCooldown(PlayerCommandPreprocessEvent e) {
        if (e.isCancelled()) return;
        String command = e.getMessage().split(" ")[0].toLowerCase().substring(1);
        if (plugin.getCommand(command) != null) command = plugin.getCommand(command).getName();
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
        /*if (CmdBack.backdb.containsKey(e.getPlayer()))
            if (CmdBack.backdb.get(e.getPlayer()).equals(e.getFrom())) return;
        CmdBack.backdb.put(e.getPlayer(), e.getFrom());*/
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        new PConfManager(e.getPlayer()).setLong(new Date().getTime(), "seen");
        if (AFKUtils.isAfk(e.getPlayer())) AFKUtils.unsetAfk(e.getPlayer());
        if (AFKUtils.moveTimesContains(e.getPlayer())) AFKUtils.removeLastMove(e.getPlayer());
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        new PConfManager(e.getPlayer()).setLong(new Date().getTime(), "seen");
        if (AFKUtils.isAfk(e.getPlayer())) AFKUtils.unsetAfk(e.getPlayer());
        if (AFKUtils.moveTimesContains(e.getPlayer())) AFKUtils.removeLastMove(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) return;
        Player p = event.getPlayer();
        PConfManager pcm = new PConfManager(p);
        if (plugin.showcommands) {
            String command = event.getMessage().split(" ")[0].toLowerCase();
            if (!plugin.logBlacklist.contains(command))
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
    public void onPlayerChat(PlayerChatEvent event) {
        if (event.isCancelled()) return;
        Player p = event.getPlayer();
        AFKUtils.setLastMove(p, new Date().getTime());
        if (AFKUtils.isAfk(p)) {
            AFKUtils.unsetAfk(p);
            plugin.getServer().broadcastMessage(p.getName() + " is no longer AFK.");
        }
        PConfManager pcm = new PConfManager(p);
        if (pcm.getBoolean("muted")) {
            if (pcm.get("mutetime") != null && !RUtils.isTimeStampValid(p, "mutetime"))
                pcm.setBoolean(false, "muted");
            event.setFormat("");
            event.setCancelled(true);
            p.sendMessage(ChatColor.RED + "You are muted.");
            plugin.log.info("[RoyalCommands] " + p.getName() + " tried to speak, but has been muted.");
        }
    }

    @EventHandler
    public void HeMan(PlayerChatEvent e) {
        if (e.isCancelled()) return;
        if (!e.getMessage().matches("(?i)by the power of gr[a|e]yskull!?")) return;
        Player p = e.getPlayer();
        if (!plugin.isAuthorized(p, "rcmds.heman")) return;
        ItemStack is = p.getItemInHand();
        if (is.getType() != Material.DIAMOND_SWORD) return;
        if (is.getEnchantments().isEmpty()) return;
        e.setCancelled(true);
        p.getWorld().strikeLightningEffect(p.getLocation());
        Matcher m = Pattern.compile("(?i)by the power of gr[a|e]yskull!?").matcher(e.getMessage());
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
    public void onChat(PlayerChatEvent e) {
        Player p = e.getPlayer();
        PConfManager pcm = new PConfManager(p);
        if (pcm.get("ignoredby") == null) return;
        Set<Player> recpts = e.getRecipients();
        ArrayList<String> ignores = (ArrayList<String>) pcm.getStringList("ignoredby");
        Set<Player> ignore = new HashSet<Player>();
        for (Player pl : recpts)
            for (String ignoree : ignores) if (pl.getName().equalsIgnoreCase(ignoree.toLowerCase())) ignore.add(pl);
        e.getRecipients().removeAll(ignore);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.isCancelled()) return;
        if (AFKUtils.isAfk(event.getPlayer())) {
            AFKUtils.unsetAfk(event.getPlayer());
            plugin.getServer().broadcastMessage(event.getPlayer().getName() + " is no longer AFK.");
            return;
        }
        if (new PConfManager(event.getPlayer()).getBoolean("frozen")) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (new PConfManager(event.getPlayer()).getBoolean("jailed")) event.setCancelled(true);
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
            else
                event.getPlayer().performCommand(s.trim());
        }
    }

    @EventHandler
    public void onPInt(PlayerInteractEvent event) {
        if (new PConfManager(event.getPlayer()).getBoolean("frozen")) event.setCancelled(true);
        if (plugin.buildPerm) if (!plugin.isAuthorized(event.getPlayer(), "rcmds.build")) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (event.getResult() != Result.ALLOWED) return;
        // Define the player
        Player p = event.getPlayer();
        PConfManager pcm = new PConfManager(p);
        if (p == null) return;
        // Check if player is banned
        if (!p.isBanned()) return;
        // Check to see that they have a bantime, and that if they do, if the timestamp is invalid.
        if (pcm.get("bantime") != null && !RUtils.isTimeStampValid(p, "bantime")) {
            // Set them unbanned
            p.setBanned(false);
            // Stop the method
            return;
        }
        // Get the banreason from the player's userdata file
        String kickMessage = pcm.getString("banreason"); // Returns string or null
        // Check if there was none, and if there wasn't, set it to default ban message.
        if (kickMessage == null) kickMessage = plugin.banMessage;
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
    public void onPlayerJoin(PlayerJoinEvent event) {
        File datafile = new File(plugin.getDataFolder() + File.separator + "userdata" + File.separator + event.getPlayer().getName().toLowerCase() + ".yml");
        if (!datafile.exists()) {
            log.info("[RoyalCommands] Creating userdata for user " + event.getPlayer().getName() + ".");
            try {
                boolean created = datafile.createNewFile();
                if (!created)
                    log.warning("[RoyalCommands] Userdata file not created. Tell the developer error code 1a.");
                FileConfiguration out = YamlConfiguration.loadConfiguration(datafile);
                out.set("name", event.getPlayer().getName());
                String dispname = event.getPlayer().getDisplayName();
                if (dispname == null || dispname.equals("")) dispname = event.getPlayer().getName();
                out.set("dispname", dispname);
                out.set("ip", event.getPlayer().getAddress().getAddress().toString().replace("/", ""));
                out.set("banreason", "");
                out.set("allow-tp", true);
                out.save(datafile);
                log.info("[RoyalCommands] Userdata creation finished.");
            } catch (Exception e) {
                log.severe("[RoyalCommands] Could not create userdata for user " + event.getPlayer().getName() + "!");
                e.printStackTrace();
            }
            if (plugin.useWelcome) {
                String welcomemessage = plugin.welcomeMessage;
                welcomemessage = welcomemessage.replace("{name}", event.getPlayer().getName());
                String dispname = event.getPlayer().getDisplayName();
                if (dispname == null || dispname.equals("")) dispname = event.getPlayer().getName();
                welcomemessage = welcomemessage.replace("{dispname}", dispname);
                welcomemessage = welcomemessage.replace("{world}", event.getPlayer().getWorld().getName());
                plugin.getServer().broadcastMessage(welcomemessage);
            }
            if (plugin.stsNew)
                RUtils.silentTeleport(event.getPlayer(), CmdSpawn.getWorldSpawn(event.getPlayer().getWorld()));
        } else {
            log.info("[RoyalCommands] Updating the IP for " + event.getPlayer().getName() + ".");
            String playerip = event.getPlayer().getAddress().getAddress().toString();
            playerip = playerip.replace("/", "");
            new PConfManager(event.getPlayer()).setString(playerip, "ip");
        }
        if (plugin.motdLogin) CmdMotd.showMotd(event.getPlayer());
        if (plugin.sendToSpawn) {
            if (plugin.stsBack)
                RUtils.teleport(event.getPlayer(), CmdSpawn.getWorldSpawn(event.getPlayer().getWorld()));
            else RUtils.silentTeleport(event.getPlayer(), CmdSpawn.getWorldSpawn(event.getPlayer().getWorld()));
        }
    }
}
