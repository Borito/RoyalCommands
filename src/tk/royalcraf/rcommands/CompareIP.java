package tk.royalcraf.rcommands;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import tk.royalcraf.royalcommands.RoyalCommands;

public class CompareIP implements CommandExecutor {

	RoyalCommands plugin;

	public CompareIP(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("compareip")) {
			if (!plugin.isAuthorized(cs, "rcmds.compareip")) {
				cs.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				return true;
			} else {
				if (plugin.getConfig().getBoolean("disable_getip") == true) {
					cs.sendMessage(ChatColor.RED
							+ "/getip and /compareip have been disabled.");
					return true;
				} else {
					if (args.length < 2) {
						return false;
					} else {
						OfflinePlayer player1 = null;
						OfflinePlayer player2 = null;
						player1 = (OfflinePlayer) plugin.getServer()
								.getOfflinePlayer(args[0]);
						player2 = (OfflinePlayer) plugin.getServer()
								.getOfflinePlayer(args[1]);

						File p1confl = new File(plugin.getDataFolder()
								+ "/userdata/" + player1.getName() + ".yml");
						File p2confl = new File(plugin.getDataFolder()
								+ "/userdata/" + player2.getName() + ".yml");
						if (p1confl.exists()) {
							if (p2confl.exists()) {
								FileConfiguration p1conf = YamlConfiguration
										.loadConfiguration(p1confl);
								FileConfiguration p2conf = YamlConfiguration
										.loadConfiguration(p2confl);
								String p1ip = p1conf.getString("ip");
								String p2ip = p2conf.getString("ip");

								cs.sendMessage(ChatColor.GRAY
										+ player1.getName() + ": " + p1ip);
								cs.sendMessage(ChatColor.GRAY
										+ player2.getName() + ": " + p2ip);
								return true;
							} else {
								cs.sendMessage(ChatColor.RED + "The player "
										+ player2.getName()
										+ " does not exist.");
								return true;
							}
						} else {
							cs.sendMessage(ChatColor.RED + "The player "
									+ player1.getName() + " does not exist.");
							return true;
						}
					}
				}
			}
		}
		return false;
	}

}
