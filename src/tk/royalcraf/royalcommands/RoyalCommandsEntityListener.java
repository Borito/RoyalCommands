package tk.royalcraf.royalcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class RoyalCommandsEntityListener extends EntityListener {

	public static RoyalCommands plugin;

	public RoyalCommandsEntityListener(RoyalCommands instance) {
		plugin = instance;
	}

	public void onEntityDamage(EntityDamageEvent event) {
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) event;
			Entity e = ev.getDamager();
			Entity ed = ev.getEntity();
			if (e instanceof Player) {
				Player p = (Player) e;
				if (PConfManager.getPValBoolean((OfflinePlayer) p, "ohk")) {
					if (ed instanceof LivingEntity) {
						LivingEntity le = (LivingEntity) ed;
						int leh = le.getMaxHealth();
						le.damage(leh + 1);
					}
				}
			}
		}
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			if (PConfManager.getPValBoolean((OfflinePlayer) p, "godmode")) {
				event.setCancelled(true);
			}
		}
	}

	public void onEntityTarget(EntityTargetEvent event) {
		if (event.getTarget() instanceof Player) {
			Player p = (Player) event.getTarget();
			if (plugin.isAuthorized(p, "rcmds.notarget")) {
				event.setCancelled(true);
			}
		}
	}

	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			if (PConfManager.getPValBoolean((OfflinePlayer) p, "godmode")) {
				event.setFoodLevel(20);
			}
		}
	}

}
