package org.royaldev.royalcommands.listeners;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
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
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.Afk;
import org.royaldev.royalcommands.rcommands.Back;
import org.royaldev.royalcommands.rcommands.Motd;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;

public class RoyalCommandsPlayerListener implements Listener {

    public static RoyalCommands plugin;

    public RoyalCommandsPlayerListener(RoyalCommands instance) {
        plugin = instance;
    }

    Logger log = Logger.getLogger("Minecraft");

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        if (e.isCancelled()) return;
        if (PConfManager.getPValBoolean(e.getPlayer(), "jailed")) {
            e.getPlayer().sendMessage(ChatColor.RED + "You are jailed and may not teleport.");
            e.setCancelled(true);
            return;
        }
        if (plugin.tpEvery) {
            if (Back.backdb.containsKey(e.getPlayer())) {
                if (e.getTo().equals(Back.backdb.get(e.getPlayer()))) return;
            }
            Back.backdb.put(e.getPlayer(), e.getFrom());
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        if (plugin.tpEvery) {
            Back.backdb.put(e.getPlayer(), e.getPlayer().getLocation());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        PConfManager.setPValLong(e.getPlayer(), new Date().getTime(), "seen");
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        PConfManager.setPValLong(e.getPlayer(), new Date().getTime(), "seen");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) return;
        if (plugin.showcommands) {
            log.info("[PLAYER_COMMAND] " + event.getPlayer().getName() + ": " + event.getMessage());
        }
        if (PConfManager.getPValBoolean(event.getPlayer(), "muted")) {
            if (!RUtils.isTimeStampValid(event.getPlayer(), "mutetime")) {
                PConfManager.setPValBoolean(event.getPlayer(), false, "muted");
            }
            for (String command : plugin.muteCmds) {
                if (!(event.getMessage().toLowerCase().startsWith(command.toLowerCase() + " ") || event.getMessage().equalsIgnoreCase(command.toLowerCase())))
                    continue;
                event.getPlayer().sendMessage(ChatColor.RED + "You are muted.");
                log.info("[RoyalCommands] " + event.getPlayer().getName() + " tried to use that command, but is muted.");
                event.setCancelled(true);
            }
        }

        if (PConfManager.getPValBoolean(event.getPlayer(), "jailed")) {
            event.getPlayer().sendMessage(ChatColor.RED + "You are jailed.");
            log.info("[RoyalCommands] " + event.getPlayer().getName() + " tried to use that command, but is jailed.");
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPLogin(PlayerLoginEvent e) {
        if (e.getResult() != Result.KICK_FULL) return;
        if (!PConfManager.getPConfExists(e.getPlayer())) return;
        if (PConfManager.getPVal(e.getPlayer(), "vip") != null && PConfManager.getPValBoolean(e.getPlayer(), "vip"))
            e.allow();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerChat(PlayerChatEvent event) {
        if (event.isCancelled()) return;
        if (Afk.afkdb.containsKey(event.getPlayer())) {
            plugin.getServer().broadcastMessage(event.getPlayer().getName() + " is no longer AFK.");
            Afk.afkdb.remove(event.getPlayer());
        }
        if (PConfManager.getPValBoolean(event.getPlayer(), "muted")) {
            if (!RUtils.isTimeStampValid(event.getPlayer(), "mutetime")) {
                PConfManager.setPValBoolean(event.getPlayer(), false, "muted");
            }
            event.setFormat("");
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "You are muted.");
            plugin.log.info("[RoyalCommands] " + event.getPlayer().getName() + " tried to speak, but has been muted.");
        }
    }

    @EventHandler()
    public void onChat(PlayerChatEvent e) {
        if (PConfManager.getPVal(e.getPlayer(), "ignoredby") == null) return;
        Set<Player> recpts = e.getRecipients();
        ArrayList<String> ignores = (ArrayList<String>) PConfManager.getPValStringList(e.getPlayer(), "ignoredby");
        Set<Player> ignore = new HashSet<Player>();
        for (Player p : recpts) {
            for (String ignoree : ignores) {
                if (p.getName().equalsIgnoreCase(ignoree.toLowerCase())) ignore.add(p);
            }
        }
        e.getRecipients().removeAll(ignore);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.isCancelled()) return;
        if (Afk.afkdb.containsKey(event.getPlayer())) {
            plugin.getServer().broadcastMessage(event.getPlayer().getName() + " is no longer AFK.");
            Afk.afkdb.remove(event.getPlayer());
        }
        if (PConfManager.getPValBoolean(event.getPlayer(), "frozen")) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        //if (event.isCancelled()) return; <- breaks everything
        if (PConfManager.getPValBoolean(event.getPlayer(), "jailed")) {
            event.setCancelled(true);
        }
        Action act = event.getAction();
        if (act.equals(Action.LEFT_CLICK_AIR) || act.equals(Action.RIGHT_CLICK_AIR) || act.equals(Action.LEFT_CLICK_BLOCK) || act.equals(Action.RIGHT_CLICK_BLOCK)) {
            ItemStack id = event.getItem();
            if (id != null) {
                int idn = id.getTypeId();
                if (idn != 0) {
                    List<String> cmds = PConfManager.getPValStringList(event.getPlayer(), "assign." + idn);
                    if (cmds != null) {
                        for (String s : cmds) {
                            if (s.toLowerCase().trim().startsWith("c:")) {
                                event.getPlayer().chat(s.trim().substring(2));
                            } else {
                                event.getPlayer().performCommand(s.trim());
                            }
                        }
                    }
                }
            }
        }
        if (PConfManager.getPValBoolean(event.getPlayer(), "frozen")) {
            event.setCancelled(true);
        }
        if (plugin.buildPerm) {
            if (!plugin.isAuthorized(event.getPlayer(), "rcmds.build")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (event.getPlayer().isBanned()) {
            if (PConfManager.getPVal(event.getPlayer(), "bantime") != null) {
                if (!RUtils.isTimeStampValid(event.getPlayer(), "bantime")) {
                    event.allow();
                    event.getPlayer().setBanned(false);
                }
            }
            String kickMessage;
            OfflinePlayer oplayer = plugin.getServer().getOfflinePlayer(event.getPlayer().getName());
            File oplayerconfl = new File(plugin.getDataFolder() + File.separator + "userdata" + File.separator + oplayer.getName() + ".yml");
            if (oplayerconfl.exists()) {
                FileConfiguration oplayerconf = YamlConfiguration.loadConfiguration(oplayerconfl);
                kickMessage = oplayerconf.getString("banreason");
            } else {
                kickMessage = plugin.banMessage;
            }
            event.setKickMessage(kickMessage);
            event.disallow(Result.KICK_BANNED, kickMessage);
            return;
        }
        Player p = event.getPlayer();
        String dispname = PConfManager.getPValString(p, "dispname").trim();
        if (dispname == null || dispname.equals("")) {
            dispname = p.getName().trim();
        }
        p.setDisplayName(dispname);
    }

    @EventHandler()
    public void onPJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!plugin.newVersion.contains(plugin.version) && plugin.isAuthorized(p, "rcmds.updates")) {
            String newV = plugin.newVersion.split("RoyalCommands")[1].trim();
            p.sendMessage(ChatColor.BLUE + "RoyalCommands " + ChatColor.GRAY + newV + ChatColor.BLUE + " is out! You are running " + ChatColor.GRAY + "v" + plugin.version + ChatColor.BLUE + ".");
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        File datafile = new File(plugin.getDataFolder() + File.separator + "userdata" + File.separator + event.getPlayer().getName().toLowerCase() + ".yml");
        if (!datafile.exists()) {
            log.info("[RoyalCommands] Creating userdata for user " + event.getPlayer().getName() + ".");
            try {
                FileConfiguration out = YamlConfiguration.loadConfiguration(datafile);
                out.set("name", event.getPlayer().getName());
                String dispname = event.getPlayer().getDisplayName();
                if (dispname == null || dispname.equals("")) {
                    dispname = event.getPlayer().getName();
                }
                out.set("dispname", dispname);
                out.set("ip", event.getPlayer().getAddress().getAddress().toString().replace("/", ""));
                out.set("banreason", "");
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
                if (dispname == null || dispname.equals("")) {
                    dispname = event.getPlayer().getName();
                }
                welcomemessage = welcomemessage.replace("{dispname}", dispname);
                welcomemessage = welcomemessage.replace("{world}", event.getPlayer().getWorld().getName());
                plugin.getServer().broadcastMessage(welcomemessage);
            }
            if (plugin.stsNew) {
                event.getPlayer().teleport(event.getPlayer().getWorld().getSpawnLocation());
            }
        } else {
            log.info("[RoyalCommands] Updating the IP for " + event.getPlayer().getName() + ".");
            String playerip = event.getPlayer().getAddress().getAddress().toString();
            playerip = playerip.replace("/", "");
            PConfManager.setPValString(event.getPlayer(), playerip, "ip");
        }
        if (plugin.motdLogin) {
            Motd.showMotd(event.getPlayer());
        }
        if (plugin.sendToSpawn) {
            if (plugin.stsBack) Back.backdb.put(event.getPlayer(), event.getPlayer().getLocation());
            event.getPlayer().teleport(event.getPlayer().getWorld().getSpawnLocation());
        }
    }
}