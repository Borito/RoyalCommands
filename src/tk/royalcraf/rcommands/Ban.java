package tk.royalcraf.rcommands;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import tk.royalcraf.royalcommands.RoyalCommands;

public class Ban implements CommandExecutor {

	RoyalCommands plugin;

	public Ban(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("ban")) {
			if (!plugin.isAuthorized(cs, "rcmds.ban")) {
				cs.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				plugin.log.warning("[RoyalCommands] " + cs.getName()
						+ " was denied access to the command!");
				return true;
			}
			if (args.length < 1) {
				return false;
			} else if (args.length == 1) {
				OfflinePlayer t = plugin.getServer().getOfflinePlayer(args[0]);
				if (t.isOp()) {
					cs.sendMessage(ChatColor.RED
							+ "You cannot ban that player!");
					return true;
				}
				File pconfl = new File(plugin.getDataFolder() + "/userdata/"
						+ t.getName().toLowerCase() + ".yml");
				if (pconfl.exists()) {
					FileConfiguration pconf = YamlConfiguration
							.loadConfiguration(pconfl);
					pconf.set("banreason", plugin.banMessage);
					try {
						pconf.save(pconfl);
					} catch (IOException e) {
						e.printStackTrace();
					}
					t.setBanned(true);
					plugin.getServer().broadcast(
							ChatColor.RED + "The player " + ChatColor.GRAY
									+ t.getName() + ChatColor.RED
									+ " has been banned for " + ChatColor.GRAY
									+ plugin.banMessage + ChatColor.RED + "by "
									+ ChatColor.GRAY + cs.getName() + ".",
							"rcmds.see.ban");
					if (t.isOnline()) {
						Player t2 = plugin.getServer().getPlayer(args[0]);
						t2.kickPlayer(plugin.banMessage);
					}
					return true;
				} else {
					cs.sendMessage(ChatColor.RED
							+ "That player does not exist!");
					return true;
				}
			} else if (args.length > 1) {
				OfflinePlayer t = plugin.getServer().getOfflinePlayer(args[0]);
				if (t.isOp()) {
					cs.sendMessage(ChatColor.RED
							+ "You cannot ban that player!");
					return true;
				}
				File pconfl = new File(plugin.getDataFolder() + "/userdata/"
						+ t.getName().toLowerCase() + ".yml");
				if (pconfl.exists()) {
					FileConfiguration pconf = YamlConfiguration
							.loadConfiguration(pconfl);
					pconf.set("banreason", plugin.getFinalArg(args, 1)
							.replaceAll("(&([a-f0-9]))", "\u00A7$2"));
					try {
						pconf.save(pconfl);
					} catch (IOException e) {
						e.printStackTrace();
					}
					t.setBanned(true);
					plugin.getServer().broadcast(
							ChatColor.RED
									+ "The player "
									+ ChatColor.GRAY
									+ t.getName()
									+ ChatColor.RED
									+ " has been banned for "
									+ ChatColor.GRAY
									+ plugin.getFinalArg(args, 1).replaceAll(
											"(&([a-f0-9]))", "\u00A7$2")
									+ ChatColor.RED + " by " + ChatColor.GRAY
									+ cs.getName() + ".", "rcmds.see.ban");
					if (t.isOnline()) {
						Player t2 = plugin.getServer().getPlayer(args[0]);
						t2.kickPlayer(plugin.getFinalArg(args, 1).replaceAll(
								"(&([a-f0-9]))", "\u00A7$2"));
					}
					return true;
				} else {
					cs.sendMessage(ChatColor.RED
							+ "That player does not exist!");
					return true;
				}
			}
		}
		return false;
	}
}
