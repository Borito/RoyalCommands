package tk.royalcraf.royalcommands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;

public class RoyalCommandsPlayerListener extends PlayerListener {

	public static RoyalCommands plugin;

	public RoyalCommandsPlayerListener(RoyalCommands instance) {
		plugin = instance;
	}

	Logger log = Logger.getLogger("Minecraft");

	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if (plugin.showcommands) {
			log.info("[PLAYER_COMMAND] " + event.getPlayer().getName() + ": "
					+ event.getMessage());
		}
		if (PConfManager.getPValBoolean((OfflinePlayer) event.getPlayer(),
				"muted")) {
			for (String command : plugin.muteCmds) {
				if (event.getMessage().substring(0, command.length())
						.equalsIgnoreCase(command)) {
					event.getPlayer().sendMessage(
							ChatColor.RED + "You are muted.");
					log.info("[RoyalCommands] " + event.getPlayer().getName()
							+ " tried to use that command, but is muted.");
					event.setCancelled(true);
				}
			}
		}
	}

	public void onGameModeChange(PlayerGameModeChangeEvent event) {
		event.setCancelled(true);
	}

	public void onPlayerChat(PlayerChatEvent event) {
		if (PConfManager.getPValBoolean((OfflinePlayer) event.getPlayer(),
				"muted")) {
			event.setFormat("");
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "You are muted.");
			plugin.log.info("[RoyalCommands] " + event.getPlayer().getName()
					+ " tried to speak, but has been muted.");
		}
	}

	public void onPlayerMove(PlayerMoveEvent event) {
		if (PConfManager.getPValBoolean((OfflinePlayer) event.getPlayer(),
				"frozen")) {
			event.setCancelled(true);
		}
	}

	public void onPlayerInteract(PlayerInteractEvent event) {
		if (PConfManager.getPValBoolean((OfflinePlayer) event.getPlayer(),
				"frozen")) {
			event.setCancelled(true);
		}
		if (plugin.buildPerm) {
			if (!plugin.isAuthorized(event.getPlayer(), "rcmds.build")) {
				event.setCancelled(true);
			}
		}
	}

	public void onPlayerLogin(PlayerLoginEvent event) {
		if (event.getPlayer().isBanned()) {
			String kickMessage = plugin.banMessage;
			OfflinePlayer oplayer = (OfflinePlayer) plugin.getServer()
					.getOfflinePlayer(event.getPlayer().getName());
			File oplayerconfl = new File(plugin.getDataFolder() + "/userdata/"
					+ oplayer.getName() + ".yml");
			if (oplayerconfl.exists()) {
				FileConfiguration oplayerconf = YamlConfiguration
						.loadConfiguration(oplayerconfl);
				kickMessage = oplayerconf.getString("banreason");
			} else {
				kickMessage = plugin.banMessage;
			}
			event.setKickMessage(kickMessage);
			event.disallow(Result.KICK_BANNED, kickMessage);
		}
	}

	public void onPlayerJoin(PlayerJoinEvent event) {
		File datafile = new File(plugin.getDataFolder() + "/userdata/"
				+ event.getPlayer().getName().toLowerCase() + ".yml");
		if (!datafile.exists()) {
			log.info("[RoyalCommands] Creating userdata for user "
					+ event.getPlayer().getName() + ".");
			try {
				datafile.createNewFile();
				FileWriter fstream = new FileWriter(plugin.getDataFolder()
						+ "/userdata/"
						+ event.getPlayer().getName().toLowerCase() + ".yml");
				BufferedWriter out = new BufferedWriter(fstream);
				out.write("name: '" + event.getPlayer().getName() + "'\n");
				out.write("dispname: '" + event.getPlayer().getDisplayName()
						+ "'\n");

				out.write("ip: "
						+ event.getPlayer().getAddress().getAddress()
								.toString().replace("/", "") + "\n");
				out.write("banreason: '" + "'\n");
				/*
				 * out.write("home:"); out.write("  set: false");
				 * out.write("  x: 0"); out.write("  y: 0");
				 * out.write("  z: 0");
				 */
				out.close();
				log.info("[RoyalCommands] Userdata creation finished.");
			} catch (Exception e) {
				log.severe("[RoyalCommands] Could not create userdata for user "
						+ event.getPlayer().getName() + "!");
				log.severe(e.getMessage());
				e.printStackTrace();
			}
			if (plugin.useWelcome) {
				String welcomemessage = plugin.welcomeMessage;
				welcomemessage = welcomemessage.replace("{name}", event
						.getPlayer().getName());
				welcomemessage = welcomemessage.replace("{dispname}", event
						.getPlayer().getDisplayName());
				welcomemessage = welcomemessage.replace("{world}", event
						.getPlayer().getWorld().getName());
				plugin.getServer().broadcastMessage(welcomemessage);
			}
		} else {
			log.info("[RoyalCommands] Updating the IP for "
					+ event.getPlayer().getName() + ".");
			File p1confl = new File(plugin.getDataFolder() + "/userdata/"
					+ event.getPlayer().getName().toLowerCase() + ".yml");
			FileConfiguration p1conf = YamlConfiguration
					.loadConfiguration(p1confl);
			String playerip = event.getPlayer().getAddress().getAddress()
					.toString();
			playerip = playerip.replace("/", "");
			p1conf.set("ip", playerip);
			p1conf.set("dispname", event.getPlayer().getDisplayName());
			try {
				p1conf.save(p1confl);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}