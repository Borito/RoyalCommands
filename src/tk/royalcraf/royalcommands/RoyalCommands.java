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

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import tk.royalcraf.rcommands.Banned;
import tk.royalcraf.rcommands.CompareIP;
import tk.royalcraf.rcommands.Facepalm;
import tk.royalcraf.rcommands.Fakeop;
import tk.royalcraf.rcommands.Freeze;
import tk.royalcraf.rcommands.GetIP;
import tk.royalcraf.rcommands.Harm;
import tk.royalcraf.rcommands.Level;
import tk.royalcraf.rcommands.Quit;
import tk.royalcraf.rcommands.RageQuit;
import tk.royalcraf.rcommands.Rank;
import tk.royalcraf.rcommands.Rcmds;
import tk.royalcraf.rcommands.Sci;
import tk.royalcraf.rcommands.Setarmor;
import tk.royalcraf.rcommands.Setlevel;
import tk.royalcraf.rcommands.Slap;
import tk.royalcraf.rcommands.Speak;
import tk.royalcraf.rcommands.Starve;
import tk.royalcraf.rcommands.Vtp;

public class RoyalCommands extends JavaPlugin {

	public static Permission permission = null;

	public String version = "0.0.6";

	public Boolean showcommands = null;
	public Plugin[] plugins = Bukkit.getServer().getPluginManager()
			.getPlugins();

	// Permissions with Vault
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

	public Logger log = Logger.getLogger("Minecraft");

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

	// getFinalArg taken from EssentialsCommand.java - Essentials by
	// EssentialsTeam
	public String getFinalArg(final String[] args, final int start) {
		final StringBuilder bldr = new StringBuilder();
		for (int i = start; i < args.length; i++) {
			if (i != start) {
				bldr.append(" ");
			}
			bldr.append(args[i]);
		}
		return bldr.toString();
	}

	public boolean isAuthorized(final Player player, final String node) {
		if (player.isOp()) {
			return true;
		} else if (this.setupPermissions()) {
			if (RoyalCommands.permission.has(player, node)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean isAuthorized(final CommandSender player, final String node) {
		if (player.isOp()) {
			return true;
		} else if (this.setupPermissions()) {
			if (RoyalCommands.permission.has((Player) player, node)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean isOnline(final String person) {
		Player player = Bukkit.getServer().getPlayer(person);

		if (player == null) {
			return false;
		} else {
			return true;
		}

	}

	public void onEnable() {
		loadConfiguration();

		PluginManager pm = this.getServer().getPluginManager();

		pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerListener,
				Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener,
				Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener,
				Event.Priority.High, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, playerListener,
				Event.Priority.High, this);
		pm.registerEvent(Event.Type.BLOCK_PLACE, playerListener,
				Event.Priority.High, this);

		getCommand("level").setExecutor(new Level(this));
		getCommand("setlevel").setExecutor(new Setlevel(this));
		getCommand("sci").setExecutor(new Sci(this));
		getCommand("speak").setExecutor(new Speak(this));
		getCommand("facepalm").setExecutor(new Facepalm(this));
		getCommand("slap").setExecutor(new Slap(this));
		getCommand("harm").setExecutor(new Harm(this));
		getCommand("starve").setExecutor(new Starve(this));
		getCommand("banned").setExecutor(new Banned(this));
		getCommand("setarmor").setExecutor(new Setarmor(this));
		getCommand("getip").setExecutor(new GetIP(this));
		getCommand("compareip").setExecutor(new CompareIP(this));
		getCommand("ragequit").setExecutor(new RageQuit(this));
		getCommand("quit").setExecutor(new Quit(this));
		getCommand("rank").setExecutor(new Rank(this));
		getCommand("freeze").setExecutor(new Freeze(this));
		getCommand("fakeop").setExecutor(new Fakeop(this));
		getCommand("vtp").setExecutor(new Vtp(this));
		getCommand("rcmds").setExecutor(new Rcmds(this));

		showcommands = this.getConfig().getBoolean("view_commands");
		log.info("[RoyalCommands] RoyalCommands v" + this.version
				+ " initiated.");
	}

	public void onDisable() {
		log.info("[RoyalCommands] RoyalCommands v" + this.version
				+ " disabled.");
	}

}
