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

public class GetIP implements CommandExecutor {

	RoyalCommands plugin;

	public GetIP(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("getip")) {

			if (!plugin.isAuthorized(cs, "rcmds.getip")) {
				cs.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				plugin.log.warning("[RoyalCommands] " + cs.getName()
						+ " was denied access to the command!");
				return true;
			}
			if (plugin.getConfig().getBoolean("disable_getip") == true) {
				cs.sendMessage(ChatColor.RED
						+ "/getip and /compareip have been disabled.");
				return true;
			}
			if (args.length < 1) {
				cs.sendMessage(cmd.getDescription());
				return false;
			}
			OfflinePlayer oplayer = (OfflinePlayer) plugin.getServer()
					.getOfflinePlayer(args[0]);
			File oplayerconfl = new File(plugin.getDataFolder() + "/userdata/"
					+ oplayer.getName().toLowerCase() + ".yml");
			if (oplayerconfl.exists()) {
				FileConfiguration oplayerconf = YamlConfiguration
						.loadConfiguration(oplayerconfl);
				cs.sendMessage(ChatColor.GRAY + oplayer.getName() + ": "
						+ oplayerconf.getString("ip"));
				return true;
			} else {
				cs.sendMessage(ChatColor.RED + "The player "
						+ oplayer.getName() + " does not exist.");
				return true;
			}
		}
		return false;
	}

}
