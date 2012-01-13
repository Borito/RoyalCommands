package tk.royalcraf.royalcommands;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class RoyalCommandsBlockListener extends BlockListener {

	public static RoyalCommands plugin;

	public RoyalCommandsBlockListener(RoyalCommands instance) {
		plugin = instance;
	}

	public void onBlockPlace(BlockPlaceEvent event) {
		if (PConfManager.getPValBoolean(event.getPlayer(),
				"frozen")) {
			event.setCancelled(true);
		}
		if (PConfManager.getPValBoolean(event.getPlayer(),
				"jailed")) {
			event.setCancelled(true);
		}
		if (plugin.buildPerm) {
			if (!plugin.isAuthorized(event.getPlayer(), "rcmds.build")) {
				event.setCancelled(true);
			}
		}
	}

	public void onBlockBreak(BlockBreakEvent event) {
		if (PConfManager.getPValBoolean(event.getPlayer(),
				"frozen")) {
			event.setCancelled(true);
		}
		if (PConfManager.getPValBoolean(event.getPlayer(),
				"jailed")) {
			event.setCancelled(true);
		}
		if (plugin.buildPerm) {
			if (!plugin.isAuthorized(event.getPlayer(), "rcmds.build")) {
				event.setCancelled(true);
			}
		}
	}

}
