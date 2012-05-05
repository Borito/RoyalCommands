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
import org.royaldev.royalcommands.rcommands.CmdBack;

public class RoyalCommandsEntityListener implements Listener {

    public static RoyalCommands plugin;

    public RoyalCommandsEntityListener(RoyalCommands instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDeath(EntityDeathEvent ent) {
        if (!(ent instanceof PlayerDeathEvent)) return;
        if (!plugin.backDeath) return;
        PlayerDeathEvent e = (PlayerDeathEvent) ent;
        if (e.getEntity() == null) return;
        Player p = e.getEntity();
        Location pLoc = p.getLocation();
        CmdBack.backdb.put(p, pLoc);
        p.sendMessage(ChatColor.BLUE + "Type " + ChatColor.GRAY + "/back" + ChatColor.BLUE + " to go back to where you died.");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void oneHitKill(EntityDamageEvent event) {
        if (!(event instanceof EntityDamageByEntityEvent)) return;
        EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) event;
        Entity e = ev.getDamager();
        Entity ed = ev.getEntity();
        if (!(e instanceof Player)) return;
        Player p = (Player) e;
        if (!PConfManager.getPValBoolean(p, "ohk")) return;
        if (ed instanceof LivingEntity) {
            LivingEntity le = (LivingEntity) ed;
            le.damage(le.getHealth() * 1000);
        }
        if (ed instanceof EnderDragonPart) {
            EnderDragonPart ldp = (EnderDragonPart) ed;
            LivingEntity le = ldp.getParent();
            le.damage(le.getHealth() * 1000);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void buddhaMode(EntityDamageEvent e) {
        Entity ent = e.getEntity();
        if (!(ent instanceof Player)) return;
        Player p = (Player) ent;
        if (!PConfManager.getPValBoolean(p, "buddha")) return;
        if (e.getDamage() >= p.getHealth()) e.setDamage(p.getHealth() - 1);
        if (p.getHealth() == 1) e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void godMode(EntityDamageEvent e) {
        Entity ent = e.getEntity();
        if (!(ent instanceof Player)) return;
        Player p = (Player) ent;
        if (PConfManager.getPValBoolean(p, "godmode")) e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityTarget(EntityTargetEvent event) {
        if (!(event.getTarget() instanceof Player)) return;
        Player p = (Player) event.getTarget();
        if (plugin.isAuthorized(p, "rcmds.notarget") && !plugin.isAuthorized(p, "rcmds.exempt.notarget"))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player p = (Player) event.getEntity();
        if (PConfManager.getPValBoolean(p, "godmode")) event.setFoodLevel(20);
    }

}
