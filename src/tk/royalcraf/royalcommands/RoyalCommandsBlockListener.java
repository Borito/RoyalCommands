package tk.royalcraf.royalcommands;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

import tk.royalcraf.rcommands.Freeze;

public class RoyalCommandsBlockListener extends BlockListener {
	
	public static RoyalCommands plugin;

	public RoyalCommandsBlockListener(RoyalCommands instance) {
		plugin = instance;
	}
	
	public void onBlockPlace(BlockPlaceEvent event) {
		if (Freeze.freezedb.containsKey(event.getPlayer().getName())) {
			event.setCancelled(true);
		}
	}
	
	public void onBlockBreak(BlockBreakEvent event) {
		if (Freeze.freezedb.containsKey(event.getPlayer().getName())) {
			event.setCancelled(true);
		}
	}

}
