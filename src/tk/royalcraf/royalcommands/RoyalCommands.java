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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.logging.Logger;
import net.milkbowl.vault.permission.Permission;

import org.blockface.bukkitstats.CallHome;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import tk.royalcraf.rcommands.Ban;
import tk.royalcraf.rcommands.Banned;
import tk.royalcraf.rcommands.ClearInventory;
import tk.royalcraf.rcommands.ClearWarns;
import tk.royalcraf.rcommands.CompareIP;
import tk.royalcraf.rcommands.DelHome;
import tk.royalcraf.rcommands.DelWarp;
import tk.royalcraf.rcommands.Facepalm;
import tk.royalcraf.rcommands.Fakeop;
import tk.royalcraf.rcommands.FixChunk;
import tk.royalcraf.rcommands.Freeze;
import tk.royalcraf.rcommands.Gamemode;
import tk.royalcraf.rcommands.GetIP;
import tk.royalcraf.rcommands.Give;
import tk.royalcraf.rcommands.Harm;
import tk.royalcraf.rcommands.Home;
import tk.royalcraf.rcommands.Item;
import tk.royalcraf.rcommands.Jump;
import tk.royalcraf.rcommands.Kick;
import tk.royalcraf.rcommands.Level;
import tk.royalcraf.rcommands.ListHome;
import tk.royalcraf.rcommands.MegaStrike;
import tk.royalcraf.rcommands.Message;
import tk.royalcraf.rcommands.Mute;
import tk.royalcraf.rcommands.Pext;
import tk.royalcraf.rcommands.Quit;
import tk.royalcraf.rcommands.RageQuit;
import tk.royalcraf.rcommands.Rank;
import tk.royalcraf.rcommands.Rcmds;
import tk.royalcraf.rcommands.Reply;
import tk.royalcraf.rcommands.Sci;
import tk.royalcraf.rcommands.SetHome;
import tk.royalcraf.rcommands.SetWarp;
import tk.royalcraf.rcommands.Setarmor;
import tk.royalcraf.rcommands.Setlevel;
import tk.royalcraf.rcommands.Slap;
import tk.royalcraf.rcommands.Speak;
import tk.royalcraf.rcommands.Starve;
import tk.royalcraf.rcommands.Strike;
import tk.royalcraf.rcommands.Time;
import tk.royalcraf.rcommands.Vtp;
import tk.royalcraf.rcommands.Vtphere;
import tk.royalcraf.rcommands.Warn;
import tk.royalcraf.rcommands.Warp;
import tk.royalcraf.rcommands.Weather;

public class RoyalCommands extends JavaPlugin {

	public static Permission permission = null;

	public String version = "0.0.8";

	public Boolean showcommands = null;
	public Boolean disablegetip = null;
	public String banMessage = null;
	public String kickMessage = null;
	public String defaultWarn = null;
	public Integer defaultStack = null;
	public Integer warnBan = null;

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
	private final RoyalCommandsBlockListener blockListener = new RoyalCommandsBlockListener(
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
		File warps = new File(this.getDataFolder() + "/warps.yml");
		if (!warps.exists()) {
			try {
				boolean success = new File(this.getDataFolder() + "/warps.yml")
						.createNewFile();
				if (success) {
					try {
						FileWriter fstream = new FileWriter(
								this.getDataFolder() + "/warps.yml");
						BufferedWriter out = new BufferedWriter(fstream);
						out.write("warps:");
						out.close();
					} catch (Exception e) {
						log.severe("[RoyalCommands] Could not write to warps file.");
					}
					log.info("[RoyalCommands] Created warps file.");
				}
			} catch (Exception e) {
				log.severe("[RoyalCommands] Failed to make warps file!");
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

		CallHome.load(this);

		PluginManager pm = this.getServer().getPluginManager();

		pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerListener,
				Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener,
				Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener,
				Event.Priority.High, this);
		pm.registerEvent(Event.Type.PLAYER_CHAT, playerListener,
				Event.Priority.High, this);
		pm.registerEvent(Event.Type.PLAYER_LOGIN, playerListener,
				Event.Priority.High, this);
		pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener,
				Event.Priority.High, this);
		pm.registerEvent(Event.Type.PLAYER_GAME_MODE_CHANGE, playerListener,
				Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener,
				Event.Priority.High, this);
		pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener,
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
		getCommand("vtphere").setExecutor(new Vtphere(this));
		getCommand("megastrike").setExecutor(new MegaStrike(this));
		getCommand("pext").setExecutor(new Pext(this));
		getCommand("item").setExecutor(new Item(this));
		getCommand("clearinventory").setExecutor(new ClearInventory(this));
		getCommand("weather").setExecutor(new Weather(this));
		getCommand("fixchunk").setExecutor(new FixChunk(this));
		getCommand("give").setExecutor(new Give(this));
		getCommand("message").setExecutor(new Message(this));
		getCommand("reply").setExecutor(new Reply(this));
		getCommand("gamemode").setExecutor(new Gamemode(this));
		getCommand("mute").setExecutor(new Mute(this));
		getCommand("ban").setExecutor(new Ban(this));
		getCommand("kick").setExecutor(new Kick(this));
		getCommand("time").setExecutor(new Time(this));
		getCommand("home").setExecutor(new Home(this));
		getCommand("sethome").setExecutor(new SetHome(this));
		getCommand("delhome").setExecutor(new DelHome(this));
		getCommand("listhome").setExecutor(new ListHome(this));
		getCommand("strike").setExecutor(new Strike(this));
		getCommand("jump").setExecutor(new Jump(this));
		getCommand("warn").setExecutor(new Warn(this));
		getCommand("clearwarns").setExecutor(new ClearWarns(this));
		getCommand("warp").setExecutor(new Warp(this));
		getCommand("setwarp").setExecutor(new SetWarp(this));
		getCommand("delwarp").setExecutor(new DelWarp(this));
		getCommand("rcmds").setExecutor(new Rcmds(this));

		showcommands = this.getConfig().getBoolean("view_commands");
		disablegetip = this.getConfig().getBoolean("disable_getip");
		banMessage = this.getConfig().getString("default_ban_message")
				.replaceAll("(&([a-f0-9]))", "\u00A7$2");
		kickMessage = this.getConfig().getString("default_kick_message")
				.replaceAll("(&([a-f0-9]))", "\u00A7$2");
		defaultStack = this.getConfig().getInt("default_stack_size");
		defaultWarn = this.getConfig().getString("default_warn_message");
		warnBan = this.getConfig().getInt("max_warns_before_ban");
		log.info("[RoyalCommands] RoyalCommands v" + this.version
				+ " initiated.");
	}

	public void onDisable() {
		log.info("[RoyalCommands] RoyalCommands v" + this.version
				+ " disabled.");
	}

}
