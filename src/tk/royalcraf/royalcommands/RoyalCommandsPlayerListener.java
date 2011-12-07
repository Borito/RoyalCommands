package tk.royalcraf.royalcommands;

import java.util.logging.Logger;

import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;

public class RoyalCommandsPlayerListener extends PlayerListener {

	public static RoyalCommands plugin;

	public RoyalCommandsPlayerListener(RoyalCommands instance) {
		plugin = instance;
	}

	Logger log = Logger.getLogger("Minecraft");

	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {

		log.info("[PLAYER_COMMAND] " + event.getPlayer().getName() + ": "
				+ event.getMessage());

	}

}