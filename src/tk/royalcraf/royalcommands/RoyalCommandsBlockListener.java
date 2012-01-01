package tk.royalcraf.royalcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class RoyalCommandsBlockListener extends BlockListener {

	public static RoyalCommands plugin;

	public RoyalCommandsBlockListener(RoyalCommands instance) {
		plugin = instance;
	}

	public void onBlockPlace(BlockPlaceEvent event) {
		if (PConfManager.getPValBoolean((OfflinePlayer) event.getPlayer(), "frozen")) {
			event.setCancelled(true);
		}
	}

	public void onBlockBreak(BlockBreakEvent event) {
		if (PConfManager.getPValBoolean((OfflinePlayer) event.getPlayer(), "frozen")) {
			event.setCancelled(true);
		}
	}

}
