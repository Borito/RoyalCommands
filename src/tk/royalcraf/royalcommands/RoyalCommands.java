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

 This plugin was written by jkcclemens <jkc.clemens@gmail.com>.
 If forked and not credited, alert him.
 */

import java.io.File;
import java.util.logging.Logger;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class RoyalCommands extends JavaPlugin {

	public static Permission permission = null;

	public Boolean setupPermissions() {
		RegisteredServiceProvider<Permission> permissionProvider = getServer()
				.getServicesManager().getRegistration(
						net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			permission = permissionProvider.getProvider();
		}
		return (permission != null);
	}

	protected FileConfiguration config;

	private final RoyalCommandsPlayerListener playerListener = new RoyalCommandsPlayerListener(
			this);

	Logger log = Logger.getLogger("Minecraft");

	public void loadConfiguration() {
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		File file = new File(this.getDataFolder() + "/userdata/");
		boolean exists = file.exists();
		if (!exists) {
			try {
				boolean success = new File(this.getDataFolder() + "/userdata")
						.mkdir();
				if (success) {
					log.info("[RoyalCommands] Created userdata directory.");
				}
			} catch (Exception e) {
				log.severe("[RoyalCommands] Failed to make userdata directory!");
				log.severe(e.getMessage());
			}
		}
	}

	public void onEnable() {

		loadConfiguration();

		PluginManager pm = this.getServer().getPluginManager();

		pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerListener,
				Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener,
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
		getCommand("setarmor").setExecutor(cmdExec);
		getCommand("getip").setExecutor(cmdExec);
		getCommand("compareip").setExecutor(cmdExec);
		getCommand("rcmds").setExecutor(cmdExec);

		log.info("[RoyalCommands] RoyalCommands v0.0.4 initiated.");
	}

	public void onDisable() {
		log.info("[RoyalCommands] RoyalCommands v0.0.4 disabled.");
	}

}
