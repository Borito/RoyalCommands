package tk.royalcraf.royalcommands;

import java.util.logging.Logger;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class RoyalCommands extends JavaPlugin {

	private final RoyalCommandsPlayerListener playerListener = new RoyalCommandsPlayerListener(
			this);
	private final RoyalCommandsBlockListener blockListener = new RoyalCommandsBlockListener(
			this);

	Logger log = Logger.getLogger("Minecraft");

	public void onEnable() {

		PluginManager pm = this.getServer().getPluginManager();

		pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerListener,
				Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.SIGN_CHANGE, blockListener,
				Event.Priority.Normal, this);

		RoyalCommandsCommandExecutor cmdExec = new RoyalCommandsCommandExecutor(
				this);
		getCommand("level").setExecutor(cmdExec);
		getCommand("setlevel").setExecutor(cmdExec);
		getCommand("sci").setExecutor(cmdExec);
		getCommand("speak").setExecutor(cmdExec);
		getCommand("facepalm").setExecutor(cmdExec);
		getCommand("slap").setExecutor(cmdExec);
		getCommand("harm").setExecutor(cmdExec);
		getCommand("starve").setExecutor(cmdExec);
		getCommand("banned").setExecutor(cmdExec);

		log.info("[RoyalCommands] RoyalCommands v0.0.1 initiated.");
	}

	public void onDisable() {
		log.info("[RoyalCommands] RoyalCommands v0.0.1 disabled.");
	}

}
