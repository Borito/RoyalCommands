package tk.royalcraf.royalcommands;

/*
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.logging.Logger;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class RoyalCommands extends JavaPlugin {

	private final RoyalCommandsPlayerListener playerListener = new RoyalCommandsPlayerListener(
			this);
	// Saved for possible later use
	// private final RoyalCommandsBlockListener blockListener = new
	// RoyalCommandsBlockListener(
	// this);

	Logger log = Logger.getLogger("Minecraft");

	public void onEnable() {

		PluginManager pm = this.getServer().getPluginManager();

		pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerListener,
				Event.Priority.Normal, this);
		// Saved for possible later use
		// pm.registerEvent(Event.Type.SIGN_CHANGE, blockListener,
		// Event.Priority.Normal, this);

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
