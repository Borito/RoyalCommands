package tk.royalcraf.rcommands;

import java.io.File;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import tk.royalcraf.royalcommands.PConfManager;
import tk.royalcraf.royalcommands.RoyalCommands;

public class Jail implements CommandExecutor {

	RoyalCommands plugin;

	public Jail(RoyalCommands instance) {
		this.plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("jail")) {
			if (!plugin.isAuthorized(cs, "rcmds.jail")) {
				cs.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				plugin.log.warning("[RoyalCommands] " + cs.getName()
						+ " was denied access to the command!");
				return true;
			}

			Player t = plugin.getServer().getPlayer(args[0].trim());
			/*
			 * It's just way too much shit, and it doesn't work atm - if (t ==
			 * null) { OfflinePlayer t2 = plugin.getServer().getOfflinePlayer(
			 * args[0].trim()); if (args.length < 2) { if
			 * (PConfManager.getPValBoolean((OfflinePlayer) t2, "jailed")) {
			 * PConfManager.setPValBoolean((OfflinePlayer) t, false, "jailed");
			 * cs.sendMessage(ChatColor.BLUE + "You have released " +
			 * ChatColor.GRAY + t2.getName() + ChatColor.BLUE + "."); return
			 * true; } cs.sendMessage(cmd.getDescription()); return false; } if
			 * (PConfManager.getPValBoolean((OfflinePlayer) t2, "jailed")) {
			 * PConfManager.setPValBoolean((OfflinePlayer) t2, false, "jailed");
			 * cs.sendMessage(ChatColor.BLUE + "You have released " +
			 * ChatColor.GRAY + t2.getName() + ChatColor.BLUE + "."); return
			 * true; } else { PConfManager.setPValBoolean((OfflinePlayer) t2,
			 * true, "jailed"); cs.sendMessage(ChatColor.BLUE +
			 * "You have jailed " + ChatColor.GRAY + t2.getName() +
			 * ChatColor.BLUE + ".");
			 * 
			 * return true; } }
			 */
			// the good shit
			if (t == null) {
				cs.sendMessage(ChatColor.RED + "That player does not exist!");
				return true;
			}
			if (plugin.isAuthorized(t, "rcmds.exempt.jail")) {
				cs.sendMessage(ChatColor.RED + "You cannot jail that player.");
				return true;
			}
			if (args.length < 2) {
				if (PConfManager.getPValBoolean((OfflinePlayer) t, "jailed")) {
					PConfManager.setPValBoolean((OfflinePlayer) t, false,
							"jailed");
					t.sendMessage(ChatColor.RED + "You have been released.");
					return true;
				}
				cs.sendMessage(cmd.getDescription());
				return false;
			}

			boolean jailSet = false;
			Double jailX = null;
			Double jailY = null;
			Double jailZ = null;
			Float jailYaw = null;
			Float jailPitch = null;
			World jailW = null;

			File pconfl = new File(plugin.getDataFolder() + "/jails.yml");
			if (pconfl.exists()) {
				FileConfiguration pconf = YamlConfiguration
						.loadConfiguration(pconfl);
				if (args.length < 1) {
					if (pconf.get("jails") == null) {
						cs.sendMessage(ChatColor.RED + "There are no jails!");
						return true;
					}
					final Map<String, Object> opts = pconf
							.getConfigurationSection("jails").getValues(false);
					if (opts.keySet().isEmpty()) {
						cs.sendMessage(ChatColor.RED + "There are no jails!");
						return true;
					}
					String jails = opts.keySet().toString();
					jails = jails.substring(1, jails.length() - 1);
					cs.sendMessage(ChatColor.BLUE + "Jails:");
					cs.sendMessage(jails);
					return true;
				}
				jailSet = pconf.getBoolean("jails." + args[1] + ".set");
				if (jailSet) {
					jailX = pconf.getDouble("jails." + args[1] + ".x");
					jailY = pconf.getDouble("jails." + args[1] + ".y");
					jailZ = pconf.getDouble("jails." + args[1] + ".z");
					jailYaw = Float.parseFloat(pconf.getString("jails."
							+ args[1] + ".yaw"));
					jailPitch = Float.parseFloat(pconf.getString("jails."
							+ args[1] + ".pitch"));
					jailW = plugin.getServer().getWorld(
							pconf.getString("jails." + args[1] + ".w"));
				} else {
					cs.sendMessage(ChatColor.RED + "That jail does not exist.");
					return true;
				}
			} else {
				cs.sendMessage(ChatColor.RED + "That player does not exist!");
				return true;
			}
			Location jailLoc = new Location(jailW, jailX, jailY, jailZ,
					jailYaw, jailPitch);
			if (PConfManager.getPValBoolean((OfflinePlayer) t, "jailed")) {
				PConfManager.setPValBoolean((OfflinePlayer) t, false, "jailed");
				cs.sendMessage(ChatColor.BLUE + "You have released "
						+ ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
				t.sendMessage(ChatColor.RED + "You have been released.");
				return true;
			} else {
				PConfManager.setPValBoolean((OfflinePlayer) t, true, "jailed");
				cs.sendMessage(ChatColor.BLUE + "You have jailed "
						+ ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
				t.sendMessage(ChatColor.RED + "You have been jailed.");
				t.teleport(jailLoc);
				return true;
			}
		}
		return false;
	}

}
