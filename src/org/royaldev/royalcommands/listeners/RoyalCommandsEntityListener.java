package org.royaldev.royalcommands.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EnderDragonPart;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.Back;

public class RoyalCommandsEntityListener implements Listener {

    public static RoyalCommands plugin;

    public RoyalCommandsEntityListener(RoyalCommands instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDeath(EntityDeathEvent ent) {
        if (ent instanceof PlayerDeathEvent) {
            if (plugin.backDeath) {
                PlayerDeathEvent e = (PlayerDeathEvent) ent;
                if (!(e.getEntity() instanceof Player)) return;
                Player p = (Player) e.getEntity();
                Location pLoc = p.getLocation();
                Back.backdb.put(p, pLoc);
                p.sendMessage(ChatColor.BLUE + "Type " + ChatColor.GRAY + "/back" + ChatColor.BLUE + " to go back to where you died.");
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) event;
            Entity e = ev.getDamager();
            Entity ed = ev.getEntity();
            if (e instanceof Player) {
                Player p = (Player) e;
                if (PConfManager.getPValBoolean(p, "ohk")) {
                    if (ed instanceof LivingEntity) {
                        LivingEntity le = (LivingEntity) ed;
                        int leh = le.getMaxHealth();
                        le.damage(leh + 1);
                    }
                    if (ed instanceof EnderDragonPart) {
                        EnderDragonPart ldp = (EnderDragonPart) ed;
                        LivingEntity le = ldp.getParent();
                        int leh = le.getMaxHealth();
                        le.damage(leh + 1);
                    }
                }
            }
        }
        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            if (PConfManager.getPValBoolean(p, "godmode")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityTarget(EntityTargetEvent event) {
        if (event.getTarget() instanceof Player) {
            Player p = (Player) event.getTarget();
            if (plugin.isAuthorized(p, "rcmds.notarget")) event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            if (PConfManager.getPValBoolean(p, "godmode")) event.setFoodLevel(20);
        }
    }

}
